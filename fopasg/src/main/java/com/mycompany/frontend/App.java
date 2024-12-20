package com.mycompany.frontend;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.util.Stack;

public class App extends Application {

    private static Stack<Scene> sceneHistory = new Stack<>();
    private static Stage stage;

    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage stage) throws IOException {
        this.stage = stage;
        Scene initialScene = new Scene(loadFXML("main-menu")); // Landing page here
        stage.setScene(initialScene); 
        stage.show();
        sceneHistory.push(initialScene); // Push the initial scene to the history stack
    }

    // Method to load the FXML file
    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    // Method to switch between scenes
    public static void switchScene(String fxml) throws IOException {
        Scene newScene = new Scene(loadFXML(fxml)); // Create a new scene
        stage.setScene(newScene); // Set the new scene into current stage
        stage.show();
        sceneHistory.push(newScene); // Push the new scene to the histpry stack
    }

    // Method to go back to the previous scene
    public static void goBackToPreviousScene() {
        if (sceneHistory.size() > 1) { // If got previous scene
            sceneHistory.pop(); // Remove the current scene
            Scene previousScene = sceneHistory.peek(); // Get the previous scene
            stage.setScene(previousScene); // Show the previous scene
            stage.show();
        }
    }

    // Method to show a confirmation pop-up
    // public static void openConfirmationPopUp(String confirmationText) throws IOException {
    //     FXMLLoader loader = new FXMLLoader(App.class.getResource("pop-up-box.fxml"));
    //     Parent root = loader.load();
    //     PopUpBoxController controller = loader.getController();
    //     controller.setConfirmationText(confirmationText);

    //     Stage popupStage = new Stage();
    //     popupStage.initStyle(StageStyle.UNDECORATED);
    //     popupStage.initModality(Modality.APPLICATION_MODAL);
    //     Scene scene = new Scene(root);
    //     popupStage.setScene(scene);

    //     // Get the dimensions of the parent stage and screen
    //     double stageX = stage.getX();
    //     double stageY = stage.getY();
    //     double stageWidth = stage.getWidth();
    //     double stageHeight = stage.getHeight();

    //     // Calculate the center position
    //     double popupWidth = root.prefWidth(-1);
    //     double popupHeight = root.prefHeight(-1);
    //     double centerX = stageX + (stageWidth - popupWidth) / 2;
    //     double centerY = stageY + (stageHeight - popupHeight) / 2;

    //     // Set the position of the pop-up
    //     popupStage.setX(centerX);
    //     popupStage.setY(centerY);

    //     popupStage.showAndWait();
    // }
}