package com.mycompany.frontend;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class MotivationalQuotesController {
    @FXML
    private Button closeBtn;

   
    @FXML
    public void initialize() {
        // Handle the 'Close' button click
        closeBtn.setOnAction(event -> {
            // Close the pop-up
            Stage stage = (Stage) closeBtn.getScene().getWindow();
            stage.close();
        });

    }
}
