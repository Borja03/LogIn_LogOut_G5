package Utils;

import exception.InvalidEmailFormatException;
import java.util.logging.Logger;
import javafx.scene.control.Alert;

/**
 * Clase que contiene métodos utilitarios para la validación de datos y la gestión de alertas.
 * 
 * <p>Esta clase proporciona métodos para validar direcciones de correo electrónico y
 * mostrar alertas en la interfaz gráfica.</p>
 * 
 * @author Alder
 */
public class UtilsMethods {

    /** Logger para registrar advertencias y mensajes. */
    public static final Logger logger = Logger.getLogger(UtilsMethods.class.getName());

    /**
     * Valida el formato de una dirección de correo electrónico.
     *
     * <p>Si el formato es inválido, se registra una advertencia y se muestra una alerta
     * al usuario.</p>
     *
     * @param email la dirección de correo electrónico a validar
     * @throws InvalidEmailFormatException si el formato del correo electrónico es inválido
     */
    public void validateEmail(String email) throws InvalidEmailFormatException {
        String emailRegex = "^[\\w-\\.]+@[\\w-]+\\.[a-zA-Z]{2,4}$";
        if (!email.matches(emailRegex)) {
            logger.warning("Formato de email inválido: " + email);
            showAlert("Formato de email inválido", "El texto tiene que estar en formato email 'example@example.extension'");
            throw new InvalidEmailFormatException("Formato email invalido: " + email);
        }
    }

    /**
     * Muestra una alerta con un mensaje específico.
     *
     * @param title el título de la alerta
     * @param message el mensaje a mostrar en la alerta
     */
    public void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    
     public void showAlertExcep( String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

   

    
}
