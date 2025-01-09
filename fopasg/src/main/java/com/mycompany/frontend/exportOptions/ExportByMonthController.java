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
        // Inherit Super Class's initialization
        super.initialize();
        
        // Get user session
        sessionUsername = UserSession.getSession().getUsername();

        // Get user diary
        diaryService = new DiaryService(sessionUsername);

        //populate combobox and spinner
        month.setItems(FXCollections.observableArrayList(Month.values()));
        year.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1900, 2100, LocalDate.now().getYear()));

        // Set a StringConverter for the ComboBox to display month names
        month.setConverter(new javafx.util.StringConverter<>() {
            @Override
            public String toString(Month object) {
                return object == null ? "" : object.name(); // Display month name
            }

            @Override
            public Month fromString(String string) {
                return string == null || string.isEmpty() ? null : Month.valueOf(string);
            }
        });

        //set default values
        month.setValue(LocalDate.now().getMonth());
        year.getValueFactory().setValue(LocalDate.now().getYear());

        //add event listener to exportBtn
        exportBtn.setOnAction(this::handleExportButtonClick);
    }

    //method to handle export button when clicked
    private void handleExportButtonClick(ActionEvent event) {
        String filename = "Diary_For_"+ month.getValue() + "_Year_" + year.getValue() + "-" + sessionUsername;

        try
        {
            ServiceResult result = diaryService.exportDiaryToPDF(month.getValue().name(), year.getValue(), filename);
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
