package com.mycompany.frontend;

import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class MainMenuController {

    @FXML
    private Button button;

    @FXML
    private void handleButtonClick() {
        System.out.println("Button was clicked!");
    }
}