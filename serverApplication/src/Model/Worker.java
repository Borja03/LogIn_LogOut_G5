package Model;

import ISignable.Signable;
import database.*;
import exception.*;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import ISignable.Signable;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class represents a custom server socket that listens for client connections,
 * processes incoming requests, and sends responses back to the clients.
 * The server operates on a specified port and handles each client in a separate thread.
 * 
 * @author Borja
 */
public class Worker implements Runnable {

    // Logger for logging server activities
    private static final Logger logger = Logger.getLogger(Worker.class.getName());

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
    public Worker() {
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

                            // Create a new thread to handle communication with the client
                            new Thread(() -> handleClient(clientSocket)).start(); // Start the worker thread
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

    private void handleClient(Socket socket) {
        ObjectInputStream objectReader = null;
        ObjectOutputStream objectWriter = null;
        Signable signable = new DaoFactory().getSignable();
        Message msg = null;

        try {
            objectReader = new ObjectInputStream(socket.getInputStream());
            msg = (Message) objectReader.readObject();

            // Log the entire message received for debugging
            logger.info("Mensaje recibido: " + msg);

            // Check if the user object is not null
            if (msg.getUser() != null) {
                String userEmail = msg.getUser().getEmail();
                switch (msg.getTipo()) {
                    case SIGN_IN_REQUEST:
                        logger.info("Iniciando sesión para el usuario: " + userEmail); // This log should happen before the sign-in
                        User user = signable.signIn(msg.getUser());
                        msg.setUser(user);
                        msg.setTipo(user == null ? TipoMensaje.SERVER_ERROR : TipoMensaje.OK_RESPONSE);
                        break;

                    case SIGN_UP_REQUEST:
                        logger.info("Registrando usuario: " + userEmail);
                        user = signable.signUp(msg.getUser());
                        msg.setUser(user);
                        msg.setTipo(user == null ? TipoMensaje.SERVER_ERROR : TipoMensaje.OK_RESPONSE);
                        break;

                    default:
                        logger.warning("Tipo de mensaje desconocido: " + msg.getTipo());
                        msg.setTipo(TipoMensaje.SERVER_ERROR);
                        break;
                }
            } else {
                logger.warning("Mensaje recibido sin usuario asociado.");
            }

        } catch (IncorrectCredentialsException e) {
            msg.setTipo(TipoMensaje.INCORRECT_CREDENTIALS_RESPONSE);
            logger.log(Level.SEVERE, "Credenciales incorrectas para el usuario: " + (msg.getUser() != null ? msg.getUser().getEmail() : "Usuario no especificado"), e);
        } catch (UserAlreadyExistsException e) {
            msg.setTipo(TipoMensaje.EMAIL_EXISTS);
            logger.log(Level.SEVERE, "El usuario ya existe: " + (msg.getUser() != null ? msg.getUser().getEmail() : "Usuario no especificado"), e);
        } catch (ConnectionException e) {
            msg.setTipo(TipoMensaje.SERVER_ERROR);
            logger.log(Level.SEVERE, "Error de conexión", e);
        } catch (ClassNotFoundException e) {
            msg.setTipo(TipoMensaje.SERVER_ERROR);
            logger.log(Level.SEVERE, "Clase no encontrada", e);
        } catch (IOException e) {
            msg.setTipo(TipoMensaje.SERVER_ERROR);
            logger.log(Level.SEVERE, "Error de entrada/salida", e);
        } catch (Exception ex) {
            logger.log(Level.SEVERE, "Error inesperado", ex);
        } finally {
            try {
                logger.info("Cerrando conexiones");
                objectWriter = new ObjectOutputStream(socket.getOutputStream());
                objectWriter.writeObject(msg);
                objectReader.close();
                objectWriter.close();
                socket.close();
            } catch (IOException ex) {
                logger.log(Level.SEVERE, "Error al cerrar las conexiones", ex);
            }
            removeClient(); // Remove the client from the counter
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
        new Worker();
    }
}
