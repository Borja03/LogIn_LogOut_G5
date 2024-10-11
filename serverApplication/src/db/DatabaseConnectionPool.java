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
import java.util.logging.Logger;

public class DatabaseConnectionPool {
        private static final Logger logger = Logger.getLogger(DatabaseConnectionPool.class.getName());

    private static BasicDataSource dataSource;

    static {
        dataSource = new BasicDataSource();
        // add this info to file of configuration 
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
        logger.info("Connection created ");
        return dataSource.getConnection();
    }
}
