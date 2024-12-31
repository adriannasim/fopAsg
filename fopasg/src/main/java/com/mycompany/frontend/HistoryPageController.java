package com.mycompany.frontend;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import com.mycompany.backend.DiaryService;
import com.mycompany.backend.UserSession;
import com.mycompany.backend.Diary;

/***
 * THIS CONTROLLER CLASS IS USED FOR diary-history-page.fxml
 * 
 ***/

public class HistoryPageController extends SharedPaneCharacteristics {

    /***
     * ELEMENTS WITH FX:ID
     * 
     ***/
    @FXML
    private Pane historyPane; // Largest Pane (Cover everything)

    @FXML
    private VBox diaryItemsVBox; // Each diary item VBox

    @FXML
    private Button exportButton; // Export button

    @FXML
    private Pane exportOptions; // Export option menu 1

    @FXML
    private Button basedOnDateRange; // Export option menu 1 item 1

    @FXML
    private Button basedOnPickedEntries; // Export option menu 1 item 2

    @FXML
    private Pane exportOptions2; // Export option menu 2 under date range

    @FXML
    private Button basedOnDay; // Export option menu 2 item 1

    @FXML
    private Button basedOnWeek; // Export option menu 2 item 2

    @FXML
    private Button basedOnMonth; // Export option menu 2 item 3

    // A class representing a diary group by date (CHANGE
    // LATER!!!!!!!!!!!!!!!!!!!!!)
    class DiaryGroup {
        private LocalDate date;
        private List<Diary> diaries = new ArrayList<Diary>();

        public DiaryGroup(LocalDate date) {
            this.date = date;
            this.diaries = null;
        }

        public DiaryGroup(LocalDate date, List<Diary> diaries) {
            this.date = date;
            this.diaries = diaries;
        }

        public DiaryGroup(LocalDate date, Diary diary) {
            this.date = date;
            this.diaries.add(diary);
        }

        public LocalDate getDate() {
            return date;
        }

        public List<Diary> getDiaries() {
            return diaries;
        }
    }

