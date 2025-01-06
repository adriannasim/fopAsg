package com.mycompany.frontend;

import java.io.IOException;

import com.mycompany.backend.ServiceResult;
import com.mycompany.backend.UserService;
import com.mycompany.frontend.helper.TogglePasswordField;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;

/***
 * THIS CONTROLLER CLASS IS USED FOR sign-up.fxml
 * 
 ***/

public class SignupController {

    private UserService userService = new UserService();

    /***
     * ELEMENTS WITH FX:ID
     * 
     ***/
    @FXML
    private AnchorPane anchorPane; // Use to store all the contents

    @FXML
    private TextField username; // This will store the user input for username, use username.getText() to get
                                // the value

    @FXML
    private TextField email; // This will store the user input for email, use email.getText() to get the
                             // value

    @FXML
    private Text usernameMsg; // This is the placeholder for username error message, use usernameMsg.setText()
                              // to set the message

    @FXML
    private Text emailMsg; // This is the placeholder for email error message, use emailMsg.setText() to
                           // set the message

    @FXML
    private Text passwordMsg; // This is the placeholder for password error message, use passwordMsg.setText()
                              // to set the message

    @FXML
    private Text confirmPasswordMsg; // This is the placeholder for confirm password error message, use
                                     // confirmPasswordMsg.setText() to set the message

    @FXML
    private Button submitBtn; // This is used to allow user submit the form

    /***
     * VARIABLES
     * 
     ***/
    private boolean isEmailValid = false;

    private boolean isPasswordValid = false;

    private boolean isConfirmPasswordMatched = false;

    /***
     * INITILIZATION OF THE CONTROLLER
     * 
     ***/
    @FXML
    public void initialize() {
        //set pointer to username textbox
        Platform.runLater(() -> username.requestFocus());

        email.setFocusTraversable(true);

        // Add in togglePasswordFields that can have password visibility toggle functions
        TogglePasswordField password = new TogglePasswordField();
        password.setLayoutX(40.0);
        password.setLayoutY(243.0);
        password.setPrefHeight(26.0);
        password.setPrefWidth(219.0);
        password.setStyle("-fx-background-color: #D9D9D9");
        anchorPane.getChildren().add(password);

        TogglePasswordField confirmPassword = new TogglePasswordField();
        confirmPassword.setLayoutX(40.0);
        confirmPassword.setLayoutY(305.0);
        confirmPassword.setPrefHeight(26.0);
        confirmPassword.setPrefWidth(219.0);
        confirmPassword.setStyle("-fx-background-color: #D9D9D9");
        anchorPane.getChildren().add(confirmPassword);

        // When user submit sign up form
        submitBtn.setOnMouseClicked(e -> {
            // If user not entered the details, display error message accordingly
            // 1. email
            if (email.getText().isEmpty()){
                emailMsg.setText("Please enter an email.");
            } else {
                emailMsg.setText("");
            }
            // 2. password
            if (password.getText().isEmpty()){
                passwordMsg.setText("Please enter a password.");
            } else {
                passwordMsg.setText("");
            }
            // 3. confirm password
            if (confirmPassword.getText().isEmpty()){
                confirmPasswordMsg.setText("Please enter the password again.");
            } else {
                confirmPasswordMsg.setText("");
            }
            // 4. username
            if (username.getText().isEmpty()){
                usernameMsg.setText("Please enter a username.");
            } else {
                usernameMsg.setText("");
            }

            // If user entered the details, validate them
            // 1. Validate email
            isEmailValid = checkEmail(email.getText());
            if(!isEmailValid && !email.getText().isEmpty()){
                emailMsg.setText("Invalid email format.");
            } else if (isEmailValid && !email.getText().isEmpty()){
                emailMsg.setText("");
            }

            // 2. Validate password confirmation matching
            isConfirmPasswordMatched = checkPasswordconfirmation(password.getText(), confirmPassword.getText());
            if(!isConfirmPasswordMatched && !password.getText().isEmpty() && !confirmPassword.getText().isEmpty()){
                confirmPasswordMsg.setText("Passwords do not matched.");
            } else if (isConfirmPasswordMatched && !password.getText().isEmpty() && !confirmPassword.getText().isEmpty()){
                confirmPasswordMsg.setText("");
            }


            // If no issue then try to sign up
            if (isEmailValid && isPasswordValid && isConfirmPasswordMatched) {
                // Close the pop-up
                Stage stage = (Stage) submitBtn.getScene().getWindow();
                stage.close();

                ServiceResult result = userService.userSignUp(username.getText(), email.getText(), password.getText());
                try {
                    if ((boolean) result.isSuccessful() == true) {
                        // Pop up sign up successful
                        App.openPopUpAtTop("success-message", result.getReturnMessage());
                        // then switch to login
                        App.switchScene("login-page");
                    } else {
                        // Display error message
                        App.openPopUpAtTop("error-message", result.getReturnMessage());
                    }
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }

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
    }

    /***
     * HELPER METHOD TO VALIDATE EMAIL
     * 
     ***/
    private boolean checkEmail(String email) {
        return (email.matches("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$"));
    }

    /***
     * HELPER METHOD TO CHECK PASSWORD MATCHING
     * 
     ***/
    private boolean checkPasswordconfirmation(String password, String passwordConfirmation) {
        return (password.equals(passwordConfirmation));
    }

}
