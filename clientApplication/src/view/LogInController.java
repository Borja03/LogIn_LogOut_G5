package view;

import Model.SignableFactory;
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
import static Utils.UtilsMethods.logger;
import exception.*;
import javafx.scene.layout.Pane;
import java.util.logging.Level;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.ConnectException;
import java.util.Properties;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.scene.Parent;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.WindowEvent;

/**
 * Controlador para la vista de inicio de sesión en la aplicación. Esta clase
 * maneja la lógica para iniciar sesión, cambiar la visibilidad de la
 * contraseña, navegar entre vistas, gestionar temas de interfaz (claro/oscuro)
 * y proporciona opciones adicionales mediante un menú contextual.
 *
 * <p>
 * El controlador interactúa con diversos elementos de la interfaz gráfica para
 * obtener las credenciales del usuario, validarlas y manejar los intentos de
 * inicio de sesión. En caso de éxito, permite al usuario acceder a la pantalla
 * principal o a la vista de registro.</p>
 *
 * <p>
 * Incluye métodos utilitarios para guardar preferencias de usuario, como el
 * tema de la aplicación, y para limpiar campos de entrada cuando se
 * solicita.</p>
 *
 * @author Alder
 */
public class LogInController {

    /**
     * Instancia de métodos utilitarios.
     */
    UtilsMethods utils = new UtilsMethods();

    /**
     * Logger para registrar eventos y mensajes.
     */
    private static final Logger logger = Logger.getLogger(LogInController.class.getName());

    @FXML
    private TextField emailTextField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private TextField visiblePasswordField;

    @FXML
    private Button logInButton;

    @FXML
    private Label emailLabel;

    @FXML
    private Label passwordLabel;

    @FXML
    private Hyperlink createUserLink;

    @FXML
    private ImageView passwordImage;

    @FXML
    private BorderPane borderPane;

    private ContextMenu contextMenu;

    @FXML
    private Pane centralPane;

    private Stage stage;

    /**
     * Establece la ventana principal (stage).
     *
     * @param stage la ventana a establecer.
     */
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    /**
     * Obtiene la ventana principal (stage).
     *
     * @return la ventana principal.
     */
    public Stage getStage() {
        return this.stage;
    }

    /**
     * Indica si la contraseña es visible.
     */
    private boolean isPasswordVisible = false;

    public static String currentTheme = loadInitialTheme(); // Load theme from config

    /**
     * Método que se ejecuta al inicializar el controlador. Configura la escena,
     * establece el título y las propiedades de la ventana principal, oculta el
     * campo de texto visible para la contraseña, y carga y aplica la
     * preferencia del tema guardado.
     *
     * @param root la raíz de la interfaz que se mostrará en la ventana.
     */
    @FXML
    public void initialize(Parent root) {
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("SignIn");
        stage.setResizable(false);
        stage.getIcons().add(new Image("/Images/userIcon.png"));
        stage.centerOnScreen();

        visiblePasswordField.setVisible(false);
        initializeContextMenu();

        borderPane.setOnContextMenuRequested(this::showContextMenu);

        // currentTheme = loadThemePreference();
        loadTheme(currentTheme);
        stage.show();
    }

    public static String loadInitialTheme() {
        try {
            ResourceBundle bundle = ResourceBundle.getBundle("config/config");
            return bundle.getString("theme");
        } catch (Exception e) {
            // Log an error message if loading the theme preference fails
        }
        return "light";
    }

    /**
     * Inicializa el menú contextual y define las opciones disponibles, como
     * cambiar el tema y limpiar los campos de entrada.
     */
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

    /**
     * Muestra el menú contextual en la posición del mouse.
     *
     * @param event el evento de menú contextual que indica la posición del
     * mouse.
     */
    private void showContextMenu(ContextMenuEvent event) {
        contextMenu.show(centralPane, event.getScreenX(), event.getScreenY());
    }

    /**
     * Guarda la preferencia del tema en un archivo de propiedades.
     *
     * @param theme el tema a guardar, puede ser "light" o "dark".
     */
    private void saveThemePreference(String theme) {
        try {
            Properties props = new Properties();
            props.setProperty("theme", theme);
            props.store(new FileOutputStream("src/config/config.properties"), "Theme Settings");
        } catch (IOException e) {
            logger.severe("Error saving theme preference: " + e.getMessage());
        }
    }

    /**
     * Loads the saved theme preference from a configuration file using
     * ResourceBundle. If no preference is found, returns the default theme
     * "light".
     *
     * @return the saved theme preference, or "light" if no preference is found
     */
//private String loadThemePreference() {
//    try {
//        ResourceBundle bundle = ResourceBundle.getBundle("config/config");
//        return bundle.getString("theme");
//    } catch (Exception e) {
//        // Log an error message if loading the theme preference fails
//    }
//
//    return "light";
//}
    /**
     * Cambia el tema de la interfaz y guarda la preferencia.
     *
     * Este método actualiza el tema actual de la interfaz, lo aplica y lo
     * guarda en las preferencias del usuario para que se mantenga la próxima
     * vez que se inicie la aplicación.
     *
     * @param theme el nuevo tema a aplicar, que puede ser "light" o "dark".
     */
    private void switchTheme(String theme) {
        currentTheme = theme;
        loadTheme(theme);
        saveThemePreference(theme);
    }

