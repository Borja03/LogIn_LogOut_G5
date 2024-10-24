package view;

import Model.User;
import static Utils.UtilsMethods.logger;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Optional;
import java.util.Properties;
import java.util.logging.Level;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.layout.Pane;

/**
 * The MainController handles the main functionalities of the main screen of the
 * application. It includes actions for showing and hiding the password, as well
 * as logging out. This class is linked to the main FXML view where user
 * interactions are handled.
 *
 * <p>
 * This controller allows the user to view the password in plain text, hide the
 * password, and log out to the login screen.</p>
 *
 * @author Borja
 */
public class MainController {

    @FXML
    private PasswordField passwordField; // The masked password input

    @FXML
    private TextField plainPasswordField; // The visible plain text input (used when the password is shown)

    @FXML
    private Button logOutButton; // LogOut button

    @FXML
    private Button eyeButton; // Eye button for showing/hiding password

    @FXML
    private ImageView eyeImageView; // Eye icon for password visibility

    // Image paths for the eye icons
    private final Image eyeClosed = new Image(getClass().getResourceAsStream("/Images/eye-solid.png"));
    private final Image eyeOpen = new Image(getClass().getResourceAsStream("/Images/eye-slash-solid.png"));

    private boolean passwordIsVisible = false;

    // Context menus for copying selected text
    private ContextMenu contextMenu;
    private String currentTheme = "light";

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
    private Pane mainPane;

    // Stage to manage the current window
    private Stage stage;

    // Getter and Setter for Stage
    public Stage getStage() {
        return stage;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    /**
     * Initializes the controller with the provided root element.
     *
     * @param root The root element of the scene.
     */
    public void initStage(Parent root, User user) {
        logger.info("Initializing MainController stage.");
        Scene scene = new Scene(root);
        // Set the stage properties
        stage.setScene(scene);
        stage.setTitle("Main");
        stage.setResizable(false);
        stage.getIcons().add(new Image("/Images/userIcon.png"));

        // Set initial visibility of password fields
        passwordField.setVisible(true);
        plainPasswordField.setVisible(false);
        eyeImageView.setImage(eyeClosed);

        // Initialize the context menu for copy functionality
        createContextMenu();
        root.setOnContextMenuRequested(this::showContextMenu);

        // Link event handlers
        logOutButton.setOnAction(this::logOut);
        eyeButton.setOnAction(this::togglePasswordVisibility);

        // Set up the stage if needed, e.g., adding close request handler
        stage.setOnCloseRequest(this::handleOnActionExit);

        logger.info("MainController initialized.");

        //
        stage.show();
    }

    /**
     * Creates a context menu with a copy option for selected text.
     */
    private void showContextMenu(ContextMenuEvent event) {
        contextMenu.show(mainPane, event.getScreenX(), event.getScreenY());
    }
    
    private void createContextMenu() {
        contextMenu = new ContextMenu();
        MenuItem copyItem = new MenuItem("Copy");

        // Set action for the copy menu item
        copyItem.setOnAction(this::handleCopyAction);

        MenuItem lightMode = new MenuItem("Light Mode");
        MenuItem darkMode = new MenuItem("Dark Mode");
        MenuItem clearFields = new MenuItem("Clear Fields");

        lightMode.setOnAction(e -> switchTheme("light"));
        darkMode.setOnAction(e -> switchTheme("dark"));

        // Add the copy option to the context menu
        contextMenu.getItems().addAll(copyItem, lightMode, darkMode, clearFields);

        // Attach the context menu to relevant text fields
        attachContextMenuToTextField(passwordField);
        attachContextMenuToTextField(plainPasswordField);
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

            // Aquí puedes agregar más acciones específicas para el tema claro
        }
    }

    /**
     * Handles the copy action from the context menu.
     *
     * @param event The action event triggered by the context menu.
     */
    private void handleCopyAction(ActionEvent event) {
        copySelectedText();
    }

    /**
     * Attaches the context menu to a TextField or PasswordField.
     *
     * @param textField The text field to which the context menu will be
     * attached.
     */
    private void attachContextMenuToTextField(TextField textField) {
        textField.setContextMenu(contextMenu);
    }

    /**
     * Copies the selected text from the active text field to the clipboard.
     */
    private void copySelectedText() {
        String selectedText = "";

        if (passwordField.isVisible() && passwordField.getSelectedText() != null) {
            selectedText = passwordField.getSelectedText();
        } else if (plainPasswordField.isVisible() && plainPasswordField.getSelectedText() != null) {
            selectedText = plainPasswordField.getSelectedText();
        }

        if (!selectedText.isEmpty()) {
            Clipboard clipboard = Clipboard.getSystemClipboard();
            ClipboardContent content = new ClipboardContent();
            content.putString(selectedText);
            clipboard.setContent(content);
        }
    }

    /**
     * Handles the log out action and navigates to the login screen.
     *
     * @param event The action event triggered by the log out button.
     */
    @FXML
    private void logOut(ActionEvent event) {
        navigateToScreen("/view/LogIn.fxml", "LogIn");
    }

    /**
     * Toggles the password visibility between masked and plain text.
     *
     * @param event The action event triggered by the eye button.
     */
    @FXML
    private void togglePasswordVisibility(ActionEvent event) {
        if (passwordIsVisible) {
            passwordField.setVisible(true);
            plainPasswordField.setVisible(false);
            eyeImageView.setImage(eyeClosed);
        } else {
            passwordField.setVisible(false);
            plainPasswordField.setVisible(true);
            plainPasswordField.setText(passwordField.getText());
            eyeImageView.setImage(eyeOpen);
        }
        passwordIsVisible = !passwordIsVisible;
    }

    /**
     * Navigates to a different screen based on the provided FXML path and
     * title.
     *
     * @param fxmlPath The path to the FXML file for the new screen.
     * @param windowTitle The title for the new window.
     */
    private void navigateToScreen(String fxmlPath, String windowTitle) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();
            Scene scene = new Scene(root);

            // Get the current stage
            Stage currentStage = stage != null ? stage : (Stage) logOutButton.getScene().getWindow();
            currentStage.setScene(scene);
            currentStage.setTitle(windowTitle);
            currentStage.show();
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Failed to load {0} screen: " + e.getMessage(), windowTitle);
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
            //LOGGER.log(Level.SEVERE, errorMsg);
        }
    }
}
