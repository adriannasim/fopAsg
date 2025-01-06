package com.mycompany.frontend;

import javafx.fxml.FXML;
import javafx.scene.control.Button;

public abstract class SharedPaneCharacteristics {
    
    /*** ELEMENTS WITH FX:ID  
     * 
     * ***/
    @FXML
    private Button backButton; 


    /*** INITILIZATION OF THE CONTROLLER
     * 
     * ***/
    @FXML
    public void initialize() {
        /*** GO BACK FUNCTION ***/
        backButton.setOnMouseClicked(_ -> {
             if (mainMenuController != null) {
                mainMenuController.goBackToPreviousAnchorPane();
            }
        });
    }

    /*** IMPORTANT FUNCTION TO GET THE MAIN MENU CONTROLLER 
     *   WITH THE SAME REFERENCE FROM MainMenuController Class 
     *   (FOR PAGE SWITCHING PURPOSE)
     * 
     * ***/
    public MainMenuController mainMenuController;
    public void setMainMenuController(MainMenuController mainMenuController) {
        this.mainMenuController = mainMenuController;
    }
}

    