    /**
     * Carga el tema CSS correspondiente según el parámetro.
     *
     * Este método limpia las hojas de estilo actuales de la escena y carga la
     * hoja de estilo correspondiente al tema especificado. También ajusta el
     * estilo del menú contextual de acuerdo al tema.
     *
     * @param theme el tema a cargar, que puede ser "light" o "dark".
     */
    private void loadTheme(String theme) {
        Scene scene = stage.getScene();
        scene.getStylesheets().clear();

        if (theme.equals("dark")) {
            String cssFile = "/css/dark-styles.css";
            scene.getStylesheets().add(getClass().getResource(cssFile).toExternalForm());
            contextMenu.getStyleClass().add("context-menu-dark");
        } else if (theme.equals("light")) {
            String cssFile = "/css/CSSglobal.css";
            scene.getStylesheets().add(getClass().getResource(cssFile).toExternalForm());
            contextMenu.getStyleClass().remove("context-menu-dark");
        }
    }

    /**
     * Limpia todos los campos de entrada.
     *
     * Este método borra el contenido de los campos de texto de email y
     * contraseña, dejando la interfaz lista para una nueva entrada de datos.
     */
    private void clearAllFields() {
        emailTextField.clear();
        passwordField.clear();
    }

    /**
     * Maneja la acción del botón de inicio de sesión.
     *
     * Este método valida las credenciales del usuario ingresadas en los campos
     * de texto y, si son correctas, navega a la pantalla principal de la
     * aplicación. En caso de que las credenciales sean incorrectas o se
     * produzca algún error, muestra un mensaje de alerta correspondiente.
     *
     * @throws InvalidEmailFormatException si el formato del email es inválido.
     */
    @FXML
    private void handleLogInButtonAction() {

        try {
            validateEmail(emailTextField.getText());
            String email = emailTextField.getText();
            String password = isPasswordVisible ? visiblePasswordField.getText() : passwordField.getText();

            User user = new User();
            user.setEmail(email);
            user.setPassword(password);

            User loggedInUser = SignableFactory.getSignable().signIn(user);

            if (loggedInUser != null) {
                logger.info("Inicio de sesion exitoso");
                navigateToScreen("/view/Main.fxml", "Main", true, loggedInUser);
            }
        } catch (IncorrectCredentialsException e) {
            utils.showAlert("Error", "No se pudo iniciar sesión. Verifique sus credenciales.");
            logger.warning("Credenciales incorrectas");
        } catch (ServerErrorException e) {
            utils.showAlert("Error", "Problemas de conexión con el servidor.");
            logger.warning("Error en la conexión");
        } catch (ConnectionException e) {
            utils.showAlert("Error", "Problemas de conexión a la base de datos.");
            logger.warning("Error en la conexión");
        } catch (MaxThreadUserException e) {
            utils.showAlert("Error", "No se pudo iniciar sesión. Máximo número de usuarios alcanzado. Espere unos minutos.");
            logger.warning("Máximo usuario alcanzado");
        } catch (InvalidEmailFormatException e) {
            utils.showAlert("Formato de email inválido", "El texto tiene que estar en formato email 'example@example.extension'");
            logger.warning("Formato de email inválido: " + emailTextField.getText());
        } catch (Exception e) {
            utils.showAlert("Error", "Ocurrió un error inesperado.");
            logger.severe("Error inesperado");
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
            passwordImage.setImage(new Image(getClass().getResourceAsStream("/Images/eye-slash-solid.png")));
            passwordField.setVisible(false);
            visiblePasswordField.setVisible(true);
            visiblePasswordField.setText(passwordField.getText());
        } else {
            passwordImage.setImage(new Image(getClass().getResourceAsStream("/Images/eye-solid.png")));
            passwordField.setVisible(true);
            visiblePasswordField.setVisible(false);
            passwordField.setText(visiblePasswordField.getText());
        }
    }

    /**
     * Navega a una pantalla diferente cargando el archivo FXML correspondiente.
     *
     * @param fxmlPath la ruta del archivo FXML de la pantalla objetivo.
     * @param title el título a establecer para la ventana.
     * @param main indica si es la pantalla principal.
     * @param user el usuario que inicia sesión.
     */
    private void navigateToScreen(String fxmlPath, String title, boolean main, User user) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();

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

    /**
     * Handle window close event to save theme preference.
     */
    @FXML
    private void handleWindowCloseRequest(WindowEvent event) {
        saveThemePreference(currentTheme); // Save theme preference when the window is closed
        Platform.exit();
    }
    
    public void validateEmail(String email) throws InvalidEmailFormatException {
        String emailRegex = "^[\\w-\\.]+@[\\w-]+\\.[a-zA-Z]{2,4}$";
        if (!email.matches(emailRegex)) {
            throw new InvalidEmailFormatException("Formato email invalido: " + email);
        }
    }
}
