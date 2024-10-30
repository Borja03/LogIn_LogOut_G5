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
 * La clase <code>SignerClient</code> implementa la interfaz
 * <code>Signable</code>, proporcionando métodos para el registro e inicio de
 * sesión de un usuario.
 *
 * <p>
 * Esta clase maneja la comunicación con un servidor para llevar a cabo
 * operaciones de registro e inicio de sesión. Los métodos <code>signUp</code> y
 * <code>signIn</code> permiten a los usuarios registrarse y acceder al sistema,
 * respectivamente. En caso de errores, se lanzan excepciones específicas.
 * </p>
 *
 * @author Alder
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
     * @param user El usuario que se desea registrar.
     * @return El usuario registrado.
     * @throws Exception Si ocurre un error durante el registro.
     */
    @Override
        public User signUp(User user) throws Exception {
        ObjectOutputStream oos = null;
        ObjectInputStream ois = null;

        try {
            // Logging start of sign-up session
            LOGGER.info("Iniciando Sesión...");

            // Establishing the socket connection
            Socket socketCliente = new Socket(HOST, PUERTO);
            oos = new ObjectOutputStream(socketCliente.getOutputStream());

            // Preparing message object
            msg = new Message();
            msg.setUser(user);
            msg.setTipo(TipoMensaje.SIGN_UP_REQUEST);

            // Sending the message to the server
            oos.writeObject(msg);

            // Receiving the response from the server
            ois = new ObjectInputStream(socketCliente.getInputStream());
            msg = (Message) ois.readObject();
            user = msg.getUser();

            // Closing connections
            oos.close();
            ois.close();
            socketCliente.close();

            // Handling server response
            switch (msg.getTipo()) {
                case OK_RESPONSE:
                    return user;
                case EMAIL_EXISTS:
                    throw new UserAlreadyExistsException("Email already exists....SignerClient");
                case CONNECTION_ERROR:
                    throw new ConnectionException("Ha ocurrido un error con la conexion a la base de datos.");
                case SERVER_ERROR:
                    throw new ServerErrorException("Server not working....SignerClient");
                default:
                    throw new Exception("Unknown server response....SignerClient");
            }
        } catch (ClassNotFoundException | IOException ex) {
            LOGGER.log(Level.SEVERE, "Error in signUp", ex);
           // throw new ServerErrorException("Communication error....SignerClient", ex);
        } catch (UserAlreadyExistsException | ServerErrorException ex) {
            // Rethrow expected exceptions to the caller
            throw ex;
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, "Error in signUp", ex);
            throw ex; // Rethrow any unexpected exceptions
        }
        return null;
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
     */
    @Override
    public User signIn(User user) throws Exception {
        ObjectOutputStream oos = null;
        ObjectInputStream ois = null;

        try {
            //Instanciamos el socket
            LOGGER.info("Iniciando Sesión...");
            Socket socketCliente = new Socket(HOST, PUERTO);

            //Creamos el output y preparamos el encapsulador para enviarlo al servidor
            oos = new ObjectOutputStream(socketCliente.getOutputStream());
            msg = new Message();
            msg.setUser(user);
            msg.setTipo(TipoMensaje.SIGN_IN_REQUEST);
            oos.writeObject(msg);

            //Recibimos el objeto encapsulado del servidor
            ois = new ObjectInputStream(socketCliente.getInputStream());
            msg = (Message) ois.readObject();
            user = msg.getUser();
            //Cerramos las conexiones
            oos.close();
            ois.close();
            socketCliente.close();
            //Dependiendo de el mensaje que reciva lanza o escribe un mensaje nuevo
            switch (msg.getTipo()) {
                case OK_RESPONSE:
                    return user;
                case INCORRECT_CREDENTIALS_RESPONSE:
                    throw new IncorrectCredentialsException("Email o contraseña incorrectos.");
                case SERVER_ERROR:
                    throw new ServerErrorException("Ha ocurrido un error en el servidor.");
                case CONNECTION_ERROR:
                    throw new ConnectionException("Ha ocurrido un error con la conexion a la base de datos.");
                case MAX_THREAD_USER:
                    throw new MaxThreadUserException("Maximo de usuarios alcanzado, inténtelo más tarde");
            }
            //Control de excepciones
        } catch (IOException ex) {
            Logger.getLogger(SignerClient.class.getName()).log(Level.SEVERE, null, ex);
            throw new ServerErrorException("Error de entrada/salida en los datos");
        }
        //Devuelve un obejto user
        return null;
    }

}
