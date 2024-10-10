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
import org.apache.commons.dbcp2.BasicDataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class DatabaseConnectionPool {
    private static BasicDataSource dataSource;

    static {
        dataSource = new BasicDataSource();
        dataSource.setUrl("jdbc:postgresql://192.168.56.128:5432/odoo_db_16");
        dataSource.setUsername("odoo16");
        dataSource.setPassword("abcd*1234");
        dataSource.setDriverClassName("org.postgresql.Driver");
        dataSource.setInitialSize(5); // Initial connections
        dataSource.setMaxTotal(10);    // Maximum total connections
        dataSource.setMinIdle(2);      // Minimum idle connections
        dataSource.setMaxIdle(5);      // Maximum idle connections
    }

    public static Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }
}
