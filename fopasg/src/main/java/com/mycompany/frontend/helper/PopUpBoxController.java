package com.mycompany.frontend.helper;

import java.io.IOException;
import com.mycompany.frontend.App;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/***
 * THIS CONTROLLER CLASS IS USED FOR pop-up-box.fxml
 * 
 ***/

public class PopUpBoxController {

    /***
     * ELEMENTS WITH FX:ID
     * 
     ***/
    @FXML
    private Text confirmationText;

    @FXML
    private Button yesButton;

    @FXML
    private Button noButton;

    /***
     * VARIABLES DECLARATION.
     * 
     ***/
    private String filename;

    private String successMessage;
    
    private String failedMessage;

    /***
     * INITILIZATION OF THE CONTROLLER.
     * 
     ***/
    @FXML
    public void initialize() {

        // Handle the 'Yes' button click.
        yesButton.setOnAction(event -> {
            handleYesButtonClick();
            
        });

        // Handle the 'No' button click.
        noButton.setOnAction(event -> {
            // Simply close the pop-up, no further action.
            Stage stage = (Stage) noButton.getScene().getWindow();
            stage.close();
            System.out.println(failedMessage);
        });
    }

    /*** METHOD TO SET THE CONFIRMATION TEXT.
     * 
     * ***/
    public void setConfirmationText(String newText) {
        confirmationText.setText(newText);
    }

    /*** METHOD TO SET THE FILENAME.
     * 
     * ***/
    public void setFilename(String filename) {
        this.filename = filename;
    }

    /*** METHOD TO SET THE SUCCESS MESSAGE TEXT.
     * 
     * ***/
    public void setSuccessMessageText(String successMessage) {
        this.successMessage = successMessage;
    }

    /*** METHOD TO SET THE FAILED MESSAGE TEXT.
     * 
     * ***/
    public void setFailedMessageText(String failedMessage) {
        this.failedMessage = failedMessage;
    }

    /*** METHOD TO HANDLE YES BUTTON CLICK.
     * 
     * ***/
    public void handleYesButtonClick() {
        boolean success = false;
        String message;

        // Close the pop-up
        Stage stage = (Stage) yesButton.getScene().getWindow();
        stage.close();
        
        // Operation here (change the success value, either true or false)

        if (success){
            filename = "success-message";
            message = successMessage;
        } else {
            filename = "error-message";
            message = failedMessage;
        }

        // Display the message box to users
        try {
            App.openPopUpAtTop(filename, message);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
