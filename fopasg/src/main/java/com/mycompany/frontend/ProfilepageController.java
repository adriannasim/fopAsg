/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.mycompany.frontend;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;

/**
 * FXML Controller class
 *
 * @author User
 */
public class ProfilePageController extends SharedPaneCharacteristics {
    
    /*** ELEMENTS WITH FX:ID  
     * 
     * ***/
    @FXML
    private TextField username;

    @FXML
    private TextField email;

    @FXML
    private PasswordField password;

    @FXML
    private ImageView usernameEdit;

    @FXML
    private ImageView emailEdit;

    @FXML
    private ImageView passwordEdit;

    @FXML
    private Button submitBtn;

    /*** INITILIZATION OF THE CONTROLLER
     * 
     * ***/
    @FXML
    public void initialize() {
        // Inherit Super Class's initialize()
        super.initialize(); 

        // Set the data
        username.setText("test123");
        email.setText("test@gmail.com");
        password.setText("12345678");

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

        submitBtn.setOnMouseClicked(e->{
            try{
                App.openConfirmationPopUp("Do you confirm to change your details?", "Your changes has been saved.", "Failed to save the details.");
            } catch (IOException ex){
                ex.printStackTrace();
            }
            
        });

    }
    
    
}
