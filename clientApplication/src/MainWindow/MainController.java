package MainWindow;

import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;

public class MainController {

    @FXML
    private PasswordField passwordField; // The masked password input

    @FXML
    private TextField plainPasswordField; // The visible plain text input (used when the password is shown)

    @FXML
    private ImageView passwordNotVisible; // ImageView for the "eye" icon when password is hidden

    @FXML
    private ImageView passwordVisible; // ImageView for the "eye" icon when password is visible

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
        // Add further logic for logout, like clearing user data or redirecting to login screen.
    }
}
