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

public class logInController {

    private static final Logger logger = Logger.getLogger(logInController.class.getName());

    @FXML
    private TextField emailTextField;
    @FXML
    private PasswordField passwordField; // Mantén el PasswordField
    @FXML
    private TextField visiblePasswordField; // Nuevo campo para la contraseña visible
    @FXML
    private Button logInButton;
    @FXML
    private Button signUpButton;
    @FXML
    private Label emailLabel;
    @FXML
    private Label passwordLabel;
    @FXML
    private ImageView passwordImage;

    private boolean isPasswordVisible = false; // Para controlar la visibilidad de la contraseña

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

    @FXML
    private void handleLogInButtonAction() {
        String email = emailTextField.getText();
        String password = isPasswordVisible ? visiblePasswordField.getText() : passwordField.getText();

        if (validateCredentials(email, password)) {
            logger.info("Log in exitoso.");
        } else {
            logger.warning("Credenciales incorrectas.");
            showAlert("Error", "Los campos no pueden estar vacios");
        }
    }

    @FXML
    private void handleSignUpButtonAction() {
        logger.info("Abrir vista de registro.");
        showAlert("Registro", "Abrir vista de registro.");
    }

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

    private boolean validateCredentials(String email, String password) {
        return !email.isEmpty() && !password.isEmpty();
    }

    private void validateEmail(String email) {
        String emailRegex = "^[\\w-\\.]+@[\\w-]+\\.[a-zA-Z]{2,4}$";
        if (!email.matches(emailRegex)) {
            logger.warning("Formato de email inválido: " + email);
            showAlert("Formato de email inválido", "El texto tiene que estar en formato email 'example@example.extension'");
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
