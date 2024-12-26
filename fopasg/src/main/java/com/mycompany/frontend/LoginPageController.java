package com.mycompany.frontend;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

/***
 * THIS CONTROLLER CLASS IS USED FOR login-page.fxml
 * 
 ***/
public class LoginPageController extends SharedPaneCharacteristics{

    /***
     * ELEMENTS WITH FX:ID
     * 
     ***/
    @FXML
    private TextField username;  // This will store the user input for username/email, use username.getText() to get the value

    @FXML
    private PasswordField password;  // This will store the user input for password, use password.getText() to get the value

    @FXML
    private Label usernameLabel;

    @FXML
    private Label passwordLabel;

    @FXML
    private Button submitBtn;

    @FXML
    private Button signUpBtn;


    /***
     * INITILIZATION OF THE CONTROLLER
     * 
     ***/
    @FXML
    public void initialize() {
        // Steps to show and hide the label
        username.setOnMouseClicked(e -> {
            usernameLabel.setVisible(true);
        });
        username.setOnMouseExited(e -> {
            String input = username.getText();
            if (input == "" || input.isEmpty()) {
                usernameLabel.setVisible(false);
            }
        });
        password.setOnMouseClicked(e -> {
            passwordLabel.setVisible(true);
        });

        password.setOnMouseExited(e -> {
            String input = password.getText();
            if (input == "" || input.isEmpty()) {
                passwordLabel.setVisible(false);
            }
        });

        // When user want to sign up, open sign up page
        signUpBtn.setOnMouseClicked(e->{
            try{
                App.openPopUpSignUp("sign-up");
            } catch(IOException ex){
                ex.printStackTrace();
            } 
        });

        // When user want to login, process the user details
        submitBtn.setOnMouseClicked(e->{
            try{
                // PROCESSING HERE...

                // If success then navigate to main menu
                App.switchScene("main-menu");
            } catch (IOException ex){
                ex.printStackTrace();
            }
        });
    }

}
