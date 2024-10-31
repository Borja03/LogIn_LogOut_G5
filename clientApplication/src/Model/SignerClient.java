package Model;

import ISignable.Signable;
import static Utils.UtilsMethods.logger;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ResourceBundle;
import java.util.logging.Logger;
import exception.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.logging.Level;

/**
 * La clase <code>SignerClient</code> implementa la interfaz {@link Signable}, proporcionando
 * métodos para el registro e inicio de sesión de un usuario mediante comunicación con un servidor.
 *
 * <p>
 * La clase <code>SignerClient</code> maneja la conexión y el intercambio de datos con un servidor
 * remoto que gestiona operaciones de autenticación y registro de usuarios. Los métodos 
 * <code>signUp</code> y <code>signIn</code> envían solicitudes al servidor y reciben respuestas
 * en función de los resultados, como éxito, credenciales incorrectas, o errores de conexión.
 * </p>
 * <p>
 * Esta clase utiliza configuraciones definidas en un archivo de recursos para establecer
 * la conexión al servidor, incluyendo la dirección IP y el puerto. Además, emplea un
 * {@link Logger} para registrar eventos significativos y errores durante la ejecución.
 * </p>
 *
 * @author Alder
 * @see Signable
 */
public class SignerClient implements Signable {

    /**
     * Archivo de configuración que contiene información del socket.
     */
    private static final ResourceBundle archivo = ResourceBundle.getBundle("Utils.socketConfig");

    /**
     * Puerto para la conexión con el servidor.
     */
    private static final int PUERTO = Integer.parseInt(archivo.getString("PORT"));

    /**
     * Dirección IP del servidor.
     */
    private static final String HOST = archivo.getString("IP");

    /**
     * Logger para registrar eventos y mensajes.
     */
    private static final Logger LOGGER = Logger.getLogger("/Model/SignerClient");

    /**
     * Tipo de mensaje utilizado para la comunicación con el servidor.
     */
    TipoMensaje mt;

    /**
     * Mensaje que se utilizará para la comunicación con el servidor.
     */
    private Message msg = null;

    /**
     * Registra un nuevo usuario en el sistema.
     *
     * <p>
     * Este método establece una conexión con el servidor y envía un mensaje de
     * solicitud de registro. Luego, espera una respuesta del servidor y maneja
     * diferentes tipos de mensajes de respuesta. En caso de éxito, devuelve el
     * usuario registrado; de lo contrario, lanza excepciones específicas según
     * el error encontrado.
     * </p>
     *
     * @param user El usuario que se desea registrar.
     * @return El usuario registrado.
     * @throws Exception Si ocurre un error durante el registro o en la conexión.
     * @throws UserAlreadyExistsException Si el email del usuario ya está registrado.
     * @throws ConnectionException Si ocurre un error en la conexión con la base de datos.
     * @throws ServerErrorException Si ocurre un error en el servidor.
     * @author Omar and Adrian
     */
    @Override
    public User signUp(User user) throws Exception {
        ObjectOutputStream oos = null;
        ObjectInputStream ois = null;

        try {
            LOGGER.info("Iniciando sesión de registro...");

            Socket socketCliente = new Socket(HOST, PUERTO);
            oos = new ObjectOutputStream(socketCliente.getOutputStream());

            msg = new Message();
            msg.setUser(user);
            msg.setTipo(TipoMensaje.SIGN_UP_REQUEST);

            oos.writeObject(msg);

            ois = new ObjectInputStream(socketCliente.getInputStream());
            msg = (Message) ois.readObject();
            user = msg.getUser();

            oos.close();
            ois.close();
            socketCliente.close();

            switch (msg.getTipo()) {
                case OK_RESPONSE:
                    return user;
                case EMAIL_EXISTS:
                    throw new UserAlreadyExistsException("El email ya existe.");
                case CONNECTION_ERROR:
                    throw new ConnectionException("Error en la conexión con la base de datos.");
                case SERVER_ERROR:
                    throw new ServerErrorException("El servidor no está disponible.");
                default:
                    throw new Exception("Respuesta desconocida del servidor.");
            }
        } catch (ClassNotFoundException | IOException ex) {
            LOGGER.log(Level.SEVERE, "Error en el método signUp", ex);
            throw new ServerErrorException("Error de comunicación con el servidor.");
        }
    }

    /**
     * Inicia sesión para un usuario existente.
     *
     * <p>
     * Este método establece una conexión con el servidor y envía un mensaje de
     * solicitud de inicio de sesión. Luego, espera una respuesta del servidor y
     * maneja diferentes tipos de mensajes de respuesta. En caso de éxito,
     * devuelve el usuario que ha iniciado sesión; de lo contrario, lanza
     * excepciones específicas según el error encontrado.
     * </p>
     *
     * @param user El usuario que desea iniciar sesión.
     * @return El usuario que ha iniciado sesión.
     * @throws Exception Si ocurre un error durante el inicio de sesión.
     * @throws IncorrectCredentialsException Si las credenciales son incorrectas.
     * @throws ServerErrorException Si ocurre un error en el servidor.
     * @throws ConnectionException Si ocurre un error en la conexión con la base de datos.
     * @throws MaxThreadUserException Si el máximo de usuarios concurrentes ha sido alcanzado.
     * @author Alder and Borja
     */
    @Override
    public User signIn(User user) throws Exception {
        ObjectOutputStream oos = null;
        ObjectInputStream ois = null;

        try {
            LOGGER.info("Iniciando Sesión...");
            Socket socketCliente = new Socket(HOST, PUERTO);

            oos = new ObjectOutputStream(socketCliente.getOutputStream());
            msg = new Message();
            msg.setUser(user);
            msg.setTipo(TipoMensaje.SIGN_IN_REQUEST);
            oos.writeObject(msg);

            ois = new ObjectInputStream(socketCliente.getInputStream());
            msg = (Message) ois.readObject();
            user = msg.getUser();

            oos.close();
            ois.close();
            socketCliente.close();

            switch (msg.getTipo()) {
                case OK_RESPONSE:
                    return user;
                case INCORRECT_CREDENTIALS_RESPONSE:
                    throw new IncorrectCredentialsException("Email o contraseña incorrectos.");
                case SERVER_ERROR:
                    throw new ServerErrorException("Ha ocurrido un error en el servidor.");
                case CONNECTION_ERROR:
                    throw new ConnectionException("Error en la conexión con la base de datos.");
                case MAX_THREAD_USER:
                    throw new MaxThreadUserException("Máximo de usuarios alcanzado. Intente más tarde.");
                default:
                    throw new Exception("Respuesta desconocida del servidor.");
            }
        } catch (IOException ex) {
            LOGGER.log(Level.SEVERE, "Error en el método signIn", ex);
            throw new ServerErrorException("Error de entrada/salida en los datos.");
        }
    }
}