    /*** INITILIZATION OF THE CONTROLLER
     * 
     * ***/
    @FXML
    public void initialize() {
        // Inherit Super Class's initialization
        super.initialize(); 

        /*** Export button click action
        * 
        * ***/
        exportButton.setOnMouseClicked(e -> {
            exportOptions.setVisible(true); 
            exportOptions2.setVisible(false);
        });

        // If user click outside and not export options, close the export options menus
        historyPane.setOnMouseClicked(e -> {
            if (exportOptions.isVisible()) {
                if (!exportOptions.getBoundsInParent().contains(e.getX(), e.getY()) &&
                        !exportButton.getBoundsInParent().contains(e.getX(), e.getY())) {
                    exportOptions.setVisible(false);
                }
            }
            if (exportOptions2.isVisible()) {
                if (!exportOptions2.getBoundsInParent().contains(e.getX(), e.getY()) &&
                        !exportButton.getBoundsInParent().contains(e.getX(), e.getY())) {
                    exportOptions2.setVisible(false);
                }
            }
        });

        // When user chosen to pick entries to export
        basedOnPickedEntries.setOnMouseClicked(e->{
            exportOptions.setVisible(false);
            // Operation here
        });

        // When user chosen to pick by date range to export
        basedOnDateRange.setOnMouseClicked(e -> {
            exportOptions2.setVisible(true);
            exportOptions.setVisible(false);
            // Operation here
        });

        // When user want export by day
        basedOnDay.setOnMouseClicked(e->{
            mainMenuController.loadNewContent("export-by-day");
            exportOptions2.setVisible(false);
        });

        // When user want export by week
        basedOnWeek.setOnMouseClicked(e->{
            mainMenuController.loadNewContent("export-by-week");
            exportOptions2.setVisible(false);
        });

        // When user want export by month
        basedOnMonth.setOnMouseClicked(e->{
            mainMenuController.loadNewContent("export-by-month");
            exportOptions2.setVisible(false);
        });



        // Get user session
        String sessionUsername = UserSession.getSession().getUsername();

        // Get user diary
        DiaryService diaryService = new DiaryService(sessionUsername);
        List<Diary> diaryList = diaryService.getAllDiary();

        // Group the diary based on date
        List<DiaryGroup> groupedDiaryItems = new ArrayList<>();

        for (Diary diary:diaryList){
            if (groupedDiaryItems.isEmpty()){
                groupedDiaryItems.add(new DiaryGroup(diary.getDiaryDate().toLocalDate(), diary));
            } else {
                boolean foundDate = false;
                int index = 0;
                
                for (int i=0; i<groupedDiaryItems.size(); i++){
                    LocalDate date = diary.getDiaryDate().toLocalDate();
                    if (groupedDiaryItems.get(i).getDate().equals(date)){
                        foundDate = true;
                        index = i;
                    }  
                }   

                if (!foundDate){
                    groupedDiaryItems.add(new DiaryGroup(diary.getDiaryDate().toLocalDate(), diary));
                } else {
                    groupedDiaryItems.get(index).getDiaries().add(diary);
                }
            }
            
        }
        

        
        
                                
        // Date Format                        
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d MMM yyyy");

        // If there is no diary entry
        if(diaryList.isEmpty()){
            Text emptyMsg = new Text();
            emptyMsg.setText("No diary entry is present.");
            diaryItemsVBox.getChildren().add(emptyMsg);
        }

        // Iterate through groups and add diary items
        // 1. 'Group' means the entries grouped by date, eg. 1 Dec 2024 got 2 entries written, 
        //     thus, the two entries are grouped together under the same date.
        // 2. 'Diary Item' means the box for each entry.
        for (DiaryGroup group : groupedDiaryItems) {

            // Create a VBox for the Group
            VBox groupBox = new VBox();
            groupBox.setSpacing(5);
            groupBox.setStyle("-fx-background-color: #ffffff; -fx-padding: 10;");

            // Add a label for the date
            Label dateLabel = new Label(group.getDate().format(formatter)); // Format the date
            dateLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: #8F8F8F;");
            dateLabel.setLayoutX(33.0);
            dateLabel.setLayoutY(107.0);
            groupBox.getChildren().add(dateLabel);

            FlowPane diaryItemsFlowPane = new FlowPane();
            diaryItemsFlowPane.setHgap(10);
            diaryItemsFlowPane.setVgap(10);
            diaryItemsFlowPane.setPrefWrapLength(600);

            // Add diary items to the FlowPane
            for (Diary item : group.getDiaries()) {
                Pane diaryItemPane = createDiaryItemPane(item); // Create a pane for each diary item
                diaryItemsFlowPane.getChildren().add(diaryItemPane); // Add the diary item to the FlowPane
            }

            // Add the FlowPane with diary items to the VBox
            groupBox.getChildren().add(diaryItemsFlowPane);

            // Add the VBox to the main FlowPane
            diaryItemsVBox.getChildren().add(groupBox);
        }
    }

