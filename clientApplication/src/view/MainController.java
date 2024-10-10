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

public class MainController {

    @FXML
    private PasswordField passwordField; // The masked password input

    @FXML
    private TextField plainPasswordField; // The visible plain text input (used when the password is shown)

    @FXML
    private ImageView passwordNotVisible; // ImageView for the "eye" icon when password is hidden

    @FXML
    private ImageView passwordVisible; // ImageView for the "eye" icon when password is visible
    
     @FXML
    private Button logOutButton; //LogOut button 

    // Method to show the password
    @FXML
    private void showPassword() {
        passwordField.setVisible(false);  // Hide the PasswordField
        plainPasswordField.setText(passwordField.getText()); // Set the password text in the TextField
        plainPasswordField.setVisible(true);  // Show the TextField

        passwordVisible.setVisible(true); // Show the "eye" icon for visible password
        passwordNotVisible.setVisible(false); // Hide the "eye" icon for hidden password
    }

    // Method to hide the password
    @FXML
    private void hidePassword() {
        plainPasswordField.setVisible(false); // Hide the TextField
        passwordField.setVisible(true); // Show the PasswordField
        passwordField.setStyle("-fx-background-color: transparent; -fx-border-color: transparent;"); // Revert style

        passwordVisible.setVisible(false); // Hide the "eye" icon for visible password
        passwordNotVisible.setVisible(true); // Show the "eye" icon for hidden password
    }
    
     @FXML
    private void logOut() {
        navigateToLogInScreen();
    }
    
    private void navigateToLogInScreen() {
    try {
        // Load the FXML file of the LogIn view
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/LogIn.fxml"));
        Scene mainScene = new Scene(loader.load());

        // Get the current stage
        Stage currentStage = (Stage) logOutButton.getScene().getWindow();

        // Change the current stage's scene to the LogIn scene
        currentStage.setScene(mainScene);
        currentStage.setTitle("LogIn"); // Title of the new window 
        currentStage.show();
        
        logger.log(Level.INFO, "Navigated to login screen.");

    } catch (Exception e) {
        logger.log(Level.SEVERE, "Failed to load login screen: " + e.getMessage(), e);
    }
}
}
