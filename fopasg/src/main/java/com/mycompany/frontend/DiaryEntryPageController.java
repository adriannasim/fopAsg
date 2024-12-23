package com.mycompany.frontend;

import java.util.ArrayList;
import java.util.List;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.FlowPane;
import javafx.scene.image.ImageView;

/***
 * THIS CONTROLLER CLASS IS USED FOR diary-entry-page.fxml
 * 
 ***/

public class DiaryEntryPageController extends SharedPaneCharacteristics {

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

    @FXML
    private Button uploadImageBtn;

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

        // Add on key press event to the textarea named 'contents'
        contents.setOnKeyTyped(event -> {
            // If there is some contents
            if (!contents.getText().isEmpty()) {
                contentsString = new StringBuilder(contents.getText().trim());
                if (contentsString.length() > 0) {
                    countWords(contentsString);
                    countCharacter(contentsString);
                }
            }
            // If there is empty
            else {
                wordCount.setText("0");
                charCount.setText("0");
            }

        });

        uploadImageBtn.setOnMouseClicked(event -> {
            // Use to display the images
            displayImages();
        });

    }

    /***
     * METHOD TO COUNT THE WORDS AND UPDATE THE VALUE OF WORDCOUNT IN UI.
     * 
     ***/
    private void countWords(StringBuilder str) {
        if (str != null && str.length() > 0) {
            String[] words = str.toString().split("([\\W\\s]+)");
            wordCount.setText(String.valueOf(words.length));
        } else {
            wordCount.setText("0");
        }
    }

    /***
     * METHOD TO COUNT THE CHARACTERS AND UPDATE THE VALUE OF CHARCOUNT IN UI.
     * 
     ***/
    private void countCharacter(StringBuilder str) {
        if (str != null && str.length() > 0) {
            str = new StringBuilder(str.toString().replaceAll(" ", "").trim());
            charCount.setText(String.valueOf(str.length()));
        } else {
            charCount.setText("0");
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
        imagePaths.add(getClass().getResource("/com/mycompany/frontend/images/test-img.jpg").toString());

        // Clear existing children
        images.getChildren().clear();

        // Iterate over each image path
        for (String path : imagePaths) {
            // Create an Image object from the path
            Image image = new Image(path);

            // Create an ImageView for the image
            ImageView imageView = new ImageView(image);

            // Optionally set properties like fit width/height
            imageView.setFitWidth(100); // Example width
            imageView.setFitHeight(100); // Example height
            imageView.setPreserveRatio(true); // Maintain aspect ratio

            // Add ImageView to FlowPane
            images.getChildren().add(imageView);
        }
    }

}
