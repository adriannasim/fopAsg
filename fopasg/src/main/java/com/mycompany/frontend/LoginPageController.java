package com.mycompany.frontend;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import com.mycompany.backend.ServiceResult;
import com.mycompany.backend.UserService;
import com.mycompany.backend.UserSession;
import com.mycompany.frontend.helper.TogglePasswordField;

/***
 * THIS CONTROLLER CLASS IS USED FOR login-page.fxml
 * 
 ***/
public class LoginPageController extends SharedPaneCharacteristics {

    private UserService userService = new UserService();

    /***
     * ELEMENTS WITH FX:ID
     * 
     ***/
    @FXML
    private Pane pane; // this will used to store all the contents

    @FXML
    private TextField username; // This will store the user input for username/email, use username.getText() to
                                // get the value

    @FXML
    private PasswordField password; // This will store the user input for password, use password.getText() to get
                                    // the value

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

        // Add in togglePasswordFields that can have password visibility toggle
        // functions
        TogglePasswordField password = new TogglePasswordField();
        password.setLayoutX(87.0);
        password.setLayoutY(273.0);
        password.setPrefHeight(38.0);
        password.setPrefWidth(600.0);
        password.setAlignment(Pos.CENTER);
        password.setPromptText("Password");
        password.setFont(Font.font("Roboto", FontWeight.BOLD, 12));
        password.setStyle("-fx-background-color:#6ABC6A; -fx-background-radius: 50; -fx-text-inner-color: #ffffff;");
        pane.getChildren().add(password);

        // When user want to sign up, open sign up page
        signUpBtn.setOnMouseClicked(e -> {
            try {
                App.openPopUpSignUp("sign-up");
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });

        // When user want to login, process the user details
        submitBtn.setOnMouseClicked(e -> {
            try {
                // pass the username and password input by user to the service
                ServiceResult result = userService.userLogin(username.getText(), password.getText());

                // If successfully logged in
                if (result.getReturnObject() != null) {
                    App.openPopUpAtTop("success-message", result.getReturnMessage());
                    UserSession.getSession().setUsername(username.getText());
                    App.switchScene("main-menu");
                } else {
                    // pop up fail msg
                    App.openPopUpAtTop("error-message", result.getReturnMessage());
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
    }

}
