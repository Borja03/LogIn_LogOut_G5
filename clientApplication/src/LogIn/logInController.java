package LogIn;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class logInController {

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
        // Inicialización, si es necesaria
        visiblePasswordField.setVisible(false); // Inicialmente, el campo de texto visible está oculto
    }

    @FXML
    private void handleLogInButtonAction() {
        String email = emailTextField.getText();
        String password = isPasswordVisible ? visiblePasswordField.getText() : passwordField.getText();

        if (validateCredentials(email, password)) {
            System.out.println("Log in exitoso.");
        } else {
            System.out.println("Credenciales incorrectas.");
        }
    }

    @FXML
    private void handleSignUpButtonAction() {
        System.out.println("Abrir vista de registro.");
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
}
