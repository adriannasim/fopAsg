package com.mycompany.frontend;

import java.io.IOException;
import java.util.List;

import com.mycompany.backend.Diary;
import com.mycompany.backend.DiaryService;
import com.mycompany.backend.UserSession;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;

/*** 
 * THIS CONTROLLER CLASS IS USED FOR diary-history-page.fxml 
 * 
 ***/

public class SearchResultController extends SharedPaneCharacteristics{

    /*** ELEMENTS WITH FX:ID  
     * 
     * ***/
    @FXML
    private Pane resultPane; // Largest Pane (Cover everything)

    @FXML
    private FlowPane diaryItemsFlowPane; // Each diary item Flow Pane

    /***
     * VARIABLES
     * 
     ***/
    private DiaryService diaryService = new DiaryService(UserSession.getSession().getUsername());

    /*** INITILIZATION OF THE CONTROLLER
     * 
     * ***/
    @FXML
    public void initialize(String searchQuery) {

        // Inherit Super Class's initialization
        super.initialize(); 

        diaryItemsFlowPane.setHgap(10);
        diaryItemsFlowPane.setVgap(10);

        //If no searchQuery, list all diaries
        List<Diary> diaries;
        if (searchQuery.equals("") || searchQuery == null)
        {
            diaries = diaryService.getAllDiary();
        }
        //else search diaries by title using the query given
        else
        {
            diaries = diaryService.searchDiariesByTitle(searchQuery);
        }
                  
        // Loop through the list and create a Pane for each diary item
        for (Diary diary : diaries) {
            Pane diaryItemPane = createDiaryItemPane(diary);
            diaryItemsFlowPane.getChildren().add(diaryItemPane); // Add the pane to the FlowPane
        }
    }

    /*** METHOD TO CREATE DIARYITEMPANE FOR EACH OF THE ENTRIES
     * 
     * ***/
    private Pane createDiaryItemPane(Diary item) {
        Pane pane = new Pane();
        pane.setPrefSize(195.0, 65.0);
        pane.setStyle("-fx-background-color: #F1F1F1;");

        // Image
        ImageView imageView = new ImageView(
                new Image(getClass().getResourceAsStream("/com/mycompany/frontend/images/diary-icon.png")));
        imageView.setFitHeight(35.0);
        imageView.setFitWidth(42.0);
        imageView.setLayoutX(14.0);
        imageView.setLayoutY(15.0);

        // Title Label
        Label titleLabel = new Label(item.getDiaryTitle());
        titleLabel.setLayoutX(74.0);
        titleLabel.setLayoutY(23.0);
        titleLabel.setStyle("-fx-background-color: #F1F1F1;");
        titleLabel.setTextFill(javafx.scene.paint.Color.web("#9abf80"));
        titleLabel.setFont(javafx.scene.text.Font.font("Roboto Bold", 15));

        // Date Label
        Label dateLabel = new Label(item.getDiaryDate().toLocalDate().toString());
        dateLabel.setLayoutX(120.0);
        dateLabel.setLayoutY(10.0);
        dateLabel.setStyle("-fx-background-color: #F1F1F1;");
        dateLabel.setTextFill(javafx.scene.paint.Color.web("#8f8f8f"));
        dateLabel.setFont(javafx.scene.text.Font.font("Roboto Bold", 8));

        // Hover Pane (Initially Hidden)
        Pane hoverPane = new Pane();
        hoverPane.setPrefSize(195.0, 65.0);
        hoverPane.setStyle("-fx-background-color: rgba(30,30,30,0.7); visibility: hidden;");

        // Hover Icons
        ImageView viewIcon = new ImageView(
                new Image(getClass().getResource("/com/mycompany/frontend/images/view-icon.png").toString()));
        viewIcon.setLayoutX(173.0);
        viewIcon.setLayoutY(7.0);
        viewIcon.setFitHeight(14.0);
        viewIcon.setFitWidth(14.0);
        viewIcon.setStyle("-fx-cursor:HAND");
        hoverPane.getChildren().add(viewIcon);

        ImageView editIcon = new ImageView(
                new Image(getClass().getResource("/com/mycompany/frontend/images/edit-icon.png").toString()));
        editIcon.setLayoutX(173.0);
        editIcon.setLayoutY(25.0);
        editIcon.setFitHeight(14.0);
        editIcon.setFitWidth(14.0);
        editIcon.setStyle("-fx-cursor:HAND");
        hoverPane.getChildren().add(editIcon);

        ImageView deleteIcon = new ImageView(
                new Image(getClass().getResource("/com/mycompany/frontend/images/delete-icon.png").toString()));
        deleteIcon.setLayoutX(173.0);
        deleteIcon.setLayoutY(43.0);
        deleteIcon.setFitHeight(14.0);
        deleteIcon.setFitWidth(14.0);
        deleteIcon.setStyle("-fx-cursor:HAND");
        hoverPane.getChildren().add(deleteIcon);

        // Add components to the pane
        pane.getChildren().addAll(imageView, titleLabel, dateLabel, hoverPane);

        // Hover effect for dynamically created pane
        pane.setOnMouseEntered(
                e -> hoverPane.setStyle("-fx-background-color: rgba(30,30,30,0.7); visibility: visible;"));
        pane.setOnMouseExited(e -> hoverPane.setStyle("-fx-background-color: rgba(30,30,30,0.7); visibility: hidden;"));

        // Event handler for edit icon
        editIcon.setOnMouseClicked(e -> {
            // Perform the edit action here 
            handleEdit();
        });

        // Event handler for delete icon
        deleteIcon.setOnMouseClicked(e -> {
            // Perform the delete action here
            handleDelete();
        });

        // Event handler for view icon
        viewIcon.setOnMouseClicked(e -> {
            // Perform the view action here 
            handleView(); 
        });

        return pane;
    }

    /*** METHOD TO HANDLE THE DELETE ACTION
     * 
     * ***/
    private void handleDelete() {
        // Show a delete confirmation page
        try {
            App.openConfirmationPopUp("Confirm to delete this entry?",
                () -> diaryService.moveEntryToBin(UserSession.getSession().getCurrentDiary())); 

            mainMenuController.reloadContent("diary-search-result");
            //App.openConfirmationPopUp("Are you sure you want to delete this entry?", "Entry has been deleted.", "Entry failed to delete.");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    /*** METHOD TO HANDLE THE EDIT ACTION
     * 
     * ***/
    private void handleEdit() {
        // Show a edit page
        mainMenuController.loadNewContent("diary-entry-page");
    }

    /*** METHOD TO HANDLE THE VIEW ACTION
     * 
     * ***/
    private void handleView() {
        // Show a view page
        mainMenuController.loadNewContent("diary-view-page");
    }

}
