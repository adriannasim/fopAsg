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

    @Override
    public void start(Stage stage) throws IOException {
        this.stage = stage;
        Scene initialScene = new Scene(loadFXML("diary-history-page"));
        stage.setScene(initialScene);
        stage.show();
        sceneHistory.push(initialScene); // Push the initial scene to the stack
    }

    // Method to load the FXML file
    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static void main(String[] args) {
        launch();
    }

    // Method to switch between scenes
    public static void switchScene(String fxml) throws IOException {
        Scene newScene = new Scene(loadFXML(fxml));
        stage.setScene(newScene);
        stage.show();
        sceneHistory.push(newScene); // Push the new scene onto the stack
    }

    // Method to go back to the previous scene
    public static void goBackToPreviousScene() {
        if (sceneHistory.size() > 1) {
            sceneHistory.pop(); // Remove the current scene
            Scene previousScene = sceneHistory.peek(); // Get the previous scene
            stage.setScene(previousScene);
            stage.show();
            
        } 
    }

    // Example method for a confirmation pop-up
    public static void openConfirmationPopUp(String confirmationText) throws IOException {
        FXMLLoader loader = new FXMLLoader(App.class.getResource("pop-up-box.fxml"));
        Parent root = loader.load();
        PopUpBoxController controller = loader.getController();
        controller.setConfirmationText(confirmationText);

        Stage popupStage = new Stage();
        popupStage.initStyle(StageStyle.UNDECORATED);
        popupStage.initModality(Modality.APPLICATION_MODAL);
        Scene scene = new Scene(root);
        popupStage.setScene(scene);
        popupStage.showAndWait();
    }
}
