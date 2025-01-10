package com.mycompany.frontend;

import java.io.IOException;
import java.time.LocalDate;

import com.mycompany.backend.DiaryService;
import com.mycompany.backend.UserSession;

import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.NumberAxis;
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
    NumberAxis yAxis;

    @FXML
    public void initialize()
    {
        super.initialize();
        
        //set default values
        start.setValue(LocalDate.now().minusMonths(1));
        end.setValue(LocalDate.now());

        //configure bar chart's y axis
        yAxis = (NumberAxis) barChart.getYAxis();
        yAxis.setTickUnit(1);
        yAxis.setMinorTickCount(0);
        yAxis.setAutoRanging(false);
        yAxis.setLowerBound(0);
        yAxis.setUpperBound(5); //default

        //attach listeners so that the bar chart will be generated dynamically when there's an input
        start.valueProperty().addListener((_, _, _) -> generateBarChart());
        end.valueProperty().addListener((_, _, _) -> generateBarChart());

        //generate bar chart
        generateBarChart();
    }

    private void generateBarChart()
    {
        //clear old bar chart data
        //barChart.getData().clear();

        //get date range
        LocalDate startDate = start.getValue();
        LocalDate endDate = end.getValue();

        if (endDate.isBefore(startDate))
        {
            try 
            {
                App.openPopUpAtTop("error-message", "End Date cannot be before Start Date.");
            }
            catch (IOException ex)
            {
                ex.printStackTrace();
            }
        }

        //get mood counts from service
        int[] moodCounts = diaryService.getMoodByDate(startDate, endDate);

        //get max count to set the upperbound of y
        int maxCount = 0;
        for (int count : moodCounts) {
            if (count > maxCount) {
                maxCount = count;
            }
        }
        yAxis.setUpperBound(maxCount + 2);

        //if no data = initialise the series
        if (barChart.getData().isEmpty())
        {
            //create data series for the bar chart
            XYChart.Series<String, Number> series = new XYChart.Series<>();
    
            //add data points to the series
            XYChart.Data<String, Number> happy = new XYChart.Data<>("Happy", moodCounts[0]);
            XYChart.Data<String, Number> normal = new XYChart.Data<>("Normal", moodCounts[1]);
            XYChart.Data<String, Number> sad = new XYChart.Data<>("Sad", moodCounts[2]);
            
            series.getData().add(happy);
            series.getData().add(normal);
            series.getData().add(sad);
    
            applyStyleToDataNode(happy, "-fx-bar-fill:rgb(149, 241, 28);");
            applyStyleToDataNode(normal, "-fx-bar-fill:rgb(215, 228, 30);");
            applyStyleToDataNode(sad, "-fx-bar-fill:rgb(13, 201, 226);");

            //add series to the chart
            barChart.getData().add(series);
        }
        //if already have data, just update it
        else
        {
            XYChart.Series<String, Number> series = barChart.getData().get(0);
            series.getData().get(0).setYValue(moodCounts[0]);
            series.getData().get(1).setYValue(moodCounts[1]);
            series.getData().get(2).setYValue(moodCounts[2]);
        }

        //hide legend
        barChart.setLegendVisible(false);
    }

    //apply the colour to each nodes once it is created
    private void applyStyleToDataNode(XYChart.Data<String, Number> data, String style) 
    {
        data.nodeProperty().addListener((_, _, newNode) -> 
        {
            if (newNode != null) 
            {
                newNode.setStyle(style);
            }
        });
    }
}
