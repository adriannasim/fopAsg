package com.mycompany.frontend;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.mycompany.frontend.helper.TextSegment;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.layout.FlowPane;
import javafx.scene.text.TextFlow;
import javafx.scene.image.ImageView;
import org.fxmisc.richtext.StyleClassedTextArea;

/***
 * THIS CONTROLLER CLASS IS USED FOR diary-entry-page.fxml
 * 
 ***/

public class DiaryEntryPageController1 extends SharedPaneCharacteristics {

    /***
     * ELEMENTS WITH FX:ID.
     * 
     ***/
    @FXML
    private Pane textarea;

    private StyleClassedTextArea contents;

    @FXML
    private TextField wordCount;

    @FXML
    private TextField charCount;

    @FXML
    private FlowPane images;

    @FXML
    private Button uploadImageBtn;

    @FXML
    private ComboBox<String> fontSizeComboBox;

    /***
     * VARIABLES.
     * 
     ***/
    private StringBuilder contentsString;

    private StringBuilder selectedContents = new StringBuilder();

    private List<TextSegment> documentContent = new ArrayList<>();

    /***
     * INITILIZATION OF THE CONTROLLER.
     * 
     ***/
    public void initialize() {

        super.initialize();

        contents = new StyleClassedTextArea();
        contents.getStyleClass().add("text-area");

        // You can also set properties such as size, style, etc.
        contents.setPrefWidth(627);
        contents.setPrefHeight(217);

        // Add the StyleClassedTextArea to the root container
        textarea.getChildren().add(contents);

        // Initialize your content
        renderContent(contents);

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

        // When user want to add image, then will display the images
        uploadImageBtn.setOnMouseClicked(event -> {
            // Use to display the images
            displayImages();
        });

        // Set up the ComboBox with font sizes
        ObservableList<String> fontSizes = FXCollections.observableArrayList(
                "10", "12", "14", "16", "18", "20", "22", "24");
        fontSizeComboBox.setItems(fontSizes);

        // Handle selection changes
        fontSizeComboBox.setOnAction(event -> {
            String selectedSize = fontSizeComboBox.getValue();
            if (selectedSize != null) {
                applyFontSizeToSelection(selectedSize);
            }
        });

        // Listen for selection changes
        contents.selectedTextProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.isEmpty()) {
                selectedContents.setLength(0); // Clear existing content
                selectedContents.append(newValue); // Set the selected content
            } else {
                selectedContents.setLength(0); // Clear existing content
            }

            System.out.println(selectedContents);
        });

    }

    private void applyFontSizeToSelection(String fontSize) {
        if (selectedContents != null && !selectedContents.toString().isEmpty()) {
            // Format the selected text with the font size
            String styledText = String.format("<font size=\"%s\">%s</font>", fontSize, selectedContents);

            // Get the start index of the substring
            int startIndex = contents.getText().indexOf(selectedContents.toString());

            // Calculate the end index
            int endIndex = startIndex != -1 ? startIndex + selectedContents.length() : -1;

            contents.replaceText(startIndex, endIndex, styledText);
            parseTaggedContent(styledText);
        }
    }

    private void parseTaggedContent(String taggedContent) {
        // Updated regex to capture text and handle <font> tags with the size attribute
        Pattern pattern = Pattern.compile("(?<text>[^<]+)|<font[^>]*>(?<styledText>.*?)</font>");
        Matcher matcher = pattern.matcher(taggedContent);
        
        while (matcher.find()) {
            if (matcher.group("text") != null) {
                // Handle plain text (no tags)
                System.out.println("Text is: " + matcher.group("text"));
                documentContent.add(new TextSegment(matcher.group("text")));
            } else if (matcher.group("tag") != null) {
                // Handle <font> tag and its corresponding styled text
                String tag = matcher.group("tag");
                String styledText = matcher.group("styledText");
                System.out.println("Tag is: " + tag);
                System.out.println("Styled Text is: " + styledText);
                
                // Extract font size from the font tag using a helper function if needed
                String fontSizeAttribute = getAttribute(tag, "size");
                int fontSize = fontSizeAttribute != null ? Integer.parseInt(fontSizeAttribute) : 0;
    
                // Create a TextSegment for the styled content
                TextSegment segment = new TextSegment(styledText);
                segment.setFontSize(fontSize);
    
                // Add the segment to the document content list
                documentContent.add(segment);
            }
        }
    
        // Render content in your TextArea
        // renderContent(contents);
    }
    
    

    // Helper method to extract attribute value from a tag (e.g., size="16")
    private String getAttribute(String tag, String attributeName) {
        Pattern attributePattern = Pattern.compile(attributeName + "=\"(.*?)\"");
        Matcher attributeMatcher = attributePattern.matcher(tag);
        if (attributeMatcher.find()) {
            return attributeMatcher.group(1);
        }
        return null;
    }

    private void renderContent(StyleClassedTextArea styledTextArea) {
        // Clear the existing text in the text area
        styledTextArea.clear();

        for (TextSegment segment : documentContent) {
            String segmentText = segment.getText();
            System.out.println("text is: " + segmentText);

            // Apply styles
            StringBuilder styleClass = new StringBuilder();

            // Apply bold style
            if (segment.isBold()) {
                styleClass.append("bold ");
            }

            // Apply italic style
            if (segment.isItalic()) {
                styleClass.append("italic ");
            }

            // Apply font size style
            if (segment.getFontSize() > 0) {
                styledTextArea.setStyle("-fx-font-size: " + segment.getFontSize() + "px;");
            }

            // Add color to text
            if (segment.getColor() != null) {
                styledTextArea.setStyle("-fx-text-fill: " + segment.getColor() + ";");
            }

            // Add highlight background color
            if (segment.getHighlightColor() != null) {
                styledTextArea.setStyle("-fx-background-color: " + segment.getHighlightColor() + ";");
            }

            // Add the styled text to the TextArea
            styledTextArea.appendText(segmentText);
        }
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
