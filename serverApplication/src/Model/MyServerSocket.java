package Model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket; // Import the correct ServerSocket class
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MyServerSocket {

    private static final Logger logger = Logger.getLogger(MyServerSocket.class.getName());
    private int port;
    private ServerSocket serverSocket; // Use the correct ServerSocket type from java.net

    // Constructor that accepts the server's port
    public MyServerSocket(int port) {
        this.port = port;
    }

    // Method to start the server
    public void start() {
        try {
            // Create the ServerSocket
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

    // Method that handles communication with the client
    private void handleClient(Socket clientSocket) {
        try {
            InputStream input = clientSocket.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(input));

            OutputStream output = clientSocket.getOutputStream();
            PrintWriter writer = new PrintWriter(output, true);

            // Loop to keep receiving messages from the client
            String message;
            while ((message = reader.readLine()) != null) {
                logger.info("Received from client: " + message);

                // Send a response to the client
                writer.println("Hello from server! You sent: " + message);

                // Optionally, perform more operations based on the client's message
            }

            // After the client closes connection, we also close the client socket
           /* clientSocket.close();
            logger.info("Client connection closed");*/

        } catch (IOException e) {
            logger.log(Level.SEVERE, "Client handling exception: " + e.getMessage(), e);
        }
    }
}
