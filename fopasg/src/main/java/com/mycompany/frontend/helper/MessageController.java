package com.mycompany.frontend.helper;

import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/*** 
 * THIS CONTROLLER CLASS IS USED FOR 
 * success-message.fxml AND error-message.fxml 
 * 
 ***/

public class MessageController {

    /*** ELEMENTS WITH FX:ID.  
     * 
     * ***/
    @FXML
    private Text messageText; 
    @FXML
    private ImageView crossIcon;


    /*** INITILIZATION OF THE CONTROLLER.
     * 
     * ***/
    public void initialize() {
        // Add a click event to the cross icon
        crossIcon.setOnMouseClicked(event -> closeMessageWindow());
    }

    /*** METHOD TO CLOSE THE ERROR MESSAGE WINDOW.
     * 
     * ***/
    private void closeMessageWindow() {
        Stage stage = (Stage) crossIcon.getScene().getWindow();
        stage.close();
    }

    /*** METHOD TO SET THE MESSAGE TEXT.
     * 
     * ***/
    public void setMessageText(String newText) {
        messageText.setText(newText); 
    }
}
