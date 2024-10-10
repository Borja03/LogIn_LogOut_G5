package view;

import static Utils.UtilsMethods.logger;
import java.util.logging.Level;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
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
     * The ImageView that displays the "eye" icon when the password is hidden.
     */
    @FXML
    private ImageView passwordNotVisible; // ImageView for the "eye" icon when password is hidden

    /**
     * The ImageView that displays the "eye" icon when the password is visible.
     */
    @FXML
    private ImageView passwordVisible; // ImageView for the "eye" icon when password is visible
    
    /**
     * The button that triggers the log-out action.
     */
    @FXML
    private Button logOutButton; // LogOut button

    /**
     * Show the password in plain text when the user clicks on the eye icon.
     * This method hides the PasswordField and displays the plain text field with the current password.
     */
    @FXML
    private void showPassword() {
        passwordField.setVisible(false);  // Hide the PasswordField
        plainPasswordField.setText(passwordField.getText()); // Set the password text in the TextField
        plainPasswordField.setVisible(true);  // Show the TextField

        passwordVisible.setVisible(true); // Show the "eye" icon for visible password
        passwordNotVisible.setVisible(false); // Hide the "eye" icon for hidden password
    }

    /**
     * Hide the password by switching back to the masked PasswordField.
     * This method hides the plain password field and shows the PasswordField with the masked password.
     */
    @FXML
    private void hidePassword() {
        plainPasswordField.setVisible(false); // Hide the TextField
        passwordField.setVisible(true); // Show the PasswordField
        passwordField.setStyle("-fx-background-color: transparent; -fx-border-color: transparent;"); // Revert style

        passwordVisible.setVisible(false); // Hide the "eye" icon for visible password
        passwordNotVisible.setVisible(true); // Show the "eye" icon for hidden password
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
