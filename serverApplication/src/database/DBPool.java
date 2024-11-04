package database;

import exception.ConnectionException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.Stack;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * La clase <code>DBPool</code> implementa un pool de conexiones a una base de datos.
 * Permite gestionar un conjunto limitado de conexiones, asegurando un uso eficiente
 * de los recursos y mejorando el rendimiento en aplicaciones que requieren múltiples
 * accesos a la base de datos.
 *
 * <p>Esta clase utiliza un patrón singleton para garantizar que solo haya una instancia
 * del pool de conexiones en toda la aplicación. Se encarga de la creación, validación
 * y liberación de conexiones a la base de datos.</p>
 *
 * @author Borja
 */
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

    /**
     * Constructor que inicializa el pool de conexiones cargando la configuración
     * y creando las conexiones necesarias.
     *
     * @throws ConnectionException Si ocurre un error al establecer la conexión
     *                             con la base de datos.
     */
    public DBPool() throws ConnectionException {
        loadConfiguration();
        initializeConnections();
    }

    /**
     * Método estático que devuelve la instancia única de <code>DBPool</code>.
     *
     * @return La instancia única de <code>DBPool</code>.
     * @throws ConnectionException Si ocurre un error al crear la instancia.
     */
    public static synchronized DBPool getInstance() throws ConnectionException {
        if (instance == null) {
            instance = new DBPool();
        }
        return instance;
    }

    /**
     * Carga la configuración necesaria para establecer conexiones a la base de datos.
     * 
     * @throws RuntimeException Si faltan valores requeridos en la configuración.
     */
    private void loadConfiguration() {
        // Cargar URL y credenciales de la base de datos
        url = "jdbc:postgresql://192.168.142.130:5432/odoooo";
        db_user = "odoo";
        db_pass = "abcd*1234";

        // Número máximo de conexiones permitidas
        maxConnections = 3;

        if (url == null || db_user == null || db_pass == null) {
            throw new RuntimeException("Missing required configuration for database connection.");
        }
    }

    /**
     * Inicializa las conexiones y las agrega a la pila de conexiones disponibles.
     *
     * @throws ConnectionException Si ocurre un error al inicializar las conexiones.
     */
    private void initializeConnections() throws ConnectionException {
        for (int i = 0; i < maxConnections; i++) {
            try {
                Connection con = DriverManager.getConnection(url, db_user, db_pass);
                connectionStack.push(con);
                idleConnections++;
            } catch (SQLException ex) {
                LOGGER.info("Error initializing connection");
                throw new ConnectionException("Error database connection");
            }
        }
    }

    /**
     * Verifica si una conexión es válida.
     *
     * @param con La conexión a verificar.
     * @return <code>true</code> si la conexión es válida; de lo contrario, <code>false</code>.
     */
    private boolean isValidConnection(Connection con) {
        try {
            return con != null && !con.isClosed() && con.isValid(2);
        } catch (SQLException e) {
            return false;
        }
    }

    /**
     * Obtiene una conexión del pool.
     *
     * @return Una conexión de la base de datos.
     * @throws ConnectionException Si no hay conexiones disponibles o se alcanza
     *                             el límite máximo de conexiones.
     */
    public synchronized Connection getConnection() throws ConnectionException {
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
                throw new ConnectionException("Maximum number of connections reached, please wait.");
            }
        } else {
            Connection con = connectionStack.pop();
            if (!isValidConnection(con)) {
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

    /**
     * Libera una conexión de vuelta al pool.
     *
     * @param con La conexión a liberar.
     */
    public synchronized void releaseConnection(Connection con) {
        if (con != null) {
            if (isValidConnection(con)) {
                connectionStack.push(con);
                idleConnections++;
            } else {
                try {
                    con.close();
                    activeConnections--;
                } catch (SQLException e) {
                    LOGGER.info("Error closing invalid connection");
                }
            }
        }
    }

    /**
     * Libera todas las conexiones en el pool, cerrándolas adecuadamente.
     */
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

    /**
     * Obtiene estadísticas sobre las conexiones en el pool.
     *
     * @return Un string con información sobre el número total de conexiones,
     *         conexiones activas e inactivas.
     */
    public synchronized String getConnectionStatistics() {
        return String.format("Total Connections: %d, Active: %d, Idle: %d",
                (activeConnections + idleConnections),
                activeConnections,
                idleConnections);
    }

    /**
     * Obtiene el número de conexiones activas.
     *
     * @return El número de conexiones activas.
     */
    public synchronized int getActiveConnectionCount() {
        return activeConnections;
    }

    /**
     * Obtiene el número de conexiones inactivas.
     *
     * @return El número de conexiones inactivas.
     */
    public synchronized int getIdleConnectionCount() {
        return idleConnections;
    }
}
