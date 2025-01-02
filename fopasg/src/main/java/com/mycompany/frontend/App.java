package com.mycompany.frontend;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import java.io.IOException;
import java.util.Stack;
import java.util.function.Supplier;

import com.mycompany.backend.ServiceResult;
import com.mycompany.frontend.helper.MessageController;
import com.mycompany.frontend.helper.PopUpBoxController;
import com.mycompany.frontend.helper.PopUpImgController;

/***
 * THIS APP CLASS IS SERVED AS THE ***ENTRY POINT*** OF THE ENTIRE APPLICATION.
 * - RUN THIS FILE TO START THE APP.
 * 
 ***/
public class App extends Application {

    /***
     * STATIC VARIABLES DECLARATION.
     * 
     ***/
    // This is used to keep track the previous scene (But it is not used for now).
    private static Stack<Scene> sceneHistory = new Stack<>();

    // This is used to refer to the main stage with the same reference, which used
    // throughout the App.
    private static Stage stage;

    /***
     * MAIN METHOD TO START.
     * 
     ***/
    public static void main(String[] args) {
        launch();
    }

    /***
     * INITIAL SETUP.
     * 
     ***/
    @Override
    public void start(Stage stage) throws IOException {
        this.stage = stage;
        // Landing page here
        Scene initialScene = new Scene(loadFXML("login-page").getRoot(), 900, 600);
        stage.setScene(initialScene);
        stage.setTitle("Digital Diary");
        stage.show();
        // Push the initial scene to the history stack
        sceneHistory.push(initialScene);
    }

