package com.mycompany.frontend.helper;

import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.skin.TextFieldSkin;
import javafx.beans.binding.DoubleBinding;

public class TogglePasswordFieldSkin extends TextFieldSkin {
    ToggleButton show;

    public TogglePasswordFieldSkin(TogglePasswordField textField) {
        super(textField);
        textField.setPadding(new Insets(4, 15.0, 4, 7));

        show = new ToggleButton();
        show.setText("👀");
        show.setCursor(Cursor.HAND);
        show.setFocusTraversable(false);
        show.setPrefSize(20, 20);
        show.setStyle("-fx-background-color: #ffffff; -fx-background-radius: 10;");
        show.setPadding(new Insets(0));
        show.selectedProperty().addListener((obs, old, selected) -> {
            // Resetting the text to invalidate the text property so that it will call the
            // maskText method.
            String txt = textField.getText();
            int pos = textField.getCaretPosition();
            textField.setText(null);
            textField.setText(txt);
            textField.positionCaret(pos);
        });
        show.translateXProperty().bind(new DoubleBinding() {
            {
                bind(textField.widthProperty(), show.widthProperty());
            }

            @Override
            protected double computeValue() {
                return (textField.getWidth() - show.getWidth()) / 2;
            }
        });
        getChildren().add(show);
    }

    @Override
    protected String maskText(String txt) {
        if (show != null && !show.isSelected()) {
            int n = txt.length();
            StringBuilder passwordBuilder = new StringBuilder(n);
            for (int i = 0; i < n; i++) {
                passwordBuilder.append("●");
            }

            return passwordBuilder.toString();
        } else {
            return txt;
        }
    }
}

