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
import javafx.scene.input.MouseEvent;

/**
 * SigninController is responsible for handling user interactions in the sign-in UI.
 * It validates user inputs and triggers actions like sign-in and navigating to the login screen.
 * 
 * @author 2dam
 */
public class SigninController implements Initializable {

    // UI Components
    @FXML
    private Label lbl_email;

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
    private Button btn_signin;

    @FXML
    private Label lbl_error;

    @FXML
    private Hyperlink hl_login;

    /**
     * Initializes the controller class and sets up any necessary logic.
     *
     * @param url The location used to resolve relative paths for the root object, or null if not available.
     * @param rb The resources used to localize the root object, or null if not available.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Initialize any components or default values, e.g., loading company names into cb_company.
        initializeCompanyComboBox();
    }

    /**
     * Action handler for the Sign In button.
     * Validates user input and processes the sign-in request.
     * 
     * @param event The mouse event that triggered the action.
     */
    @FXML
    private void handleSignInButtonAction(MouseEvent event) {
        String email = tf_email.getText();
        String password = tf_password.getText();
        String confirmPassword = tf_password_confirm.getText();
        String name = tf_name.getText();
        String dni = tf_dni.getText();
        String phoneNumber = tf_phone_number.getText();
        String company = cb_company.getValue();

        if (validateInputs(email, password, confirmPassword, name, dni, phoneNumber, company)) {
            // Code to process sign-in with valid inputs
            lbl_error.setText("");  // Clear any previous error messages
            // Trigger sign-in logic (calling the service layer)
            performSignIn(email, password, name, dni, phoneNumber, company);
        } else {
            lbl_error.setText("Please check your inputs. Ensure all fields are filled correctly.");
        }
    }

    /**
     * Action handler for the Login hyperlink.
     * Redirects the user to the login screen.
     * 
     * @param event The mouse event that triggered the action.
     */
    @FXML
    private void handleLoginHyperlinkAction(MouseEvent event) {
        // Code to navigate to the login screen
        navigateToLoginScreen();
    }

    /**
     * Populates the company ComboBox with a list of available companies.
     */
    private void initializeCompanyComboBox() {
        // Simulate loading company names (this would usually come from the database or a service)
        cb_company.getItems().addAll("Company A", "Company B", "Company C");
    }

    /**
     * Validates user input fields.
     * 
     * @return true if all inputs are valid, false otherwise.
     */
    private boolean validateInputs(String email, String password, String confirmPassword, String name, String dni, String phoneNumber, String company) {
        // Perform necessary validation logic, like checking if the fields are non-empty, passwords match, etc.
        // to improve
        if (email == null || email.isEmpty() || !email.contains("@")) return false;
        if (password == null || password.isEmpty() || !password.equals(confirmPassword)) return false;
      
        if (name == null || name.isEmpty()) return false;
        if (dni == null || dni.isEmpty()) return false;
        // only number
        if (phoneNumber == null || phoneNumber.isEmpty()) return false;
        if (company == null || company.isEmpty()) return false;

        return true;
    }

    /**
     * Performs the sign-in process by sending data to the backend.
     * 
     * @param email User's email.
     * @param password User's password.
     * @param name User's name.
     * @param dni User's DNI.
     * @param phoneNumber User's phone number.
     * @param company User's selected company.
     */
    private void performSignIn(String email, String password, String name, String dni, String phoneNumber, String company) {
        // Logic to handle sign-in, usually involves calling a service layer to handle the business logic
        // For example: UserService.signIn(new User(email, password, name, dni, phoneNumber, company));

        // Show success or error messages based on the sign-in result
        System.out.println("Sign-in successful for: " + email);
    }

    /**
     * Navigates the user to the login screen.
     */
    private void navigateToLoginScreen() {
        // Logic to switch to the login screen
        System.out.println("Navigating to login screen...");
    }
}
