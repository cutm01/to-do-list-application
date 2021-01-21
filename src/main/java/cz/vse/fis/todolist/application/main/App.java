package cz.vse.fis.todolist.application.main;

import cz.vse.fis.todolist.application.ui.MainWindowSceneController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.InputStream;


/**
 * Main class of whole project. It is used to start new instance of To-Do List Application
 *
 * @version 1.0.0
 */
public class App extends Application {

    /**
     * Method builds the first scene of To-Do List Application and initializes its FXML controller
     *
     * @param stage top level JavaFX container containing additional application objects
     */
    @Override
    public void start(Stage stage) {
        //get scene resources
        FXMLLoader fxmlLoader = new FXMLLoader();
        InputStream mainWindowSceneInputStream = App.class.getClassLoader().getResourceAsStream("main_window_scene.fxml");
        Parent rootSceneElement = null;
        try {
            rootSceneElement = fxmlLoader.load(mainWindowSceneInputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }

        //build scene
        Scene mainWindowScene = new Scene(rootSceneElement);
        stage.setScene(mainWindowScene);
        stage.setTitle("To-Do List");

        //init FXML loader
        MainWindowSceneController mainWindowSceneController = fxmlLoader.getController();

        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}