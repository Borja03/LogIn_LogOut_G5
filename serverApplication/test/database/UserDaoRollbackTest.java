package database;

import Model.User;
import exception.ConnectionException;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.junit.After;

/*
* @author Adrian y Omar 
 */
public class UserDaoRollbackTest {

    private UserDao userDao;
    private Connection connection;

    @Before
    public void setUp() throws ConnectionException {
        userDao = new UserDao();
        // Get connection from pool for verification
        connection = DBPool.getInstance().getConnection();
    }

    @Test
    public void testSignUpRollbackOnError() throws Exception {
        // Create test user data
        User testUser = new User();
        testUser.setName("Test User");
        testUser.setEmail("test.rollback@example.com");
        testUser.setPassword("password123");
        testUser.setStreet("Test Street");
        testUser.setCity("Test City");
        testUser.setZip(12345);
        testUser.setActivo(true);
        testUser.setCompanyID(1);

        // First, verify user doesn't exist
        assertFalse(userExists(testUser.getEmail()));

        try {
            // Force an error by setting company_id to invalid value
            testUser.setCompanyID(-999); // This should cause a foreign key constraint violation

            User result = userDao.signUp(testUser);

            // The signup should fail and return null
            assertNull(result);

            // Verify that no records were created in either table due to rollback
            assertFalse("User should not exist after rollback", userExists(testUser.getEmail()));
            assertFalse("Partner should not exist after rollback", partnerExists(testUser.getEmail()));

        } catch (Exception e) {
            // Expected exception due to constraint violation
            // Verify rollback occurred
            assertFalse("User should not exist after exception", userExists(testUser.getEmail()));
            assertFalse("Partner should not exist after exception", partnerExists(testUser.getEmail()));
        }
    }

    // Helper method to check if user exists in res_users table
    private boolean userExists(String email) throws SQLException {
        String query = "SELECT COUNT(*) FROM public.res_users WHERE login = ?";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }
        }
    }

    // Helper method to check if partner exists in res_partner table
    private boolean partnerExists(String email) throws SQLException {
        String query = "SELECT COUNT(*) FROM public.res_partner WHERE email = ?";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }
        }
    }

    @After
    public void tearDown() throws SQLException, ConnectionException {
        // Clean up any test data if necessary
        if (connection != null && !connection.isClosed()) {
            // Return connection to pool
            DBPool.getInstance().releaseConnection(connection);
        }
    }
}
