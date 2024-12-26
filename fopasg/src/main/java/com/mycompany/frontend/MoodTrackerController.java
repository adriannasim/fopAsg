package com.mycompany.frontend;

import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.control.DatePicker;

/***
 * THIS CONTROLLER CLASS IS USED FOR mood-tracker.fxml
 * 
 ***/

public class MoodTrackerController extends SharedPaneCharacteristics{
    /***
     * ELEMENTS WITH FX:ID
     * 
     ***/
    @FXML 
    private DatePicker start; // This is used to store user entered start date range

    @FXML
    private DatePicker end; // This is used to store user entered end date range

    @FXML
    private BarChart barChart;  // This is used to display result

}
