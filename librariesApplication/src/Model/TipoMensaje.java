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
 * </ul>
 * 
 * @author Alder
 */ 
public enum TipoMensaje {

    SIGN_IN_REQUEST,  // Solicitud de inicio de sesión
    SIGN_UP_REQUEST,   // Solicitud de registro
    USER_NOT_EXIST,    // Usuario no existe
    WRONG_PASSWORD,     // Contraseña incorrecta
    EMAIL_EXISTS,      // Email ya existe
    SERVER_ERROR,       // Error en el servidor
    MAX_THREAD_USER,
    OK_RESPONSE
}

