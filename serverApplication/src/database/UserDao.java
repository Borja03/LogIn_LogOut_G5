package database;

import ISignable.Signable;
import Model.User;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Logger;

public class UserDao implements Signable{
    private static final Logger logger = Logger.getLogger(UserDao.class.getName());
    
    private static final String INSERT_USER_PARTNERS_TABLE = "INSERT INTO public.res_partner (name, email, display_name, is_company, company_id, street, city, zip, active) "
                    + "VALUES (?, ?, ?, FALSE, 1, ?, ?, ?, ?) RETURNING id";
    
    private static final String INSERT_USER_USERS_TABLE= "INSERT INTO public.res_users (login, password, partner_id, company_id, notification_type) "
                    + "VALUES (?, ?, ?, ?, 'email')";
    
    // Inserts a user into res_partner and res_users


    // Utility function to hash the password (replace with your own hashing method)
    private String hashPassword(String password) {
        // For demo, return the password as is, but you should hash it properly using SHA-512 or bcrypt.
        return password;  
    }

    @Override
    public User signIn(User user) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public User signUp(User user) throws Exception {
        Connection conn = null;
        PreparedStatement psPartner = null;
        PreparedStatement psUser = null;
        ResultSet rs = null;

        try {
            // Get a connection from the pool
            conn = DBConnectionPool.getConnection();
            
            conn.setAutoCommit(false); // Start transaction
            // Insert into res_partner
            psPartner = conn.prepareStatement(INSERT_USER_PARTNERS_TABLE);
            psPartner.setString(1, user.getName());
            psPartner.setString(2, user.getEmail());
            psPartner.setString(3, user.getName());
            psPartner.setString(4, user.getStreet());
            psPartner.setString(5, user.getCity());
            psPartner.setInt(6, user.getZip());
            psPartner.setBoolean(7, user.isActivo()); 
            rs = psPartner.executeQuery();
            //other way : we can implement this by adding a new query to get last added operation to the table 
            int partnerId = 0;
            if (rs.next()) {
                partnerId = rs.getInt("id");  // Get generated partner_id
            }

            // Insert into res_users
            psUser = conn.prepareStatement(INSERT_USER_USERS_TABLE);
            psUser.setString(1, user.getEmail());
            psUser.setString(2,user.getPassword()); 
            psUser.setInt(3, partnerId);  // Use the partner ID from res_partner
            psUser.setInt(4, user.getCompanyID());
            psUser.executeUpdate();

            // Commit the transaction
            conn.commit();
            return user;

        } catch (SQLException e) {
            // Rollback in case of error
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException rollbackEx) {
                    rollbackEx.printStackTrace();
                }
            }
            e.printStackTrace();
            return null;
        } finally {
            // Close all resources
            try {
                if (rs != null) rs.close();
                if (psPartner != null) psPartner.close();
                if (psUser != null) psUser.close();
                if (conn != null) conn.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }
}
