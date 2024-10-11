package view;

import Model.SignerClient;
import Model.User;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Hyperlink;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.logging.Logger;
import Utils.UtilsMethods;
import javafx.scene.layout.Pane;
import java.util.logging.Level;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Controlador para la interfaz de inicio de sesión. Este controlador maneja la
 * lógica de la vista de inicio de sesión, incluyendo la validación de
 * credenciales y la visibilidad de la contraseña.
 *
 * <p>
 * Se encarga de gestionar la interacción del usuario con los componentes de la
 * interfaz de usuario, así como de proporcionar retroalimentación mediante
 * mensajes de alerta.</p>
 *
 * @author Alder
 */
public class logInController {

    /**
     * Instancia de métodos utilitarios.
     */
    UtilsMethods utils = new UtilsMethods();

    /**
     * Logger para registrar eventos y mensajes.
     */
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
    private Label emailLabel; // Etiqueta para el campo de email.

    @FXML
    private Label passwordLabel; // Etiqueta para el campo de contraseña.

    @FXML
    private Hyperlink createUserLink; // Enlace para crear usuario.

    @FXML
    private ImageView passwordImage; // Imagen que indica la visibilidad de la contraseña.

    @FXML
    private Pane centralPane;

    /**
     * Indica si la contraseña es visible.
     */
    private boolean isPasswordVisible = false;

    /**
     * Método que se ejecuta al inicializar el controlador. Configura el campo
     * de texto visible para la contraseña y agrega un listener para validar el
     * email cuando pierde el foco.
     */
    @FXML
    public void initialize() {
        centralPane.setFocusTraversable(true);
        centralPane.requestFocus();
        visiblePasswordField.setVisible(false); // Inicialmente, el campo de texto visible está oculto

        // Agregar listener para verificar el email cuando pierde el foco
        emailTextField.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) { // Si pierde el foco
                utils.validateEmail(emailTextField.getText());
            }
        });
    }

    /**
     * Maneja la acción del botón de inicio de sesión. Valida las credenciales
     * del usuario y muestra un mensaje apropiado.
     */
    @FXML
    private void handleLogInButtonAction() throws Exception {
        String email = emailTextField.getText();
        String password = isPasswordVisible ? visiblePasswordField.getText() : passwordField.getText();
        SignerClient s = new SignerClient();
        User user = new User();
        user.setEmail(emailLabel.getText());
        user.setPassword(passwordLabel.getText());
        s.signIn(user);
        navigateToScreen("/view/Main.fxml", "Main");
    }

    /**
     * Maneja la acción del enlace para crear un usuario. Muestra un mensaje
     * indicando que se abrirá la vista de registro.
     */
    @FXML
    private void handleCreateUserLinkAction() {
        logger.info("Abrir vista de registro.");
        navigateToScreen("/view/SignUpView.fxml", "SignUp");
    }

    /**
     * Maneja la acción de cambiar la visibilidad de la contraseña. Alterna
     * entre mostrar y ocultar la contraseña.
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
     * General method to navigate to different screens.
     *
     * @param fxmlPath the path to the FXML file of the target screen
     * @param windowTitle the title to set for the window
     * @author Borja
     */
    private void navigateToScreen(String fxmlPath, String title) {
        try {
            // Load the FXML file of the target view
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Scene scene = new Scene(loader.load());

            // Get the current stage
            Stage currentStage = (Stage) logInButton.getScene().getWindow();

            // Change the current stage's scene to the new scene
            currentStage.setScene(scene);
            currentStage.setTitle(title); // Set the title of the new window
            currentStage.show();

            logger.log(Level.INFO, "Navigated to " + title + " screen.");

        } catch (Exception e) {
            logger.log(Level.SEVERE, "Failed to load " + title + " screen: " + e.getMessage(), e);
        }
    }
}
