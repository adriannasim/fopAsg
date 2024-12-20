package com.mycompany.frontend;

import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class ExportByMonthController {
    @FXML
    private Button backButton;

    private MainMenuController mainMenuController;

    // Setter method to allow MainMenuController reference injection
    public void setMainMenuController(MainMenuController mainMenuController) {
        this.mainMenuController = mainMenuController;
    }
     @FXML
    public void initialize() {
        backButton.setOnMouseClicked(e -> {
             if (mainMenuController != null) {
                mainMenuController.goBackToPreviousAnchorPane();
            }
        });
    }
}
