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
        // ... (la lógica de validación sigue igual)
    }

    /**
     * Performs the sign-in logic, typically involves calling a backend service.
     *
     * @param email the email entered by the user
     * @param password the password entered by the user
     * @param name the name entered by the user
     * @param dni the DNI entered by the user
     * @param phoneNumber the phone number entered by the user
     * @param companyID the selected company's ID
     */
    private void performSignUp(String email, String password, String name, String dni, String phoneNumber, int companyID) {
        // Lógica de registro de usuario
        navigateToScreen("/view/Main.fxml", "Main");
        logger.log(Level.INFO, "Sign-up successful for: {0}", email);
    }

    /**
     * Populates the ComboBox with company names (simulated data).
     */
    private void initializeCompanyComboBox() {
        cb_company.getItems().addAll("Company A", "Company B", "Company C");
    }

    /**
     * Action handler for navigating to the login screen.
     *
     * @param event the ActionEvent triggered by the hyperlink click
     */
    private void handleLoginHyperlinkAction(ActionEvent event) {
        navigateToScreen("/view/LogIn.fxml", "LogIn");
    }

    /**
     * General method to navigate to different screens.
     *
     * @param fxmlPath the path to the FXML file of the target screen
     * @param windowTitle the title to set for the window
     * @author Borja
     */
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
