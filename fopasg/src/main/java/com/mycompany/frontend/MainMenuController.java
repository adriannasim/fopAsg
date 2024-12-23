package com.mycompany.frontend;

import java.io.IOException;
import java.util.Stack;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;


/*** 
 * THIS CONTROLLER CLASS IS USED FOR main-menu.fxml
 * 
 ***/

public class MainMenuController {

    // This used to store the user navigation history, so user can navigate back to
    // previous pages.
    private static Stack<AnchorPane> anchorPaneHistory = new Stack<>();

    /***
     * ELEMENTS WITH FX:ID
     * 
     ***/
    @FXML
    private AnchorPane rootPane; // This is where the page content will change, other will be fixed

    @FXML
    private Button newDiaryBtn; // Button to open diary-entry-page.fxml

    @FXML
    private Button historyBtn; // Button to open diary-history-page.fxml

    @FXML
    private Button recycleBinBtn; // Button to open diary-recycle-bin.fxml

    @FXML
    private Button moodTrackerBtn; // Button to open mood-tracer.fxml

    /***
     * INITILIZATION OF THE CONTROLLER
     * 
     ***/
    @FXML
    public void initialize() {
        // Initial page
        loadNewContent("main-root-pane.fxml");

        // Open the mood indicator page when user enter (Call this after the main UI is
        // set up)
        // Can add the logic of when it should display here...
        Platform.runLater(() -> {
            try {
                App.openPopUp("mood-indicator");
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });

        // When user click on newDiaryBtn, navigate to diary-entry-page
        newDiaryBtn.setOnMouseClicked(e -> {
            loadNewContent("diary-entry-page.fxml");
        });

        // When user click on recycleBinBtn, navigate to diary-recycle-bin
        recycleBinBtn.setOnMouseClicked(e -> {
            loadNewContent("diary-recycle-bin.fxml");
        });

        // When user click on historyBtn, navigate to diary-history-page
        historyBtn.setOnMouseClicked(e -> {
            loadNewContent("diary-history-page.fxml");
        });

        // When user click on moodTrackerBtn, navigate to mood-tracker
        moodTrackerBtn.setOnMouseClicked(e -> {
            loadNewContent("mood-tracker.fxml");
        });
    }

    /***
     * METHOD TO LOAD NEW CONTENT INTO THE ROOTPANE
     * - MEANS CHANGE PAGE.
     * 
     ***/
    public void loadNewContent(String filename) {
        try {
            // Save the current state of the rootPane to history
            AnchorPane currentState = new AnchorPane();
            currentState.getChildren().setAll(rootPane.getChildren());
            anchorPaneHistory.push(currentState);

            // Load the new content from the FXML file
            FXMLLoader loader = new FXMLLoader(getClass().getResource(filename));
            AnchorPane newContent = loader.load();

            // Get the controller of the loaded FXML
            Object controller = loader.getController();

            // Check if the controller is an instance of SharedPaneCharacteristics and set
            // the MainMenuController
            if (controller instanceof SharedPaneCharacteristics) {
                SharedPaneCharacteristics sharedController = (SharedPaneCharacteristics) controller;
                sharedController.setMainMenuController(this); // Pass reference to controller
            }

            // Replace the children of rootPane with the new content
            rootPane.getChildren().setAll(newContent.getChildren());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /***
     * METHOD TO GO BACK TO THE PREVIOUS ANCHORPANE
     * - MEANS GO BACK TO PREVIOUS PAGE.
     * 
     ***/
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