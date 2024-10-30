package view;

import Model.SignableFactory; // Importa la fábrica de objetos signables para manejar el inicio de sesión.
import Model.User; // Importa la clase User para manejar información del usuario.
import javafx.fxml.FXML; // Importa las anotaciones FXML para vincular el controlador a la interfaz gráfica.
import javafx.scene.control.Alert; // Importa Alert para mostrar mensajes de alerta.
import javafx.scene.control.Button; // Importa Button para crear botones.
import javafx.scene.control.Label; // Importa Label para crear etiquetas.
import javafx.scene.control.PasswordField; // Importa PasswordField para campos de contraseña.
import javafx.scene.control.TextField; // Importa TextField para campos de texto.
import javafx.scene.control.Hyperlink; // Importa Hyperlink para enlaces.
import javafx.scene.image.Image; // Importa Image para manejar imágenes.
import javafx.scene.image.ImageView; // Importa ImageView para mostrar imágenes.

import java.util.logging.Logger; // Importa Logger para registrar eventos y mensajes.
import Utils.UtilsMethods; // Importa métodos utilitarios personalizados.
import exception.*; // Importa excepciones personalizadas.
import javafx.scene.layout.Pane; // Importa Pane para la gestión de layouts.
import java.util.logging.Level; // Importa Level para manejar niveles de registro.
import javafx.fxml.FXMLLoader; // Importa FXMLLoader para cargar archivos FXML.
import javafx.scene.Scene; // Importa Scene para manejar escenas.
import javafx.stage.Stage; // Importa Stage para manejar ventanas.

