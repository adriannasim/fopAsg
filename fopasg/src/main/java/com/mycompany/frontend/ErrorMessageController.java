package com.mycompany.frontend;

import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class ErrorMessageController {
    @FXML
    private Text messageText; // Reference to the Text element in FXML

    @FXML
    private ImageView crossIcon;

    public void initialize() {
        // Add a click event to the cross icon
        crossIcon.setOnMouseClicked(event -> closeMessageWindow());
    }

    // Method to close the window
    private void closeMessageWindow() {
        Stage stage = (Stage) crossIcon.getScene().getWindow();
        stage.close();
    }

    // Method to update the text dynamically
    public void setMessageText(String newText) {
        messageText.setText(newText); 
    }
}
