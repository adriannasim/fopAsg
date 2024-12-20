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
import java.util.Arrays;

public class HistoryPageController {

    @FXML
    private Pane historyPane;

    @FXML
    private VBox diaryItemsVBox;

    @FXML
    private Button backButton;

    @FXML
    private Button exportButton;

    @FXML
    private Pane exportOptions;

    @FXML
    private Pane exportOptions2;

    @FXML
    private Button basedOnDateRange;

    @FXML
    private Button basedOnPickedEntries;

    @FXML
    private Button basedOnDay;

    @FXML
    private Button basedOnWeek;

    @FXML
    private Button basedOnMonth;

    // A class representing a diary item (Can Change Later)
    static class DiaryItem {
        private String name;

        public DiaryItem(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }

    // A class representing a diary group by date (Can Change Later)
    class DiaryGroup {
        private LocalDate date;
        private List<DiaryItem> diaryItems;

        public DiaryGroup(LocalDate date, List<DiaryItem> diaryItems) {
            this.date = date;
            this.diaryItems = diaryItems;
        }

        public LocalDate getDate() {
            return date;
        }

        public List<DiaryItem> getDiaryItems() {
            return diaryItems;
        }
    }

    @FXML
    public void initialize() {
        // Back button click action
        backButton.setOnAction(e -> {
            App.goBackToPreviousScene();
        });

        // Export button click action
        exportButton.setOnMouseClicked(e -> {
            exportOptions.setVisible(true);
            exportOptions2.setVisible(false);
        });

        historyPane.setOnMouseClicked(e -> {
            if (exportOptions.isVisible()) {
                // Check if the click is outside exportOptions
                if (!exportOptions.getBoundsInParent().contains(e.getX(), e.getY()) &&
                        !exportButton.getBoundsInParent().contains(e.getX(), e.getY())) {
                    exportOptions.setVisible(false);
                }
            }

            if (exportOptions2.isVisible()) {
                // Check if the click is outside exportOptions
                if (!exportOptions2.getBoundsInParent().contains(e.getX(), e.getY()) &&
                        !exportButton.getBoundsInParent().contains(e.getX(), e.getY())) {
                    exportOptions2.setVisible(false);
                }
            }
        });

        basedOnDateRange.setOnMouseClicked(e -> {
            exportOptions2.setVisible(true);
            exportOptions.setVisible(false);
        });

        // Sample data for diary items
        List<DiaryGroup> groupedDiaryItems = new ArrayList<>();
        groupedDiaryItems.add(
                new DiaryGroup(LocalDate.of(2024, 6, 10),
                        Arrays.asList(new DiaryItem("Diary 1"), new DiaryItem("Diary 2"))));
        groupedDiaryItems.add(
                new DiaryGroup(LocalDate.of(2024, 6, 11),
                        Arrays.asList(new DiaryItem("Diary 3"))));
        groupedDiaryItems.add(
                new DiaryGroup(LocalDate.of(2024, 5, 11),
                        Arrays.asList(new DiaryItem("Diary 3"), new DiaryItem("Diary 3"), new DiaryItem("Diary 3"),
                                new DiaryItem("Diary 3"))));
        groupedDiaryItems.add(
                new DiaryGroup(LocalDate.of(2024, 4, 11),
                        Arrays.asList(new DiaryItem("Diary 3"), new DiaryItem("Diary 3"), new DiaryItem("Diary 3"),
                                new DiaryItem("Diary 3"))));

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d MMMM yyyy");

        // Iterate through groups and add diary items
        for (DiaryGroup group : groupedDiaryItems) {

            // Create a VBox for the group
            VBox groupBox = new VBox();
            groupBox.setSpacing(10);
            groupBox.setStyle("-fx-background-color: #ffffff; -fx-padding: 10;");

            // Add a label for the date
            Label dateLabel = new Label(group.getDate().format(formatter)); // Format the date
            dateLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #8F8F8F;");
            dateLabel.setLayoutX(33.0);
            dateLabel.setLayoutY(107.0);
            groupBox.getChildren().add(dateLabel);

            FlowPane diaryItemsFlowPane = new FlowPane();
            diaryItemsFlowPane.setHgap(10);
            diaryItemsFlowPane.setVgap(10);
            diaryItemsFlowPane.setPrefWrapLength(600);

            // Add diary items to the FlowPane
            for (DiaryItem item : group.getDiaryItems()) {
                Pane diaryItemPane = createDiaryItemPane(item); // Create a pane for each diary item
                diaryItemsFlowPane.getChildren().add(diaryItemPane); // Add the diary item to the FlowPane
            }

            // Add the FlowPane with diary items to the VBox
            groupBox.getChildren().add(diaryItemsFlowPane);

            // Add the VBox to the main FlowPane
            diaryItemsVBox.getChildren().add(groupBox);
        }
    }

