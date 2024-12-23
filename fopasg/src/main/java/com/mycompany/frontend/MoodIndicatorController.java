package com.mycompany.frontend;

import java.io.IOException;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class MoodIndicatorController {
    @FXML
    private Button happyBtn;

    @FXML
    private Button normalBtn;

    @FXML
    private Button sadBtn;

    @FXML
    public void initialize() {
        // Handle the 'Happy' button click
        happyBtn.setOnAction(event -> {
            // Close the pop-up
            Stage stage = (Stage) happyBtn.getScene().getWindow();
            stage.close();

            Platform.runLater(() -> {
                try {
                    App.openPopUp("motivational-quotes"); // Call this after the main UI is set up
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            });

        });

        // Handle the 'Normal' button click
        normalBtn.setOnAction(event -> {
            // Close the pop-up
            Stage stage = (Stage) normalBtn.getScene().getWindow();
            stage.close();
            Platform.runLater(() -> {
                try {
                    App.openPopUp("motivational-quotes"); // Call this after the main UI is set up
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            });
        });

        // Handle the 'Sad' button click
        sadBtn.setOnAction(event -> {
            // Close the pop-up
            Stage stage = (Stage) sadBtn.getScene().getWindow();
            stage.close();
            Platform.runLater(() -> {
                try {
                    App.openPopUp("motivational-quotes"); // Call this after the main UI is set up
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            });
        });
    }
}
