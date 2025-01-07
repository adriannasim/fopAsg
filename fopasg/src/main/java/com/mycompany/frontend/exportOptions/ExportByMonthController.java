package com.mycompany.frontend.exportOptions;

import java.io.IOException;
import java.time.LocalDate;
import java.time.Month;

import com.mycompany.backend.DiaryService;
import com.mycompany.backend.ServiceResult;
import com.mycompany.backend.UserSession;
import com.mycompany.frontend.App;
import com.mycompany.frontend.SharedPaneCharacteristics;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;

/***
 * THIS CONTROLLER CLASS IS USED FOR export-by-month.fxml
 * 
 ***/

public class ExportByMonthController extends SharedPaneCharacteristics {
    /***
     * ELEMENTS WITH FX:ID.
     * 
     ***/
    @FXML
    private ComboBox<Month> month; // this is used to store user entered month

    @FXML
    private Spinner<Integer> year;

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
        // Get user session
        sessionUsername = UserSession.getSession().getUsername();

        // Get user diary
        diaryService = new DiaryService(sessionUsername);

        //populate combobox and spinner
        month.setItems(FXCollections.observableArrayList(Month.values()));
        year.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1900, 2100, LocalDate.now().getYear()));

        //set default values
        month.setValue(LocalDate.now().getMonth());
        year.getValueFactory().setValue(LocalDate.now().getYear());

        //add event listener to exportBtn
        exportBtn.setOnAction(this::handleExportButtonClick);
    }

    //method to handle export button when clicked
    private void handleExportButtonClick(ActionEvent event) {
        String filename = "Diary_For_"+ month + "_Year_" + year + "-" + sessionUsername;

        ServiceResult result = diaryService.exportDiaryToPDF(month.getValue(), year.getValue(), filename);
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
