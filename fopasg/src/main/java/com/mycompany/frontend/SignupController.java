package com.mycompany.frontend;

import java.io.IOException;

import com.mycompany.backend.UserService;

import javafx.fxml.FXML;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

import com.mycompany.backend.UserService;

/***
 * THIS CONTROLLER CLASS IS USED FOR sign-up.fxml
 * 
 ***/

public class SignupController {

    private UserService userService = new UserService();

    /*** ELEMENTS WITH FX:ID  
     * 
     * ***/
    @FXML 
    private TextField username; // This will store the user input for username, use username.getText() to get the value
    
    @FXML 
    private TextField email; // This will store the user input for email, use email.getText() to get the value

    @FXML 
    private PasswordField password; // This will store the user input for password, use password.getText() to get the value

    @FXML 
    private PasswordField confirmPassword; // This will store the user input for confirm password, use confirmPassword.getText() to get the value

    @FXML
    private Text usernameMsg; // This is the placeholder for username error message, use usernameMsg.setText() to set the message

    @FXML
    private Text emailMsg; // This is the placeholder for email error message, use emailMsg.setText() to set the message

    @FXML
    private Text passwordMsg; // This is the placeholder for password error message, use passwordMsg.setText() to set the message

    @FXML
    private Text confirmPasswordMsg; // This is the placeholder for confirm password error message, use confirmPasswordMsg.setText() to set the message

    @FXML
    private Button submitBtn; 


    /*** INITILIZATION OF THE CONTROLLER
     * 
     * ***/
    @FXML
    public void initialize(){
        // When user submit sign up form
        submitBtn.setOnMouseClicked(e->{
            // Close the pop-up
            Stage stage = (Stage) submitBtn.getScene().getWindow();
            stage.close();
            // OPERATION HERE...
            boolean success = userService.userSignUp(username.getText(), email.getText(), password.getText());
            
            if (success)
            {
                //Pop up sign up successful (TODO)

                //then switch to login
                try
                {
                    App.switchScene("login-page");
                }
                catch (IOException ex)
                {
                    ex.printStackTrace();
                }
            }
            else
            {
                // Display error message (TODO)
            }
        });

        // Check for password strength
        password.setOnKeyTyped(e->{
            // Weak password
            if (password.getText().length() < 6){ // NEED TO CHANGE THE FORMAT COMPARE
                password.setStyle("-fx-background-color: #FF9696;");
                passwordMsg.setText("Password strength weak.");
                passwordMsg.setStyle("-fx-fill: #FF9696;");
            }
            // Moderate password 
            else if (password.getText().equals("123456")){ // NEED TO CHANGE THE FORMAT COMPARE
                password.setStyle("-fx-background-color: #669A9D;");
                passwordMsg.setText("Password strength moderate.");
                passwordMsg.setStyle("-fx-fill: #669A9D;");
            }
            // Strong password 
            else if (password.getText().equals("abc123")){ // NEED TO CHANGE THE FORMAT COMPARE
                password.setStyle("-fx-background-color: #9ABF80;");
                passwordMsg.setText("Password strength strong.");
                passwordMsg.setStyle("-fx-fill: #9ABF80;");
            }
        });
    }
    
}
