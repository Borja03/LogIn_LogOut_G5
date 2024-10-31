package database;

import exception.ConnectionException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.Stack;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Singleton class that manages a pool of database connections.
 * It provides methods to acquire and release database connections while managing
 * a stack of available connections and tracking active and idle connections.
 * @author Omar
 */
public class DBPool {

      /** ResourceBundle for loading configuration properties. */
    private ResourceBundle configFile;

    /** The database username for authentication. */
    private String db_user;

    /** The database password for authentication. */
    private String db_pass;

    /** The database connection URL. */
    private String url;

    /** Stack of available database connections. */
    private static Stack<Connection> connectionStack = new Stack<>();

    /** Maximum number of connections allowed in the pool. */
    private int maxConnections;

    /** Counter for currently active connections. */
    private int activeConnections = 0;

    /** Counter for currently idle connections. */
    private int idleConnections = 0;

    /** Singleton instance of the DBPool. */
    private static DBPool instance;

    private static final Logger LOGGER = Logger.getLogger(DBPool.class.getName());

    /**
     * Private constructor to prevent instantiation from outside.
     * Loads the configuration and initializes the connections.
     *
     * @throws ConnectionException if there is an error during connection initialization.
     */
    private DBPool() throws ConnectionException {
        loadConfiguration();
        initializeConnections();
    }

    /**
     * Retrieves the singleton instance of the DBPool.
     *
     * @return the single instance of DBPool.
     * @throws ConnectionException if there is an error during connection initialization.
     */
    public static synchronized DBPool getInstance() throws ConnectionException {
        if (instance == null) {
            instance = new DBPool();
        }
        return instance;
    }

    /**
     * Loads the database configuration from a properties file.
     * Sets the database URL, username, password, and max connections.
     *
     * @throws RuntimeException if any required configuration is missing.
     */
    private void loadConfiguration() {
           LOGGER.info("Start loading Database configuration");
        url = "jdbc:postgresql://192.168.142.130:5432/odoooo";
        db_user = "odoo";
        db_pass = "abcd*1234";
        maxConnections = 3;

        if (url == null || db_user == null || db_pass == null) {
            throw new RuntimeException("Missing required configuration for database connection.");
        }
        LOGGER.info("Database configuration loaded successfully.");
    }

    /**
     * Initializes the database connections and pushes them onto the stack.
     *
     * @throws ConnectionException if there is an error initializing connections.
     */
    private void initializeConnections() throws ConnectionException {
        for (int i = 0; i < maxConnections; i++) {
            try {
                Connection con = DriverManager.getConnection(url, db_user, db_pass);
                connectionStack.push(con);
                idleConnections++;
            } catch (SQLException ex) {
                LOGGER.log(Level.SEVERE, "Error initializing connection", ex);
                throw new ConnectionException("Error database connection");
            }
        }
        LOGGER.info("Connections initialized successfully.");
    }

    /**
     * Checks if a given connection is valid.
     *
     * @param con the connection to check.
     * @return true if the connection is valid, false otherwise.
     */
    private boolean isValidConnection(Connection con) {
        try {
            return con != null && !con.isClosed() && con.isValid(2);
        } catch (SQLException e) {
            return false;
        }
    }

    /**
     * Acquires a database connection from the pool.
     *
     * @return a valid database connection.
     * @throws ConnectionException if there are no available connections.
     */
    public synchronized Connection getConnection() throws ConnectionException {
        if (connectionStack.isEmpty()) {
            if (activeConnections < maxConnections) {
                try {
                    Connection con = DriverManager.getConnection(url, db_user, db_pass);
                    activeConnections++;
                    LOGGER.info("New connection created. Active connections: " + activeConnections);
                    return con;
                } catch (SQLException ex) {
                    throw new ConnectionException(ex.getMessage());
                }
            } else {
                throw new ConnectionException("Maximum number of connections reached, please wait.");
            }
        } else {
            Connection con = connectionStack.pop();
            if (!isValidConnection(con)) {
                try {
                    con = DriverManager.getConnection(url, db_user, db_pass);
                    activeConnections++;
                    LOGGER.info("Invalid connection replaced with a new one. Active connections: " + activeConnections);
                } catch (SQLException ex) {
                    throw new ConnectionException(ex.getMessage());
                }
            }
            idleConnections--;
            LOGGER.info("Connection acquired from stack. Idle connections: " + idleConnections);
            return con;
        }
    }

    /**
     * Releases a database connection back to the pool.
     *
     * @param con the connection to release.
     */
    public synchronized void releaseConnection(Connection con) {
        if (con != null) {
            if (isValidConnection(con)) {
                connectionStack.push(con);
                idleConnections++;
                LOGGER.info("Connection released back to pool. Idle connections: " + idleConnections);
            } else {
                try {
                    con.close();
                    activeConnections--;
                    LOGGER.info("Invalid connection closed. Active connections: " + activeConnections);
                } catch (SQLException e) {
                    LOGGER.log(Level.WARNING, "Error closing invalid connection", e);
                }
            }
        }
    }

    /**
     * Releases all database connections and clears the connection stack.
     */
    public synchronized void releaseAllConnections() {
        for (Connection con : connectionStack) {
            try {
                con.close();
            } catch (SQLException e) {
                LOGGER.log(Level.WARNING, "Error closing connection", e);
            }
        }
        connectionStack.clear();
        activeConnections = 0;
        idleConnections = 0;
        LOGGER.info("All connections released. Active connections reset to 0.");
    }

    /**
     * Gets statistics about the current connections.
     *
     * @return a string summarizing total, active, and idle connections.
     */
    public synchronized String getConnectionStatistics() {
        return String.format("Total Connections: %d, Active: %d, Idle: %d",
                (activeConnections + idleConnections),
                activeConnections,
                idleConnections);
    }

    /**
     * Gets the count of active connections.
     *
     * @return the number of active connections.
     */
    public synchronized int getActiveConnectionCount() {
        return activeConnections;
    }

    /**
     * Gets the count of idle connections.
     *
     * @return the number of idle connections.
     */
    public synchronized int getIdleConnectionCount() {
        return idleConnections;
    }
}
