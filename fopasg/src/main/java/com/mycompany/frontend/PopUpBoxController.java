package com.mycompany.frontend;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class PopUpBoxController {
    @FXML
    private Text confirmationText; // Reference to the Text element in FXML
    @FXML
    private Button yesButton;
    @FXML
    private Button noButton;

    @FXML
    public void initialize() {
        yesButton.setOnAction(event -> {
            // Close the pop-up
            Stage stage = (Stage) yesButton.getScene().getWindow();
            stage.close();

            // Handle the 'Yes' button click
            try {
                FXMLLoader loader = new FXMLLoader(App.class.getResource("success-message.fxml"));
                Parent root = loader.load();

                SuccessMessageController controller = loader.getController();
                controller.setMessageText("Action has completed successfully.");

                Stage messageBoxStage = new Stage();
                messageBoxStage.initStyle(StageStyle.UNDECORATED);
                Scene scene = new Scene(root);
                messageBoxStage.setScene(scene); // Change the scene of the stage
                
                messageBoxStage.show();
                // After 3 seconds, close the pop-up
                javafx.animation.PauseTransition pause = new javafx.animation.PauseTransition(
                        javafx.util.Duration.seconds(3));
                pause.setOnFinished(e -> messageBoxStage.close()); // Close the pop-up after 3 seconds
                pause.play();

            } catch (IOException ex) {
                ex.printStackTrace();
            }

        });

        noButton.setOnAction(event -> {
            // Handle the 'No' button click

            // Close the pop-up
            Stage stage = (Stage) noButton.getScene().getWindow();
            stage.close();
        });
    }

    // Method to update the text dynamically
    public void setConfirmationText(String newText) {
        confirmationText.setText(newText); // Set the new text dynamically
    }
}
