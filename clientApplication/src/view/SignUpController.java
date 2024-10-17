package view;

import Model.User;
import exception.EmptyFieldException;
import exception.InvalidCityFormatException;
import exception.InvalidEmailFormatException;
import exception.InvalidPasswordFormatException;
import exception.InvalidStreetFormatException;
import exception.InvalidZipFormatException;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.stage.Stage;

public class SignUpController implements Initializable {

    // Logger for logging events
    private static final Logger logger = Logger.getLogger(SignUpController.class.getName());

    // UI Components
    @FXML
    private TextField tf_email;
    @FXML
    private PasswordField pf_password;
    @FXML
    private PasswordField pf_password_confirm;
    @FXML
    private TextField tf_password;
    @FXML
    private TextField tf_name;
    @FXML
    private TextField tf_street;
    @FXML
    private TextField tf_city;
    @FXML
    private TextField tf_zip;
    @FXML
    private CheckBox chb_active;
    @FXML
    private Button btn_signup;
    //@FXML
   // private Button btn_show_password;
    @FXML
    private Label lbl_error;
    @FXML
    private Hyperlink hl_login;

    
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        btn_signup.setOnAction(this::handleSignUpButtonAction);
        hl_login.setOnAction(this::handleLoginHyperlinkAction);
      

    }

    private void handleSignUpButtonAction(ActionEvent event) {
        try {
            String email = tf_email.getText();
            String password = pf_password.getText();
            String confirmPassword = pf_password_confirm.getText();
            String name = tf_name.getText();
            String street = tf_street.getText();
            String city = tf_city.getText();
            String zip = tf_zip.getText();
            boolean isActive = chb_active.isSelected();
            // Clear previous error messages            
            lbl_error.setText("");

            // Validate inputs
            validateInputs(email, password, confirmPassword, name, street, city, zip);
            // Proceed with sign-up logic
            performSignUp(email, password, name, 1, street, city, Integer.parseInt(zip), isActive);
            logger.info("Performing signup");
        } catch (Exception e) {
            lbl_error.setText(e.getMessage());
            logger.log(Level.WARNING, e.getMessage(), e);
        }
    }

    public void handleShowHidePasswordAction() {
    
    }

    private void validateInputs(String email, String password, String confirmPassword, String name, String street, String city, String zip)
                    throws EmptyFieldException, InvalidEmailFormatException, InvalidPasswordFormatException,
                    InvalidCityFormatException, InvalidZipFormatException, InvalidStreetFormatException {

        checkEmptyFields(email, password, confirmPassword, name, street, city, zip);
        checkFieldsFormat(email, password, confirmPassword, name, street, city, zip);

    }

    public void checkEmptyFields(String email, String password, String confirmPassword, String name, String street, String city, String zip) throws EmptyFieldException {
        // Validate email
        if (email == null || email.isEmpty()) {
            throw new EmptyFieldException("Email cannot be empty.");
        }
        // Validate password
        if (password == null || password.isEmpty()) {
            throw new EmptyFieldException("Password cannot be empty.");
        }
        // Validate password confirmation empty 
        if (confirmPassword == null || confirmPassword.isEmpty()) {
            throw new EmptyFieldException("Password confirmation cannot be empty.");
        }
        // Validate name is empty 
        if (name == null || name.isEmpty()) {
            throw new EmptyFieldException("Name cannot be empty.");
        }
        // Validate street
        if (street == null || street.isEmpty()) {
            throw new EmptyFieldException("Street cannot be empty.");
        }
        // You can add any additional format checks for street if needed
        // Validate city
        if (city == null || city.isEmpty()) {
            throw new EmptyFieldException("City cannot be empty.");
        }
        // Validate zip
        if (zip == null || zip.isEmpty()) {
            throw new EmptyFieldException("Zip code cannot be empty.");
        }
    }

    public void checkFieldsFormat(String email, String password, String confirmPassword, String name, String street, String city, String zip) throws EmptyFieldException, InvalidEmailFormatException, InvalidPasswordFormatException,
                    InvalidCityFormatException, InvalidZipFormatException, InvalidStreetFormatException {
        // Regex pattern for a valid email format
        String emailRegex = "^[a-zA-Z0-9._%+-]+@([a-zA-Z0-9.-]+\\.[a-zA-Z]{2,})$";
        if (!email.matches(emailRegex)) {
            throw new InvalidEmailFormatException("Email must be in a valid format (e.g., example@domain.com).");
        }
        // Validate password format
        if (!validatePassword(password)) {
            throw new InvalidPasswordFormatException("Password must be at least 6 characters, with lowercase, uppercase, numbers, and special characters.");
        }
        // Validate password format equals
        if (!password.equals(confirmPassword)) {
            throw new InvalidPasswordFormatException("Passwords do not match.");
        }
        // Example: check that city only contains letters (basic validation)
        if (!city.matches("[a-zA-Z\\s]+")) {
            throw new InvalidCityFormatException("City must only contain letters.");
        }
        // Example: check that zip contains exactly 5 digits
        if (!zip.matches("\\d{5}")) {
            throw new InvalidZipFormatException("Zip code must be exactly 5 digits.");
        }
    }

    private boolean validatePassword(String password) {
        if (password.length() < 6) {
            return false;
        }

        boolean hasUppercase = false;
        boolean hasDigit = false;
        boolean hasSpecialChar = false;

        for (char c : password.toCharArray()) {
            if (Character.isUpperCase(c)) {
                hasUppercase = true;
            } else if (Character.isDigit(c)) {
                hasDigit = true;
            } else if (!Character.isLetterOrDigit(c)) {
                hasSpecialChar = true;
            }
        }
        return hasUppercase && hasDigit && hasSpecialChar;
    }

    private void performSignUp(String email, String password, String name, int companyID, String street, String city, int zip, boolean isActive) {
        User user = new User(email, password, name, isActive, companyID, street, city, zip);
        try {
            // UserDao userq = new UserDao();
            // User insertedUser = userq.signUp(user);

            // Log sign-up success
            logger.log(Level.INFO, "Sign-up successful for: {0}", email);

            // Inform the user of successful sign-up using an Alert
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Sign-up Successful");
            alert.setHeaderText(null);
            alert.setContentText("Your account has been created successfully!");

            // Handle alert button click
            alert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    // Navigate to another screen after the user clicks OK
                    navigateToScreen("/view/LogIn.fxml", "LogIn");
                }
            });

        } catch (Exception ex) {
            Logger.getLogger(SignUpController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void handleLoginHyperlinkAction(ActionEvent event) {
        navigateToScreen("/view/LogIn.fxml", "LogIn");
    }

    private void navigateToScreen(String fxmlPath, String windowTitle) {
        try {
            // Load the FXML file of the target view
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Scene scene = new Scene(loader.load());

            // Get the current stage
            Stage currentStage = (Stage) btn_signup.getScene().getWindow();

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