import java.io.File; // Importa File para manipular archivos.
import java.io.FileInputStream; // Importa FileInputStream para leer archivos.
import java.io.FileOutputStream; // Importa FileOutputStream para escribir archivos.
import java.io.IOException; // Importa IOException para manejar excepciones de entrada/salida.
import java.net.ConnectException;
import java.util.Properties; // Importa Properties para manejar propiedades.
import javafx.scene.Parent; // Importa Parent para la raíz de la jerarquía de nodos.
import javafx.scene.control.ContextMenu; // Importa ContextMenu para manejar menús contextuales.
import javafx.scene.control.MenuItem; // Importa MenuItem para manejar ítems de menú.
import javafx.scene.input.ContextMenuEvent; // Importa ContextMenuEvent para eventos de menú contextual.
import javafx.scene.layout.BorderPane; // Importa BorderPane para la gestión de layouts.

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
    private BorderPane borderPane; // Pane para la organización de la interfaz.

    private ContextMenu contextMenu; // Menú contextual para opciones adicionales.
    private String currentTheme = "light"; // Tema actual de la interfaz.
    @FXML
    private Pane centralPane; // Pane central para la organización.
    private Stage stage; // Ventana principal.

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

    /**
     * Método que se ejecuta al inicializar el controlador. Configura el campo
     * de texto visible para la contraseña y agrega un listener para validar el
     * email cuando pierde el foco.
     *
     * @param root la raíz de la interfaz.
     */
    @FXML
    public void initialize(Parent root) {
        Scene scene = new Scene(root); // Crea una nueva escena con el root proporcionado.
        stage.setScene(scene); // Establece la escena en la ventana principal.
        stage.setTitle("SignIn"); // Establece el título de la ventana.
        stage.setResizable(false); // Deshabilita el redimensionamiento de la ventana.
        stage.centerOnScreen(); // Centra la ventana en la pantalla.

        visiblePasswordField.setVisible(false); // Oculta el campo de texto visible para la contraseña.
        initializeContextMenu(); // Inicializa el menú contextual.

        // Asigna el menú contextual al BorderPane
        borderPane.setOnContextMenuRequested(this::showContextMenu); // Muestra el menú contextual al solicitarlo.

        currentTheme = loadThemePreference(); // Carga la preferencia del tema guardada.
        loadTheme(currentTheme); // Aplica el tema cargado.
        stage.show(); // Muestra la ventana.
    }

    // Método para inicializar el menú contextual y sus opciones.
    private void initializeContextMenu() {
        contextMenu = new ContextMenu(); // Crea un nuevo menú contextual.

        MenuItem lightMode = new MenuItem("Light Mode"); // Opción para el modo claro.
        MenuItem darkMode = new MenuItem("Dark Mode"); // Opción para el modo oscuro.
        MenuItem clearFields = new MenuItem("Clear Fields"); // Opción para limpiar campos.

        // Establece acciones para las opciones del menú.
        lightMode.setOnAction(e -> switchTheme("light")); // Cambia al modo claro.
        darkMode.setOnAction(e -> switchTheme("dark")); // Cambia al modo oscuro.
        clearFields.setOnAction(e -> clearAllFields()); // Limpia todos los campos.

        contextMenu.getItems().addAll(lightMode, darkMode, clearFields); // Agrega opciones al menú contextual.
    }

    /**
     * Muestra el menú contextual en la posición del mouse.
     *
     * @param event el evento de menú contextual.
     */
    private void showContextMenu(ContextMenuEvent event) {
        contextMenu.show(centralPane, event.getScreenX(), event.getScreenY()); // Muestra el menú contextual.
    }

    /**
     * Guarda la preferencia del tema en un archivo de propiedades.
     *
     * @param theme el tema a guardar.
     */
    private void saveThemePreference(String theme) {
        try {
            Properties props = new Properties(); // Crea un objeto Properties para almacenar el tema.
            props.setProperty("theme", theme); // Establece la propiedad del tema.
            File file = new File("config.properties"); // Archivo donde se guardará la preferencia.
            props.store(new FileOutputStream(file), "Theme Settings"); // Almacena las propiedades en el archivo.
        } catch (IOException e) {
            logger.severe("Error saving theme preference: " + e.getMessage()); // Registra un error si ocurre una excepción.
        }
    }

    /**
     * Carga la preferencia del tema desde un archivo de propiedades.
     *
     * @return el tema cargado, o "light" si no se encuentra.
     */
    private String loadThemePreference() {
        try {
            Properties props = new Properties(); // Crea un objeto Properties para cargar el tema.
            File file = new File("config.properties"); // Archivo donde se espera encontrar la preferencia.
            if (file.exists()) {
                props.load(new FileInputStream(file)); // Carga las propiedades desde el archivo.
                return props.getProperty("theme", "light"); // Devuelve el tema cargado, o "light" por defecto.
            }
        } catch (IOException e) {
            logger.severe("Error loading theme preference: " + e.getMessage()); // Registra un error si ocurre una excepción.
        }
        return "light"; // Devuelve el tema por defecto.
    }

    /**
     * Cambia el tema de la interfaz y guarda la preferencia.
     *
     * @param theme el nuevo tema a aplicar.
     */
    private void switchTheme(String theme) {
        currentTheme = theme; // Actualiza el tema actual.
        loadTheme(theme); // Aplica el nuevo tema.
        saveThemePreference(theme); // Guarda la preferencia del nuevo tema.
    }

    /**
     * Carga el tema CSS correspondiente.
     *
     * @param theme el tema a cargar.
     */
    private void loadTheme(String theme) {
        Scene scene = stage.getScene(); // Obtiene la escena actual.
        scene.getStylesheets().clear(); // Limpia las hojas de estilo actuales.

        if (theme.equals("dark")) { // Si el tema es oscuro
            String cssFile = "/css/dark-styles.css"; // Ruta del archivo CSS para el tema oscuro.
            scene.getStylesheets().add(getClass().getResource(cssFile).toExternalForm()); // Agrega la hoja de estilo para el tema oscuro.
            contextMenu.getStyleClass().add("context-menu-dark"); // Establece el estilo oscuro para el menú contextual.
        } else if (theme.equals("light")) { // Si el tema es claro
            String cssFile = "/css/CSSglobal.css"; // Ruta del archivo CSS para el tema claro.
            scene.getStylesheets().add(getClass().getResource(cssFile).toExternalForm()); // Agrega la hoja de estilo para el tema claro.
            contextMenu.getStyleClass().remove("context-menu-dark"); // Remueve el estilo oscuro del menú contextual.
        }
    }

    /**
     * Limpia todos los campos de entrada.
     */
    private void clearAllFields() {
        emailTextField.clear(); // Limpia el campo de email.
        passwordField.clear(); // Limpia el campo de contraseña.
    }

    /**
     * Maneja la acción del botón de inicio de sesión. Valida las credenciales
     * del usuario y muestra un mensaje apropiado. Si el inicio de sesión es
     * exitoso, navega a la pantalla principal.
     */
    @FXML
    private void handleLogInButtonAction() throws InvalidEmailFormatException {
        utils.validateEmail(emailTextField.getText()); // Valida el formato del email ingresado.
        String email = emailTextField.getText(); // Obtiene el email del campo de texto.
        String password = isPasswordVisible ? visiblePasswordField.getText() : passwordField.getText(); // Obtiene la contraseña según su visibilidad.

        User user = new User(); // Crea una nueva instancia de User.
        user.setEmail(email); // Establece el email del usuario.
        user.setPassword(password); // Establece la contraseña del usuario.

        try {
            User loggedInUser = SignableFactory.getSignable().signIn(user); // Intenta iniciar sesión con el usuario.

            if (loggedInUser != null) { // Si el inicio de sesión es exitoso
                // Si el inicio de sesión es exitoso, navega a la pantalla principal
                navigateToScreen("/view/Main.fxml", "Main", true, loggedInUser); // Navega a la vista principal.
            }
        } catch (IncorrectCredentialsException e) { // Captura excepciones específicas
            utils.showAlert("Error", "No se pudo iniciar sesión. Verifique sus credenciales."); // Muestra un mensaje de error.
            logger.warning("Credenciales incorrectas"); // Registra una advertencia.
        } catch (ServerErrorException e) { // Captura excepciones de conexión
            utils.showAlert("Error", "Problemas de conexión con el servidor."); // Muestra un mensaje de error.
            logger.warning("Error en la conexion"); // Registra una advertencia.
        } catch (ConnectionException e) { // Captura excepciones de conexión
            utils.showAlert("Error", "Problemas de conexión a la base de datos."); // Muestra un mensaje de error.
            logger.warning("Error en la conexion"); // Registra una advertencia.
        }catch (MaxThreadUserException e) { // Captura excepcion de max user
            utils.showAlert("Error", "No se pudo iniciar sesión. Maximo número de usuarios alcanzado espere unos minutos."); // Muestra un mensaje de error.
            logger.warning("Maximo usuario alcanzado"); // Registra una advertencia.
        } catch (Exception e) { // Captura cualquier otra excepción
            utils.showAlert("Error", "Ocurrió un error inesperado."); // Muestra un mensaje de error.
            logger.severe("Error inesperado"); // Registra un error grave.
        }
        // Captura excepciones específicas
        // Muestra un mensaje de error.
        // Registra una advertencia.
         
    }

    /**
     * Maneja la acción del enlace para crear un usuario. Navega a la vista de
     * registro.
     */
    @FXML
    private void handleCreateUserLinkAction() {
        logger.info("Abrir vista de registro."); // Registra la acción de abrir la vista de registro.
        navigateToScreen("/view/SignUpView.fxml", "SignUp", false, null); // Navega a la vista de registro.
    }

    /**
     * Maneja la acción de cambiar la visibilidad de la contraseña. Alterna
     * entre mostrar y ocultar la contraseña en el campo correspondiente.
     */
    @FXML
    private void handlePasswordImageButtonAction() {
        isPasswordVisible = !isPasswordVisible; // Cambia el estado de visibilidad de la contraseña.

        if (isPasswordVisible) { // Si la contraseña es visible
            passwordImage.setImage(new Image(getClass().getResourceAsStream("/Images/eye-slash-solid.png"))); // Cambia la imagen a visible.
            passwordField.setVisible(false); // Oculta el campo de contraseña.
            visiblePasswordField.setVisible(true); // Muestra el campo de contraseña visible.
            visiblePasswordField.setText(passwordField.getText()); // Copia la contraseña al campo visible.
        } else { // Si la contraseña no es visible
            passwordImage.setImage(new Image(getClass().getResourceAsStream("/Images/eye-solid.png"))); // Cambia la imagen a no visible.
            passwordField.setVisible(true); // Muestra el campo de contraseña.
            visiblePasswordField.setVisible(false); // Oculta el campo de contraseña visible.
            passwordField.setText(visiblePasswordField.getText()); // Copia la contraseña del campo visible al campo oculto.
        }
    }

    /**
     * Navega a una pantalla diferente cargando el archivo FXML correspondiente.
     *
     * @param fxmlPath la ruta del archivo FXML de la pantalla objetivo
     * @param title el título a establecer para la ventana
     * @param main indica si es la pantalla principal
     * @param user el usuario que inicia sesión
     */
    private void navigateToScreen(String fxmlPath, String title, boolean main, User user) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath)); // Carga el archivo FXML.
            Parent root = loader.load(); // Crea la raíz de la nueva escena.
            // Get the current stage
            if (!main) { // Si no es la pantalla principal
                SignUpController controller = loader.getController(); // Obtiene el controlador de la vista de registro.
                Stage newStage = new Stage(); // Crea una nueva ventana.
                controller.setStage(newStage); // Establece la nueva ventana en el controlador.
                controller.initStage(root); // Inicializa la nueva escena.
                stage.close(); // Cierra la ventana actual.
            } else { // Si es la pantalla principal
                MainController controller = loader.getController(); // Obtiene el controlador de la vista principal.
                Stage newStage = new Stage(); // Crea una nueva ventana.
                controller.setStage(newStage); // Establece la nueva ventana en el controlador.
                controller.initStage(root, user); // Inicializa la nueva escena con el usuario.
                stage.close(); // Cierra la ventana actual.
            }

        } catch (Exception e) {
            logger.log(Level.SEVERE, "Failed to load " + title + " screen: " + e.getMessage(), e); // Registra un error si falla la carga de la pantalla.
        }
    }
}