package com.mycompany.frontend.exportOptions;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

import com.mycompany.backend.Diary;
import com.mycompany.backend.DiaryService;
import com.mycompany.backend.ServiceResult;
import com.mycompany.backend.UserSession;
import com.mycompany.frontend.App;
import com.mycompany.frontend.SharedPaneCharacteristics;

import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

/***
 * THIS CONTROLLER CLASS IS USED FOR export-by-day.fxml
 * 
 ***/

public class ExportByDayController extends SharedPaneCharacteristics {

    /***
     * ELEMENTS WITH FX:ID.
     * 
     ***/
    @FXML
    private DatePicker startDate; // this is used to store user entered start day

    @FXML
    private DatePicker endDate; // this is used to store user entered end day

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

        //set default values
        startDate.setValue(LocalDate.now().minusWeeks(1));
        endDate.setValue(LocalDate.now());

        //add event listener to exportBtn
        exportBtn.setOnAction(this::handleExportButtonClick);
    }

    //method to handle export button when clicked
    private void handleExportButtonClick(ActionEvent event) {
        String filename = "Diary_From_Date_"+ startDate.getValue() + "_To_" + endDate.getValue() + "-" + sessionUsername;

        ServiceResult result = diaryService.exportDiaryToPDF(startDate.getValue(), endDate.getValue(), filename);
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
