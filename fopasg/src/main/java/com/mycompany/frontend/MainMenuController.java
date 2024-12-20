package com.mycompany.frontend;

import java.io.IOException;
import java.util.Stack;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;

public class MainMenuController {

    private static final Stack<AnchorPane> anchorPaneHistory = new Stack<>();

    @FXML
    private Button button;

    @FXML
    private AnchorPane rootPane;

    @FXML
    private Button newDiaryBtn;

    @FXML
    private Button historyBtn;

    @FXML
    private Button recycleBinBtn;

    @FXML
    private Button moodTrackerBtn;

    @FXML
    private void handleButtonClick() {
        System.out.println("Button was clicked!");
    }

    @FXML
    public void initialize() {
        
        
        loadNewContent("main-root-pane.fxml");

        Platform.runLater(() -> {
        try {
            App.openPopUp("mood-indicator.fxml"); // Call this after the main UI is set up
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    });

        newDiaryBtn.setOnMouseClicked(e -> {
            loadNewContent("diary-entry-page.fxml");
        });

        recycleBinBtn.setOnMouseClicked(e -> {
            loadNewContent("diary-recycle-bin.fxml");
        });

        historyBtn.setOnMouseClicked(e -> {
            loadNewContent("diary-history-page.fxml");
        });

        moodTrackerBtn.setOnMouseClicked(e -> {
            loadNewContent("mood-tracker.fxml");
        });
    }

    // Method to load new content into the rootPane
    public void loadNewContent(String filename) {
        try {
            // Save the current state of the rootPane
            AnchorPane currentState = new AnchorPane();
            currentState.getChildren().setAll(rootPane.getChildren());
            anchorPaneHistory.push(currentState);

            // Load the new content from the FXML file
            FXMLLoader loader = new FXMLLoader(getClass().getResource(filename));
            AnchorPane newContent = loader.load();

            // Set the new content's controller for proper navigation
            if (filename.equals("diary-entry-page.fxml")) {
                DiaryEntryPageController controller = loader.getController();
                controller.setMainMenuController(this); // Pass reference to controller
            }

            if (filename.equals("diary-recycle-bin.fxml")) {
                RecycleBinController controller = loader.getController();
                controller.setMainMenuController(this); // Pass reference to controller
            }

            if (filename.equals("diary-history-page.fxml")) {
                HistoryPageController controller = loader.getController();
                controller.setMainMenuController(this); // Pass reference to controller
            }

            if (filename.equals("diary-view-page.fxml")) {
                DiaryViewController controller = loader.getController();
                controller.setMainMenuController(this); // Pass reference to controller
            }

            if (filename.equals("mood-tracker.fxml")) {
                MoodTrackerController controller = loader.getController();
                controller.setMainMenuController(this); // Pass reference to controller
            }

            if (filename.equals("export-by-day.fxml")) {
                ExportByDayController controller = loader.getController();
                controller.setMainMenuController(this); // Pass reference to controller
            }

            if (filename.equals("export-by-week.fxml")) {
                ExportByWeekController controller = loader.getController();
                controller.setMainMenuController(this); // Pass reference to controller
            }

            if (filename.equals("export-by-month.fxml")) {
                ExportByMonthController controller = loader.getController();
                controller.setMainMenuController(this); // Pass reference to controller
            }


            // Replace the children of rootPane with the new content
            rootPane.getChildren().setAll(newContent.getChildren());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Method to go back to the previous AnchorPane
    public void goBackToPreviousAnchorPane() {
        if (!anchorPaneHistory.isEmpty()) {
            // Get the previous AnchorPane state
            AnchorPane previousState = anchorPaneHistory.pop();

            // Restore the previous state to the rootPane
            rootPane.getChildren().setAll(previousState.getChildren());
        } else {
            System.out.println("No previous state to go back to.");
        }
    }
}