package com.mycompany.frontend.exportOptions;

import com.mycompany.frontend.SharedPaneCharacteristics;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;

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
    private DatePicker month; // this is used to store user entered month
}
