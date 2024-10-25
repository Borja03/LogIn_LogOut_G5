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
 * processes incoming requests, and creates Worker instances to handle each client.
 * 
 * @Author Borja
 */
public class Server implements Runnable {

    // Logger for logging server activities
    private static final Logger logger = Logger.getLogger(Server.class.getName());

    // Load configuration settings
    private static final ResourceBundle config = ResourceBundle.getBundle("Utils.socketConfig");
    private static final int MAX_USERS = Integer.parseInt(config.getString("MAX_USERS"));
    private static final int PORT = Integer.parseInt(config.getString("PORT"));

    // Server state variables
    private static boolean serverOn = true;
    private ServerSocket serverSocket;
    private static final numThread clientCounter = new numThread(); // Thread-safe client counting

    /**
     * Constructor for initializing the server with the port from configuration.
     */
    public Server() {
        this.start();
    }

    /**
     * Starts the server and begins listening for incoming client connections.
     */
    public void start() {
        try {
            serverSocket = new ServerSocket(PORT);
            logger.info("Server is listening on port " + PORT);

            // Start a dedicated thread to listen for 'q' input to close the server
            new Thread(this::keyboardListener).start();

            // Accept clients continuously while server is on
            while (serverOn) {
                logger.info("Listening for new connections...");
                try {
                    synchronized (clientCounter) {
                        if (clientCounter.value() < MAX_USERS) {
                            // Accept client connections
                            Socket clientSocket = serverSocket.accept();
                            logger.info("New client connected");

                            // Increment the number of connected clients
                            clientCounter.increment();

                            // Create a new thread to handle communication with the client
                            new Thread(new Worker(clientSocket, clientCounter)).start();
                        } else {
                            logger.warning("Max users reached, rejecting new connection.");
                            Socket tempSocket = serverSocket.accept();
                            tempSocket.close();
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

    /**
     * Dedicated method to listen for 'q' input to close the server.
     */
    private void keyboardListener() {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        logger.info("Press 'q' to stop the server.");
        try {
            while (serverOn) {
                String input = reader.readLine();
                if (input != null && input.equalsIgnoreCase("q")) {
                    stopServer();
                    break;
                }
            }
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Error reading input", e);
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

    @Override
    public void run() {
        // Main execution is handled in start()
    }
}
   