package view;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.event.ActionEvent;

/**
 * SignUpController is responsible for handling user interactions in the sign-up UI.
 * It validates user inputs and triggers actions like sign-up and navigating to the login screen.
 * 
 */
public class SignUpController implements Initializable {

    // UI Components
    @FXML
    private TextField tf_email;

    @FXML
    private PasswordField tf_password;

    @FXML
    private PasswordField tf_password_confirm;

    @FXML
    private TextField tf_name;

    @FXML
    private TextField tf_dni;

    @FXML
    private TextField tf_phone_number;

    @FXML
    private ComboBox<String> cb_company;

    @FXML
    private Button btn_signup;

    @FXML
    private Label lbl_error;

    @FXML
    private Hyperlink hl_login;

    /**
     * Initializes the controller class and sets up any necessary logic.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Initialize any components or default values, e.g., loading company names into cb_company.
        initializeCompanyComboBox();

        // Set action handlers for button and hyperlink using setOnAction
        btn_signup.setOnAction(this::handleSignUpButtonAction);
        hl_login.setOnAction(this::handleLoginHyperlinkAction);
    }

    /**
     * Action handler for the Sign Up button.
     * Validates user input and processes the sign-up request.
     */
    private void handleSignUpButtonAction(ActionEvent event) {
        String email = tf_email.getText();
        String password = tf_password.getText();
        String confirmPassword = tf_password_confirm.getText();
        String name = tf_name.getText();
        String dni = tf_dni.getText();
        String phoneNumber = tf_phone_number.getText();
        String company = cb_company.getValue();

        if (validateInputs(email, password, confirmPassword, name, dni, phoneNumber, company)) {
            lbl_error.setText("");  // Clear any previous error messages
            performSignIn(email, password, name, dni, phoneNumber, company);
        } else {
            lbl_error.setText("Please check your inputs. Ensure all fields are filled correctly.");
        }
    }

    /**
     * Validates user input fields with specific conditions:
     * - Email must be exactly "qqq@qqq.com"
     * - Password must be at least 6 characters, contain one uppercase letter, one digit, and one special character
     */
    private boolean validateInputs(String email, String password, String confirmPassword, String name, String dni, String phoneNumber, String company) {
        // Validate email (must be "qqq@qqq.com")
        if (!"qqq@qqq.com".equals(email)) {
            lbl_error.setText("Email must be 'qqq@qqq.com'");
            return false;
        }

        // Validate password (min 6 characters, 1 uppercase, 1 number, 1 special character)
        if (!validatePassword(password)) {
            lbl_error.setText("Password must be at least 6 characters, contain 1 uppercase, 1 digit, and 1 special character.");
            return false;
        }

        // Ensure passwords match
        if (!password.equals(confirmPassword)) {
            lbl_error.setText("Passwords do not match.");
            return false;
        }

        // Validate other fields (basic checks)
        if (name == null || name.isEmpty()) return false;
        if (dni == null || dni.isEmpty()) return false;
        if (phoneNumber == null || phoneNumber.isEmpty()) return false;
        if (company == null || company.isEmpty()) return false;

        return true;
    }

    /**
     * Checks if the password meets the following conditions:
     * - At least 6 characters
     * - Contains one uppercase letter
     * - Contains one digit
     * - Contains one special character
     */
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

    /**
     * Populates the company ComboBox with a list of available companies.
     */
    private void initializeCompanyComboBox() {
        // Simulate loading company names (this would usually come from the database or a service)
        cb_company.getItems().addAll("Company A", "Company B", "Company C");
    }

    /**
     * Performs the sign-up process by sending data to the backend.
     */
    private void performSignIn(String email, String password, String name, String dni, String phoneNumber, String company) {
        // Logic to handle sign-in, usually involves calling a service layer
        System.out.println("Sign-in successful for: " + email);
    }

    /**
     * Action handler for the Login hyperlink.
     * Redirects the user to the login screen.
     */
    private void handleLoginHyperlinkAction(ActionEvent event) {
        navigateToLoginScreen();
    }

    /**
     * Navigates the user to the login screen.
     */
    private void navigateToLoginScreen() {
        // Logic to switch to the login screen
        System.out.println("Navigating to login screen...");
    }
}