    private Pane createDiaryItemPane(DiaryItem item) {
        Pane pane = new Pane();
        pane.setPrefSize(190.0, 65.0);
        pane.setStyle("-fx-background-color: #F1F1F1;");

        // Image
        ImageView imageView = new ImageView(
                new Image(getClass().getResourceAsStream("/com/mycompany/frontend/images/diary-icon.png")));
        imageView.setFitHeight(35.0);
        imageView.setFitWidth(42.0);
        imageView.setLayoutX(14.0);
        imageView.setLayoutY(15.0);

        // Title Label
        Label titleLabel = new Label(item.name);
        titleLabel.setLayoutX(74.0);
        titleLabel.setLayoutY(23.0);
        titleLabel.setStyle("-fx-background-color: #F1F1F1;");
        titleLabel.setTextFill(javafx.scene.paint.Color.web("#9abf80"));
        titleLabel.setFont(javafx.scene.text.Font.font("Roboto Bold", 15));

        // Hover Pane (Initially Hidden)
        Pane hoverPane = new Pane();
        hoverPane.setPrefSize(190.0, 65.0);
        hoverPane.setStyle("-fx-background-color: rgba(30,30,30,0.7); visibility: hidden;");

        // Hover Icons
        ImageView viewIcon = new ImageView(
                new Image(getClass().getResource("/com/mycompany/frontend/images/view-icon.png").toString()));
        viewIcon.setLayoutX(170.0);
        viewIcon.setLayoutY(7.0);
        viewIcon.setFitHeight(14.0);
        viewIcon.setFitWidth(14.0);
        hoverPane.getChildren().add(viewIcon);

        ImageView editIcon = new ImageView(
                new Image(getClass().getResource("/com/mycompany/frontend/images/edit-icon.png").toString()));
        editIcon.setLayoutX(170.0);
        editIcon.setLayoutY(25.0);
        editIcon.setFitHeight(14.0);
        editIcon.setFitWidth(14.0);
        hoverPane.getChildren().add(editIcon);

        ImageView deleteIcon = new ImageView(
                new Image(getClass().getResource("/com/mycompany/frontend/images/delete-icon.png").toString()));
        deleteIcon.setLayoutX(170.0);
        deleteIcon.setLayoutY(43.0);
        deleteIcon.setFitHeight(14.0);
        deleteIcon.setFitWidth(14.0);
        hoverPane.getChildren().add(deleteIcon);

        // Add components to the pane
        pane.getChildren().addAll(imageView, titleLabel, hoverPane);

        // Hover effect for dynamically created pane
        pane.setOnMouseEntered(
                e -> hoverPane.setStyle("-fx-background-color: rgba(30,30,30,0.7); visibility: visible;"));
        pane.setOnMouseExited(e -> hoverPane.setStyle("-fx-background-color: rgba(30,30,30,0.7); visibility: hidden;"));

        // Event handler for restore icon
        editIcon.setOnMouseClicked(e -> {
            // Perform the restore action here
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

        // Here used to handle entries selection when user want to export into PDF
        pane.setOnMouseClicked(e -> {
            if (pane.getStyle().contains("-fx-border-color: #6A669D;")) {
                // Unselect
                pane.setStyle("-fx-background-color: #F1F1F1; -fx-border-color: transparent;");
            } else {
                // Select
                pane.setStyle("-fx-background-color: #F1F1F1; -fx-border-color: #6A669D; -fx-border-width: 2px;");
            }
        });

        return pane;
    }

    // Method to handle the delete action
    private void handleDelete() {
        // Show a delete confirmation page
        try {
            App.openConfirmationPopUp("Are you sure you want to delete this entry?");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    // Method to handle the edit action
    private void handleEdit() {
        // Show a edit page
        try {
            App.switchScene("diary-entry-page");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    // Method to handle the view action
    private void handleView() {
        // Show a view page
        try {
            App.switchScene("diary-view-page");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

}
