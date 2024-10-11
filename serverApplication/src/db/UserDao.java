/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package db;

/**
 *
 * @author Omar
 */
import Model.User;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDao {

    // Inserts a user into res_partner and res_users
    public static User insertUser(User user) {
        Connection conn = null;
        PreparedStatement psPartner = null;
        PreparedStatement psUser = null;
        ResultSet rs = null;

        try {
            // Step 1: Get a connection (Use a connection pool for scalability)
            conn = DatabaseConnectionPool.getConnection();
            conn.setAutoCommit(false); // Start transaction

            // Step 2: Insert into res_partner
            String sqlPartner = "INSERT INTO res_partner (name, email, phone, display_name, is_company, company_id) "
                    + "VALUES (?, ?, ?, ?, FALSE, 1) RETURNING id";
            psPartner = conn.prepareStatement(sqlPartner);
            psPartner.setString(1, user.getName());
            psPartner.setString(2, user.getEmail());
            psPartner.setString(3, user.getTelefono());       
            psPartner.setInt(4, user.getCompanyID());

            rs = psPartner.executeQuery();
            int partnerId = 0;
            if (rs.next()) {
                partnerId = rs.getInt("id");  // Get generated partner_id
            }

            // Step 3: Insert into res_users
            String sqlUser = "INSERT INTO res_users (login, password, partner_id, company_id, notification_type) "
                 + "VALUES (?, ?, ?, ?, 'email')";
            psUser = conn.prepareStatement(sqlUser);
            psUser.setString(1, user.getEmail());
           // psUser.setString(2, hashPassword(password));  // Password should be hashed
            psUser.setString(2, user.getPassword());  

            psUser.setInt(3, partnerId);  // Use the partner ID from res_partner
            psUser.setInt(4,user.getCompanyID());
            psUser.executeUpdate();

            // Step 4: Commit the transaction
            conn.commit();
            return user;

        } catch (SQLException e) {
            // Step 5: Rollback in case of error
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
            // Step 6: Close all resources
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

    // Utility function to hash the password (replace with your own hashing method)
    private String hashPassword(String password) {
        // For demo, return the password as is, but you should hash it properly using SHA-512 or bcrypt.
        return password;  
    }
}
