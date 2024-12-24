package com.mycompany.frontend;

import org.fxmisc.richtext.StyleClassedTextArea;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;

import com.gluonhq.emoji.Emoji;
import com.gluonhq.emoji.EmojiData;
import com.gluonhq.richtextarea.RichTextArea;
import com.gluonhq.richtextarea.Selection;
import com.gluonhq.richtextarea.Tools;
import com.gluonhq.richtextarea.action.Action;
import com.gluonhq.richtextarea.action.DecorateAction;
import com.gluonhq.richtextarea.action.ParagraphDecorateAction;
import com.gluonhq.richtextarea.action.TextDecorateAction;
import com.gluonhq.richtextarea.model.Decoration;
import com.gluonhq.richtextarea.model.DecorationModel;
import com.gluonhq.richtextarea.model.Document;
import com.gluonhq.richtextarea.model.ImageDecoration;
import com.gluonhq.richtextarea.model.ParagraphDecoration;
import com.gluonhq.richtextarea.model.TableDecoration;
import com.gluonhq.richtextarea.model.TextDecoration;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ObjectProperty;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Separator;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import org.kordamp.ikonli.Ikon;
import org.kordamp.ikonli.javafx.FontIcon;
import org.kordamp.ikonli.lineawesome.LineAwesomeSolid;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.gluonhq.richtextarea.model.ParagraphDecoration.GraphicType.BULLETED_LIST;
import static com.gluonhq.richtextarea.model.ParagraphDecoration.GraphicType.NONE;
import static com.gluonhq.richtextarea.model.ParagraphDecoration.GraphicType.NUMBERED_LIST;
import static javafx.scene.text.FontPosture.ITALIC;
import static javafx.scene.text.FontPosture.REGULAR;
import static javafx.scene.text.FontWeight.BOLD;
import static javafx.scene.text.FontWeight.NORMAL;
import javafx.scene.text.FontWeight;

public class DiaryEntryPageController extends SharedPaneCharacteristics {

        @FXML
        private Pane textarea;
        private final RichTextArea editor = new RichTextArea();

        @FXML
        private ComboBox<Double> fontSizeComboBox; // Changed to Double type

        @FXML
        private ColorPicker textForeground; // Add these FXML components

        @FXML
        private ColorPicker textBackground;

        @FXML
        private Button textBold;

        @FXML
        private Button textItalic;

        @FXML
        private Button textUnderline;

        @FXML
        private Button textStrikethrough;

        @FXML
        private Button bulletList;

        @FXML
        private Button numberList;

        private static final String MARKER_BOLD = "*", MARKER_ITALIC = "_", MARKER_MONO = "`";

