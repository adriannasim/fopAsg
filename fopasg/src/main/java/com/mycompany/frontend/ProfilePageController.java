package com.mycompany.frontend;

import java.io.IOException;

import com.mycompany.backend.User;
import com.mycompany.backend.UserService;
import com.mycompany.backend.UserSession;
import com.mycompany.frontend.helper.TogglePasswordField;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;

/***
 * THIS CONTROLLER CLASS IS USED FOR profile-page.fxml
 * 
 ***/

public class ProfilePageController extends SharedPaneCharacteristics {

    private UserService userService = new UserService();

    /***
     * ELEMENTS WITH FX:ID
     * 
     ***/
    @FXML
    private Pane pane; // this is used to hold the password and the password edit icon

    @FXML
    private TextField username; // this is ued to keep username

    @FXML
    private TextField email; // this is used to keep email

    @FXML
    private ImageView emailEdit; // this is used to enable email edit

    @FXML
    private ImageView passwordEdit; // this is used to enable password edit

    @FXML
    private Text passwordMsg; // this is used to display password error message

    @FXML
    private Text emailMsg; // this is used to display email error message

    @FXML
    private Button submitBtn;

    /***
     * VARIABLES
     * 
     ***/
    private boolean isEmailValid = true;

    private boolean isPasswordValid = true;

    /***
     * INITILIZATION OF THE CONTROLLER
     * 
     ***/
    @FXML
    public void initialize() {
        // Inherit Super Class's initialize()
        super.initialize();

        // get user session
        String sessionUsername = UserSession.getSession().getUsername();

        // get user info
        User user = userService.getUserByUsername(sessionUsername);

        // Add in togglePasswordFields that can have password visibility toggle
        // functions
        TogglePasswordField password = new TogglePasswordField();
        password.setLayoutX(134.0);
        password.setLayoutY(342.0);
        password.setPrefHeight(26.0);
        password.setPrefWidth(453);
        password.setStyle("-fx-background-color: #D9D9D9");
        pane.getChildren().add(password);

        // Set the data
        username.setText(sessionUsername);
        email.setText(user.getEmail());
        password.setText(user.getPassword());

        // Default cannot edit
        username.setEditable(false);
        email.setEditable(false);
        password.setEditable(false);

        // Unless user click on edit icon
        emailEdit.setOnMouseClicked(e -> {
            email.setEditable(true);
            email.setStyle("-fx-background-color: #F1F1F1;");
            email.requestFocus();
        });

        passwordEdit.setOnMouseClicked(e -> {
            password.setEditable(true);
            password.setStyle("-fx-background-color: #F1F1F1;");
            password.requestFocus();
        });

        // Check for password strength & Validate password format
        password.setOnKeyTyped(e -> {
            // Weak password
            if (password.getText().length() < 6) { // Less than 6 characters
                password.setStyle("-fx-background-color: #FF9696;");
                passwordMsg.setText("Password strength weak.");
                passwordMsg.setStyle("-fx-fill: #FF9696;");
                isPasswordValid = false;
            }
            // Moderate password
            else if (password.getText().matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[A-Za-z\\d]{6,}$")) { // Small letters + capital letters + number && > 6 characters
                password.setStyle("-fx-background-color: #669A9D;");
                passwordMsg.setText("Password strength moderate.");
                passwordMsg.setStyle("-fx-fill: #669A9D;");
                isPasswordValid = true;
            }
            // Strong password
            else if (password.getText().matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&#])[A-Za-z\\d@$!%*?&#]{6,}$")) { // Small letters + capital letters + number + special characters && > 6 characters
                password.setStyle("-fx-background-color: #9ABF80;");
                passwordMsg.setText("Password strength strong.");
                passwordMsg.setStyle("-fx-fill: #9ABF80;");
                isPasswordValid = true;
            }
        });

        // When user click on submit button
        submitBtn.setOnMouseClicked(e -> {

            // If user not entered the details, display error message accordingly
            // 1. email
            if (email.getText().isEmpty()) {
                emailMsg.setText("Please enter an email.");
                isEmailValid = false;
            } else {
                emailMsg.setText("");
                isEmailValid = true;
            }
            // 2. password
            if (password.getText().isEmpty()) {
                passwordMsg.setText("Please enter a password.");
            } else {
                passwordMsg.setText("");
            }

            // If user entered the details, validate them
            // 1. Validate email
            isEmailValid = checkEmail(email.getText());
            if (!isEmailValid && !email.getText().isEmpty()) {
                emailMsg.setText("Invalid email format.");
                isEmailValid = false;
            } else if (isEmailValid && !email.getText().isEmpty()) {
                emailMsg.setText("");
                isEmailValid = true;
            }

            // If no issue then try to update profile
            if (isEmailValid && isPasswordValid) {
                try {
                    // Pop up a confimation message
                    App.openConfirmationPopUp("Confirm to change your details?",
                            () -> userService.userEdit(username.getText(), email.getText(), password.getText()));
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });

    }

    /***
     * HELPER METHOD TO VALIDATE EMAIL
     * 
     ***/
    private boolean checkEmail(String email) {
        return (email.matches("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$"));
    }
}
