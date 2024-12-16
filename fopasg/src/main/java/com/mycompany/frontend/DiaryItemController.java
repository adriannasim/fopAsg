package com.mycompany.frontend;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;

public class DiaryItemController {
    @FXML
    private Pane diaryPane; 

    @FXML
    private Label titleLabel;
    
    @FXML
    public void initialize() {
        System.out.println("DiaryItemController initialized!");
    }

    public void setTitle(String title) {
        titleLabel.setText(title);
    }
}

