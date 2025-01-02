package com.mycompany.frontend.helper;

import javafx.scene.control.Skin;
import javafx.scene.control.TextField;

public class TogglePasswordField extends TextField {
    @Override
    protected Skin<?> createDefaultSkin() {
        return new TogglePasswordFieldSkin(this);
    }
}
