package com.mycompany.frontend;

import java.io.IOException;

import com.mycompany.backend.User;
import com.mycompany.backend.UserService;
import com.mycompany.backend.UserSession;

import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;

/***
 * THIS CONTROLLER CLASS IS USED FOR profile-page.fxml
 * 
 ***/

public class ProfilePageController extends SharedPaneCharacteristics {

    private UserService userService = new UserService();
    
    /*** ELEMENTS WITH FX:ID  
     * 
     * ***/
    @FXML
    private TextField username;  // this is ued to keep username

    @FXML
    private TextField email; // this is used to keep email

    @FXML
    private PasswordField password; // this is used to keep password

    @FXML
    private ImageView usernameEdit; // this is used to enable username edit

    @FXML
    private ImageView emailEdit; // this is used to enable email edit

    @FXML
    private ImageView passwordEdit; // this is used to enable password edit

    @FXML
    private Text passwordMsg;  // this is used to display password error message

    @FXML
    private Button submitBtn;


    /*** INITILIZATION OF THE CONTROLLER
     * 
     * ***/
    @FXML
    public void initialize() {
        // Inherit Super Class's initialize()
        super.initialize(); 

        //get user session
        String sessionUsername = UserSession.getSession().getUsername();
        //get user info
        User user = userService.getUserByUsername(sessionUsername);

        // Set the data
        username.setText(sessionUsername);
        email.setText(user.getEmail());
        password.setText(user.getPassword());

        // Default cannot edit
        username.setEditable(false);
        email.setEditable(false);
        password.setEditable(false);

        // Unless user click on edit icon
        usernameEdit.setOnMouseClicked(e->{
            username.setEditable(true);
            username.setStyle("-fx-background-color: #F1F1F1;");
            username.requestFocus();
        });

        emailEdit.setOnMouseClicked(e->{
            email.setEditable(true);
            email.setStyle("-fx-background-color: #F1F1F1;");
            email.requestFocus();
        });

        passwordEdit.setOnMouseClicked(e->{
            password.setEditable(true);
            password.setStyle("-fx-background-color: #F1F1F1;");
            password.requestFocus();
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

        // When user click on submit button
        submitBtn.setOnMouseClicked(e->{
            try{
                // Pop up a confimation message
                App.openConfirmationPopUp("Confirm to change your details?",
                    () -> userService.userEdit(username.getText(), email.getText(), password.getText())
                );  
            } catch (IOException ex){
                ex.printStackTrace();
            }
        });

    } 
}
