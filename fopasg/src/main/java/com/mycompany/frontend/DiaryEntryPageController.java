package com.mycompany.frontend;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class DiaryEntryPageController {
    @FXML
    private Button backButton; // This will reference the "Back" button from FXML

    @FXML
    public void initialize() {
        backButton.setOnAction(e -> {

            App.goBackToPreviousScene();

        });
    }
}
