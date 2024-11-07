package Model;

import java.io.Serializable;

/**
 * Representa un mensaje en el sistema, que está asociado a un usuario y un tipo de mensaje.
 * 
 * <p>Esta clase contiene la información básica de un mensaje, incluyendo el usuario
 * que lo envía y el tipo de mensaje que representa.</p>
 * 
 * @author Alder
 */
public class Message implements Serializable {
    
    private User user;      // Usuario que envía el mensaje
    private TipoMensaje tipo; // Tipo de mensaje (ej. texto, imagen, etc.)

    /**
     * Obtiene el usuario que envía el mensaje.
     * 
     * @return el usuario que envía el mensaje.
     */
    public User getUser() {
        return user;
    }

    /**
     * Establece el usuario que enviará el mensaje.
     * 
     * @param user el usuario que se asignará al mensaje.
     */
    public void setUser(User user) {
        this.user = user;
    }

    /**
     * Obtiene el tipo de mensaje.
     * 
     * @return el tipo de mensaje (ej. texto, imagen, etc.).
     */
    public TipoMensaje getTipo() {
        return tipo;
    }

    /**
     * Establece el tipo de mensaje.
     * 
     * @param tipo el tipo de mensaje que se asignará.
     */
    public void setTipo(TipoMensaje tipo) {
        this.tipo = tipo;
    }
}
