package com.mycompany.frontend;

import java.io.IOException;

import com.mycompany.backend.UserService;
import com.mycompany.backend.UserSession;
import com.mycompany.frontend.helper.TogglePasswordField;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;

/***
 * THIS CONTROLLER CLASS IS USED FOR profile-page.fxml
 * 
 ***/

public class ChangePasswordController extends SharedPaneCharacteristics {

    private UserService userService = new UserService();

    /***
     * ELEMENTS WITH FX:ID
     * 
     ***/
    @FXML
    private Pane pane; // this is used to hold the password and the password edit icon

    @FXML
    private Text oldPasswordMsg;

    @FXML
    private Text newPasswordMsg;

    @FXML
    private Text confirmNewPasswordMsg;

    @FXML
    private Button submitBtn;

    /***
     * VARIABLES
     * 
     ***/
    private boolean isPasswordValid = false;
    private boolean isConfirmPasswordMatched = false;

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

        // Add in togglePasswordFields that can have password visibility toggle
        // functions
        TogglePasswordField oldPassword = new TogglePasswordField();
        oldPassword.setLayoutX(134.0);
        oldPassword.setLayoutY(229.0);
        oldPassword.setPrefHeight(26.0);
        oldPassword.setPrefWidth(453);
        oldPassword.setStyle("-fx-background-color: #D9D9D9");
        pane.getChildren().add(oldPassword);

        TogglePasswordField newPassword = new TogglePasswordField();
        newPassword.setLayoutX(134.0);
        newPassword.setLayoutY(286.0);
        newPassword.setPrefHeight(26.0);
        newPassword.setPrefWidth(453);
        newPassword.setStyle("-fx-background-color: #D9D9D9");
        pane.getChildren().add(newPassword);

        TogglePasswordField confirmNewPassword = new TogglePasswordField();
        confirmNewPassword.setLayoutX(134.0);
        confirmNewPassword.setLayoutY(342.0);
        confirmNewPassword.setPrefHeight(26.0);
        confirmNewPassword.setPrefWidth(453);
        confirmNewPassword.setStyle("-fx-background-color: #D9D9D9");
        pane.getChildren().add(confirmNewPassword);

        // Check for password strength & Validate password format
        newPassword.setOnKeyTyped(_ -> {
            // Weak password
            if (newPassword.getText().length() < 6) { // Less than 6 characters
                newPassword.setStyle("-fx-background-color: #FF9696;");
                newPasswordMsg.setText("Password strength weak.");
                newPasswordMsg.setStyle("-fx-fill: #FF9696;");
                isPasswordValid = false;
            }
            // Moderate password
            else if (newPassword.getText().matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[A-Za-z\\d]{6,}$")) { // Small letters + capital letters + number && > 6 characters
                newPassword.setStyle("-fx-background-color: #669A9D;");
                newPasswordMsg.setText("Password strength moderate.");
                newPasswordMsg.setStyle("-fx-fill: #669A9D;");
                isPasswordValid = true;
            }
            // Strong password
            else if (newPassword.getText().matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&#])[A-Za-z\\d@$!%*?&#]{6,}$")) { // Small letters + capital letters + number + special characters && > 6 characters
                newPassword.setStyle("-fx-background-color: #9ABF80;");
                newPasswordMsg.setText("Password strength strong.");
                newPasswordMsg.setStyle("-fx-fill: #9ABF80;");
                isPasswordValid = true;
            }
        });

        // When user click on submit button
        submitBtn.setOnMouseClicked(_ -> {
            // If user not entered the details, display error message accordingly
            // 1. Old Password
            if (oldPassword.getText().isEmpty()) {
                oldPasswordMsg.setText("Please enter your old password.");
            } else {
                oldPasswordMsg.setText("");
            }
            // 2. New Password
            if (newPassword.getText().isEmpty()) {
                newPasswordMsg.setText("Please enter a new password.");
            } else {
                newPasswordMsg.setText("");
            }
            // 2. Confirm New Password
            if (confirmNewPassword.getText().isEmpty()) {
                confirmNewPasswordMsg.setText("Please confirm your new password.");
            } else {
                confirmNewPasswordMsg.setText("");
            }

            // If user entered the details, validate them
            // 1. Validate password confirmation matching
            isConfirmPasswordMatched = newPassword.getText().equals(confirmNewPassword.getText());
            if(!isConfirmPasswordMatched && !newPassword.getText().isEmpty() && !confirmNewPassword.getText().isEmpty()){
                confirmNewPasswordMsg.setText("Passwords do not matched.");
            } else if (isConfirmPasswordMatched && !newPassword.getText().isEmpty() && !confirmNewPassword.getText().isEmpty()){
                confirmNewPasswordMsg.setText("");
            }


            // If no issue then try to update profile
            if (isConfirmPasswordMatched && isPasswordValid) {
                try {
                    // Pop up a confimation message
                    App.openConfirmationPopUp("Confirm to change your password?",
                        () -> userService.changePassword(sessionUsername, oldPassword.getText(), newPassword.getText()),
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
            }
        });

    }
}
