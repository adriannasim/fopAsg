package com.mycompany.frontend.helper;

import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class PopUpImgController {

    /***
     * ELEMENTS WITH FX:ID.
     * 
     ***/
    @FXML
    private Pane imgContainer;

    @FXML
    private ImageView img;

    @FXML
    private ImageView crossIcon;

    /***
     * INITILIZATION OF THE CONTROLLER.
     * 
     ***/
    public void initialize() {
        // Add a click event to the cross icon
        crossIcon.setOnMouseClicked(event -> closeMessageWindow());
    }

    /***
     * METHOD TO CLOSE THE IMAGE WINDOW.
     * 
     ***/
    private void closeMessageWindow() {
        Stage stage = (Stage) crossIcon.getScene().getWindow();
        stage.close();
    }

    /***
     * METHOD TO SET THE IMAGE.
     * 
     ***/
    public void setImage(Image img) {
        this.img.setImage(img);
    }

    /***
     * METHOD TO SET THE HEIGHT.
     * 
     ***/
    public void setHeight(double height) {
        this.imgContainer.setMaxHeight(height);
    }
}
