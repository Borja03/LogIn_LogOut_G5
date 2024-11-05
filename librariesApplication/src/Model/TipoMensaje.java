package Model;

/**
 * Enumera los tipos de mensajes que se pueden enviar o recibir en el sistema.
 * 
 * <p>Esta enumeración incluye los siguientes tipos:</p>
 * <ul>
 *     <li><b>SIGN_IN_REQUEST:</b> Indica que se está solicitando iniciar sesión.</li>
 *     <li><b>SIGN_UP_REQUEST:</b> Indica que se está solicitando registrarse.</li>
 *     <li><b>USER_NOT_EXIST:</b> Indica que el usuario no existe en el sistema.</li>
 *     <li><b>WRONG_PASSWORD:</b> Indica que la contraseña proporcionada es incorrecta.</li>
 *     <li><b>EMAIL_EXISTS:</b> Indica que el correo electrónico ya está registrado en el sistema.</li>
 *     <li><b>SERVER_ERROR:</b> Indica que ha ocurrido un error en el servidor.</li>
 *     <li><b>MAX_THREAD_USER:</b> Indica que se ha alcanzado el número máximo de usuarios en hilo.</li>
 *     <li><b>OK_RESPONSE:</b> Indica una respuesta exitosa.</li>
 *     <li><b>INCORRECT_CREDENTIALS_RESPONSE:</b> Indica que las credenciales proporcionadas son incorrectas.</li>
 * </ul>
 * 
 * @author Alder
 */
public enum TipoMensaje {

    /**
     * Solicitud de inicio de sesión.
     */
    SIGN_IN_REQUEST,  

    /**
     * Solicitud de registro.
     */
    SIGN_UP_REQUEST,   

    /**
     * Indica que el correo electrónico ya existe en el sistema.
     */
    EMAIL_EXISTS,      

    /**
     * Indica que ha ocurrido un error en el servidor.
     */
    SERVER_ERROR,      

    /**
     * Indica que se ha alcanzado el número máximo de usuarios en hilo.
     */
    MAX_THREAD_USER,

    /**
     * Indica una respuesta exitosa.
     */
    OK_RESPONSE,

    /**
     * Indica que las credenciales proporcionadas son incorrectas.
     */
    INCORRECT_CREDENTIALS_RESPONSE,

    /**
     * Indica que ha ocurrido un error de conexión.
     */
    CONNECTION_ERROR
}
