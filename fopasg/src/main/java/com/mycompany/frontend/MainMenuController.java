package com.mycompany.frontend;

import java.io.IOException;
import java.util.Stack;

import com.mycompany.backend.DiaryService;
import com.mycompany.backend.UserSession;

import javafx.util.Duration;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

/***
 * THIS CONTROLLER CLASS IS USED FOR main-menu.fxml
 * 
 ***/

public class MainMenuController {

    // This used to store the user navigation history and current file name, so user can navigate back to
    // previous pages.
    private static Stack<String> navigationHistory = new Stack<>();
    private String currentFilename;

    /***
     * ELEMENTS WITH FX:ID
     * 
     ***/
    @FXML
    private AnchorPane rootPane; // This is where the page content will change, other will be fixed

    @FXML
    private TextField searchBar; // TextField to enter search queries, use serachBar.getText() to get the query

    @FXML
    private Button newDiaryBtn; // Button to open diary-entry-page.fxml

    @FXML
    private Button historyBtn; // Button to open diary-history-page.fxml

    @FXML
    private Button recycleBinBtn; // Button to open diary-recycle-bin.fxml

    @FXML
    private Button moodTrackerBtn; // Button to open mood-tracer.fxml

    @FXML
    private Button settingsBtn; // Button to open profile

    @FXML
    private Button logoutBtn; // button to logout

    /***
     * INITILIZATION OF THE CONTROLLER
     * 
     ***/
    @FXML
    public void initialize() {
        // Initial page
        loadNewContent("main-root-pane");

        // Get user session
        String sessionUsername = UserSession.getSession().getUsername();

        // Get user diary
        DiaryService diaryService = new DiaryService(sessionUsername);

        // Delete < 0 days entries
        diaryService.clearOldDiaryEntry(sessionUsername);

        // When user click on newDiaryBtn, navigate to diary-entry-page
        newDiaryBtn.setOnMouseClicked(_ -> {
            UserSession.getSession().setCurrentDiary(null);
            loadNewContent("diary-entry-page");
        });

        // When user click on recycleBinBtn, navigate to diary-recycle-bin
        recycleBinBtn.setOnMouseClicked(_ -> {
            loadNewContent("diary-recycle-bin");
        });

        // When user click on historyBtn, navigate to diary-history-page
        historyBtn.setOnMouseClicked(_ -> {
            loadNewContent("diary-history-page");
        });

        // When user click on moodTrackerBtn, navigate to mood-tracker
        moodTrackerBtn.setOnMouseClicked(_ -> {
            loadNewContent("mood-tracker");
        });

        // When user click on settingsBtn, navigate to profile
        settingsBtn.setOnMouseClicked(_ -> {
            loadNewContent("profile-page");
        });

        // When user click on logoutBtn, navigate to login
        logoutBtn.setOnMouseClicked(_ -> {
            try{
                // LOGOUT OPERATION HERE...
                UserSession.getSession().setUsername("");
                App.switchScene("login-page");
            } catch (IOException ex){
                ex.printStackTrace();
            }
        });

        // When user type on searchbar, display search result
        Timeline debounceTimer = new Timeline(); 
        debounceTimer.setCycleCount(1);

        // This is used to ensure we only execute the search task when user stop typing for 500ms
        searchBar.setOnKeyReleased(_ -> {
            String query = searchBar.getText();
            if (query != null && !query.isEmpty() && !query.equals("")) {
                // Reset and schedule the debounce timer
                debounceTimer.stop(); // Stop ongoing timer
                debounceTimer.getKeyFrames().clear(); // Clear existing keyframes

                // Add a new delayed task
                debounceTimer.getKeyFrames().add(new KeyFrame(Duration.millis(500), _ -> {
                    // SEARCH OPERATION HERE...
                    // Show results in result page
                    loadNewContent("diary-search-result", query.trim());
                }));

                debounceTimer.play(); // Start the timer
            } else {
                debounceTimer.stop(); // Stop  
                goBackToPreviousAnchorPane();
            }
        });
    }

    /***
     * METHOD TO LOAD NEW CONTENT INTO THE ROOTPANE
     * - MEANS CHANGE PAGE.
     * 
     ***/
    public void loadNewContent(String filename) {
        try {
            // Save the current file name to history
            if(currentFilename != null){
                navigationHistory.push(currentFilename);
            }

            // Load the new content from the FXML file
            FXMLLoader loader = new FXMLLoader(App.class.getResource(filename + ".fxml"));
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
            currentFilename = filename;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /***
     * METHOD TO LOAD NEW CONTENT AFTER SEARCH
     * 
     ***/
    public void loadNewContent(String filename, String searchQuery) {
        try {
            // Save the current file name to history
            if(currentFilename != null){
                navigationHistory.push(currentFilename);
            }

            // Load the new content from the FXML file
            FXMLLoader loader = new FXMLLoader(App.class.getResource(filename + ".fxml"));
            AnchorPane newContent = loader.load();

            // Get the controller of the loaded FXML
            Object controller = loader.getController();

            // Check if the controller is an instance of SharedPaneCharacteristics and set
            // the MainMenuController
            if (controller instanceof SharedPaneCharacteristics) {
                SharedPaneCharacteristics sharedController = (SharedPaneCharacteristics) controller;
                sharedController.setMainMenuController(this); // Pass reference to controller
            }

            //if the controller is SearchResultController, pass the query to it
            if (controller instanceof SharedPaneCharacteristics) {
                SearchResultController searchResultController = (SearchResultController) controller;
                searchResultController.initialize(searchQuery);
            }

            // Replace the children of rootPane with the new content
            rootPane.getChildren().setAll(newContent.getChildren());
            currentFilename = filename;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /***
     * METHOD TO RELOAD THE CONTENT INTO THE ROOTPANE
     * 
     ***/
    public void reloadContent(String filename) {
        try {
            // Load the new content from the FXML file
            FXMLLoader loader = new FXMLLoader(App.class.getResource(filename + ".fxml"));
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
        if (!navigationHistory.isEmpty()) {
            // Get the previous AnchorPane state
            String previousState = navigationHistory.pop();

            // Restore the previous state to the rootPane
            reloadContent(previousState);
            currentFilename = previousState;
        } else {
            System.out.println("No previous state to go back to.");
        }
    }
}