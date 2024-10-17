package view;

import static Utils.UtilsMethods.logger;
import java.util.logging.Level;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
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

    @FXML
    public void initialize() {
        // Set the eye icon to the closed eye by default
        eyeImageView.setImage(eyeClosed);
        passwordField.setVisible(true);
        plainPasswordField.setVisible(false);

        // Initialize the context menu for copy functionality
        createContextMenu();
    }

    /**
     * Creates a context menu with a copy option for selected text.
     */
    private void createContextMenu() {
        contextMenu = new ContextMenu();
        MenuItem copyItem = new MenuItem("Copy");

        // Set action for the copy menu item
        copyItem.setOnAction(event -> copySelectedText());

        // Add the copy option to the context menu
        contextMenu.getItems().add(copyItem);

        // Attach the context menu to relevant text fields
        attachContextMenuToTextField(passwordField);
        attachContextMenuToTextField(plainPasswordField);
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

    @FXML
    private void logOut() {
        navigateToScreen("/view/LogIn.fxml", "LogIn");
    }

    @FXML
    private void togglePasswordVisibility() {
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

    private void navigateToScreen(String fxmlPath, String windowTitle) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Scene scene = new Scene(loader.load());

            Stage currentStage = (Stage) logOutButton.getScene().getWindow();
            currentStage.setScene(scene);
            currentStage.setTitle(windowTitle);
            currentStage.show();
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Failed to load {0} screen: " + e.getMessage(), windowTitle);
        }
    }
}