    /***
     * METHOD TO CREATE DIARYITEMPANE FOR EACH OF THE ENTRIES
     * 
     ***/
    private Pane createDiaryItemPane(Diary item) {
        Pane pane = new Pane();
        pane.setPrefSize(190.0, 65.0);
        pane.setStyle("-fx-background-color: #F1F1F1;");

        // Image Icon
        ImageView imageView = new ImageView(
                new Image(getClass().getResourceAsStream("/com/mycompany/frontend/images/diary-icon.png")));
        imageView.setFitHeight(35.0);
        imageView.setFitWidth(42.0);
        imageView.setLayoutX(14.0);
        imageView.setLayoutY(15.0);

        // Title Label
        Label titleLabel = new Label(item.getDiaryTitle()); // Name put here
        titleLabel.setLayoutX(74.0);
        titleLabel.setLayoutY(23.0);
        titleLabel.setStyle("-fx-background-color: #F1F1F1; -fx-max-width: 100px; -fx-wrap-text: false; overflow: hidden; text-overflow: ellipsis; display: inline-block;");
        titleLabel.setTextFill(javafx.scene.paint.Color.web("#9abf80"));
        titleLabel.setFont(javafx.scene.text.Font.font("Roboto Bold", 15));

        // Hover Pane (Initially Hidden)
        Pane hoverPane = new Pane();
        hoverPane.setPrefSize(190.0, 65.0);
        hoverPane.setStyle("-fx-background-color: rgba(30,30,30,0.7); visibility: hidden;");

        // Hover Icons
        ImageView viewIcon = new ImageView(
                new Image(getClass().getResource("/com/mycompany/frontend/images/view-icon.png").toString()));
        viewIcon.setLayoutX(168.0);
        viewIcon.setLayoutY(7.0);
        viewIcon.setFitHeight(14.0);
        viewIcon.setFitWidth(14.0);
        viewIcon.setStyle("-fx-cursor:HAND");
        hoverPane.getChildren().add(viewIcon);

        ImageView editIcon = new ImageView(
                new Image(getClass().getResource("/com/mycompany/frontend/images/edit-icon.png").toString()));
        editIcon.setLayoutX(168.0);
        editIcon.setLayoutY(25.0);
        editIcon.setFitHeight(14.0);
        editIcon.setFitWidth(14.0);
        editIcon.setStyle("-fx-cursor:HAND");
        hoverPane.getChildren().add(editIcon);

        ImageView deleteIcon = new ImageView(
                new Image(getClass().getResource("/com/mycompany/frontend/images/delete-icon.png").toString()));
        deleteIcon.setLayoutX(168.0);
        deleteIcon.setLayoutY(43.0);
        deleteIcon.setFitHeight(14.0);
        deleteIcon.setFitWidth(14.0);
        deleteIcon.setStyle("-fx-cursor:HAND");
        hoverPane.getChildren().add(deleteIcon);

        // Add components to the pane
        pane.getChildren().addAll(imageView, titleLabel, hoverPane);

        // Hover effect for dynamically created pane
        pane.setOnMouseEntered(
                e -> hoverPane.setStyle("-fx-background-color: rgba(30,30,30,0.7); visibility: visible;"));
        pane.setOnMouseExited(e -> hoverPane.setStyle("-fx-background-color: rgba(30,30,30,0.7); visibility: hidden;"));

        // Event handler for edit icon
        editIcon.setOnMouseClicked(e -> {
            // Perform the edit action here
            handleEdit(item);
        });

        // Event handler for delete icon
        deleteIcon.setOnMouseClicked(e -> {
            // Perform the delete action here
            handleDelete(item);
        });

        // Event handler for view icon
        viewIcon.setOnMouseClicked(e -> {
            // Perform the view action here
            handleView(item);
        });

        // Here used to handle entries selection when user want to export into PDF
        pane.setOnMouseClicked(e -> {
            if (pane.getStyle().contains("-fx-border-color: #6A669D;")) {
                // Unselect
                pane.setStyle("-fx-background-color: #F1F1F1; -fx-border-color: transparent;");
                // Add logic here...
            } else {
                // Select
                pane.setStyle("-fx-background-color: #F1F1F1; -fx-border-color: #6A669D; -fx-border-width: 2px;");
                // Add logic here...
            }
        });

        return pane;
    }

    /***
     * METHOD TO HANDLE THE DELETE ACTION
     * 
     ***/
    private void handleDelete(Diary diary) {
        // Show a delete confirmation page
        // try {
        // App.openConfirmationPopUp("Are you sure you want to delete this entry?",
        // "Entry has been deleted.", "Entry failed to delete.");
        // } catch (IOException ex) {
        // ex.printStackTrace();
        // }
    }

    /***
     * METHOD TO HANDLE THE EDIT ACTION
     * 
     ***/
    private void handleEdit(Diary diary) {
        // Set current diary to refer
        UserSession.getSession().setCurrentDiary(diary);
        // Show a edit page
        mainMenuController.loadNewContent("diary-entry-page");
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
