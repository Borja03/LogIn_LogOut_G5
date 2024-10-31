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
 * The MainController class handles the main functionalities of the
 * application's main screen. It provides features for displaying user details,
 * toggling password visibility, and logging out. This controller is linked to
 * the main FXML view where user interactions are processed.
 *
 * <p>
 * Key functionalities include viewing the password in plain text, hiding the
 * password, and logging out to the login screen. It also manages theme
 * preferences and context menus for copying text.
 * </p>
 *
 * <p>
 * The controller maintains a clear separation of UI components, events, and
 * utility functions to enhance maintainability and readability.
 * </p>
 *
 * @author Borja
 */
public class MainController {

    // FXML components to display user details
    @FXML
    private TextField emailField;  // TextField to display user's email

    @FXML
    private TextField nameField;   // TextField to display user's name

    @FXML
    private TextField addressField; // TextField to display user's address

    // Password-related fields
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

    @FXML
    private Pane mainPane; // Main pane for context menu display

    // Image paths for the eye icons
    private final Image eyeClosed = new Image(getClass().getResourceAsStream("/Images/eye-solid.png"));
    private final Image eyeOpen = new Image(getClass().getResourceAsStream("/Images/eye-slash-solid.png"));

    private boolean passwordIsVisible = false; // Flag to track password visibility

    // Context menus for copying selected text
    private ContextMenu contextMenu;

    // Stage to manage the current window
    private Stage stage;
    
     // Getter and Setter for Stage
    public Stage getStage() {
        return stage;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    // Current theme setting
    private String currentTheme = "light";

    /**
     * Initializes the controller with the provided root element and the user
     * data.
     *
     * @param root The root element of the scene.
     * @param user The user object containing the user's details.
     */
    public void initStage(Parent root, User user) {
        logger.info("Initializing MainController stage.");
        Scene scene = new Scene(root);
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

        // Link event handlers
        logOutButton.setOnAction(this::logOut);
        eyeButton.setOnAction(this::togglePasswordVisibility);

        // Set user details in text fields
        // emailField.setText(user.getEmail());
        // nameField.setText(user.getName());
        // Concatenate street, city, and zip for the address field
        // String address = String.format("%s, %s, %d", user.getStreet(), user.getCity(), user.getZip());
        // addressField.setText(address);
        // Set the user's password securely
        // passwordField.setText(user.getPassword());
        // Initialize context menu
        initializeContextMenu();
        root.setOnContextMenuRequested(this::showContextMenu);

        // Load default theme
        currentTheme = loadThemePreference();
        loadTheme(currentTheme);

        logger.info("MainController initialized.");
        stage.show();
    }

    /**
     * Initializes the context menu with options for switching themes. Adds
     * "Light Mode" and "Dark Mode" menu items to the context menu, each of
     * which, when selected, triggers a theme switch action.
     */
    private void initializeContextMenu() {
        // Create a new context menu for theme selection
        contextMenu = new ContextMenu();

        // Create menu items for light and dark themes
        MenuItem lightMode = new MenuItem("Light Mode");
        MenuItem darkMode = new MenuItem("Dark Mode");

        // Set action for light mode menu item to switch to light theme
        lightMode.setOnAction(e -> switchTheme("light"));
        // Set action for dark mode menu item to switch to dark theme
        darkMode.setOnAction(e -> switchTheme("dark"));

        // Add menu items to the context menu
        contextMenu.getItems().addAll(lightMode, darkMode);
    }

    /**
     * Creates a context menu with options for copying text and changing themes.
     */
    private void createContextMenu() {
        contextMenu = new ContextMenu();
        MenuItem copyItem = new MenuItem("Copy");
        MenuItem lightMode = new MenuItem("Light Mode");
        MenuItem darkMode = new MenuItem("Dark Mode");

        lightMode.setOnAction(e -> switchTheme("light"));
        darkMode.setOnAction(e -> switchTheme("dark"));

        // Set action for the copy menu item
        copyItem.setOnAction(this::handleCopyAction);

        // Add options to the context menu
        contextMenu.getItems().addAll(copyItem, lightMode, darkMode);

        // Attach the context menu to relevant text fields
        attachContextMenuToTextField(passwordField);
        attachContextMenuToTextField(plainPasswordField);
    }

    /**
     * Displays the context menu at the specified location on the screen.
     *
     * @param event The event that triggered the context menu display.
     */
    private void showContextMenu(ContextMenuEvent event) {
        contextMenu.show(mainPane, event.getScreenX(), event.getScreenY());
    }

    /**
     * Saves the user's theme preference to a configuration file.
     *
     * @param theme The theme to be saved, such as "light" or "dark".
     */
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

    /**
     * Loads the user's theme preference from a configuration file.
     *
     * @return The saved theme preference, or "light" if not found.
     */
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

    /**
     * Switches the current theme to the specified theme and saves the
     * preference.
     *
     * @param theme The new theme to be applied, such as "light" or "dark".
     */
    private void switchTheme(String theme) {
        currentTheme = theme;
        loadTheme(theme);
        saveThemePreference(theme);
    }

    /**
     * Loads the specified theme and applies the corresponding styles to the
     * scene.
     *
     * @param theme The theme to be loaded, either "light" or "dark".
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
     * @param event The action event triggered by clicking the log out button.
     */
    @FXML
    public void logOut(ActionEvent event) {
        logger.info("Logging out...");
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/Login.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) logOutButton.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Login");
            stage.show();
        } catch (IOException e) {
            logger.severe("Error during log out: " + e.getMessage());
            showAlert("Error", "An error occurred during log out.");
        }
    }

    /**
     * Toggles the visibility of the password between masked and plain text.
     *
     * @param event The action event triggered by clicking the eye button.
     */
    @FXML
    public void togglePasswordVisibility(ActionEvent event) {
        passwordIsVisible = !passwordIsVisible;

        if (passwordIsVisible) {
            passwordField.setVisible(false);
            plainPasswordField.setVisible(true);
            eyeImageView.setImage(eyeOpen);
            plainPasswordField.setText(passwordField.getText());
        } else {
            passwordField.setVisible(true);
            plainPasswordField.setVisible(false);
            eyeImageView.setImage(eyeClosed);
            passwordField.setText(plainPasswordField.getText());
        }
    }

    /**
     * Displays an alert dialog with a specified title and message.
     *
     * @param title The title of the alert dialog.
     * @param message The message to display in the alert dialog.
     */
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION, message, ButtonType.OK);
        alert.setTitle(title);
        alert.showAndWait();
    }
}
