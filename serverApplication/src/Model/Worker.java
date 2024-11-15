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
 * Esta clase representa un trabajador que maneja la comunicación con un único
 * cliente en un hilo separado.
 *
 * <p>El {@link Worker} es responsable de procesar los mensajes entrantes de los clientes,
 * manejar las solicitudes de inicio de sesión y registro, y enviar respuestas
 * adecuadas de vuelta al cliente. Se asegura de que el número máximo de usuarios
 * conectados no se exceda y realiza el seguimiento de las conexiones activas.</p>
 *
 * @author Alder and Borja
 */
public class Worker implements Runnable {

    private static final Logger logger = Logger.getLogger(Worker.class.getName());
    private Socket socket;
    private static final NumThread clientCounter = new NumThread(); // Contador de clientes seguro para hilos
    private static final ResourceBundle config = ResourceBundle.getBundle("config/config");
    private static final int MAX_USERS = Integer.parseInt(config.getString("MAX_USERS"));

    /**
     * Constructor para inicializar el trabajador con el socket del cliente.
     *
     * @param socket El socket del cliente con el que se va a comunicar.
     */
    public Worker(Socket socket) {
        this.socket = socket;
    }

    /**
     * Maneja la comunicación con el cliente, procesa los mensajes y envía respuestas.
     *
     * <p>Este método lee los mensajes enviados por el cliente, determina el tipo de
     * solicitud (inicio de sesión o registro) y ejecuta las acciones correspondientes
     * utilizando la capa de acceso a datos. También maneja excepciones específicas
     * relacionadas con las operaciones de usuario.</p>
     */
    private void handleClient() {
        ObjectInputStream objectReader = null;
        ObjectOutputStream objectWriter = null;
        Message msg = null;

        try {
            if (clientCounter.value() < MAX_USERS) {
                clientCounter.increment();
                logger.info("Número de hilos de cliente: " + clientCounter.value());
                objectReader = new ObjectInputStream(socket.getInputStream());
                msg = (Message) objectReader.readObject();

                // Registro del mensaje completo recibido para depuración
                logger.info("Mensaje recibido: " + msg);

                // Verifica si el objeto de usuario no es nulo
                if (msg.getUser() != null) {
                    String userEmail = msg.getUser().getEmail();
                    switch (msg.getTipo()) {
                        case SIGN_IN_REQUEST:
                            logger.info("Iniciando sesión para el usuario: " + userEmail);
                            User user = DaoFactory.getSignable().signIn(msg.getUser());
                            msg.setUser(user);
                            if (user == null) {
                                msg.setTipo(TipoMensaje.INCORRECT_CREDENTIALS_RESPONSE);
                            } else {
                                msg.setTipo(TipoMensaje.OK_RESPONSE);
                            }
                            break;

                        case SIGN_UP_REQUEST:
                            logger.info("Registrando usuario: " + userEmail);
                            user = DaoFactory.getSignable().signUp(msg.getUser());
                            msg.setUser(user);
                            if (user == null) {
                                msg.setTipo(TipoMensaje.EMAIL_EXISTS);
                            } else {
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
                    logger.warning("Máximo de usuarios alcanzado.");
                }
            } else {
                logger.warning("Máximo de usuarios alcanzado, rechazando nueva conexión.");
            }

        } catch (IncorrectCredentialsException e) {
            msg.setTipo(TipoMensaje.INCORRECT_CREDENTIALS_RESPONSE);
            logger.log(Level.SEVERE, "Credenciales incorrectas para el usuario: " + 
                (msg.getUser() != null ? msg.getUser().getEmail() : "Usuario no especificado"), e);
        } catch (UserAlreadyExistsException e) {
            msg.setTipo(TipoMensaje.EMAIL_EXISTS);
            logger.log(Level.SEVERE, "El usuario ya existe: " + 
                (msg.getUser() != null ? msg.getUser().getEmail() : "Usuario no especificado"), e);
        } catch (ConnectionException e) {
            msg.setTipo(TipoMensaje.CONNECTION_ERROR);
            logger.log(Level.SEVERE, "Error de conexión", e);
        } catch (ServerErrorException e) {
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
            clientCounter.decrement(); // Eliminar el cliente del contador
            logger.info("Cliente eliminado. Número actual de clientes conectados: " + clientCounter.value());
        }
    }

    @Override
    public void run() {
        handleClient();
    }
}
