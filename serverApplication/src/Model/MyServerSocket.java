package Model;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class represents a custom server socket that listens for client connections,
 * processes incoming requests, and sends responses back to the clients.
 * The server operates on a specified port and handles each client in a separate thread.
 * 
 * @author Borja
 */
public class MyServerSocket implements Runnable {

    // Logger for logging server activities
    private static final Logger logger = Logger.getLogger(MyServerSocket.class.getName());

    // Load configuration settings
    private static final ResourceBundle config = ResourceBundle.getBundle("Utils.socketConfig");
    private static final int MAX_USERS = Integer.parseInt(config.getString("MAX_USERS"));
    private static final int PORT = Integer.parseInt(config.getString("PORT"));

    // Server state variables
    private static boolean serverOn = true;
    private ServerSocket serverSocket;
    private static Integer connectedClients = 0;  // Client counter, synchronized for thread-safety

    /**
     * Constructor for initializing the server with the port from configuration.
     */
    public MyServerSocket() {
        this.start();
    }

    /**
     * Starts the server and begins listening for incoming client connections.
     */
    public void start() {
        try {
            serverSocket = new ServerSocket(PORT);
            logger.info("Server is listening on port " + PORT);

            // Start thread to listen for 'q' input to close the server
            Thread keyboardThread = new Thread(() -> {
                BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
                logger.info("Press 'q' to stop the server.");
                try {
                    while (true) {
                        String input = reader.readLine();
                        if (input != null && input.equalsIgnoreCase("q")) {
                            stopServer();
                            break;
                        }
                    }
                } catch (IOException e) {
                    logger.log(Level.SEVERE, "Error reading input", e);
                }
            });
            keyboardThread.start();

            // Accept clients continuously while server is on
            while (serverOn) {
                logger.info("Listening for new connections...");
                try {
                    synchronized (connectedClients) {
                        if (connectedClients < MAX_USERS) {
                            // Accept client connections
                            Socket clientSocket = serverSocket.accept(); // Wait for a client connection
                            logger.info("New client connected");

                            // Increment the number of connected clients
                            addClient();

                            // Create a Worker to handle communication with the client
                            Worker worker = new Worker(clientSocket);
                            new Thread(worker).start(); // Start the worker thread
                        } else {
                            logger.warning("Max users reached, rejecting new connection.");
                            Socket tempSocket = serverSocket.accept();
                            tempSocket.close(); // Close the connection
                        }
                    }
                } catch (SocketException e) {
                    // If the socket is closed, exit the loop gracefully
                    logger.warning("Server socket closed. Stopping accept loop.");
                    break;
                } catch (IOException e) {
                    logger.log(Level.SEVERE, "IO Exception: " + e.getMessage(), e);
                }
            }
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Server exception: " + e.getMessage(), e);
        }
    }

    private void stopServer() {
        try {
            serverOn = false; // Set serverOn to false
            if (serverSocket != null && !serverSocket.isClosed()) {
                serverSocket.close(); // Close the server socket
            }
            logger.info("Server has been stopped.");
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Error stopping the server: " + e.getMessage(), e);
        }
    }

    /**
     * Synchronized method to add a client (increment client counter).
     */
    public static synchronized void addClient() {
        connectedClients++;
        logger.info("Client added. Current number of connected clients: " + connectedClients);
    }

    /**
     * Synchronized method to remove a client (decrement client counter).
     */
    public static synchronized void removeClient() {
        connectedClients--;
        logger.info("Client removed. Current number of connected clients: " + connectedClients);
    }

    @Override
    public void run() {
        // Main execution is handled in start()
    }

    public static void main(String[] args) {
        new MyServerSocket();
    }
}
