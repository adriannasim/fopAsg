package com.mycompany.frontend;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;

/***
 * THIS CONTROLLER CLASS IS USED FOR motivational-quotes.fxml
 * 
 ***/

public class MotivationalQuotesController {

    /***
     * ELEMENTS WITH FX:ID
     * 
     ***/
    @FXML
    private Button closeBtn;

    /***
     * INITILIZATION OF THE CONTROLLER
     * 
    ***/
    @FXML
    public void initialize() {
        // Handle the 'Close' button click
        closeBtn.setOnAction(_ -> {
            // Close the pop-up
            Stage stage = (Stage) closeBtn.getScene().getWindow();
            stage.close();
        });

    }
}
