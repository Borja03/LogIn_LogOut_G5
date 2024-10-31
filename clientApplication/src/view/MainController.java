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
 * The MainController class manages the main screen of the application.
 * It includes actions for showing and hiding the password, logging out,
 * theme switching, and handling user interactions.
 *
 * <p>
 * The controller is associated with the main FXML view where various
 * actions are managed, including toggling password visibility, logging out,
 * copying selected text, and switching between light and dark themes.
 * </p>
 *
 * @see javafx.fxml.FXML
 * @see javafx.stage.Stage
 * @see javafx.scene.Scene
 * @see javafx.scene.control.Button
 * @see javafx.scene.control.PasswordField
 * @see javafx.scene.control.TextField
 * @see javafx.scene.input.ContextMenuEvent
 * @see javafx.scene.layout.Pane
 * 
 * @since 1.0
 * @version 1.0
 */
public class MainController {

    @FXML
    private TextField emailField;

    @FXML
    private TextField nameField;

    @FXML
    private TextField addressField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private TextField plainPasswordField;

    @FXML
    private Button logOutButton;

    @FXML
    private Button eyeButton;

    @FXML
    private ImageView eyeImageView;

    @FXML
    private Pane mainPane;

    private final Image eyeClosed = new Image(getClass().getResourceAsStream("/Images/eye-solid.png"));
    private final Image eyeOpen = new Image(getClass().getResourceAsStream("/Images/eye-slash-solid.png"));

    private boolean passwordIsVisible = false;

    private ContextMenu contextMenu;

    private Stage stage;

    private String currentTheme = "light";

    /**
     * Retrieves the current Stage instance.
     *
     * @return The active stage where the scene is set.
     */
    public Stage getStage() {
        return stage;
    }

    /**
     * Assigns a Stage instance to this controller.
     *
     * @param stage the stage instance to associate with the controller.
     */
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    /**
     * Initializes the main stage with user data and sets up UI elements, theme preference,
     * and event handlers.
     *
     * @param root the root element of the scene.
     * @param user the User object containing details to populate the UI fields.
     */
    public void initStage(Parent root, User user) {
        logger.info("Initializing MainController stage.");
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Main");
        stage.setResizable(false);
        stage.getIcons().add(new Image("/Images/userIcon.png"));

        passwordField.setVisible(true);
        plainPasswordField.setVisible(false);
        eyeImageView.setImage(eyeClosed);

        createContextMenu();

        logOutButton.setOnAction(this::logOut);
        eyeButton.setOnAction(this::togglePasswordVisibility);

        emailField.setText(user.getEmail());
        nameField.setText(user.getName());
        String address = String.format("%s, %s, %d", user.getStreet(), user.getCity(), user.getZip());
        addressField.setText(address);
        passwordField.setText(user.getPassword());

        initializeContextMenu();
        root.setOnContextMenuRequested(this::showContextMenu);

        currentTheme = loadThemePreference();
        loadTheme(currentTheme);

        logger.info("MainController initialized.");
        stage.show();
    }

    /**
     * Creates a context menu for text fields, with options to copy text and change themes.
     */
    private void createContextMenu() {
        contextMenu = new ContextMenu();
        MenuItem copyItem = new MenuItem("Copy");
        MenuItem lightMode = new MenuItem("Light Mode");
        MenuItem darkMode = new MenuItem("Dark Mode");

        lightMode.setOnAction(e -> switchTheme("light"));
        darkMode.setOnAction(e -> switchTheme("dark"));
        copyItem.setOnAction(this::handleCopyAction);

        contextMenu.getItems().add(copyItem);

        attachContextMenuToTextField(passwordField);
        attachContextMenuToTextField(plainPasswordField);
    }

    /**
     * Displays the context menu at the specified screen coordinates when triggered.
     *
     * @param event the context menu request event.
     */
    private void showContextMenu(ContextMenuEvent event) {
        contextMenu.show(mainPane, event.getScreenX(), event.getScreenY());
    }

