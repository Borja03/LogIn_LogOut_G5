/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package database;

/**
 *
 * @author Omar
 */
import org.apache.commons.dbcp2.BasicDataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Logger;

public class DBConnectionPool {

    private static final Logger logger = Logger.getLogger(DBConnectionPool.class.getName());

    private static BasicDataSource dataSource;

   // private static final ResourceBundle rsbundle = ResourceBundle.getBundle("config.db_config");


    static {
        dataSource = new BasicDataSource();
        //ip + port + db name
        dataSource.setUrl("jdbc:postgresql://192.168.86.128:5432/G5");
        dataSource.setUsername("2dami");
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