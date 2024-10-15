package Model;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.OutputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
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
public class MyServerSocket implements Runnable{

    // Logger for logging server activities
    private static final Logger logger = Logger.getLogger(MyServerSocket.class.getName());

    // Port number for the server to listen on
    private int port;
    private ServerSocket serverSocket;

    /**
     * Constructor for initializing the server with a specified port.
     * 
     * @param port the port number the server will listen on
     */
    public MyServerSocket(int port) {
        this.port = port;
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
            // Create the ServerSocket to accept client connections
            serverSocket = new ServerSocket(port);
            logger.info("Server is listening on port " + port);

            // Infinite loop to accept clients continuously
            while (true) {
                Socket clientSocket = serverSocket.accept(); // Wait for a client connection
                logger.info("New client connected");

                // Handle communication with the client in a new thread
                new Thread(() -> handleClient(clientSocket)).start(); // Each client in a separate thread
            }
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Server exception: " + e.getMessage(), e);
        }
    }

    /**
     * Handles communication with a connected client.
     * <p>This method processes incoming messages from the client, and based on the
     * message type, it performs appropriate actions. For example, a sign-up request
     * is processed, and a response is sent back to the client. The client connection
     * is closed after the communication is completed.</p>
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
            logger.info("Received message from client: " + clientMessage);

            // Example of processing the received message
            if (clientMessage != null && clientMessage.getTipo() == TipoMensaje.SIGN_UP_REQUEST) {
                logger.info("Processing sign-up request...");

                // You can perform any operations like user registration here.
                // For now, let's send back a response to the client.

                clientMessage.setTipo(TipoMensaje.OK_RESPONSE); // Set response type as OK
                clientMessage.getUser().setName("Server Response"); // Add example user response

                // Send response back to the client
                objectWriter.writeObject(clientMessage);
                logger.info("Response sent to client");
            }

            // Clean up: close the client socket and streams
            objectReader.close();
            objectWriter.close();
            clientSocket.close();
            logger.info("Client connection closed");

        } catch (IOException | ClassNotFoundException e) {
            logger.log(Level.SEVERE, "Client handling exception: " + e.getMessage(), e);
        }
    }
}
