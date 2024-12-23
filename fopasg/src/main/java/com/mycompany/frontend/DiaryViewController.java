package com.mycompany.frontend;

import java.util.ArrayList;
import java.util.List;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;

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
    private TextArea contents;

    @FXML
    private TextField wordCount;

    @FXML
    private TextField charCount;

    @FXML
    private FlowPane images;

    /***
     * VARIABLES.
     * 
     ***/
    private StringBuilder contentsString;

    /***
     * INITILIZATION OF THE CONTROLLER.
     * 
     ***/
    public void initialize() {
        
        super.initialize();

        // If there is some contents
        if (!contents.getText().isEmpty()) {
            contentsString = new StringBuilder(contents.getText().trim());
            countWords(contentsString);
            countCharacter(contentsString);
        }
        // If there is empty
        else {
            wordCount.setText(String.valueOf(0));
            charCount.setText(String.valueOf(0));
        }

        // Use to display the images
        displayImages();

    }

    /***
     * METHOD TO COUNT THE WORDS AND UPDATE THE VALUE OF WORDCOUNT IN UI.
     * 
     ***/
    private void countWords(StringBuilder str) {
        String[] words = str.toString().split("([\\W\\s]+)");
        wordCount.setText(String.valueOf(words.length));
    }

    /***
     * METHOD TO COUNT THE CHARACTERS AND UPDATE THE VALUE OF CHARCOUNT IN UI.
     * 
     ***/
    private void countCharacter(StringBuilder str) {
        str = new StringBuilder(str.toString().replaceAll(" ", "").trim());
        charCount.setText(String.valueOf(str.length()));
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
            // Create an Image object from the path
            Image image = new Image(path);
            
            // Create an ImageView for the image
            ImageView imageView = new ImageView(image);
            
            // Optionally set properties like fit width/height
            imageView.setFitWidth(100);  // Example width
            imageView.setFitHeight(100); // Example height
            imageView.setPreserveRatio(true);  // Maintain aspect ratio
            
            // Add ImageView to FlowPane
            images.getChildren().add(imageView);
        }
    }

}
