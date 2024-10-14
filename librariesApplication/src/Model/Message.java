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
public class Message implements Serializable{
    private User user;      // Usuario que envía el mensaje
    private TipoMensaje tipo; // Tipo de mensaje (ej. texto, imagen, etc.)

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public TipoMensaje getTipo() {
        return tipo;
    }

    public void setTipo(TipoMensaje tipo) {
        this.tipo = tipo;
    }

    
}
