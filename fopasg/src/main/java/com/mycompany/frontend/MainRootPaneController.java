package com.mycompany.frontend;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import com.mycompany.backend.UserSession;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class MainRootPaneController {
    /***
     * ELEMENTS WITH FX:ID.
     * 
     ***/
    @FXML
    private Label date; // Used to display the current date

    @FXML
    private Label day; // Used to display the current day of week

    @FXML
    private Label time; // used to display the current time

    @FXML
    private Label username; // used to display username

    /***
     * VARIABLES.
     * 
     ***/
    // Used to control the stop of timeNow
    private volatile boolean stop = false;

    /***
     * INITILIZATION OF THE CONTROLLER.
     * 
     ***/
    public void initialize() {
        // Set username
        String uname = UserSession.getSession().getUsername();
        username.setText(uname);

        // Set Date
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy");
        date.setText(LocalDateTime.now().format(formatter));

        // Set Day of Week
        String formattedDay = LocalDateTime.now().getDayOfWeek().toString().toLowerCase();
        formattedDay = Character.toUpperCase(formattedDay.charAt(0)) + formattedDay.substring(1);
        day.setText(formattedDay);

        // Set time
        timeNow();
    }

    /***
     * METHOD TO GET CURRENT TIME
     * 
     ***/
    private void timeNow() {
        Thread thread = new Thread(() -> {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("hh:mm:ss a");
            while (!stop) {
                try {
                    Thread.sleep(1000);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }

                final String timenow = LocalDateTime.now().format(formatter);
                Platform.runLater(() -> {
                    time.setText(timenow);
                });
            }
        });
        thread.start();
    }

}
