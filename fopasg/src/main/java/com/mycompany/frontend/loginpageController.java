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
    private TextField username;

    @FXML
    private PasswordField password;

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

        // When user want to sign up
        signUpBtn.setOnMouseClicked(e->{
            try{
                App.openPopUpSignUp("sign-up");
            } catch(IOException ex){
                ex.printStackTrace();
            } 
        });

        // When user want to login
        signUpBtn.setOnMouseClicked(e->{
            try{
                App.openPopUpSignUp("sign-up");
            } catch(IOException ex){
                ex.printStackTrace();
            } 
        });

        // When user want to login
        submitBtn.setOnMouseClicked(e->{
            try{
                App.switchScene("main-menu");
            } catch (IOException ex){
                ex.printStackTrace();
            }
        });
    }

}
