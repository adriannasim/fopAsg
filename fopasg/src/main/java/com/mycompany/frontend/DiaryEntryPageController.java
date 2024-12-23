package com.mycompany.frontend;

import javafx.fxml.FXML;
import javafx.scene.control.*;

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

}
