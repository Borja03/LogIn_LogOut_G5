package Model;

import java.io.Serializable;

/**
 * Enumera los tipos de mensajes que se pueden enviar o recibir en el sistema.
 * 
 * <p>Esta enumeración incluye los siguientes tipos:</p>
 * <ul>
 *     <li><b>ENVIAR:</b> Indica que el mensaje está siendo enviado.</li>
 *     <li><b>RECIBIR:</b> Indica que el mensaje ha sido recibido.</li>
 *     <li><b>OK:</b> Indica que una operación se ha completado con éxito.</li>
 *     <li><b>USER_NOT_EXIST:</b> Indica que el usuario no existe en el sistema.</li>
 *     <li><b>WRONG_PASSWORD:</b> Indica que la contraseña proporcionada es incorrecta.</li>
 *     <li><b>EMAIL_EXISTS:</b> Indica que el email ya está registrado en el sistema.</li>
 *     <li><b>SERVER_ERROR:</b> Indica que ha ocurrido un error en el servidor.</li>
 * </ul>
 * 
 * @author Alder
 */
public enum TipoMensaje implements Serializable{
    ENVIAR, // Mensaje enviado
    RECIBIR, // Mensaje recibido
    OK, // Operación exitosa
    USER_NOT_EXIST, // Usuario no existe
    WRONG_PASSWORD, // Contraseña incorrecta
    EMAIL_EXISTS, // Email ya existe
    SERVER_ERROR // Error en el servidor
}
