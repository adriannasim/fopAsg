package com.mycompany.frontend;

import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class SuccessMessageController {
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
        // Get the current stage (window) and close it
        Stage stage = (Stage) crossIcon.getScene().getWindow();
        stage.close();
    }

    // Method to update the text dynamically
    public void setMessageText(String newText) {
        messageText.setText(newText); // Set the new text dynamically
    }
}
