package database;

import Model.User;
import exception.ConnectionException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.Assert.*;

public class UserDaoRollbackTest {

    private UserDao userDao;
    private Connection connection;

    @Before
    public void setUp() throws SQLException, ConnectionException {
        userDao = new UserDao();
        connection = DBPool.getInstance().getConnection();
        connection.setAutoCommit(true); // Ensure auto-commit for setup and cleanup
    }

    @After
    public void tearDown() throws SQLException {
        try (PreparedStatement stmt = connection.prepareStatement("DELETE FROM public.res_partner WHERE email = ?")) {
            stmt.setString(1, "testuser@example.com");
            stmt.executeUpdate();
        }
        connection.close();
    }

    @Test
    public void testRollbackOnSecondInsertFailure() {
        // Full user details according to UserDao structure
        User testUser = new User();
        //"testuser@example.com", "Test@1234", "Test User", "123 Test St", "Test City", "12345", true
        testUser.setEmail("testuser@example.com");
        testUser.setName("Test User");
        //testUser.
        
        try {
            connection.setAutoCommit(false); // Start transaction
            
            // First Insert: Insert into res_partner
            int partnerId = insertPartner(testUser);
            if (partnerId == 0) {
                fail("Failed to insert into res_partner"); // Insertion failed, stop test
            }

            // Second Insert: Insert into res_users (simulate failure)
            boolean insertFailed = false;
            try {
                insertUser(testUser, partnerId);
            } catch (SQLException e) {
                insertFailed = true;
                connection.rollback(); // Rollback if there’s an error in second insert
            }
            
            assertTrue(insertFailed); // Assert that the second insert failed

            // Verify that no data is left in `res_partner` after rollback
            verifyNoPartnerData(testUser.getEmail());

        } catch (SQLException e) {
            e.printStackTrace();
            fail("Test encountered an unexpected SQL exception.");
        } finally {
            try {
                connection.setAutoCommit(true); // Reset auto-commit mode
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private int insertPartner(User user) throws SQLException {
        String insertPartnerQuery = "INSERT INTO public.res_partner (name, email, street, city, zip, active) VALUES (?, ?, ?, ?, ?, ?) RETURNING id";
        try (PreparedStatement psPartner = connection.prepareStatement(insertPartnerQuery)) {
            psPartner.setString(1, user.getName());
            psPartner.setString(2, user.getEmail());
            psPartner.setString(3, user.getStreet());
            psPartner.setString(4, user.getCity());
            psPartner.setInt(5, user.getZip());
            psPartner.setBoolean(6, user.isActivo());
            ResultSet rs = psPartner.executeQuery();
            if (rs.next()) {
                return rs.getInt("id");
            }
        }
        return 0; // Return 0 if partner insert failed
    }

    private void insertUser(User user, int partnerId) throws SQLException {
        String insertUserQuery = "INSERT INTO public.res_users (login, password, partner_id) VALUES (?, ?, ?)";
        try (PreparedStatement psUser = connection.prepareStatement(insertUserQuery)) {
            psUser.setString(1, user.getEmail());
            psUser.setString(2, user.getPassword());
            psUser.setInt(3, partnerId);
            // Simulate failure for testing purposes
            throw new SQLException("Simulated exception on second insert.");
        }
    }

    private void verifyNoPartnerData(String email) throws SQLException {
        String verifyQuery = "SELECT COUNT(*) FROM public.res_partner WHERE email = ?";
        try (PreparedStatement psCheck = connection.prepareStatement(verifyQuery)) {
            psCheck.setString(1, email);
            ResultSet rsCheck = psCheck.executeQuery();
            if (rsCheck.next()) {
                int count = rsCheck.getInt(1);
                assertEquals(0, count); // Assert that no partner data exists after rollback
            }
        }
    }
}