    /**
     * Persists the theme preference in a properties file.
     *
     * @param theme the theme name ("light" or "dark") to save as a preference.
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
     * Loads the stored theme preference from the properties file.
     *
     * @return the name of the theme ("light" or "dark"), defaults to "light".
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
     * Switches the UI theme to the specified theme and updates the preference file.
     *
     * @param theme the theme to apply ("light" or "dark").
     */
    private void switchTheme(String theme) {
        currentTheme = theme;
        loadTheme(theme);
        saveThemePreference(theme);
    }

    /**
     * Applies the specified theme to the scene's stylesheet.
     *
     * @param theme the theme name ("light" or "dark") to load.
     */
    private void loadTheme(String theme) {
        Scene scene = stage.getScene();
        scene.getStylesheets().clear();
        String cssFile = theme.equals("dark") ? "/css/dark-styles.css" : "/css/CSSglobal.css";
        scene.getStylesheets().add(getClass().getResource(cssFile).toExternalForm());
        if (theme.equals("dark")) {
            contextMenu.getStyleClass().add("context-menu-dark");
        } else {
            contextMenu.getStyleClass().remove("context-menu-dark");
        }
    }

    /**
     * Copies selected text from a text field to the clipboard.
     *
     * @param event the action event triggered for the copy action.
     */
    private void handleCopyAction(ActionEvent event) {
        copySelectedText();
    }

    /**
     * Attaches the context menu to a specified text field, enabling right-click copy.
     *
     * @param textField the TextField instance to attach the context menu to.
     */
    private void attachContextMenuToTextField(TextField textField) {
        textField.setContextMenu(contextMenu);
    }

    /**
     * Initializes the context menu with theme options for light and dark modes.
     */
    private void initializeContextMenu() {
        contextMenu = new ContextMenu();
        MenuItem lightMode = new MenuItem("Light Mode");
        MenuItem darkMode = new MenuItem("Dark Mode");
        lightMode.setOnAction(e -> switchTheme("light"));
        darkMode.setOnAction(e -> switchTheme("dark"));
        contextMenu.getItems().addAll(lightMode, darkMode);
    }

    /**
     * Copies the selected text from the visible text field (password field or plain text field)
     * to the system clipboard.
     */
    private void copySelectedText() {
        String selectedText = passwordField.isVisible() ? passwordField.getSelectedText() : plainPasswordField.getSelectedText();
        if (!selectedText.isEmpty()) {
            Clipboard clipboard = Clipboard.getSystemClipboard();
            ClipboardContent content = new ClipboardContent();
            content.putString(selectedText);
            clipboard.setContent(content);
        }
    }

    /**
     * Toggles password visibility between plain text and masked text.
     *
     * @param event the ActionEvent for toggling visibility.
     */
    private void togglePasswordVisibility(ActionEvent event) {
        passwordIsVisible = !passwordIsVisible;
        passwordField.setVisible(!passwordIsVisible);
        plainPasswordField.setVisible(passwordIsVisible);

        if (passwordIsVisible) {
            plainPasswordField.setText(passwordField.getText());
            eyeImageView.setImage(eyeOpen);
        } else {
            passwordField.setText(plainPasswordField.getText());
            eyeImageView.setImage(eyeClosed);
        }
    }

    /**
     * Handles the logout action by closing the current stage and returning to the login screen.
     *
     * @param event the ActionEvent triggered by clicking the log out button.
     */
    private void logOut(ActionEvent event) {
        logger.info("User logged out successfully.");
        showLogoutMessage();
        closeCurrentStageAndReturnToLogin();
    }

    /**
     * Shows an information alert message confirming the logout action.
     */
    private void showLogoutMessage() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Log Out");
        alert.setHeaderText(null);
        alert.setContentText("User logged out successfully.");
        alert.showAndWait();
    }

    /**
     * Closes the current stage and loads the login screen.
     */
    private void closeCurrentStageAndReturnToLogin() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/Login.fxml"));
            Parent loginRoot = loader.load();
            Stage loginStage = new Stage();
            loginStage.setScene(new Scene(loginRoot));
            loginStage.setTitle("Login");
            loginStage.setResizable(false);
            loginStage.getIcons().add(new Image("/Images/userIcon.png"));
            stage.close();
            loginStage.show();
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Failed to load login screen: {0}", e.getMessage());
        }
    }
}
