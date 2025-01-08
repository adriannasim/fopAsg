package com.mycompany.frontend.exportOptions;

import java.io.IOException;
import java.time.LocalDate;

import com.mycompany.backend.DiaryService;
import com.mycompany.backend.ServiceResult;
import com.mycompany.backend.UserSession;
import com.mycompany.frontend.App;
import com.mycompany.frontend.SharedPaneCharacteristics;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;

/***
 * THIS CONTROLLER CLASS IS USED FOR export-by-week.fxml
 * 
 ***/

public class ExportByWeekController extends SharedPaneCharacteristics {
    /***
     * ELEMENTS WITH FX:ID.
     * 
     ***/
    @FXML
    private DatePicker startDate; 

    @FXML
    private Spinner<Integer> week; 

    @FXML
    private Button exportBtn;

    /***
     * VARIABLES
     * 
     ***/
    DiaryService diaryService;
    String sessionUsername;

    /*** INITILIZATION OF THE CONTROLLER
     * 
     * ***/
    @FXML
    public void initialize() 
    {
        // Inherit Super Class's initialization
        super.initialize();
        
        // Get user session
        sessionUsername = UserSession.getSession().getUsername();

        // Get user diary
        diaryService = new DiaryService(sessionUsername);

        //populate combobox and spinner
        week.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 53, 1));

        //set default values
        startDate.setValue(LocalDate.now().minusWeeks(1)); 
        week.getValueFactory().setValue(1);

        //add event listener to exportBtn
        exportBtn.setOnAction(this::handleExportButtonClick);
    }

    //method to handle export button when clicked
    private void handleExportButtonClick(ActionEvent event) {
        String filename = "Diary_For_"+ week.getValue() + "_Weeks_From_" + startDate.getValue() + "-" + sessionUsername;

        ServiceResult result = diaryService.exportDiaryToPDF(startDate.getValue(), week.getValue(), filename);
        try
        {
            //pop up export successful
            if (result.isSuccessful())
            {
                App.openPopUpAtTop("success-message", result.getReturnMessage());
            }
            else
            {
                App.openPopUpAtTop("error-message", result.getReturnMessage());
            }
        }
        catch(IOException ex)
        {
            ex.printStackTrace();
        }
    }
}
