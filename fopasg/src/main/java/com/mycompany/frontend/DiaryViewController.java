package com.mycompany.frontend;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.gluonhq.richtextarea.RichTextArea;
import com.gluonhq.richtextarea.model.Document;

import javafx.fxml.FXML;
import javafx.application.Platform;
import javafx.event.ActionEvent;
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

    /***
     * INITILIZATION OF THE CONTROLLER.
     * 
     ***/
    public void initialize() {

        super.initialize();

        // Place the viewer (rich text area) into Pane textarea
        textarea.getChildren().add(viewer);
        // Set the viewer same width and height with the textarea container
        viewer.prefWidthProperty().bind(textarea.widthProperty());
        viewer.prefHeightProperty().bind(textarea.heightProperty());
        // Set viewer view-only
        viewer.setEditable(false);

        // Import the content from CSV file (CAN CHANGE OR MODIFY)
        try {
            viewer.getActionFactory().open(RichTextCSVExporter.importFromCSV("data.csv")).execute(new ActionEvent());
            ;
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        // Set character count
        charCount.setText(String.valueOf(viewer.getTextLength()));

        // Make sure the word count is initiated when the document is exists only
        Platform.runLater(() -> {
            Document linkedDocument = viewer.getDocument();
            if (linkedDocument != null) {
                // Update word count
                countWords();
            }
        });

        // Display the images uploaded by users
        displayImages();
    }

    /***
     * METHOD TO COUNT THE WORDS AND UPDATE THE VALUE OF WORDCOUNT IN UI.
     * 
     ***/
    private void countWords() {
        Document document = viewer.getDocument();
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
    public void displayImages() {

        // Sample for illustration purpose (MUST CHANGES !!!!!!!!!!!!!!)
        List<String> imagePaths = new ArrayList<>();
        imagePaths.add(getClass().getResource("/com/mycompany/frontend/images/test-img.jpg").toString());
        imagePaths.add(getClass().getResource("/com/mycompany/frontend/images/italic-icon.png").toString());

        // Clear existing children
        images.getChildren().clear();

        // Iterate over each image path
        for (String path : imagePaths) {
            // Create an ImageView from the path
            ImageView imageView = new ImageView(new Image(path));

            // Image settings
            imageView.setFitWidth(100);
            imageView.setFitHeight(100);
            imageView.setPreserveRatio(true);

            // Add ImageView to the container
            images.getChildren().add(imageView);
        }
    }

}
