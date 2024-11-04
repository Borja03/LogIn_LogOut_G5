package exception;

/**
 * Excepción que indica que el formato de un número de teléfono es inválido.
 * 
 * <p>Esta clase extiende {@link Exception} y se utiliza para señalar
 * situaciones en las que un número de teléfono proporcionado no cumple con
 * los criterios de formato válidos establecidos por la aplicación, como
 * longitud o estructura numérica.</p>
 * 
 * @author Omar
 */
public class InvalidPhoneNumberFormatException extends Exception {
    
    /**
     * Crea una nueva instancia de InvalidPhoneNumberFormatException con un mensaje específico.
     *
     * @param message el mensaje que describe el motivo de la excepción
     */
    public InvalidPhoneNumberFormatException(String message) {
        super(message);
    }
}
