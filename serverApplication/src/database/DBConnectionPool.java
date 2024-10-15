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

    private static final ResourceBundle rsbundle = ResourceBundle.getBundle("config.db_config");

    static {
        dataSource = new BasicDataSource();

        // add this info to file of configuration 
        //dataSource.setUrl("jdbc:postgresql://192.168.56.128:5432/odoo_db_16");  
        String dburl = rsbundle.getString("db_url") + rsbundle.getString("db_ip")
                        + rsbundle.getString("db_port") + rsbundle.getString("db_name");

        dataSource.setUrl(dburl);
        dataSource.setUsername(rsbundle.getString("db_user"));
        dataSource.setPassword(rsbundle.getString("db_password"));
        dataSource.setDriverClassName(rsbundle.getString("driver"));
        // Initial connections
        dataSource.setInitialSize(Integer.parseInt(rsbundle.getString("pool_int_size")));
        // Maximum total connections
        dataSource.setMaxTotal(Integer.parseInt(rsbundle.getString("pool_max_total")));
        dataSource.setMinIdle(2);      // Minimum idle connections
        dataSource.setMaxIdle(5);      // Maximum idle connections
    }

    public static Connection getConnection() throws SQLException {
        logger.info("Connection created ");
        return dataSource.getConnection();
    }
}
