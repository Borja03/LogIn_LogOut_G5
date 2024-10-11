package view;

import Model.SignableFactory;
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
import exception.*;

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
        visiblePasswordField.setVisible(false); // Inicialmente, el campo de texto visible está oculto
    }

    /**
     * Maneja la acción del botón de inicio de sesión. Valida las credenciales
     * del usuario y muestra un mensaje apropiado. Si el inicio de sesión es
     * exitoso, navega a la pantalla principal.
     */
    @FXML
    private void handleLogInButtonAction() throws InvalidEmailFormatException {
            utils.validateEmail(emailTextField.getText());
            String email = emailTextField.getText();
            String password = isPasswordVisible ? visiblePasswordField.getText() : passwordField.getText();

            User user = new User();
            user.setEmail(email);
            user.setPassword(password);

            try {
                User loggedInUser = SignableFactory.getSignable().signIn(user);

                if (loggedInUser != null) {
                    // Si el inicio de sesión es exitoso, navega a la pantalla principal
                    navigateToScreen("/view/Main.fxml", "Main");
                } else {
                    // Manejar el caso en que el usuario no se devuelve
                    utils.showAlert("Error", "No se pudo iniciar sesión. Verifique sus credenciales.");
                }
            } catch (UserAlreadyExistsException e) {
                utils.showAlert("Error", "El usuario ya existe.");
                logger.warning(e.getMessage());
            } catch (ConnectionException e) {
                utils.showAlert("Error", "Problemas de conexión con el servidor.");
                logger.warning(e.getMessage());
            } catch (Exception e) {
                utils.showAlert("Error", "Ocurrió un error inesperado.");
                logger.severe(e.getMessage());
            }
    }

    /**
     * Maneja la acción del enlace para crear un usuario. Navega a la vista de
     * registro.
     */
    @FXML
    private void handleCreateUserLinkAction() {
        logger.info("Abrir vista de registro.");
        navigateToScreen("/view/SignUpView.fxml", "SignUp");
    }

    /**
     * Maneja la acción de cambiar la visibilidad de la contraseña. Alterna
     * entre mostrar y ocultar la contraseña en el campo correspondiente.
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
     * Navega a una pantalla diferente cargando el archivo FXML correspondiente.
     *
     * @param fxmlPath la ruta del archivo FXML de la pantalla objetivo
     * @param title el título a establecer para la ventana
     */
    private void navigateToScreen(String fxmlPath, String title) {
        try {
            // Cargar el archivo FXML de la vista objetivo
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Scene scene = new Scene(loader.load());

            // Obtener el escenario actual
            Stage currentStage = (Stage) logInButton.getScene().getWindow();

            // Cambiar la escena del escenario actual a la nueva escena
            currentStage.setScene(scene);
            currentStage.setTitle(title); // Establecer el título de la nueva ventana
            currentStage.show();

            logger.log(Level.INFO, "Navigated to " + title + " screen.");

        } catch (Exception e) {
            logger.log(Level.SEVERE, "Failed to load " + title + " screen: " + e.getMessage(), e);
        }
    }
}
