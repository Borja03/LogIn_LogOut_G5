package view;

import static Utils.UtilsMethods.logger;
import java.util.logging.Level;
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

/**
 * The MainController handles the main functionalities of the main screen of the application.
 * It includes actions for showing and hiding the password, as well as logging out.
 * This class is linked to the main FXML view where user interactions are handled.
 * 
 * <p>This controller allows the user to view the password in plain text, hide the password, and log out to the login screen.</p>
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
    private final Image eyeClosed = new Image(getClass().getResourceAsStream("/Images/passwordVisible.png"));
    private final Image eyeOpen = new Image(getClass().getResourceAsStream("/Images/passwordNotVisible.png"));

    private boolean passwordIsVisible = false;

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

    /**
     * Initializes the controller with the provided root element.
     * 
     * @param root The root element of the scene.
     */
    public void initStage(Parent root) {
        logger.info("Initializing MainController stage.");

        // Set initial visibility of password fields
        passwordField.setVisible(true);
        plainPasswordField.setVisible(false);
        eyeImageView.setImage(eyeClosed);

        // Initialize the context menu for copy functionality
        createContextMenu();

        // Link event handlers
        logOutButton.setOnAction(this::logOut);
        eyeButton.setOnAction(this::togglePasswordVisibility);

        // Set up the stage if needed, e.g., adding close request handler
        if (stage != null) {
            stage.setOnCloseRequest(event -> handleOnCloseRequest(event));
        }

        logger.info("MainController initialized.");
    }

    /**
     * Creates a context menu with a copy option for selected text.
     */
    private void createContextMenu() {
        contextMenu = new ContextMenu();
        MenuItem copyItem = new MenuItem("Copy");

        // Set action for the copy menu item
        copyItem.setOnAction(this::handleCopyAction);

        // Add the copy option to the context menu
        contextMenu.getItems().add(copyItem);

        // Attach the context menu to relevant text fields
        attachContextMenuToTextField(passwordField);
        attachContextMenuToTextField(plainPasswordField);
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
     * @param textField The text field to which the context menu will be attached.
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
     * Navigates to a different screen based on the provided FXML path and title.
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

    /**
     * Handles the close request action to confirm exit or perform other tasks.
     * 
     * @param event The event triggered on close request.
     */
    private void handleOnCloseRequest(javafx.event.Event event) {
        // Handle close request logic (e.g., show confirmation dialog)
        logger.info("Handling close request.");
    }
}