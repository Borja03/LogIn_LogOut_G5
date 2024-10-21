package Model;

import ISignable.Signable;
import database.*;
import exception.*;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Clase que representa un trabajador que maneja las solicitudes de 
 * conexión de los clientes en el sistema. Implementa la interfaz 
 * {@link Runnable} para permitir su ejecución en un hilo separado.
 * 
 * <p>El {@code Worker} se encarga de procesar mensajes de tipo 
 * {@link Message}, gestionando operaciones de inicio y registro de 
 * sesión. Utiliza un objeto {@link Signable} para realizar las 
 * operaciones de autenticación y mantiene un flujo de entrada y salida 
 * de objetos para comunicarse con el cliente.</p>
 * 
 * <p>Al recibir un mensaje, el trabajador lo procesa y envía una 
 * respuesta correspondiente al cliente, manejando excepciones 
 * relacionadas con las credenciales y el estado de la conexión.</p>
 * 
 * @author Alder y Borja
 */
public class Worker implements Runnable {

    /** Flujo de entrada de objetos para leer mensajes del cliente. */
    private ObjectInputStream objectReader = null;

    /** Flujo de salida de objetos para enviar mensajes al cliente. */
    private ObjectOutputStream objectWriter = null;

    /** Socket de conexión con el cliente. */
    private Socket socket;

    /** Objeto que implementa la interfaz {@link Signable} para 
     * gestionar la autenticación. */
    private Signable signable;

    /** Tipo de mensaje que se está procesando. */
    private TipoMensaje type;

    /** Mensaje que se recibe y se envía al cliente. */
    private Message msg;

    /** Usuario asociado a la solicitud. */
    private User user;

    /** Logger para registrar eventos y errores. */
    private static final Logger LOGGER = Logger.getLogger(Worker.class.getName());

    /**
     * Método que se ejecuta al iniciar el hilo. Establece la 
     * comunicación con el cliente, procesa el mensaje recibido y 
     * ejecuta las acciones correspondientes (inicio de sesión o 
     * registro). Maneja diferentes tipos de excepciones para 
     * gestionar errores y cierra las conexiones al finalizar.
     */
    @Override
    public void run() {
        try {
              LOGGER.info("Start run ");
            objectReader = new ObjectInputStream(socket.getInputStream());
            DaoFactory factory = new DaoFactory();
            signable = factory.getSignable();
            msg = (Message) objectReader.readObject();
            switch (msg.getTipo()) {
                case SIGN_IN_REQUEST:
                    LOGGER.info("Iniciando sesión");
                    user = signable.signIn(msg.getUser());
                    msg.setUser(user);
                    if (user == null) {
                        msg.setTipo(type.SERVER_ERROR);
                    } else {
                        msg.setTipo(type.OK_RESPONSE);
                    }
                    break;

                case SIGN_UP_REQUEST:
                    LOGGER.info("Registrando usuario");
                    user = signable.signUp(msg.getUser());
                    msg.setUser(user);
                    if (user == null) {
                        msg.setTipo(type.SERVER_ERROR);
                    } else {
                        msg.setTipo(type.OK_RESPONSE);
                    }
                    break;
            }

        } catch (IncorrectCredentialsException e) {
            msg.setTipo(type.INCORRECT_CREDENTIALS_RESPONSE);
            LOGGER.log(Level.SEVERE, "Credenciales incorrectas", e);
        } catch (UserAlreadyExistsException e) {
            msg.setTipo(type.EMAIL_EXISTS);
            LOGGER.log(Level.SEVERE, "El usuario ya existe", e);
        } catch (ConnectionException e) {
            msg.setTipo(type.SERVER_ERROR);
            LOGGER.log(Level.SEVERE, "Error de conexión", e);
        } catch (ClassNotFoundException e) {
            msg.setTipo(type.SERVER_ERROR);
            LOGGER.log(Level.SEVERE, "Clase no encontrada", e);
        } catch (IOException e) {
            msg.setTipo(type.SERVER_ERROR);
            LOGGER.log(Level.SEVERE, "Error de entrada/salida", e);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, "Error inesperado", ex);
        } finally {
            try {
                LOGGER.info("Cerrando conexiones");
                objectWriter = new ObjectOutputStream(socket.getOutputStream());
                objectWriter.writeObject(msg);
                // Crear función para borrar cliente (si es necesario)
                objectReader.close();
                objectWriter.close();
                socket.close();
            } catch (IOException ex) {
                LOGGER.log(Level.SEVERE, "Error al cerrar las conexiones", ex);
            }
        }
    }
}
