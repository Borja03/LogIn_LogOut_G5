package view;

import Model.SignableFactory;
import Model.User;
import Utils.UtilsMethods;
import static Utils.UtilsMethods.logger;
import exception.ConnectionException;
import exception.EmptyFieldException;
import exception.InvalidCityFormatException;
import exception.InvalidEmailFormatException;
import exception.InvalidPasswordFormatException;
import exception.InvalidStreetFormatException;
import exception.InvalidZipFormatException;
import exception.ServerErrorException;
import exception.UserAlreadyExistsException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Optional;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class SignUpController {

    // Logger for logging events
    private static final Logger LOGGER = Logger.getLogger(SignUpController.class.getName());

    // UI Components
    @FXML
    private Button btn_show_password;

    @FXML
    private Button btn_signup;

    @FXML
    private CheckBox chb_active;

    @FXML
    private Hyperlink hl_login;

    @FXML
    private ImageView imgCity;

    @FXML
    private ImageView imgEmail;

    @FXML
    private ImageView imgKey;

    @FXML
    private ImageView imgLock;

    @FXML
    private ImageView imgShowPassword;

    @FXML
    private ImageView imgStreet;

    @FXML
    private ImageView imgUser;

    @FXML
    private ImageView imgZIP;

    @FXML
    private Label lbl_error;

    @FXML
    private PasswordField pf_password;

    @FXML
    private PasswordField pf_password_confirm;

    @FXML
    private TextField tf_city;

    @FXML
    private TextField tf_email;

    @FXML
    private TextField tf_name;

    @FXML
    private TextField tf_password;

    @FXML
    private TextField tf_password_confirm;

    @FXML
    private TextField tf_street;

    @FXML
    private TextField tf_zip;

    @FXML
    private VBox vbx_card;

    private Stage stage;

    private boolean isPasswordVisible = false;

    public Stage getStage() {
        return stage;
    }
    
    UtilsMethods utils = new UtilsMethods();

    public void setStage(Stage stage) {
        this.stage = stage;
    }
    private ContextMenu contextMenu;
    private String currentTheme = "light";

    public void initStage(Parent root) {
        LOGGER.info("Initialising Sign Up window.");

        Scene scene = new Scene(root);
        // Set the stage properties
        stage.setScene(scene);
        stage.setTitle("Sign Up");
        stage.setResizable(false);
        // stage.initModality(Modality.APPLICATION_MODAL);
        stage.centerOnScreen();

        stage.getIcons().add(new Image("/Images/userIcon.png"));

        tf_password.setVisible(false);
        tf_password_confirm.setVisible(false);

        btn_signup.setOnAction(this::handleSignUpButtonAction);
        hl_login.setOnAction(this::handleLoginHyperlinkAction);
        btn_show_password.setOnAction(this::handlePasswordImageButtonAction);
        stage.setOnCloseRequest(this::handleOnActionExit);

        // menu
        // Initialize context menu
        initializeContextMenu();

        // Add context menu to the scene
        root.setOnContextMenuRequested(this::showContextMenu);

        // Load default theme
        currentTheme = loadThemePreference();
        loadTheme(currentTheme);
        LOGGER.info("Window opened.");

        // Show the stage
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
        contextMenu.show(vbx_card, event.getScreenX(), event.getScreenY());
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
            imgEmail.setImage(new Image(getClass().getResourceAsStream("/Images/envelope-solid-white.png")));
            imgLock.setImage(new Image(getClass().getResourceAsStream("/Images/lock-solid-white.png")));
            imgKey.setImage(new Image(getClass().getResourceAsStream("/Images/key-solid-white.png")));
            imgUser.setImage(new Image(getClass().getResourceAsStream("/Images/user-solid-white.png")));
            imgStreet.setImage(new Image(getClass().getResourceAsStream("/Images/location-dot-solid-white.png")));
            imgCity.setImage(new Image(getClass().getResourceAsStream("/Images/city-solid-white.png")));
            imgZIP.setImage(new Image(getClass().getResourceAsStream("/Images/imgZIP-white.png")));
             contextMenu.getStyleClass().add("context-menu-dark");

            // Aquí puedes agregar más acciones específicas para el tema oscuro
        } else if (theme.equals("light")) {
            // Código adicional para el tema claro

            String cssFile = "/css/CSSglobal.css";

            scene.getStylesheets().add(getClass().getResource(cssFile).toExternalForm());
            imgEmail.setImage(new Image(getClass().getResourceAsStream("/Images/envelope-solid.png")));
            imgLock.setImage(new Image(getClass().getResourceAsStream("/Images/lock-solid.png")));
            imgKey.setImage(new Image(getClass().getResourceAsStream("/Images/key-solid.png")));
            imgUser.setImage(new Image(getClass().getResourceAsStream("/Images/user-solid.png")));
            imgStreet.setImage(new Image(getClass().getResourceAsStream("/Images/location-dot-solid.png")));
            imgCity.setImage(new Image(getClass().getResourceAsStream("/Images/city-solid.png")));
            imgZIP.setImage(new Image(getClass().getResourceAsStream("/Images/imgZIP.png")));
             contextMenu.getStyleClass().remove("context-menu-dark");

            // Aquí puedes agregar más acciones específicas para el tema claro
        }
    }

    private void clearAllFields() {
        LOGGER.info("Clearing all input fields.");
        tf_email.clear();
        pf_password.clear();
        tf_password.clear();
        pf_password_confirm.clear();
        tf_password_confirm.clear();
        tf_name.clear();
        tf_street.clear();
        tf_city.clear();
        tf_zip.clear();
        chb_active.setSelected(false);
        lbl_error.setText("");
    }

    private void handlePasswordImageButtonAction(ActionEvent event) {
        isPasswordVisible = !isPasswordVisible;
        if (isPasswordVisible) {
            imgShowPassword.setImage(new Image(getClass().getResourceAsStream("/Images/eye-slash-solid.png")));
            pf_password.setVisible(false);
            tf_password.setVisible(true);
            tf_password.setText(pf_password.getText());
            pf_password_confirm.setVisible(false);
            tf_password_confirm.setVisible(true);
            tf_password_confirm.setText(pf_password_confirm.getText());
        } else {
            imgShowPassword.setImage(new Image(getClass().getResourceAsStream("/Images/eye-solid.png")));
            pf_password.setVisible(true);
            tf_password.setVisible(false);
            pf_password.setText(tf_password.getText());
            pf_password_confirm.setVisible(true);
            tf_password_confirm.setVisible(false);
            pf_password_confirm.setText(tf_password_confirm.getText());
        }
    }

    private void handleSignUpButtonAction(ActionEvent event) {
        try {

            String password;
            String confirmPassword;
            String email = tf_email.getText();

            // Revisar qué campos de contraseña están activos y usarlos
            if (pf_password.isVisible()) {
                password = pf_password.getText();
                confirmPassword = pf_password_confirm.getText();
            } else {
                password = tf_password.getText();
                confirmPassword = tf_password_confirm.getText();
            }

            String name = tf_name.getText();
            String street = tf_street.getText();
            String city = tf_city.getText();
            String zip = tf_zip.getText();
            boolean isActive = chb_active.isSelected();
            // Clear previous error messages            
            lbl_error.setText("");

            // Validate inputs
            validateInputs(email, password, confirmPassword, name, street, city, zip);
            // Proceed with sign-up logic
            performSignUp(email, password, name, 1, street, city, Integer.parseInt(zip), isActive);
            LOGGER.info("Performing signup");
        } catch (Exception e) {
            lbl_error.setText(e.getMessage());
            LOGGER.log(Level.WARNING, e.getMessage(), e);
        }
    }

    private void validateInputs(String email, String password, String confirmPassword, String name, String street, String city, String zip)
                    throws EmptyFieldException, InvalidEmailFormatException, InvalidPasswordFormatException,
                    InvalidCityFormatException, InvalidZipFormatException, InvalidStreetFormatException {
        // check empty fileds
        checkEmptyFields(email, password, confirmPassword, name, street, city, zip);
        //check fields format 
        checkFieldsFormat(email, password, confirmPassword, name, street, city, zip);

    }

    private void checkEmptyFields(String email, String password, String confirmPassword, String name, String street, String city, String zip) throws EmptyFieldException {
        // Validate email
        if (email == null || email.isEmpty()) {
            throw new EmptyFieldException("Email cannot be empty.");
        }
        // Validate password
        if (password == null || password.isEmpty()) {
            throw new EmptyFieldException("Password cannot be empty.");
        }
        // Validate password confirmation empty 
        if (confirmPassword == null || confirmPassword.isEmpty()) {
            throw new EmptyFieldException("Password confirmation cannot be empty.");
        }
        // Validate name is empty 
        if (name == null || name.isEmpty()) {
            throw new EmptyFieldException("Name cannot be empty.");
        }
        // Validate street
        if (street == null || street.isEmpty()) {
            throw new EmptyFieldException("Street cannot be empty.");
        }
        // You can add any additional format checks for street if needed
        // Validate city
        if (city == null || city.isEmpty()) {
            throw new EmptyFieldException("City cannot be empty.");
        }
        // Validate zip
        if (zip == null || zip.isEmpty()) {
            throw new EmptyFieldException("Zip code cannot be empty.");
        }
    }

    private void checkFieldsFormat(String email, String password, String confirmPassword, String name, String street, String city, String zip) throws EmptyFieldException, InvalidEmailFormatException, InvalidPasswordFormatException,
                    InvalidCityFormatException, InvalidZipFormatException, InvalidStreetFormatException {
        // Regex pattern for a valid email format
        String emailRegex = "^[a-zA-Z0-9._%+-]+@([a-zA-Z0-9.-]+\\.[a-zA-Z]{2,})$";
        if (!email.matches(emailRegex)) {
            throw new InvalidEmailFormatException("Email must be in a valid format (e.g., example@domain.com).");
        }
        // Validate password format
        if (!validatePassword(password)) {
            throw new InvalidPasswordFormatException("Password must be at least 6 characters, with lowercase, uppercase, numbers, and special characters.");
        }
        // Validate password format equals
        if (!password.equals(confirmPassword)) {
            throw new InvalidPasswordFormatException("Passwords do not match.");
        }
        //  check that city only contains letters (basic validation)
        if (!city.matches("[a-zA-Z\\s]+")) {
            throw new InvalidCityFormatException("City must only contain letters.");
        }
        // Example: check that zip contains exactly 5 digits
        if (!zip.matches("\\d{5}")) {
            throw new InvalidZipFormatException("Zip code must be exactly 5 digits.");
        }
    }

    private boolean validatePassword(String password) {
        if (password.length() < 6) {
            return false;
        }

        boolean hasUppercase = false;
        boolean hasDigit = false;
        boolean hasSpecialChar = false;

        for (char c : password.toCharArray()) {
            if (Character.isUpperCase(c)) {
                hasUppercase = true;
            } else if (Character.isDigit(c)) {
                hasDigit = true;
            } else if (!Character.isLetterOrDigit(c)) {
                hasSpecialChar = true;
            }
        }
        return hasUppercase && hasDigit && hasSpecialChar;
    }

   		
    private void performSignUp(String email, String password, String name, int companyID, String street, String city, int zip, boolean isActive) {
        User user = new User(email, password, name, isActive, companyID, street, city, zip);

        try {
            // Attempting to sign up the user
            User nuevoUser = SignableFactory.getSignable().signUp(user);
            // If the sign-up is successful
            if (nuevoUser != null) {
                utils.showAlert("Felicidades","Usuario añadido correctamente");
            }
        } catch (UserAlreadyExistsException e) {
            // Handle duplicate email error
            utils.showAlert("Error", "Email already exists. Please use another email.");
            LOGGER.warning("Email already exists");
        } catch (ServerErrorException e) {
            // Handle server error
            utils.showAlert("Error", "Server is not available at the moment. Please try again later.");
            LOGGER.warning("Server error occurred");
        } catch (ConnectionException e) { // Captura excepciones de conexión
            utils.showAlert("Error", "Problemas de conexión a la base de datos."); // Muestra un mensaje de error.
            logger.warning("Error en la conexion"); // Registra una advertencia.
        }catch (Exception e) {
            // Handle unexpected errors
            utils.showAlert("Error", "An unexpected error occurred: " + e.getMessage());
            LOGGER.log(Level.SEVERE, "Unexpected error in performSignUp", e);
        }
     }

    public void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void handleLoginHyperlinkAction(ActionEvent event) {
        navigateToScreen("/view/LogIn.fxml", "LogIn");
    }

    private void navigateToScreen(String fxmlPath, String title) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();
            // Get the current stage
            
                logInController controller = loader.getController();
                Stage newStage = new Stage();
                controller.setStage(newStage);
                controller.initialize(root);

            

            stage = (Stage) btn_signup.getScene().getWindow();
            //stage.hide();
            stage.close();
            logger.log(Level.SEVERE, "Stage closed");

        } catch (Exception e) {
            logger.log(Level.SEVERE, "Failed to load " + title + " screen: " + e.getMessage(), e);
        }
    }

    private void handleOnActionExit(Event event) {
        try {
            //Ask user for confirmation on exit
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION,
                            "Are you sure you want to exit the application?",
                            ButtonType.OK, ButtonType.CANCEL);
            Optional<ButtonType> result = alert.showAndWait();
            //If OK to exit
            if (result.isPresent() && result.get() == ButtonType.OK) {
                Platform.exit();
            } else {
                event.consume();
            }
        } catch (Exception e) {
            String errorMsg = "Error exiting application:" + e.getMessage();
            Alert alert = new Alert(Alert.AlertType.ERROR,
                            errorMsg,
                            ButtonType.OK);
            alert.showAndWait();
            LOGGER.log(Level.SEVERE, errorMsg);
        }
    }

}