        public void initialize() {
                super.initialize();
                textarea.getChildren().add(editor);

                editor.getStyleClass().add("text-area");
                editor.prefWidthProperty().bind(textarea.widthProperty());
                editor.prefHeightProperty().bind(textarea.heightProperty());

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
                fontSizeComboBox.setValue(14.0);

                // Set up text color action
                // new TextDecorateAction<>(editor, textForeground.valueProperty(),
                // td -> Color.web(td.getForeground()),
                // (builder, color) -> builder.foreground(toHexString(color)).build());

                new TextDecorateAction<>(editor, textForeground.valueProperty(), td -> Color.web(td.getForeground()),
                                (builder, color) -> builder.foreground(toHexString(color)).build());
                textForeground.setValue(Color.BLACK);

                // // Set up background color action
                // new TextDecorateAction<>(editor, textBackground.valueProperty(),
                // td -> Color.web(td.getBackground()),
                // (builder, color) -> builder.background(toHexString(color)).build());

                // setupTextFormatting();

                textBackground.getStyleClass().add("background");
                new TextDecorateAction<>(editor, textBackground.valueProperty(), td -> Color.web(td.getBackground()),
                                (builder, color) -> builder.background(toHexString(color)).build());
                textBackground.setValue(Color.TRANSPARENT);

                ImageView boldIcon = new ImageView(
                                new Image(getClass()
                                                .getResourceAsStream("/com/mycompany/frontend/images/bold-icon.png")));

                textBold.setGraphic(createToggleButton(boldIcon, property -> new TextDecorateAction<>(editor, property,
                                d -> d.getFontWeight() == BOLD,
                                (builder, a) -> builder.fontWeight(a ? BOLD : NORMAL).build())));
                textBold.getGraphic().setStyle(
                                "-fx-background-color: #f1f1f1 !important; -fx-background-radius: 0; -fx-border-color: transparent;");

                ImageView italicIcon = new ImageView(
                                new Image(getClass().getResourceAsStream(
                                                "/com/mycompany/frontend/images/italic-icon.png")));

                textItalic.setGraphic(createToggleButton(italicIcon,
                                property -> new TextDecorateAction<>(editor, property,
                                                d -> d.getFontPosture() == ITALIC,
                                                (builder, a) -> builder.fontPosture(a ? ITALIC : REGULAR).build())));
                textItalic.getGraphic().setStyle(
                                "-fx-background-color: #f1f1f1 !important; -fx-background-radius: 0; -fx-border-color: transparent;");

                textBold.setGraphic(createToggleButton(boldIcon, property -> new TextDecorateAction<>(editor, property,
                                d -> d.getFontWeight() == BOLD,
                                (builder, a) -> builder.fontWeight(a ? BOLD : NORMAL).build())));
                textBold.getGraphic().setStyle(
                                "-fx-background-color: #f1f1f1 !important; -fx-background-radius: 0; -fx-border-color: transparent;");

                ImageView underlineIcon = new ImageView(
                                new Image(getClass().getResourceAsStream(
                                                "/com/mycompany/frontend/images/underline-icon.png")));

                textUnderline.setGraphic(createToggleButton(underlineIcon, property -> new TextDecorateAction<>(editor,
                                property, TextDecoration::isUnderline, (builder, a) -> builder.underline(a).build())));
                textUnderline.getGraphic().setStyle(
                                "-fx-background-color: #f1f1f1 !important; -fx-background-radius: 0; -fx-border-color: transparent;");

                ImageView strikethroughIcon = new ImageView(
                                new Image(getClass().getResourceAsStream(
                                                "/com/mycompany/frontend/images/strikethrough-icon.png")));

                textStrikethrough.setGraphic(createToggleButton(strikethroughIcon,
                                property -> new TextDecorateAction<>(editor,
                                                property, TextDecoration::isStrikethrough,
                                                (builder, a) -> builder.strikethrough(a).build())));
                textStrikethrough.getGraphic().setStyle(
                                "-fx-background-color: #f1f1f1 !important; -fx-background-radius: 0; -fx-border-color: transparent;");

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
        }

        private ToggleButton createToggleButton(ImageView icon,
                        Function<ObjectProperty<Boolean>, DecorateAction<Boolean>> function) {
                final ToggleButton toggleButton = new ToggleButton();
                icon.setFitWidth(11);
                icon.setFitHeight(11);

                toggleButton.setGraphic(icon);
                function.apply(toggleButton.selectedProperty().asObject());
                return toggleButton;
        }

        // private void setupTextFormatting() {
        // // Example of handling bold text
        // boldButton.setOnAction(e -> {
        // editor.getActionFactory()
        // .decorate(TextDecoration.builder()
        // .fontWeight(boldButton.isSelected() ? BOLD : NORMAL)
        // .build())
        // .execute(e);
        // });

        // // Example of handling italic text
        // italicButton.setOnAction(e -> {
        // editor.getActionFactory()
        // .decorate(TextDecoration.builder()
        // .fontPosture(italicButton.isSelected() ? ITALIC : REGULAR)
        // .build())
        // .execute(e);
        // });

        // // Example of handling underline
        // underlineButton.setOnAction(e -> {
        // editor.getActionFactory()
        // .decorate(TextDecoration.builder()
        // .underline(underlineButton.isSelected())
        // .build())
        // .execute(e);
        // });

        // // Example of handling bullet list
        // bulletListButton.setOnAction(e -> {
        // editor.getActionFactory()
        // .decorate(ParagraphDecoration.builder()
        // .graphicType(bulletListButton.isSelected() ? BULLETED_LIST : NONE)
        // .build())
        // .execute(e);
        // });
        // }

        // Helper method for converting Color to hex string
        private String toHexString(Color value) {
                return String.format("#%02X%02X%02X%02X",
                                (int) Math.round(value.getRed() * 255),
                                (int) Math.round(value.getGreen() * 255),
                                (int) Math.round(value.getBlue() * 255),
                                (int) Math.round(value.getOpacity() * 255));
        }

        // Example of image insertion method
        @FXML
        private void handleImageInsertion() {
                FileChooser fileChooser = new FileChooser();
                fileChooser.getExtensionFilters().add(
                                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif"));

                File file = fileChooser.showOpenDialog(textarea.getScene().getWindow());
                if (file != null) {
                        String url = file.toURI().toString();
                        editor.getActionFactory()
                                        .decorate(new ImageDecoration(url))
                                        .execute(new ActionEvent());
                }
        }

}
