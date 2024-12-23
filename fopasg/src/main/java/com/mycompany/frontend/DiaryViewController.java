package com.mycompany.frontend;

import javafx.fxml.FXML;
import javafx.scene.control.Button;

/*** 
 * THIS CONTROLLER CLASS IS USED FOR diary-view-page.fxml 
 * 
 ***/

public class DiaryViewController {

    /*** ELEMENTS WITH FX:ID  
     * 
     * ***/
    @FXML
    private Button backButton;

    /*** IMPORTANT FUNCTION TO GET THE MAIN MENU CONTROLLER 
     *   WITH THE SAME REFERENCE FROM MainMenuController Class 
     *   (FOR PAGE SWITCHING PURPOSE)
     * 
     * ***/
    private MainMenuController mainMenuController;
    public void setMainMenuController(MainMenuController mainMenuController) {
        this.mainMenuController = mainMenuController;
    }

    @FXML
    public void initialize() {
        backButton.setOnMouseClicked(e -> {
             if (mainMenuController != null) {
                mainMenuController.goBackToPreviousAnchorPane();
            }
        });
    }
}
