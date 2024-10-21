package Model;

import ISignable.Signable;
import database.*;
import exception.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Clase que representa un trabajador que maneja las solicitudes de conexión de
 * los clientes en el sistema. Implementa la interfaz {@link Runnable} para
 * permitir su ejecución en un hilo separado.
 */
public class Worker implements Runnable {

    private ObjectInputStream objectReader = null;
    private ObjectOutputStream objectWriter = null;
    private Socket socket;
    private Signable signable;
    private Message msg;
    private User user;
    private static final Logger LOGGER = Logger.getLogger(Worker.class.getName());

    /**
     * Constructor que recibe el socket del cliente.
     *
     * @param socket el socket de conexión con el cliente.
     */
    public Worker(Socket socket) {
        this.socket = socket;
    }

    /**
     * Método que se ejecuta al iniciar el hilo. Establece la comunicación con
     * el cliente, procesa el mensaje recibido y ejecuta las acciones
     * correspondientes (inicio de sesión o registro). Maneja diferentes tipos
     * de excepciones para gestionar errores y cierra las conexiones al
     * finalizar.
     */
  @Override
public void run() {
    try {
        objectReader = new ObjectInputStream(socket.getInputStream());
        DaoFactory factory = new DaoFactory();
        signable = factory.getSignable();
        msg = (Message) objectReader.readObject();

        // Log the entire message received for debugging
        LOGGER.info("Mensaje recibido: " + msg);

        // Check if the user object is not null
        if (msg.getUser() != null) {
            String userEmail = msg.getUser().getEmail();
            switch (msg.getTipo()) {
                case SIGN_IN_REQUEST:
                    LOGGER.info("Iniciando sesión para el usuario: " + userEmail); // This log should happen before the sign-in
                    user = signable.signIn(msg.getUser());
                    msg.setUser(user);
                    msg.setTipo(user == null ? TipoMensaje.SERVER_ERROR : TipoMensaje.OK_RESPONSE);
                    break;

                case SIGN_UP_REQUEST:
                    LOGGER.info("Registrando usuario: " + userEmail);
                    user = signable.signUp(msg.getUser());
                    msg.setUser(user);
                    msg.setTipo(user == null ? TipoMensaje.SERVER_ERROR : TipoMensaje.OK_RESPONSE);
                    break;

                default:
                    LOGGER.warning("Tipo de mensaje desconocido: " + msg.getTipo());
                    msg.setTipo(TipoMensaje.SERVER_ERROR);
                    break;
            }
        } else {
            LOGGER.warning("Mensaje recibido sin usuario asociado.");
        }

    } catch (IncorrectCredentialsException e) {
        msg.setTipo(TipoMensaje.INCORRECT_CREDENTIALS_RESPONSE);
        LOGGER.log(Level.SEVERE, "Credenciales incorrectas para el usuario: " + (msg.getUser() != null ? msg.getUser().getEmail() : "Usuario no especificado"), e);
    } catch (UserAlreadyExistsException e) {
        msg.setTipo(TipoMensaje.EMAIL_EXISTS);
        LOGGER.log(Level.SEVERE, "El usuario ya existe: " + (msg.getUser() != null ? msg.getUser().getEmail() : "Usuario no especificado"), e);
    } catch (ConnectionException e) {
        msg.setTipo(TipoMensaje.SERVER_ERROR);
        LOGGER.log(Level.SEVERE, "Error de conexión", e);
    } catch (ClassNotFoundException e) {
        msg.setTipo(TipoMensaje.SERVER_ERROR);
        LOGGER.log(Level.SEVERE, "Clase no encontrada", e);
    } catch (IOException e) {
        msg.setTipo(TipoMensaje.SERVER_ERROR);
        LOGGER.log(Level.SEVERE, "Error de entrada/salida", e);
    } catch (Exception ex) {
        LOGGER.log(Level.SEVERE, "Error inesperado", ex);
    } finally {
        try {
            LOGGER.info("Cerrando conexiones");
            objectWriter = new ObjectOutputStream(socket.getOutputStream());
            objectWriter.writeObject(msg);
            objectReader.close();
            objectWriter.close();
            socket.close();
        } catch (IOException ex) {
            LOGGER.log(Level.SEVERE, "Error al cerrar las conexiones", ex);
        }
        MyServerSocket.removeClient(); // Remove the client from the counter
    }
}

}
