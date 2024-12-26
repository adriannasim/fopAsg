package com.mycompany.frontend;

import javafx.fxml.FXML;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.text.Text;

/***
 * THIS CONTROLLER CLASS IS USED FOR sign-up.fxml
 * 
 ***/

public class SignupController {

    /*** ELEMENTS WITH FX:ID  
     * 
     * ***/
    @FXML
    private Button submitBtn;

    @FXML 
    private PasswordField password;

    @FXML
    private Text usernameMsg;

    @FXML
    private Text emailMsg;

    @FXML
    private Text passwordMsg;

    @FXML
    private Text confirmPasswordMsg;


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
            // Operation here
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
