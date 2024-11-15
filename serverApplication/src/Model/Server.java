package Model;

import database.DBPool;
import exception.ConnectionException;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Esta clase representa un servidor personalizado que escucha conexiones de clientes,
 * procesa solicitudes entrantes y crea instancias de {@link Worker} para manejar
 * cada cliente.
 *
 * <p>El servidor se ejecuta en un puerto específico configurado a través de un
 * archivo de recursos y permite múltiples conexiones simultáneas mediante hilos.</p>
 *
 * @author Borja
 */
public class Server {

    // Logger para registrar actividades del servidor
    private static final Logger logger = Logger.getLogger(Server.class.getName());

    // Carga de configuraciones
    private static final ResourceBundle config = ResourceBundle.getBundle("config/config");
    private static final int PORT = Integer.parseInt(config.getString("PORT"));

    // Variables de estado del servidor
    private static boolean serverOn = true;
    private ServerSocket serverSocket;

    /**
     * Constructor para inicializar el servidor con el puerto de la configuración.
     */
    public Server() {
        this.startServer();
    }

    /**
     * Inicia el servidor y comienza a escuchar conexiones de clientes entrantes.
     *
     * <p>El servidor aceptará conexiones continuamente hasta que se indique lo contrario.
     * Se ejecuta un hilo dedicado para escuchar la entrada del teclado para cerrar el
     * servidor de forma controlada.</p>
     */
    public void startServer() {
        try {
            serverSocket = new ServerSocket(PORT);
            logger.info("El servidor está escuchando en el puerto " + PORT);

            // Inicia un hilo dedicado para escuchar la entrada 'q' para cerrar el servidor
            new Thread(this::keyboardListener).start();

            // Acepta clientes continuamente mientras el servidor esté activo
            while (serverOn) {
                logger.info("Esperando nuevas conexiones...");
                try {
                    // Acepta conexiones de clientes
                    Socket clientSocket = serverSocket.accept();
                    logger.info("Nuevo cliente conectado");

                    // Crea un nuevo hilo para manejar la comunicación con el cliente
                    new Thread(new Worker(clientSocket)).start();

                } catch (SocketException e) {
                    // Si el socket está cerrado, salir del bucle de forma controlada
                    logger.warning("Socket del servidor cerrado. Deteniendo el bucle de aceptación.");
                    break;
                } catch (IOException e) {
                    logger.log(Level.SEVERE, "Excepción de IO: " + e.getMessage(), e);
                }
            }
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Excepción del servidor: " + e.getMessage(), e);
        }
    }

    /**
     * Método dedicado para escuchar la entrada 'q' para cerrar el servidor.
     *
     * <p>Este método lee la entrada del teclado y, al recibir 'q', detiene
     * el servidor de forma controlada.</p>
     */
    private void keyboardListener() {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        logger.info("Presione 'q' para detener el servidor.");
        try {
            while (serverOn) {
                String input = reader.readLine();
                if (input != null && input.equalsIgnoreCase("q")) {
                    stopServer();
                    break;
                }
            }
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Error al leer la entrada", e);
        }
    }

    /**
     * Detiene el servidor y cierra el socket del servidor.
     *
     * <p>Este método establece la variable {@code serverOn} a false,
     * cierra el socket del servidor y registra la detención del servidor.</p>
     */
    private void stopServer() {
        try {
            
            DBPool.getInstance().releaseAllConnections();
            serverOn = false; // Set serverOn to false
            serverOn = false; // Establece serverOn a false
            if (serverSocket != null && !serverSocket.isClosed()) {
                serverSocket.close(); // Cierra el socket del servidor
            }
            logger.info("El servidor ha sido detenido.");
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Error stopping the server: " + e.getMessage(), e);
        } catch (ConnectionException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
            logger.log(Level.SEVERE, "Error al detener el servidor: " + ex.getMessage(), ex);
        }
    }
}
