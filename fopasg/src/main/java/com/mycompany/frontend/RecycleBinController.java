package com.mycompany.frontend;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.scene.image.ImageView;
import com.mycompany.backend.Diary;
import com.mycompany.backend.DiaryService;
import com.mycompany.backend.UserSession;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

/***
 * THIS CONTROLLER CLASS IS USED FOR diary-recycle-bin.fxml
 * 
 ***/

public class RecycleBinController extends SharedPaneCharacteristics {

    /***
     * ELEMENTS WITH FX:ID
     * 
     ***/
    @FXML
    private FlowPane diariesFlowPane; // Reference to the FlowPane in diary-recycle-bin.fxml

    /***
     * VARIABLES
     * 
     ***/
    private DiaryService diaryService = new DiaryService(UserSession.getSession().getUsername());

    /***
     * INITILIZATION OF THE CONTROLLER
     * 
     ***/
    @FXML
    public void initialize() {
        // Inherit Super Class's initialize()
        super.initialize();

        // Get user session
        String sessionUsername = UserSession.getSession().getUsername();

        // Get user diary
        DiaryService diaryService = new DiaryService(sessionUsername);
        List<Diary> diaries = diaryService.getAllDiary();
        // filter deleted diries
        diaries = diaries.stream().filter(diary -> diary.getDeletionDate() != null).collect(Collectors.toList());

        // If there is no diary entry
        if (diaries.isEmpty()) {
            Text emptyMsg = new Text();
            emptyMsg.setText("No diary entry is present.");
            diariesFlowPane.getChildren().add(emptyMsg);
        }


        // Loop through the list and create a Pane for each diary item
        for (Diary item : diaries) {
            Pane diaryPane = createDiaryPane(item);
            diariesFlowPane.getChildren().add(diaryPane); // Add the pane to the FlowPane
        }
    }

    /***
     * METHOD TO CREATE DIARYITEMPANE FOR EACH OF THE ENTRIES
     * 
     ***/
    private Pane createDiaryPane(Diary diary) {
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
        Label titleLabel = new Label(diary.getDiaryTitle());
        titleLabel.setLayoutX(74.0);
        titleLabel.setLayoutY(23.0);
        titleLabel.setStyle("-fx-background-color: #F1F1F1;");
        titleLabel.setTextFill(javafx.scene.paint.Color.web("#9abf80"));
        titleLabel.setFont(javafx.scene.text.Font.font("Roboto Bold", 15));

        // Days Left Label
        Label daysLeftLabel = new Label(
                (30 - ChronoUnit.DAYS.between(diary.getDeletionDate(), LocalDate.now())) + " day(s) left");
        daysLeftLabel.setLayoutX(140.0);
        daysLeftLabel.setLayoutY(45.0);
        daysLeftLabel.setStyle("-fx-background-color: #F1F1F1;");
        daysLeftLabel.setTextFill(javafx.scene.paint.Color.web("#1c325b"));
        daysLeftLabel.setFont(javafx.scene.text.Font.font("Roboto Bold", 8));

        // Date Label
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd MMMM yyyy");
        Label dateLabel = new Label(diary.getDiaryDate().format(dateFormatter));
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

        ImageView restoreIcon = new ImageView(
                new Image(getClass().getResource("/com/mycompany/frontend/images/restore-icon.png").toString()));
        restoreIcon.setLayoutX(173.0);
        restoreIcon.setLayoutY(25.0);
        restoreIcon.setFitHeight(14.0);
        restoreIcon.setFitWidth(14.0);
        restoreIcon.setStyle("-fx-cursor:HAND");
        hoverPane.getChildren().add(restoreIcon);

        ImageView deleteIcon = new ImageView(
                new Image(getClass().getResource("/com/mycompany/frontend/images/delete-icon.png").toString()));
        deleteIcon.setLayoutX(173.0);
        deleteIcon.setLayoutY(43.0);
        deleteIcon.setFitHeight(14.0);
        deleteIcon.setFitWidth(14.0);
        deleteIcon.setStyle("-fx-cursor:HAND");
        hoverPane.getChildren().add(deleteIcon);

        // Add components to the pane
        pane.getChildren().addAll(imageView, titleLabel, daysLeftLabel, dateLabel, hoverPane);

        // Hover effect for dynamically created pane
        pane.setOnMouseEntered(
                e -> hoverPane.setStyle("-fx-background-color: rgba(30,30,30,0.7); visibility: visible;"));
        pane.setOnMouseExited(e -> hoverPane.setStyle("-fx-background-color: rgba(30,30,30,0.7); visibility: hidden;"));

        // Event handler for restore icon
        restoreIcon.setOnMouseClicked(e -> {
            // Perform the restore action here
            handleRestore(diary);
        });

        // Event handler for delete icon
        deleteIcon.setOnMouseClicked(e -> {
            // Perform the delete action here
            handleDelete(diary);
        });

        // Event handler for view icon
        viewIcon.setOnMouseClicked(e -> {
            // Perform the view action here
            handleView(diary);
        });

        return pane;
    }

    /***
     * METHOD TO HANDLE THE DELETE ACTION
     * 
     ***/
    private void handleDelete(Diary diary) {
        // Set current diary to refer
        UserSession.getSession().setCurrentDiary(diary);
        try {
            // show a confimation pop up
            App.openConfirmationPopUp("Confirm to delete this entry permanently?",
                    () -> diaryService.deleteDiaryEntry(diary.getDiaryId()));
            mainMenuController.reloadContent("diary-recycle-bin");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    /***
     * METHOD TO HANDLE THE RESTORE ACTION
     * 
     ***/
    private void handleRestore(Diary diary) {
        // Set current diary to refer
        UserSession.getSession().setCurrentDiary(diary);
        try {
            // show a confimation pop up
            App.openConfirmationPopUp("Confirm to retore this entry?",
                    () -> diaryService.restoreDiaryEntry(diary));
            mainMenuController.reloadContent("diary-recycle-bin");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    /***
     * METHOD TO HANDLE THE VIEW ACTION
     * 
     ***/
    private void handleView(Diary diary) {
        // Set current diary to refer
        UserSession.getSession().setCurrentDiary(diary);
        // Show a view page
        mainMenuController.loadNewContent("diary-view-page");
    }

}
