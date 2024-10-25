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
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;
import javafx.scene.Parent;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.layout.BorderPane;

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
    private BorderPane borderPane;

    private ContextMenu contextMenu;
    private String currentTheme = "light";
    @FXML
    private Pane centralPane;
    private Stage stage;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public Stage getStage() {
        return this.stage;
    }
    /**
     * Indica si la contraseña es visible.
     */
    private boolean isPasswordVisible = false;

    /**
     * Método que se ejecuta al inicializar el controlador. Configura el campo
     * de texto visible para la contraseña y agrega un listener para validar el
     * email cuando pierde el foco.
     *
     * @param root
     */
    @FXML
    public void initialize(Parent root) {
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("SignIn");
        stage.setResizable(false);
        stage.centerOnScreen();

        visiblePasswordField.setVisible(false);
        initializeContextMenu();

        // Asigna el menú contextual al BorderPane
        borderPane.setOnContextMenuRequested(this::showContextMenu);

        currentTheme = loadThemePreference();
        loadTheme(currentTheme);
        stage.show();
    }

    //menu and theme
    private void initializeContextMenu() {
        contextMenu = new ContextMenu();

        MenuItem lightMode = new MenuItem("Light Mode");
        MenuItem darkMode = new MenuItem("Dark Mode");
        MenuItem clearFields = new MenuItem("Clear Fields");

        lightMode.setOnAction(e -> switchTheme("light"));
        darkMode.setOnAction(e -> switchTheme("dark"));
        clearFields.setOnAction(e -> clearAllFields());

        contextMenu.getItems().addAll(lightMode, darkMode, clearFields);
    }

    private void showContextMenu(ContextMenuEvent event) {
        contextMenu.show(centralPane, event.getScreenX(), event.getScreenY());
    }

    private void saveThemePreference(String theme) {
        try {
            Properties props = new Properties();
            props.setProperty("theme", theme);
            File file = new File("config.properties");
            props.store(new FileOutputStream(file), "Theme Settings");
        } catch (IOException e) {
            logger.severe("Error saving theme preference: " + e.getMessage());
        }
    }

    private String loadThemePreference() {
        try {
            Properties props = new Properties();
            File file = new File("config.properties");
            if (file.exists()) {
                props.load(new FileInputStream(file));
                return props.getProperty("theme", "light");
            }
        } catch (IOException e) {
            logger.severe("Error loading theme preference: " + e.getMessage());
        }
        return "light";
    }

    private void switchTheme(String theme) {
        currentTheme = theme;
        loadTheme(theme);
        saveThemePreference(theme);
    }

    private void loadTheme(String theme) {
        Scene scene = stage.getScene();
        scene.getStylesheets().clear();

        if (theme.equals("dark")) {
            // Código adicional para el tema oscuro

            String cssFile = "/css/dark-styles.css";
            scene.getStylesheets().add(getClass().getResource(cssFile).toExternalForm());

            contextMenu.getStyleClass().add("context-menu-dark");

            // Aquí puedes agregar más acciones específicas para el tema oscuro
        } else if (theme.equals("light")) {
            // Código adicional para el tema claro

            String cssFile = "/css/CSSglobal.css";

            scene.getStylesheets().add(getClass().getResource(cssFile).toExternalForm());

            contextMenu.getStyleClass().remove("context-menu-dark");

            // Aquí puedes agregar más acciones específicas para el tema claro
        }
    }

    private void clearAllFields() {
        emailTextField.clear();
        passwordField.clear();

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
            navigateToScreen("/view/Main.fxml", "Main", true, loggedInUser);

            if (loggedInUser != null) {
                // Si el inicio de sesión es exitoso, navega a la pantalla principal
                navigateToScreen("/view/Main.fxml", "Main", true, loggedInUser);
            }
        } catch (IncorrectCredentialsException e) {
            utils.showAlert("Error", "No se pudo iniciar sesión. Verifique sus credenciales.");
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
        navigateToScreen("/view/SignUpView.fxml", "SignUp", false, null);
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
     * @author Borja
     */
    private void navigateToScreen(String fxmlPath, String title, boolean main, User user) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();
            // Get the current stage
            if (!main) {
                SignUpController controller = loader.getController();
                Stage newStage = new Stage();
                controller.setStage(newStage);
                controller.initStage(root);
                stage.close();

            } else {
                MainController controller = loader.getController();
                Stage newStage = new Stage();
                controller.setStage(newStage);
                controller.initStage(root, user);
                stage.close();
            }

        } catch (Exception e) {
            logger.log(Level.SEVERE, "Failed to load " + title + " screen: " + e.getMessage(), e);
        }
    }
}
