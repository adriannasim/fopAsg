package com.mycompany.frontend;

import javafx.fxml.FXML;
import javafx.scene.control.Button;

/*** 
 * THIS CONTROLLER CLASS IS USED FOR diary-entry-page.fxml 
 * 
 ***/

public class DiaryEntryPageController {

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

    /*** INITILIZATION OF THE CONTROLLER
     * 
     * ***/
    @FXML
    public void initialize() {
        /*** GO BACK FUNCTION ***/
        backButton.setOnMouseClicked(e -> {
             if (mainMenuController != null) {
                mainMenuController.goBackToPreviousAnchorPane();
            }
        });
    }
}
