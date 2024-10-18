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
 * <p>The server listens for incoming connections on the given port and processes
 * messages received from clients. Each message can be processed based on its type
 * and can generate an appropriate response.</p>
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
     * <p>This method creates a {@link ServerSocket} that listens on the specified
     * port. When a client connects, the server accepts the connection and spawns
     * a new thread to handle the communication with that client.</p>
     * <p>Each client is handled in a separate thread to ensure the server can
     * accept multiple clients concurrently.</p>
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

                        // Handle communication with the client in a new thread
                        new Thread(() -> handleClient(clientSocket)).start();
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
     * Handles communication with a connected client.
     * 
     * @param clientSocket the socket representing the client's connection
     */
  private void handleClient(Socket clientSocket) {
    try {
        // Input and output streams for communication with the client
        InputStream input = clientSocket.getInputStream();
        ObjectInputStream objectReader = new ObjectInputStream(input);

        OutputStream output = clientSocket.getOutputStream();
        ObjectOutputStream objectWriter = new ObjectOutputStream(output);

        // Read the message object sent by the client
        Message clientMessage = (Message) objectReader.readObject();
        
        // Log the received message
        logger.info("Received message from client: " + clientMessage);

        // Example of processing the received message
        if (clientMessage != null) {
            // Log the type of message received
            logger.info("Message type: " + clientMessage.getTipo());

            // Process specific message types
            if (clientMessage.getTipo() == TipoMensaje.SIGN_UP_REQUEST) {
                logger.info("Processing sign-up request...");
                // Process sign-up request...

                // Example response
                clientMessage.setTipo(TipoMensaje.OK_RESPONSE); // Set response type as OK
                clientMessage.getUser().setName("Server Response"); // Add example user response

                // Send response back to the client
                objectWriter.writeObject(clientMessage);
                logger.info("Response sent to client");
            }
            // Add additional message handling logic as needed
        }

        // Clean up: close the client socket and streams
        objectReader.close();
        objectWriter.close();
        clientSocket.close();
        logger.info("Client connection closed");

        // Decrement the number of connected clients
        removeClient();

    } catch (IOException | ClassNotFoundException e) {
        logger.log(Level.SEVERE, "Client handling exception: " + e.getMessage(), e);
    }
}


    /**
     * Sends a message to the client when the server has reached its maximum user limit.
     * 
     * @param clientSocket the client's socket that will receive the message
     */
   /* private void sendMaxUserMessage(Socket clientSocket) {
        try {
            ObjectOutputStream oos = new ObjectOutputStream(clientSocket.getOutputStream());
            Message maxUserMessage = new Message();
            maxUserMessage.setMsg(TipoMensaje.MAX_THREAD_USER); // Assuming a MAX_THREAD_USER enum value
            oos.writeObject(maxUserMessage);
            logger.info("Max user limit message sent to client.");
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Error sending max user message: " + e.getMessage(), e);
        }
    }*/

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
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public static void main(String[] args) {
        new MyServerSocket();
    }
}
