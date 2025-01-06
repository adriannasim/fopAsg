package com.mycompany.frontend.helper;

import java.io.IOException;
import java.util.function.Supplier;

import com.mycompany.backend.ServiceResult;
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

    // private String successMessage;
    
    // private String failedMessage;

    private Supplier<ServiceResult> serviceOperation;

    /***
     * INITILIZATION OF THE CONTROLLER.
     * 
     ***/
    @FXML
    public void initialize() {

        // Handle the 'Yes' button click.
        yesButton.setOnAction(_ -> {
            handleYesButtonClick();
            
        });

        // Handle the 'No' button click.
        noButton.setOnAction(_ -> {
            // Simply close the pop-up, no further action.
            Stage stage = (Stage) noButton.getScene().getWindow();
            stage.close();
            
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

    // /*** METHOD TO SET THE SUCCESS MESSAGE TEXT.
    //  * 
    //  * ***/
    // public void setSuccessMessageText(String successMessage) {
    //     this.successMessage = successMessage;
    // }

    // /*** METHOD TO SET THE FAILED MESSAGE TEXT.
    //  * 
    //  * ***/
    // public void setFailedMessageText(String failedMessage) {
    //     this.failedMessage = failedMessage;
    // }

    /*** METHOD TO SET SERVICE OPERATION RESULT.
     * 
     * ***/
    public void setServiceOperation(Supplier<ServiceResult> serviceOperation) {
        this.serviceOperation = serviceOperation;
    }

    /*** METHOD TO HANDLE YES BUTTON CLICK.
     * 
     * ***/
    public void handleYesButtonClick() {
        boolean success;
        String message;

        // Close the pop-up
        Stage stage = (Stage) yesButton.getScene().getWindow();
        stage.close();
        
        //Get the result of the operation
        // success = (boolean) serviceOperation.get().getReturnObject();
        success = (boolean) serviceOperation.get().isSuccessful();

        if (success){
            filename = "success-message";
            message = serviceOperation.get().getReturnMessage();
        } else {
            filename = "error-message";
            message = serviceOperation.get().getReturnMessage();
        }

        // Display the message box to users
        try {
            App.openPopUpAtTop(filename, message);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
