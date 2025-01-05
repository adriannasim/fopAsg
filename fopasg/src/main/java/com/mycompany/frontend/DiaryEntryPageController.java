package com.mycompany.frontend;

import javafx.fxml.FXML;

import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ToggleButton;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.event.ActionEvent;
import javafx.util.StringConverter;

import com.gluonhq.richtextarea.RichTextArea;
import com.gluonhq.richtextarea.action.DecorateAction;
import com.gluonhq.richtextarea.action.ParagraphDecorateAction;
import com.gluonhq.richtextarea.action.TextDecorateAction;
import com.gluonhq.richtextarea.model.DecorationModel;
import com.gluonhq.richtextarea.model.Document;
import com.gluonhq.richtextarea.model.ParagraphDecoration;
import com.gluonhq.richtextarea.model.TextDecoration;

import com.mycompany.backend.Diary;
import com.mycompany.backend.DiaryService;
import com.mycompany.backend.ServiceResult;
import com.mycompany.backend.UserSession;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static com.gluonhq.richtextarea.model.ParagraphDecoration.GraphicType.BULLETED_LIST;
import static com.gluonhq.richtextarea.model.ParagraphDecoration.GraphicType.NONE;
import static com.gluonhq.richtextarea.model.ParagraphDecoration.GraphicType.NUMBERED_LIST;
import static javafx.scene.text.FontPosture.ITALIC;
import static javafx.scene.text.FontPosture.REGULAR;
import static javafx.scene.text.FontWeight.BOLD;
import static javafx.scene.text.FontWeight.NORMAL;

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
        private TextField title; // Used to hold title

        @FXML
        private Pane textarea; // Used to hold the rich text area

        @FXML
        private ComboBox<String> fontFamilyComboBox; // Used to change font family

        @FXML
        private ComboBox<Double> fontSizeComboBox; // Used to change font size of the content

        @FXML
        private ColorPicker textForeground; // Used to change font color of the content

        @FXML
        private ColorPicker textBackground; // Used to change highlight color of the content

        @FXML
        private Button textBold; // Used to bold the content

        @FXML
        private Button textItalic; // Used to italize the content

        @FXML
        private Button textUnderline; // Used to underline the content

        @FXML
        private Button textStrikethrough; // Used to strikethrough the content

        @FXML
        private Button bulletList; // Used to set bullet list in the content

        @FXML
        private Button numberList; // Used to set number list in the content

        @FXML
        private TextField wordCount; // Used to display the word count of the content

        @FXML
        private TextField charCount; // Used to display the character count of the content

        @FXML
        private FlowPane images; // Used to display the images uploaded by users

        @FXML
        private Button uploadImageBtn; // Used to upload images

        @FXML
        private Button submitBtn; // used to save the diary

        @FXML
        private Text titleMsg; // Used to store the title error message, use titleMsg.setText() to set message

        @FXML
        private Text contentMsg; // Used to store the content error message, use contentMsg.setText() to set
                                 // message

        /***
         * VARIABLES.
         * 
         ***/

        // Initialize the text contents with empty string
        String text = "";

        // Initialize the place user enter and edit their diary contents
        private final RichTextArea editor = new RichTextArea();

        // Initial text decoration settings
        TextDecoration textDecoration = TextDecoration.builder().presets()
                        .fontFamily("Roboto")
                        .fontSize(14)
                        .foreground("black")
                        .build();

        // Initial paragraph decoration settings
        ParagraphDecoration paragraphDecoration = ParagraphDecoration.builder().presets().build();

        // Initial decoration model (with the initial value of text decoration &
        // paragraph decoration)
        DecorationModel decorationModel = new DecorationModel(0, text.length(), textDecoration,
                        paragraphDecoration);

        // Initialize the document that will linked with the rich text area (editor)
        // later
        Document document = new Document(text, List.of(decorationModel), text.length());

        // Used to control the stop of timeNow
        private volatile boolean stop = false;

        private String mood;

        /***
         * INITILIZATION OF THE CONTROLLER.
         * 
         ***/
        public void initialize() {

                super.initialize();

                // Place the editor (rich text area) into Pane textarea
                textarea.getChildren().add(editor);

                // Set the editor same width and height with the textarea container
                editor.prefWidthProperty().bind(textarea.widthProperty());
                editor.prefHeightProperty().bind(textarea.heightProperty());

                // Used to define whether this is ADD NEW or EDIT page by retrieving diary from
                // currentDiary
                // If no currentDiary -> ADD NEW
                // If has currentDiary -> EDIT
                Diary diary = UserSession.getSession().getCurrentDiary();

                // Link the document to the editor
                // If there is a diary to refer to
                if (diary != null) {
                        try {
                                 // Get the mood
                                 mood = diary.getMood().toString();

                                 // Set the mood
                                 setMoodLabelAndIcon();

                                 moodIcon.setOnMouseClicked(e -> {
                                         try {
                                                 mood = App.openMoodIndicator();
                                                 setMoodLabelAndIcon();
                                         } catch (IOException ex) {
                                                 ex.printStackTrace();
                                         }
                                 });

                                 moodLabel.setOnMouseClicked(e -> {
                                         try {
                                                 mood = App.openMoodIndicator();
                                                 setMoodLabelAndIcon();
                                         } catch (IOException ex) {
                                                 ex.printStackTrace();
                                         }
                                 });

                                // Linked to the existing diary
                                editor.getActionFactory()
                                                .open(RichTextCSVExporter.importFromCSV(diary.getDiaryContent()))
                                                .execute(new ActionEvent());

                                // Set title
                                title.setText(diary.getDiaryTitle());

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

                        } catch (IOException ex) {
                                ex.printStackTrace();
                        }
                }
                // If there is no diary to refer to
                else {
                        // Open the mood indicator page when user enter (Call this after enter main
                        // menu)
                        // CAN ADD LOGIC of when it should display here...
                        Platform.runLater(() -> {
                                try {
                                        // Get the mood
                                        mood = App.openMoodIndicator();

                                        // Set the mood
                                        setMoodLabelAndIcon();

                                        moodIcon.setOnMouseClicked(e -> {
                                                try {
                                                        mood = App.openMoodIndicator();
                                                        setMoodLabelAndIcon();
                                                } catch (IOException ex) {
                                                        ex.printStackTrace();
                                                }
                                        });

                                        moodLabel.setOnMouseClicked(e -> {
                                                try {
                                                        mood = App.openMoodIndicator();
                                                        setMoodLabelAndIcon();
                                                } catch (IOException ex) {
                                                        ex.printStackTrace();
                                                }
                                        });
                                } catch (IOException ex) {
                                        ex.printStackTrace();
                                }
                        });

                        // link to empty diary
                        editor.getActionFactory().open(document).execute(new ActionEvent());
                        editor.setPromptText("Start typing your diary here.");

                        // Set Date
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy");
                        date.setText(LocalDateTime.now().format(formatter));

                        // Set Day of Week
                        String formattedDay = LocalDateTime.now().getDayOfWeek().toString().toLowerCase();
                        formattedDay = Character.toUpperCase(formattedDay.charAt(0)) + formattedDay.substring(1);
                        day.setText(formattedDay);

                        // Set time
                        timeNow();
                }

                // Make sure the document is updated whenever user has entered something
                editor.autoSaveProperty().set(true);

                /*** STEPS TO HANDLE CONTENTS FORMATTING ***/

                // Steps to handle font family change
                fontFamilyComboBox.setEditable(true);
                fontFamilyComboBox.getItems().setAll(Font.getFamilies());
                fontFamilyComboBox.setValue("Roboto");
                new TextDecorateAction<>(editor, fontFamilyComboBox.valueProperty(), TextDecoration::getFontFamily,
                                (builder, a) -> builder.fontFamily(a).build());

                // Steps to handle the font size change
                fontSizeComboBox.setEditable(true);
                fontSizeComboBox.getItems().addAll(IntStream.range(8, 102)
                                .filter(i -> i % 2 == 0 || i < 18)
                                .asDoubleStream().boxed().collect(Collectors.toList()));
                new TextDecorateAction<>(editor, fontSizeComboBox.valueProperty(), TextDecoration::getFontSize,
                                (builder, a) -> builder.fontSize(a).build());

                fontSizeComboBox.setConverter(new StringConverter<>() {
                        @Override
                        public String toString(Double aDouble) {
                                return Integer.toString(aDouble.intValue());
                        }

                        @Override
                        public Double fromString(String s) {
                                return Double.parseDouble(s);
                        }
                });
                fontSizeComboBox.setValue(14.0); // Default font size set as 14

                // Steps to handle the font color change
                new TextDecorateAction<>(editor, textForeground.valueProperty(), td -> Color.web(td.getForeground()),
                                (builder, color) -> builder.foreground(toHexString(color)).build());
                textForeground.setValue(Color.BLACK); // Default font color set to BLACK

                // Steps to handle the text highlight color change
                new TextDecorateAction<>(editor, textBackground.valueProperty(), td -> Color.web(td.getBackground()),
                                (builder, color) -> builder.background(toHexString(color)).build());
                textBackground.setValue(Color.TRANSPARENT); // Default text highlight color set to TRANSPARENT

                // Steps to handle the bold text change
                ImageView boldIcon = new ImageView(
                                new Image(getClass()
                                                .getResourceAsStream("/com/mycompany/frontend/images/bold-icon.png")));
                textBold.setGraphic(createToggleButton(boldIcon, property -> new TextDecorateAction<>(editor, property,
                                d -> d.getFontWeight() == BOLD,
                                (builder, a) -> builder.fontWeight(a ? BOLD : NORMAL).build())));
                textBold.getGraphic().setStyle(
                                "-fx-background-color: #f1f1f1 !important; -fx-background-radius: 0; -fx-border-color: transparent;");

                // Steps to handle the italic text change
                ImageView italicIcon = new ImageView(
                                new Image(getClass().getResourceAsStream(
                                                "/com/mycompany/frontend/images/italic-icon.png")));
                textItalic.setGraphic(createToggleButton(italicIcon,
                                property -> new TextDecorateAction<>(editor, property,
                                                d -> d.getFontPosture() == ITALIC,
                                                (builder, a) -> builder.fontPosture(a ? ITALIC : REGULAR).build())));
                textItalic.getGraphic().setStyle(
                                "-fx-background-color: #f1f1f1 !important; -fx-background-radius: 0; -fx-border-color: transparent;");

                // Steps to handle the underline text change
                ImageView underlineIcon = new ImageView(
                                new Image(getClass().getResourceAsStream(
                                                "/com/mycompany/frontend/images/underline-icon.png")));
                textUnderline.setGraphic(createToggleButton(underlineIcon, property -> new TextDecorateAction<>(editor,
                                property, TextDecoration::isUnderline, (builder, a) -> builder.underline(a).build())));
                textUnderline.getGraphic().setStyle(
                                "-fx-background-color: #f1f1f1 !important; -fx-background-radius: 0; -fx-border-color: transparent;");

                // Steps to handle the strikethrough text change
                ImageView strikethroughIcon = new ImageView(
                                new Image(getClass().getResourceAsStream(
                                                "/com/mycompany/frontend/images/strikethrough-icon.png")));
                textStrikethrough.setGraphic(createToggleButton(strikethroughIcon,
                                property -> new TextDecorateAction<>(editor,
                                                property, TextDecoration::isStrikethrough,
                                                (builder, a) -> builder.strikethrough(a).build())));
                textStrikethrough.getGraphic().setStyle(
                                "-fx-background-color: #f1f1f1 !important; -fx-background-radius: 0; -fx-border-color: transparent;");

                // Steps to handle the bullet list change
                ImageView bulletListIcon = new ImageView(
                                new Image(getClass().getResourceAsStream(
                                                "/com/mycompany/frontend/images/bullet-list-icon.png")));
                bulletList.setGraphic(createToggleButton(bulletListIcon,
                                property -> new ParagraphDecorateAction<>(editor, property,
                                                d -> d.getGraphicType() == BULLETED_LIST,
                                                (builder, a) -> builder.graphicType(a ? BULLETED_LIST : NONE)
                                                                .build())));
                bulletList.getGraphic().setStyle(
                                "-fx-background-color: #f1f1f1 !important; -fx-background-radius: 0; -fx-border-color: transparent;");

                // Steps to handle the numbered list change
                ImageView numberListIcon = new ImageView(
                                new Image(getClass().getResourceAsStream(
                                                "/com/mycompany/frontend/images/number-list-icon.png")));
                numberList.setGraphic(createToggleButton(numberListIcon,
                                property -> new ParagraphDecorateAction<>(editor, property,
                                                d -> d.getGraphicType() == NUMBERED_LIST,
                                                (builder, a) -> builder.graphicType(a ? NUMBERED_LIST : NONE)
                                                                .build())));
                numberList.getGraphic().setStyle(
                                "-fx-background-color: #f1f1f1 !important; -fx-background-radius: 0; -fx-border-color: transparent;");

                // Listen to any changes on the editor contents (user add, modify or delete the
                // contents in the rich text area)
                editor.textLengthProperty().addListener((o, ov, nv) -> {
                        // Update character count
                        charCount.setText(String.valueOf(nv));

                        // Make sure the word count is initiated when the document is exists only
                        Platform.runLater(() -> {
                                Document linkedDocument = editor.getDocument();
                                if (linkedDocument != null) {
                                        // Update word count
                                        countWords();
                                }
                        });
                });

                // When user want to save the diary content
                submitBtn.setOnMouseClicked(e -> {
                        title.getStyleClass().remove("error");
                        titleMsg.setText("");
                        textarea.getStyleClass().remove("error");
                        contentMsg.setText("");

                        if (title.getText() != null && !title.getText().equals("") &&
                                        editor.getTextLength() != 0) {
                                Platform.runLater(() -> {
                                        // Check if the document exists
                                        Document linkedDocument = editor.getDocument();
                                        if (linkedDocument != null) {
                                                String username = UserSession.getSession().getUsername();
                                                // If this is a new entry
                                                if (diary == null) {
                                                        // Try insert the diary
                                                        try {
                                                                DiaryService diaryService = new DiaryService(username);
                                                                String diaryContent = RichTextCSVExporter
                                                                                .exportToCSV(editor);
                                                                ServiceResult result = diaryService.newDiaryEntry(
                                                                                title.getText(),
                                                                                LocalDateTime.now(), diaryContent,
                                                                                Diary.Mood.valueOf(mood));

                                                                if (result.isSuccessful()) {
                                                                        App.openPopUpAtTop("success-message",
                                                                                        result.getReturnMessage());
                                                                        mainMenuController.loadNewContent(
                                                                                        "diary-history-page");
                                                                } else {
                                                                        App.openPopUpAtTop("error-message",
                                                                                        result.getReturnMessage());
                                                                }
                                                        } catch (IOException ex) {
                                                                ex.printStackTrace();
                                                        }
                                                }
                                                // If the diary existed
                                                else {
                                                        // Try update the diary
                                                        try {
                                                                DiaryService diaryService = new DiaryService(username);
                                                                String diaryContent = RichTextCSVExporter
                                                                                .exportToCSV(editor);
                                                                ServiceResult result = diaryService.editDiaryEntry(
                                                                                diary.getDiaryId(), title.getText(),
                                                                                diary.getDiaryDate(), diaryContent,
                                                                                Diary.Mood.valueOf(mood));
                                                                if (result.isSuccessful()) {
                                                                        App.openPopUpAtTop("success-message",
                                                                                        result.getReturnMessage());
                                                                } else {
                                                                        App.openPopUpAtTop("error-message",
                                                                                        result.getReturnMessage());
                                                                }
                                                        } catch (IOException ex) {
                                                                ex.printStackTrace();
                                                        }

                                                }
                                        } else {
                                                // Error handling here...
                                        }
                                });
                        } else {
                                if (title.getText() == null || title.getText().equals("")) {
                                        title.getStyleClass().add("error");
                                        titleMsg.setText("Title cannot be empty.");
                                }

                                if (editor.getTextLength() == 0) {
                                        textarea.getStyleClass().add("error");
                                        contentMsg.setText("Content cannot be empty.");
                                }

                                // If this is a new entry
                                if (diary == null) {
                                        try {
                                                App.openPopUpAtTop("error-message", "Failed to add this diary.");
                                        } catch (IOException ex) {
                                                ex.printStackTrace();
                                        }
                                } else {
                                        try {
                                                App.openPopUpAtTop("error-message", "Failed to update this diary.");
                                        } catch (IOException ex) {
                                                ex.printStackTrace();
                                        }
                                }

                        }
                });

                // When user want to add image, then will display the images
                uploadImageBtn.setOnMouseClicked(event -> {
                        //open file chooser
                        FileChooser fileChooser = new FileChooser();
                        fileChooser.setTitle("Choose image(s) to be uploaded");
                        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg"));
                        
                        List<File> selectedImageFiles = fileChooser.showOpenMultipleDialog(uploadImageBtn.getScene().getWindow());
                        List<String> selectedImageFilesPath = diary.getImagePaths();

                        if (!selectedImageFiles.isEmpty())
                        {
                                for (File file : selectedImageFiles)
                                {
                                        selectedImageFilesPath.add(file.getAbsolutePath());
                                }
                        }

                        // Use to display the images
                        displayImages(diary.getImagePaths());
                });
        }

        /***
         * HELPER METHOD TO CREATE TOGGLE BUTTON.
         * 
         ***/
        private ToggleButton createToggleButton(ImageView icon,
                        Function<ObjectProperty<Boolean>, DecorateAction<Boolean>> function) {
                final ToggleButton toggleButton = new ToggleButton();

                // Set icon
                icon.setFitWidth(11);
                icon.setFitHeight(11);
                toggleButton.setGraphic(icon);

                // Add function
                function.apply(toggleButton.selectedProperty().asObject());

                return toggleButton;
        }

        /***
         * HELPER METHOD FOR CONVERTING COLOR TO HEX STRING.
         * 
         ***/
        private String toHexString(Color value) {
                return String.format("#%02X%02X%02X%02X",
                                (int) Math.round(value.getRed() * 255),
                                (int) Math.round(value.getGreen() * 255),
                                (int) Math.round(value.getBlue() * 255),
                                (int) Math.round(value.getOpacity() * 255));
        }

        /***
         * METHOD TO COUNT THE WORDS AND UPDATE THE VALUE OF WORDCOUNT IN UI.
         * 
         ***/
        private void countWords() {
                Document document = editor.getDocument();
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
        public void displayImages(List<String> imagePaths) {
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
                        imageView.setStyle("-fx-cursor: HAND;");

                        // Open image pop up view
                        imageView.setOnMouseClicked(e -> {
                                try {
                                        App.openPopUpImg("pop-up-img", new Image(path));
                                } catch (IOException ex) {
                                        ex.printStackTrace();
                                }
                        });

                        // Add ImageView to the container
                        images.getChildren().add(imageView);
                }
        }

        /***
         * METHOD TO GET CURRENT TIME
         * 
         ***/
        private void timeNow() {
                Thread thread = new Thread(() -> {
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("hh:mm a");
                        while (!stop) {
                                try {
                                        Thread.sleep(1000);
                                } catch (Exception ex) {
                                        ex.printStackTrace();
                                }

                                final String timenow = LocalDateTime.now().format(formatter);
                                Platform.runLater(() -> {
                                        time.setText(timenow);
                                });
                        }
                });
                thread.start();
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
