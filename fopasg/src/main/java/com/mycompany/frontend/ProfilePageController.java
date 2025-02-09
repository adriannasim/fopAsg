package com.mycompany.frontend;

import java.io.IOException;

import com.mycompany.backend.User;
import com.mycompany.backend.UserService;
import com.mycompany.backend.UserSession;

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
    private Text emailMsg; // this is used to display email error message

    @FXML
    private Button changePasswordBtn;

    @FXML
    private Button deleteAccountBtn;

    @FXML
    private Button submitBtn;

    /***
     * VARIABLES
     * 
     ***/
    private boolean isEmailValid = true;

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

        // Set the data
        username.setText(sessionUsername);
        email.setText(user.getEmail());

        // Default cannot edit
        username.setEditable(false);
        email.setEditable(false);

        // Unless user click on edit icon
        emailEdit.setOnMouseClicked(_ -> {
            email.setEditable(true);
            email.setStyle("-fx-background-color: #F1F1F1;");
            email.requestFocus();
        });

        // When user click on submit button
        submitBtn.setOnMouseClicked(_ -> {

            // If user not entered the details, display error message accordingly
            // 1. email
            if (email.getText().isEmpty()) {
                emailMsg.setText("Please enter an email.");
                isEmailValid = false;
            } else {
                emailMsg.setText("");
                isEmailValid = true;
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
            if (isEmailValid) {
                try {
                    // Pop up a confimation message
                    App.openConfirmationPopUp("Confirm to change your details?",
                        () -> userService.userEdit(username.getText(), email.getText(), null),
                        () -> {});
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });

        // handle change password btn
        changePasswordBtn.setOnMouseClicked(_ -> {
            mainMenuController.loadNewContent("change-password");
        });

        // handle delete account btn
        deleteAccountBtn.setOnMouseClicked(_ -> {
            try {
                // Pop up a confimation message
                App.openConfirmationPopUp("Confirm to delete your account? This action is irreversible!",
                    () -> userService.userDelete(username.getText()),
                    () -> 
                    {
                        //logout
                        UserSession.getSession().setUsername("");
                        try
                        {
                            App.switchScene("login-page");
                        }
                        catch (IOException ex)
                        {
                            ex.printStackTrace();
                        }
                    }
                );
            } catch (IOException ex) {
                ex.printStackTrace();
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
