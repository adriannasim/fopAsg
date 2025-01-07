package com.mycompany.frontend;

import java.time.LocalDate;

import com.mycompany.backend.DiaryService;
import com.mycompany.backend.UserSession;

import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
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
    private BarChart<String, Number> barChart;  // This is used to display result

    /***
    * VARIABLES
    * 
    ***/
    private DiaryService diaryService = new DiaryService(UserSession.getSession().getUsername());

    @FXML
    public void initialize()
    {
        super.initialize();
        
        //set default values
        start.setValue(LocalDate.now().minusMonths(1));
        end.setValue(LocalDate.now());

        //attach listeners so that the bar chart will be generated dynamically when there's an input
        start.valueProperty().addListener((_, _, _) -> generateBarChart());
        end.valueProperty().addListener((_, _, _) -> generateBarChart());

        //generate bar chart
        generateBarChart();
    }

    private void generateBarChart()
    {
        //clear old bar chart data
        barChart.getData().clear();

        //get date range
        LocalDate startDate = start.getValue();
        LocalDate endDate = end.getValue();

        if (endDate.isBefore(startDate))
        {
            //can like put a small msg say end date cnt be before start date
        }

        //get mood counts from service
        int[] moodCounts = diaryService.getMoodByDate(startDate, endDate);

        //create data series for the bar chart
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Mood From " + startDate + " To " + endDate);

        //add data points to the series
        series.getData().add(new XYChart.Data<>("Happy", moodCounts[0]));
        series.getData().add(new XYChart.Data<>("Normal", moodCounts[1]));
        series.getData().add(new XYChart.Data<>("Sad", moodCounts[2]));

        //add series to the chart
        barChart.getData().add(series);
    }
}
