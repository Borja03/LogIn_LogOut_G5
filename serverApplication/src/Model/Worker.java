package Model;

import ISignable.Signable;
import database.*;
import exception.*;
import java.io.*;
import java.net.Socket;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class represents a worker that handles communication with a single
 * client in a separate thread.
 *
 * @Author Borja
 */
public class Worker implements Runnable {

    private static final Logger logger = Logger.getLogger(Worker.class.getName());
    private Socket socket;
    private static final NumThread clientCounter = new NumThread(); // Thread-safe client counting
    private static final ResourceBundle config = ResourceBundle.getBundle("Utils.socketConfig");
    private static final int MAX_USERS = Integer.parseInt(config.getString("MAX_USERS"));

    /**
     * Constructor to initialize the worker with the client socket and client
     * counter.
     */
    public Worker(Socket socket) {
        this.socket = socket;
    }

    /**
     * Handles client communication, processes messages, and sends responses.
     */
    private void handleClient() {
        ObjectInputStream objectReader = null;
        ObjectOutputStream objectWriter = null;
        Message msg = null;

        try {
            if (clientCounter.value() < MAX_USERS) {
                clientCounter.increment();
                logger.info("Numero de hilos de cliente: " + clientCounter.value());
                objectReader = new ObjectInputStream(socket.getInputStream());
                msg = (Message) objectReader.readObject();

                // Log the entire message received for debugging
                logger.info("Mensaje recibido: " + msg);

                // Check if the user object is not null
                if (msg.getUser() != null) {
                    String userEmail = msg.getUser().getEmail();
                    switch (msg.getTipo()) {
                        case SIGN_IN_REQUEST:
                            logger.info("Iniciando sesión para el usuario: " + userEmail);
                            User user = DaoFactory.getSignable().signIn(msg.getUser());
                            msg.setUser(user);
                            if(user==null){
                                msg.setTipo(TipoMensaje.INCORRECT_CREDENTIALS_RESPONSE);
                            }else{
                                msg.setTipo(TipoMensaje.OK_RESPONSE);
                            }
                            break;

                        case SIGN_UP_REQUEST:
                            logger.info("Registrando usuario: " + userEmail);
                            user = DaoFactory.getSignable().signUp(msg.getUser());
                            msg.setUser(user);
                             if(user==null){
                                msg.setTipo(TipoMensaje.EMAIL_EXISTS);
                            }else{
                                msg.setTipo(TipoMensaje.OK_RESPONSE);
                            }
                            break;

                        default:
                            logger.warning("Tipo de mensaje desconocido: " + msg.getTipo());
                            msg.setTipo(TipoMensaje.SERVER_ERROR);
                            break;
                    }
                } else {
                    msg.setTipo(TipoMensaje.MAX_THREAD_USER);
                    logger.warning("Maximo de usuarios alcanzado.");
                }
            } else {
                logger.warning("Max users reached, rejecting new connection.");
            }

        } catch (IncorrectCredentialsException e) {
            msg.setTipo(TipoMensaje.INCORRECT_CREDENTIALS_RESPONSE);
            logger.log(Level.SEVERE, "Credenciales incorrectas para el usuario: " + (msg.getUser() != null ? msg.getUser().getEmail() : "Usuario no especificado"), e);
        } catch (UserAlreadyExistsException e) {
            msg.setTipo(TipoMensaje.EMAIL_EXISTS);
            logger.log(Level.SEVERE, "El usuario ya existe: " + (msg.getUser() != null ? msg.getUser().getEmail() : "Usuario no especificado"), e);
        } catch (ConnectionException e) {
            msg.setTipo(TipoMensaje.CONNECTION_ERROR);
            logger.log(Level.SEVERE, "Error de conexión", e);
        } catch (ServerErrorException e) {
            msg.setTipo(TipoMensaje.SERVER_ERROR);
            logger.log(Level.SEVERE, "Error de conexión", e);
        }catch (ClassNotFoundException e) {
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
            clientCounter.decrement(); // Remove the client from the counter
            logger.info("Client removed. Current number of connected clients: " + clientCounter.value());
        }
    }

    @Override
    public void run() {
        handleClient();
    }
}