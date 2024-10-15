package view;

import static Utils.UtilsMethods.logger;
import java.util.logging.Level;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
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

    /**
     * The masked password input field that hides the user password.
     */
    @FXML
    private PasswordField passwordField; // The masked password input

    /**
     * The plain text password input field that displays the user password in clear text when shown.
     */
    @FXML
    private TextField plainPasswordField; // The visible plain text input (used when the password is shown)

    /**
     * The button that triggers the log-out action.
     */
    @FXML
    private Button logOutButton; // LogOut button

    /**
     * The button that toggles the visibility of the password.
     */
    @FXML
    private Button eyeButton; // Eye button for showing/hiding password

    /**
     * The ImageView that displays the eye icon.
     */
    @FXML
    private ImageView eyeImageView; // Eye icon for password visibility

    // Image paths for the eye icons
    private final Image eyeClosed = new Image(getClass().getResourceAsStream("/Images/passwordVisible.png"));
    private final Image eyeOpen = new Image(getClass().getResourceAsStream("/Images/passwordNotVisible.png"));

    // Variable to keep track of the password visibility
    private boolean passwordIsVisible = false;

    /**
     * Initializes the controller.
     * This method is called automatically after the FXML file is loaded.
     */
    @FXML
    public void initialize() {
        // Set the eye icon to the closed eye by default
        eyeImageView.setImage(eyeClosed);

        // Hide the plain password field initially and show the masked password field
        passwordField.setVisible(true);
        plainPasswordField.setVisible(false);
    }

    /**
     * Handles the log-out action.
     * Navigates the user back to the LogIn screen.
     */
    @FXML
    private void logOut() {
        navigateToScreen("/view/LogIn.fxml", "LogIn");
    }

    /**
     * Toggles the password visibility and changes the eye icon.
     * If the password is visible, it hides it; if the password is hidden, it shows it.
     */
    @FXML
    private void togglePasswordVisibility() {
        if (passwordIsVisible) {
            // Hide password and change eye icon to closed
            passwordField.setVisible(true);
            plainPasswordField.setVisible(false);
            eyeImageView.setImage(eyeClosed);
        } else {
            // Show password and change eye icon to open
            passwordField.setVisible(false);
            plainPasswordField.setVisible(true);
            plainPasswordField.setText(passwordField.getText()); // Transfer text from PasswordField to TextField
            eyeImageView.setImage(eyeOpen);
        }
        // Toggle visibility flag
        passwordIsVisible = !passwordIsVisible;
    }

    /**
     * General method to navigate between different screens in the application.
     * 
     * @param fxmlPath the path to the FXML file of the target screen
     * @param windowTitle the title to set for the window
     */
    private void navigateToScreen(String fxmlPath, String windowTitle) {
        try {
            // Load the FXML file of the target view
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Scene scene = new Scene(loader.load());

            // Get the current stage
            Stage currentStage = (Stage) logOutButton.getScene().getWindow();

            // Change the current stage's scene to the new scene
            currentStage.setScene(scene);
            currentStage.setTitle(windowTitle); // Set the title of the new window
            currentStage.show();

            logger.log(Level.INFO, "Navigated to {0} screen.", windowTitle);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Failed to load {0} screen: " + e.getMessage(), windowTitle);
        }
    }
}
