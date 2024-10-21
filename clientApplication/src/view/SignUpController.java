package view;

import Model.SignableFactory;
import Model.User;
import exception.EmptyFieldException;
import exception.InvalidCityFormatException;
import exception.InvalidEmailFormatException;
import exception.InvalidPasswordFormatException;
import exception.InvalidStreetFormatException;
import exception.InvalidZipFormatException;

import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class SignUpController {

    // Logger for logging events
    private static final Logger LOGGER = Logger.getLogger(SignUpController.class.getName());

    // UI Components
    @FXML
    private Button btn_show_password;

    @FXML
    private Button btn_signup;

    @FXML
    private CheckBox chb_active;

    @FXML
    private Hyperlink hl_login;

    @FXML
    private ImageView imgShowPassword;

    @FXML
    private Label lbl_error;

    @FXML
    private PasswordField pf_password;

    @FXML
    private PasswordField pf_password_confirm;

    @FXML
    private TextField tf_city;

    @FXML
    private TextField tf_email;

    @FXML
    private TextField tf_name;

    @FXML
    private TextField tf_password;

    @FXML
    private TextField tf_password_confirm;

    @FXML
    private TextField tf_street;

    @FXML
    private TextField tf_zip;

    @FXML
    private VBox vbx_card;

    private Stage stage;
    private boolean isPasswordVisible = false;

    public Stage getStage() {
        return stage;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    void initStage(Parent root) {
        LOGGER.info("Initialising Sign Up window.");

       // Scene scene = new Scene(root);

        // Set the stage properties
        //stage.setScene(scene);
        //        stage.setTitle("SignUp");
        //stage.setResizable(false);
       // stage.initModality(Modality.APPLICATION_MODAL);

        // Set the icon (if needed)
       // stage.getIcons().add(new Image("/Images/userIcon.png"));

        // Set the close request handler
       // stage.setOnCloseRequest(this::handleOnActionExit); // Here is where you add it


        btn_signup.setOnAction(this::handleSignUpButtonAction);
        hl_login.setOnAction(this::handleLoginHyperlinkAction);

        tf_password.setVisible(false);
        btn_show_password.setOnAction(event -> handlePasswordImageButtonAction());
        //stage.setOnCloseRequest(this::handleOnActionExit);
        //stage.showAndWait(); // Show the stage and wait until it is closed

        // MENU
        initMenu(root);

        LOGGER.info("Window opened.");
    }

    private void initMenu(Parent root) {
        LOGGER.info("Initialising Sign Up window menu .");
        MenuItem darkMode = new MenuItem("Dark Mode");
        MenuItem lightMode = new MenuItem("Light Mode");
        MenuItem clearFields = new MenuItem("Clear All Fields");
        ContextMenu contextMenu = new ContextMenu(darkMode, lightMode, clearFields);
        // Create the ContextMenu and add the MenuItems

        // Action for Dark Mode
        darkMode.setOnAction(e -> {
            // scene.getStylesheets().add(getClass().getResource("/css/dark-styles.css").toExternalForm());
            System.out.println("Dark Mode Activated");
            applyDarkMode();
            contextMenu.hide();  // Hide the context menu
        });

        // Action for Light Mode
        lightMode.setOnAction(e -> {
            // Remove all stylesheets
            // scene.getStylesheets().clear();
            // Apply light mode stylesheet
            //scene.getStylesheets().add(getClass().getResource("/css/light-styles.css").toExternalForm());
            System.out.println("Light Mode Activated");
            contextMenu.hide();  // Hide the context menu
        });

        // Action for Clear All Fields
        clearFields.setOnAction(e -> {
            clearAllFields();  // Clear all input fields
            System.out.println("All Fields Cleared");

            applyLightMode();
            contextMenu.hide();  // Hide the context menu
        });

        // Show context menu on right-click (context menu request)
        root.setOnContextMenuRequested(e -> {
            contextMenu.show(root, e.getScreenX(), e.getScreenY());
        });

    }

    private void applyDarkMode() {
        LOGGER.info("Applying dark mode.");
        // Set dark mode styles (you can modify this to point to a stylesheet or CSS rules)
        stage.getScene().getRoot().setStyle("-fx-base: #333; -fx-background-color: #2B2B2B; -fx-text-fill: white;");
    }

    private void applyLightMode() {
        LOGGER.info("Applying light mode.");
        // Reset to light mode (or default) styles
        stage.getScene().getRoot().setStyle("-fx-base: #FFF; -fx-background-color: #F0F0F0; -fx-text-fill: black;");
    }

    private void clearAllFields() {
        LOGGER.info("Clearing all input fields.");
        tf_email.clear();
        pf_password.clear();
        tf_password.clear();
        pf_password_confirm.clear();
        tf_password_confirm.clear();
        tf_name.clear();
        tf_street.clear();
        tf_city.clear();
        tf_zip.clear();
        chb_active.setSelected(false);
        lbl_error.setText("");
    }

    @FXML
    private void handlePasswordImageButtonAction() {
        isPasswordVisible = !isPasswordVisible;
        if (isPasswordVisible) {
            imgShowPassword.setImage(new Image(getClass().getResourceAsStream("/Images/eye-slash-solid.png")));
            pf_password.setVisible(false);
            tf_password.setVisible(true);
            tf_password.setText(pf_password.getText());
            pf_password_confirm.setVisible(false);
            tf_password_confirm.setVisible(true);
            tf_password_confirm.setText(pf_password_confirm.getText());
        } else {
            imgShowPassword.setImage(new Image(getClass().getResourceAsStream("/Images/eye-solid.png")));
            pf_password.setVisible(true);
            tf_password.setVisible(false);
            pf_password.setText(tf_password.getText());
            pf_password_confirm.setVisible(true);
            tf_password_confirm.setVisible(false);
            pf_password_confirm.setText(tf_password_confirm.getText());
        }
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
            LOGGER.info("Performing signup");
        } catch (Exception e) {
            lbl_error.setText(e.getMessage());
            LOGGER.log(Level.WARNING, e.getMessage(), e);
        }
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

            //  User users =SignableFactory.getSignable().signUp(user);
            // Log sign-up success
            LOGGER.log(Level.INFO, "Calling user from Signable");
            // Inform the user of successful sign-up using an Alert
            showAlert();
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

            LOGGER.log(Level.INFO, "Navigated to {0} screen.", windowTitle);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Failed to load {0} screen: " + e.getMessage(), windowTitle);
        }
    }

    public void handleOnActionExit(Event event) {
        try {
            //Ask user for confirmation on exit
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION,
                            "Are you sure you want to exit the application?",
                            ButtonType.OK, ButtonType.CANCEL);
            Optional<ButtonType> result = alert.showAndWait();
            //If OK to exit
            if (result.isPresent() && result.get() == ButtonType.OK) {
                Platform.exit();
            } else {
                event.consume();
            }
        } catch (Exception e) {
            String errorMsg = "Error exiting application:" + e.getMessage();
            Alert alert = new Alert(Alert.AlertType.ERROR,
                            errorMsg,
                            ButtonType.OK);
            alert.showAndWait();
            LOGGER.log(Level.SEVERE, errorMsg);
        }
    }

    public void showAlert() {
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
    }

}
