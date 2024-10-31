package database;

import exception.ConnectionException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.Stack;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DBPool {

    private ResourceBundle configFile;
    private String db_user;
    private String db_pass;
    private String url;
    private static Stack<Connection> connectionStack = new Stack<>();
    private int maxConnections;
    private int activeConnections = 0;
    private int idleConnections = 0;
    private static DBPool instance;

    private static final Logger LOGGER = Logger.getLogger(DBPool.class.getName());

    public DBPool() throws ConnectionException {
        // Load configuration from the properties file
        loadConfiguration();
        initializeConnections();
    }

    public static synchronized DBPool getInstance() throws ConnectionException {
        if (instance == null) {
            instance = new DBPool();
        }
        return instance;
    }

    private void loadConfiguration() {
        //to read from db file of config 
        url = "jdbc:postgresql://192.168.142.130:5432/odoooo";
        //url = "jdbc:postgresql://192.168.86.128:5432/odooDB";
        db_user = "odoo";
        db_pass = "abcd*1234";

        // Maximum number of connections allowed
        maxConnections = 3;

        // You can include validation checks to ensure that these values are correctly set
        if (url == null || db_user == null || db_pass == null) {
            throw new RuntimeException("Missing required configuration for database connection.");
        }
    }

    private void initializeConnections() throws ConnectionException {
        // Initialize connections and push them to the stack
        for (int i = 0; i < maxConnections; i++) {
            try {
                Connection con = DriverManager.getConnection(url, db_user, db_pass);
                connectionStack.push(con);
                idleConnections++;
            } catch (SQLException ex) {
                LOGGER.info("Error initializing connection");
                throw new ConnectionException("Error database connecxion");
            }
        }
    }

    private boolean isValidConnection(Connection con) {
        try {
            return con != null && !con.isClosed() && con.isValid(2);
        } catch (SQLException e) {
            return false;
        }
    }

    public synchronized Connection getConnection() throws ConnectionException {
        // Check for available connections
        if (connectionStack.isEmpty()) {
            if (activeConnections < maxConnections) {
                try {
                    Connection con = DriverManager.getConnection(url, db_user, db_pass);
                    activeConnections++;
                    return con;
                } catch (SQLException ex) {
                    throw new ConnectionException(ex.getMessage());
                }
            } else {
                // No connections available and max limit reached
                throw new ConnectionException("Maximum number of connections reached, please wait.");
            }
        } else {
            // Get a connection from the stack
            Connection con = connectionStack.pop();
            if (!isValidConnection(con)) {
                // If invalid, create a new connection
                try {
                    con = DriverManager.getConnection(url, db_user, db_pass);
                    activeConnections++;
                } catch (SQLException ex) {
                    throw new ConnectionException(ex.getMessage());
                }
            }
            idleConnections--;
            return con;
        }
    }

    public synchronized void releaseConnection(Connection con) {
        if (con != null) {
            if (isValidConnection(con)) {
                connectionStack.push(con);
                idleConnections++;
            } else {
                // If the connection is invalid, just close it
                try {
                    con.close();
                    activeConnections--;
                } catch (SQLException e) {
                    LOGGER.info("Error closing invalid connection");
                }
            }
        }
    }

    public synchronized void releaseAllConnections() {
        for (Connection con : connectionStack) {
            try {
                con.close();
            } catch (SQLException e) {
                LOGGER.info("Error closing connection");

            }
        }
        connectionStack.clear();
        activeConnections = 0;
        idleConnections = 0;
    }

    public synchronized String getConnectionStatistics() {
        return String.format("Total Connections: %d, Active: %d, Idle: %d",
                (activeConnections + idleConnections),
                activeConnections,
                idleConnections);
    }

    public synchronized int getActiveConnectionCount() {
        return activeConnections;
    }

    public synchronized int getIdleConnectionCount() {
        return idleConnections;
    }

}
