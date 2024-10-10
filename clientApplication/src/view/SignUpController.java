package view;

import Model.User;
import exception.EmptyFieldException;
import exception.InvalidEmailFormatException;
import exception.InvalidPasswordFormatException;
import exception.InvalidPhoneNumberFormatException;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * SignUpController is responsible for handling user interactions in the sign-up
 * UI. It validates user inputs and triggers actions like sign-up and navigating
 * to the login screen.
 */
public class SignUpController implements Initializable {

    // Logger for logging events
    private static final Logger logger = Logger.getLogger(SignUpController.class.getName());

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
        initializeCompanyComboBox();

        btn_signup.setOnAction(this::handleSignUpButtonAction);
        hl_login.setOnAction(this::handleLoginHyperlinkAction);
    }

    /**
     * Handles the sign-up button action. It validates user inputs and 
     * performs the sign-up process if all inputs are valid.
     *
     * @param event the ActionEvent triggered by the button click
     */
    private void handleSignUpButtonAction(ActionEvent event) {
        try {
            String email = tf_email.getText();
            String password = tf_password.getText();
            String confirmPassword = tf_password_confirm.getText();
            String name = tf_name.getText();
            String dni = tf_dni.getText();
            String phoneNumber = tf_phone_number.getText();
            String company = cb_company.getValue();

            validateInputs(email, password, confirmPassword, name, dni, phoneNumber, company);
            lbl_error.setText("");  // Clear previous error messages
            //get id of comapny here by hashmap
            performSignUp(email, password, name, dni, phoneNumber, 1);
        } catch (Exception e) {
            lbl_error.setText(e.getMessage());  // Display the error message
            logger.log(Level.WARNING, e.getMessage(), e);  // Log the warning
        }
    }

    /**
     * Validates the user inputs for email, password, phone number, and other fields.
     *
     * @param email the email entered by the user
     * @param password the password entered by the user
     * @param confirmPassword the confirmed password entered by the user
     * @param name the name entered by the user
     * @param dni the DNI entered by the user
     * @param phoneNumber the phone number entered by the user
     * @param company the selected company from the ComboBox
     * @throws EmptyFieldException if any required fields are empty
     * @throws InvalidEmailFormatException if the email format is invalid
     * @throws InvalidPasswordFormatException if the password does not meet the criteria
     * @throws InvalidPhoneNumberFormatException if the phone number is invalid
     */
private void validateInputs(String email, String password, String confirmPassword, String name, String dni, String phoneNumber, String company)
        throws EmptyFieldException, InvalidEmailFormatException, InvalidPasswordFormatException, InvalidPhoneNumberFormatException {

    // Validate email
    if (email == null || email.isEmpty()) {
        throw new EmptyFieldException("Email cannot be empty.");
    }
    // Regex pattern for a valid email format
    String emailRegex = "^[a-zA-Z0-9._%+-]+@([a-zA-Z0-9.-]+\\.[a-zA-Z]{2,})$";
    if (!email.matches(emailRegex)) {
        throw new InvalidEmailFormatException("Email must be in a valid format (e.g., example@domain.com).");
    }

    // Validate password
    if (password == null || password.isEmpty()) {
        throw new EmptyFieldException("Password cannot be empty.");
    }
    if (!validatePassword(password)) {
        throw new InvalidPasswordFormatException("Password must be at least 6 characters, with lowercase, uppercase, numbers, and special characters.");
    }

    // Validate password confirmation
    if (confirmPassword == null || confirmPassword.isEmpty()) {
        throw new EmptyFieldException("Password confirmation cannot be empty.");
    }
    if (!password.equals(confirmPassword)) {
        throw new InvalidPasswordFormatException("Passwords do not match.");
    }

    // Validate name
    if (name == null || name.isEmpty()) {
        throw new EmptyFieldException("Name cannot be empty.");
    }

    // Validate DNI
    if (dni == null || dni.isEmpty()) {
        throw new EmptyFieldException("DNI cannot be empty.");
    }

    // Validate phone number
    if (phoneNumber == null || phoneNumber.isEmpty()) {
        throw new EmptyFieldException("Phone number cannot be empty.");
    }
    if (!validatePhoneNumber(phoneNumber)) {
        throw new InvalidPhoneNumberFormatException("Phone number must be exactly 9 digits.");
    }

    // Validate company
    if (company == null || company.isEmpty()) {
        throw new EmptyFieldException("Company cannot be empty.");
    }
}


    /**
     * Validates the password based on specified criteria.
     *
     * @param password the password to validate
     * @return true if the password is valid, false otherwise
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
     * Validates the phone number to ensure it is exactly 9 digits.
     *
     * @param phoneNumber the phone number to validate
     * @return true if the phone number is valid, false otherwise
     */
    private boolean validatePhoneNumber(String phoneNumber) {
        return phoneNumber.matches("\\d{9}");
    }

    /**
     * Performs the sign-in logic, typically involves calling a backend service.
     *
     * @param email the email entered by the user
     * @param password the password entered by the user
     * @param name the name entered by the user
     * @param dni the DNI entered by the user
     * @param phoneNumber the phone number entered by the user
     * @param company the selected company from the ComboBox
     */
    private void performSignUp(String email, String password, String name, String dni, String phoneNumber, int companyID) {
       
       // UserDao userdao= new UserDao();
        //boolean insert = userdao.insertUser(name,email,phoneNumber,password,1);
       // User insertedUSer = userdao.insertUser(user);

      //  UserDao userdao= new UserDao();
        //boolean insert = userdao.insertUser(name,email,phoneNumber,password,1);
      //  User insertedUSer = userdao.insertUser(user);
      navigateToMainScreen();
        logger.log(Level.INFO, "Sign-up successful for: {0}", email);
    }

    /**
     * Populates the ComboBox with company names (simulated data).
     */
    private void initializeCompanyComboBox() {
        // hashmap list of company with id names show only name
        //when name is selected we get it s id
        cb_company.getItems().addAll("Company A", "Company B", "Company C");
    }

    /**
     * Action handler for navigating to the login screen.
     *
     * @param event the ActionEvent triggered by the hyperlink click
     */
    private void handleLoginHyperlinkAction(ActionEvent event) {
        navigateToLoginScreen();
    }

    /**
     * Navigates to the login screen (dummy logic for demonstration).
     */
    
 private void navigateToMainScreen() {
    try {
        // Load the FXML file of the main view
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/Main.fxml"));
        Scene mainScene = new Scene(loader.load());

        // Get the current stage
        Stage currentStage = (Stage) btn_signup.getScene().getWindow();

        // Change the current stage's scene to the main scene
        currentStage.setScene(mainScene);
        currentStage.setTitle("Main"); // Title of the new window 
        currentStage.show();
        
        logger.log(Level.INFO, "Navigated to main screen.");

    } catch (Exception e) {
        logger.log(Level.SEVERE, "Failed to load main screen: " + e.getMessage(), e);
    }
}
 
private void navigateToLoginScreen() {
    try {
        // Load the FXML file of the login view
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/LogIn.fxml"));
        Scene loginScene = new Scene(loader.load());

        // Get the current stage
        Stage currentStage = (Stage) btn_signup.getScene().getWindow();

        // Change the current stage's scene to the login scene
        currentStage.setScene(loginScene);
        currentStage.setTitle("LogIn"); // Title of the new window 
        currentStage.show();
        
        logger.log(Level.INFO, "Navigated to login screen.");

    } catch (Exception e) {
        logger.log(Level.SEVERE, "Failed to load login screen: " + e.getMessage(), e);
    }
}

}