    /***
     * METHOD TO LOAD THE FXML FILE.
     * 
     ***/
    private static FXMLLoader loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        fxmlLoader.load();
        return fxmlLoader;
    }

    /***
     * METHOD TO SHOW A CONFIRMATION POP UP.
     * 
     ***/
    public static void openConfirmationPopUp(String confirmationText, Supplier<ServiceResult> serviceOperation)
            throws IOException {
        FXMLLoader loader = loadFXML("pop-up-box");
        Parent root = loader.getRoot();
        // Get the PopUpBoxController.
        PopUpBoxController controller = loader.getController();
        // Use the controller to set the confirmation text that displayed to users.
        controller.setConfirmationText(confirmationText);
        // // Use the controller to set the success message that displayed to the users
        // // when user wanted action performed correctly.
        // controller.setSuccessMessageText(successMessage);
        // // Use the controller to set the failed message that displayed to the users
        // // when user wanted action performed wrongly.
        // controller.setFailedMessageText(failedMessage);

        // set what service function to invoke if user press yes
        controller.setServiceOperation(serviceOperation);

        Stage popupStage = new Stage();
        // Remove the default window decorations (title bar, close, minimize, and
        // maximize buttons).
        popupStage.initStyle(StageStyle.UNDECORATED);
        // Set the modality of the pop-up to APPLICATION_MODAL, making it block
        // interactions
        // with other application windows until the pop-up is closed.
        popupStage.initModality(Modality.APPLICATION_MODAL);
        Scene scene = new Scene(root);
        popupStage.setScene(scene);

        // Get the dimensions of the parent stage and screen
        double stageX = stage.getX();
        double stageY = stage.getY();
        double stageWidth = stage.getWidth();
        double stageHeight = stage.getHeight();

        // Calculate the center position
        double popupWidth = root.prefWidth(-1);
        double popupHeight = root.prefHeight(-1);
        double centerX = (stageX + 100) + (stageWidth - popupWidth) / 2;
        double centerY = stageY + (stageHeight - popupHeight) / 2;

        // Set the position of the pop-up
        popupStage.setX(centerX);
        popupStage.setY(centerY);

        // Display the pop-up window and block further code execution until the pop-up
        // is closed.
        popupStage.showAndWait();
    }

    /***
     * METHOD TO SHOW A POP UP AT CENTER.
     * 
     ***/
    public static void openPopUp(String filename) throws IOException {
        Parent root = loadFXML(filename).getRoot();
        Stage popupStage = new Stage();
        // Remove the default window decorations (title bar, close, minimize, and
        // maximize buttons).
        popupStage.initStyle(StageStyle.UNDECORATED);
        // Set the modality of the pop-up to APPLICATION_MODAL, making it block
        // interactions
        // with other application windows until the pop-up is closed.
        popupStage.initModality(Modality.APPLICATION_MODAL);
        Scene scene = new Scene(root);
        popupStage.setScene(scene);

        // Get the dimensions of the parent stage and screen
        double stageX = stage.getX();
        double stageY = stage.getY();
        double stageWidth = stage.getWidth();
        double stageHeight = stage.getHeight();

        // Calculate the center position
        double popupWidth = root.prefWidth(-1);
        double popupHeight = root.prefHeight(-1);
        double centerX = (stageX) + (stageWidth - popupWidth) / 2;
        double centerY = stageY + (stageHeight - popupHeight) / 2;

        // Set the position of the pop-up
        popupStage.setX(centerX);
        popupStage.setY(centerY);

        // Display the pop-up window and block further code execution until the pop-up
        // is closed.
        popupStage.showAndWait();
    }

    /***
     * METHOD TO SHOW A MOOD INDICATOR.
     * 
     ***/
    public static String openMoodIndicator() throws IOException {
        FXMLLoader loader = loadFXML("mood-indicator");
        Parent root = loader.getRoot();

        // Get the controller
        MoodIndicatorController controller = loader.getController();

        // Create a new stage for the popup
        Stage popupStage = new Stage();
        popupStage.initStyle(StageStyle.UNDECORATED);
        popupStage.initModality(Modality.APPLICATION_MODAL);
        popupStage.setScene(new Scene(root));

         // Get the dimensions of the parent stage and screen
         double stageX = stage.getX();
         double stageY = stage.getY();
         double stageWidth = stage.getWidth();
         double stageHeight = stage.getHeight();
 
         // Calculate the center position
         double popupWidth = root.prefWidth(-1);
         double popupHeight = root.prefHeight(-1);
         double centerX = (stageX) + (stageWidth - popupWidth) / 2;
         double centerY = stageY + (stageHeight - popupHeight) / 2;
 
         // Set the position of the pop-up
         popupStage.setX(centerX);
         popupStage.setY(centerY); 

        // Show the popup and wait for it to close
        popupStage.showAndWait();

        // Retrieve the selected mood from the controller
        return controller.getMood();
    }

    /***
     * METHOD TO SHOW A POP UP SIGN UP AT CENTER.
     * 
     ***/
    public static void openPopUpSignUp(String filename) throws IOException {
        Parent root = loadFXML(filename).getRoot();
        Stage popupStage = new Stage();
        // Remove the default window decorations (title bar, close, minimize, and
        // maximize buttons).
        popupStage.initStyle(StageStyle.UNDECORATED);
        Scene scene = new Scene(root);
        popupStage.setScene(scene);

        // Get the dimensions of the parent stage and screen
        double stageX = stage.getX();
        double stageY = stage.getY();
        double stageWidth = stage.getWidth();
        double stageHeight = stage.getHeight();

        // Calculate the center position
        double popupWidth = root.prefWidth(-1);
        double popupHeight = root.prefHeight(-1);
        double centerX = stageX + (stageWidth - popupWidth) / 2;
        double centerY = stageY + (stageHeight - popupHeight) / 2;

        // Set the position of the pop-up
        popupStage.setX(centerX);
        popupStage.setY(centerY);

        // Display the pop-up window and block further code execution until the pop-up
        // is closed.
        popupStage.showAndWait();
    }

    /***
     * METHOD TO SHOW A POP UP IMAGE AT CENTER.
     * 
     ***/
    public static void openPopUpImg(String filename, Image img) throws IOException {
        FXMLLoader loader = loadFXML(filename);
        Parent root = loader.getRoot();
        // Get controller to set the image
        PopUpImgController controller = loader.getController();
        controller.setImage(img);

        Stage popupStage = new Stage();
        popupStage.initStyle(StageStyle.UNDECORATED);
        Scene scene = new Scene(root);
        popupStage.setScene(scene);

        // Get the dimensions of the parent stage and screen
        double stageX = stage.getX();
        double stageY = stage.getY();
        double stageWidth = stage.getWidth();
        double stageHeight = stage.getHeight();

        // Calculate the center position
        double popupWidth = root.prefWidth(-1);
        double popupHeight = root.prefHeight(-1);
        double centerX = (stageX + 100) + (stageWidth - popupWidth) / 2;
        double centerY = stageY + (stageHeight - popupHeight) / 2;

        // Set the position of the pop-up
        popupStage.setX(centerX);
        popupStage.setY(centerY);

        // Display the pop-up window and block further code execution until the pop-up
        // is closed.
        popupStage.show();
    }

    /***
     * METHOD TO SHOW A POP UP AT TOP.
     * 
     ***/
    public static void openPopUpAtTop(String filename, String message) throws IOException {
        FXMLLoader loader = loadFXML(filename);
        Parent root = loader.getRoot();
        // Get the controller to set the message text
        MessageController controller = loader.getController();
        controller.setMessageText(message);

        Stage messageBoxStage = new Stage();
        messageBoxStage.initStyle(StageStyle.UNDECORATED);
        Scene scene = new Scene(root);
        messageBoxStage.setScene(scene);

        // Get the dimensions of the parent stage and screen
        double stageX = stage.getX();
        double stageY = stage.getY();
        double stageWidth = stage.getWidth();

        // Calculate the center position
        double popupWidth = root.prefWidth(-1);
        double centerX = (stageX) + (stageWidth - popupWidth) / 2;
        double higherY = stageY + 80;

        // Set the position of the pop-up
        messageBoxStage.setX(centerX);
        messageBoxStage.setY(higherY);

        messageBoxStage.show();

        // After 3 seconds, close the pop-up
        javafx.animation.PauseTransition pause = new javafx.animation.PauseTransition(
                javafx.util.Duration.seconds(3));
        pause.setOnFinished(e -> messageBoxStage.close());
        pause.play();
    }

    /*** Below two are not used for now ***/
    // Method to switch between scenes
    public static void switchScene(String fxml) throws IOException {
        Scene newScene = new Scene(loadFXML(fxml).getRoot()); // Create a new scene
        stage.setScene(newScene); // Set the new scene into current stage
        stage.show();
        sceneHistory.push(newScene); // Push the new scene to the history stack
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
}
