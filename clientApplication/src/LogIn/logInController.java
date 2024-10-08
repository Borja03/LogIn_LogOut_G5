package LogIn;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.logging.Logger;

/**
 * Controlador para la interfaz de inicio de sesión.
 * Este controlador maneja la lógica de la vista de inicio de sesión, incluyendo la
 * validación de credenciales y la visibilidad de la contraseña.
 */
public class logInController {

    private static final Logger logger = Logger.getLogger(logInController.class.getName());

    @FXML
    private TextField emailTextField; // Campo de texto para el email del usuario.
    
    @FXML
    private PasswordField passwordField; // Campo de texto para la contraseña del usuario.
    
    @FXML
    private TextField visiblePasswordField; // Campo de texto para mostrar la contraseña en texto plano.
    
    @FXML
    private Button logInButton; // Botón para iniciar sesión.
    
    @FXML
    private Button signUpButton; // Botón para abrir la vista de registro.
    
    @FXML
    private Label emailLabel; // Etiqueta para el campo de email.
    
    @FXML
    private Label passwordLabel; // Etiqueta para el campo de contraseña.
    
    @FXML
    private ImageView passwordImage; // Imagen que indica la visibilidad de la contraseña.

    private boolean isPasswordVisible = false; // Indica si la contraseña es visible.

    /**
     * Método que se ejecuta al inicializar el controlador.
     * Configura el campo de texto visible para la contraseña y agrega un listener
     * para validar el email cuando pierde el foco.
     */
    @FXML
    public void initialize() {
        visiblePasswordField.setVisible(false); // Inicialmente, el campo de texto visible está oculto

        // Agregar listener para verificar el email cuando pierde el foco
        emailTextField.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) { // Si pierde el foco
                validateEmail(emailTextField.getText());
            }
        });
    }

    /**
     * Maneja la acción del botón de inicio de sesión.
     * Valida las credenciales del usuario y muestra un mensaje apropiado.
     */
    @FXML
    private void handleLogInButtonAction() {
        String email = emailTextField.getText();
        String password = isPasswordVisible ? visiblePasswordField.getText() : passwordField.getText();

        if (validateCredentials(email, password)) {
            logger.info("Log in exitoso.");
        } else {
            logger.warning("Credenciales incorrectas.");
            showAlert("Error", "Los campos no pueden estar vacíos");
        }
    }

    /**
     * Maneja la acción del botón de registro.
     * Muestra un mensaje indicando que se abrirá la vista de registro.
     */
    @FXML
    private void handleSignUpButtonAction() {
        logger.info("Abrir vista de registro.");
        showAlert("Registro", "Abrir vista de registro.");
    }

    /**
     * Maneja la acción de cambiar la visibilidad de la contraseña.
     * Alterna entre mostrar y ocultar la contraseña.
     */
    @FXML
    private void handlePasswordImageButtonAction() {
        isPasswordVisible = !isPasswordVisible;

        if (isPasswordVisible) {
            passwordImage.setImage(new Image(getClass().getResourceAsStream("/Images/passwordVisible.png")));
            passwordField.setVisible(false);
            visiblePasswordField.setVisible(true);
            visiblePasswordField.setText(passwordField.getText());
        } else {
            passwordImage.setImage(new Image(getClass().getResourceAsStream("/Images/passwordNotVisible.png")));
            passwordField.setVisible(true);
            visiblePasswordField.setVisible(false);
            passwordField.setText(visiblePasswordField.getText());
        }
    }

    /**
     * Valida las credenciales del usuario.
     * @param email El email del usuario.
     * @param password La contraseña del usuario.
     * @return true si las credenciales son válidas, false de lo contrario.
     */
    private boolean validateCredentials(String email, String password) {
        return !email.isEmpty() && !password.isEmpty();
    }

    /**
     * Valida el formato del email.
     * Muestra una alerta si el formato no es válido.
     * @param email El email a validar.
     */
    private void validateEmail(String email) {
        String emailRegex = "^[\\w-\\.]+@[\\w-]+\\.[a-zA-Z]{2,4}$";
        if (!email.matches(emailRegex)) {
            logger.warning("Formato de email inválido: " + email);
            showAlert("Formato de email inválido", "El texto tiene que estar en formato email 'example@example.extension'");
        }
    }

    /**
     * Muestra una alerta con el título y el mensaje especificados.
     * @param title El título de la alerta.
     * @param message El mensaje de la alerta.
     */
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
