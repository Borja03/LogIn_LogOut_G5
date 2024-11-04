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
            // Create a Properties object to hold the theme setting.
            Properties props = new Properties();

            // Set the theme property to the specified value.
            props.setProperty("theme", theme);

            // Define the configuration file location.
            File file = new File("config.properties");

            // Store the properties in the file with a comment header "Theme Settings".
            props.store(new FileOutputStream(file), "Theme Settings");
        } catch (IOException e) {
            // Log an error message if saving the theme preference fails.
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
            // Create a Properties object to hold the loaded settings.
            Properties props = new Properties();

            // Define the configuration file location.
            File file = new File("config.properties");

            // Check if the configuration file exists.
            if (file.exists()) {
                // Load properties from the file.
                props.load(new FileInputStream(file));

                // Retrieve and return the theme property, defaulting to "light" if not set.
                return props.getProperty("theme", "light");
            }
        } catch (IOException e) {
            // Log an error message if loading the theme preference fails.
            logger.severe("Error loading theme preference: " + e.getMessage());
        }

        // Return the default theme if no preference is saved or an error occurs.
        return "light";
    }

    /**
     * Switches the current theme to the specified theme and saves the
     * preference.
     *
     * @param theme The new theme to be applied, such as "light" or "dark".
     */
    private void switchTheme(String theme) {
        // Update the current theme variable.
        currentTheme = theme;

        // Apply the specified theme to the application.
        loadTheme(theme);

        // Save the theme preference to persist the user's selection.
        saveThemePreference(theme);
    }

    /**
     * Loads the specified theme and applies the corresponding styles to the
     * scene.
     *
     * @param theme The theme to be loaded, either "light" or "dark".
     */
    private void loadTheme(String theme) {
        // Get the current scene and clear any existing stylesheets.
        Scene scene = stage.getScene();
        scene.getStylesheets().clear();

        // Apply the "dark" theme stylesheet and add dark styling to the context menu.
        if (theme.equals("dark")) {
            String cssFile = "/css/dark-styles.css";
            scene.getStylesheets().add(getClass().getResource(cssFile).toExternalForm());
            contextMenu.getStyleClass().add("context-menu-dark");
        } // Apply the "light" theme stylesheet and remove dark styling from the context menu.
        else if (theme.equals("light")) {
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
        // Initialize the selected text variable.
        String selectedText = "";

        // Check if the password field is visible and has selected text.
        if (passwordField.isVisible() && passwordField.getSelectedText() != null) {
            selectedText = passwordField.getSelectedText();
        } // If the plain password field is visible, use its selected text instead.
        else if (plainPasswordField.isVisible() && plainPasswordField.getSelectedText() != null) {
            selectedText = plainPasswordField.getSelectedText();
        }

        // Copy the selected text to the system clipboard if it's not empty.
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
        // Log the logout attempt.
        logger.info("Logging out...");

        try {
            // Load the Login screen from the FXML file.
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/Login.fxml"));
            Parent root = loader.load();

            // Get the current stage and set the new scene.
            Stage stage = (Stage) logOutButton.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);

            // Update the window title to "Login" and show the login screen.
            stage.setTitle("Login");
            stage.show();
        } catch (IOException e) {
            // Log an error message and show an alert if the logout fails.
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
        // Toggle the password visibility state.
        passwordIsVisible = !passwordIsVisible;

        if (passwordIsVisible) {
            // Show the plain text password field and update the icon to "eye open".
            passwordField.setVisible(false);
            plainPasswordField.setVisible(true);
            eyeImageView.setImage(eyeOpen);

            // Transfer the masked password text to the plain text field.
            plainPasswordField.setText(passwordField.getText());
        } else {
            // Show the masked password field and update the icon to "eye closed".
            passwordField.setVisible(true);
            plainPasswordField.setVisible(false);
            eyeImageView.setImage(eyeClosed);

            // Transfer the plain text password back to the masked field.
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
        // Create an informational alert with the specified message and an OK button.
        Alert alert = new Alert(Alert.AlertType.INFORMATION, message, ButtonType.OK);

        // Set the title of the alert dialog.
        alert.setTitle(title);

        // Display the alert dialog and wait for user interaction.
        alert.showAndWait();
    }
}
