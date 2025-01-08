package com.mycompany.frontend;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.time.format.DateTimeFormatter;
import java.util.List;

import com.gluonhq.richtextarea.RichTextArea;
import com.gluonhq.richtextarea.model.Document;
import com.mycompany.backend.Diary;
import com.mycompany.backend.FileIO;
import com.mycompany.backend.UserSession;

import javafx.fxml.FXML;
import javafx.event.ActionEvent;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;

/***
 * THIS CONTROLLER CLASS IS USED FOR diary-view-page.fxml
 * 
 ***/

public class DiaryViewController extends SharedPaneCharacteristics {

    /***
     * ELEMENTS WITH FX:ID.
     * 
     ***/
    @FXML
    private Label date; // Used to display the current date

    @FXML
    private Label day; // Used to display the current day of week

    @FXML
    private Label time; // used to display the current time

    @FXML
    private ImageView moodIcon; // used to display the selected mood icon

    @FXML
    private Label moodLabel; // Used to display the selected mood label

    @FXML
    private TextField title; // Used to display the title

    @FXML
    private TextField wordCount; // Used to display the word count of the content

    @FXML
    private TextField charCount; // Used to display the character count of the content

    @FXML
    private FlowPane images; // Used to display the images uploaded by users

    @FXML
    private Pane textarea; // Used to hold the rich text area

    /***
     * VARIABLES.
     * 
     ***/
    // Initialize the place user view their diary content
    private final RichTextArea viewer = new RichTextArea();
    FileIO fileIO;

    private String mood;

    /***
     * INITILIZATION OF THE CONTROLLER.
     * 
     ***/
    public void initialize() {
        super.initialize();

        //fileIO to load file
        fileIO = new FileIO();

        // Place the viewer (rich text area) into Pane textarea
        textarea.getChildren().add(viewer);

        // Set the viewer same width and height with the textarea container
        viewer.prefWidthProperty().bind(textarea.widthProperty());
        viewer.prefHeightProperty().bind(textarea.heightProperty());

        // Set viewer view-only
        viewer.setEditable(false);

        // Set current diary to refer
        Diary diary = UserSession.getSession().getCurrentDiary();

        String username = UserSession.getSession().getUsername();
        
        // Get the mood
        mood = diary.getMood().toString();

        // Set the mood
        setMoodLabelAndIcon();

        // Get the images
        //List<File> selectedImageFilesPath = diary.getImagePaths().stream().filter(path -> path != null && !path.equals("null")).map(File::new).collect(Collectors.toList());
        try
        {
            List<File> selectedImageFilesPath = (fileIO.loadFiles("images/" + username, diary.getDiaryId()));

            // Display the images
            displayImages(selectedImageFilesPath);
        }
        catch (IOException ex)
        {
            ex.printStackTrace();
        }
        catch (URISyntaxException ex)
        {
            ex.printStackTrace();
        }

        // Set title
        title.setText(diary.getDiaryTitle());

        // Import the content from CSV file (CAN CHANGE OR MODIFY)
        try {
            viewer.getActionFactory().open(RichTextCSVExporter.importFromCSV(diary.getDiaryContent()))
                    .execute(new ActionEvent());
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        // Set Date
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd MMMM yyyy");
        date.setText(diary.getDiaryDate().format(dateFormatter));

        // Set Day of Week
        String formattedDay = diary.getDiaryDate().getDayOfWeek().toString().toLowerCase();
        formattedDay = Character.toUpperCase(formattedDay.charAt(0))
                + formattedDay.substring(1);
        day.setText(formattedDay);

        // Set time
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("hh:mm a");
        time.setText(diary.getDiaryDate().format(timeFormatter));

        // Listen to the document change event for viewer
        viewer.documentProperty().addListener((_, _, nv) -> {
            // If there is a document for viewer
            if (nv != null) {
                // Set character count
                charCount.setText(String.valueOf(viewer.getTextLength()));
                // Set word count
                countWords(viewer.getDocument());
            }
        });
    }

    /***
     * METHOD TO COUNT THE WORDS AND UPDATE THE VALUE OF WORDCOUNT IN UI.
     * 
     ***/
    private void countWords(Document document) {
        if (document != null) {
            String text = document.getText();
            if (text != null && text.length() > 0) {
                String[] words = text.toString().split("([\\W\\s]+)");
                wordCount.setText(String.valueOf(words.length));
            } else {
                wordCount.setText("0");
            }
        }
    }

    /***
     * METHOD TO DISPLAY THE IMAGES IN UI.
     * 
     ***/
     public void displayImages(List<File> imageFiles) {
        // Clear existing children
        images.getChildren().clear();

        // Iterate over each image path
        for (File file : imageFiles) {
            // Create an ImageView from the path
            ImageView imageView = new ImageView(new Image(file.toURI().toString()));

            // Image settings
            imageView.setFitWidth(100);
            imageView.setFitHeight(100);
            imageView.setPreserveRatio(true);
            imageView.setStyle("-fx-cursor: HAND;");

            double maxHeight = imageView.getFitHeight();

            // Open image pop up view
            imageView.setOnMouseClicked(_ -> {
                    try {
                            App.openPopUpImg("pop-up-img", new Image(file.toURI().toString()), maxHeight);
                    } catch (IOException ex) {
                            ex.printStackTrace();
                    }
            });

            // Add ImageView to the container
            images.getChildren().add(imageView);
        }
    }


    /***
     * METHOD TO SET MOOD LABEL AND ICON
     * 
     ***/
    private void setMoodLabelAndIcon() {
        // Set the mood
        moodLabel.setText(mood);
        switch (mood) {
            case "HAPPY":
                moodIcon.setImage(new Image(getClass()
                        .getResourceAsStream(
                                "/com/mycompany/frontend/images/Happy-hover (mood).png")));
                break;
            case "NORMAL":
                moodIcon.setImage(new Image(getClass()
                        .getResourceAsStream(
                                "/com/mycompany/frontend/images/Neutral-hover (mood).png")));
                break;
            case "SAD":
                moodIcon.setImage(new Image(getClass()
                        .getResourceAsStream(
                                "/com/mycompany/frontend/images/Sad-hover (mood).png")));
                break;
        }
    }

}
