package cz.vse.fis.todolist.application.main;

import cz.vse.fis.todolist.application.logic.ReadUpdateFile;
import cz.vse.fis.todolist.application.logic.UserData;
import cz.vse.fis.todolist.application.ui.LoginWindowSceneController;
import cz.vse.fis.todolist.application.ui.MainWindowSceneController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;


/**
 * Main class of whole project. It holds all necessary data and is used to start new instance of To-Do List Application
 * and switch between app scenes
 *
 * @version 1.0.0
 */
public class App extends Application {
    private static UserData userData;
    private static Scene mainScene;
    private static HashMap<String, Pane> applicationScenes;

    public static void main(String[] args) {
        launch();
    }

    /**
     * Method builds the first scene of To-Do List Application and initializes its FXML controller
     *
     * @param stage top level JavaFX container containing additional application objects
     */
    @Override
    public void start(Stage stage) {
        loadApplicationScenes();

        mainScene = new Scene(applicationScenes.get("login"));
        stage.setScene(mainScene);
        stage.setTitle("To-Do List");

        stage.show();
    }

    /**
     * Method to load all application scenes which user can switch between within GUI
     *
     * @return HashMap with scene name as key and FXML scene file as value
     */
    private Map<String, Pane> loadApplicationScenes() {
        applicationScenes = new HashMap<>();
        String[] scenesNames = {"login", "main", "manual", "register", "settings"};

        for (String scenesName : scenesNames) {
            try {
                String sceneToLoad = scenesName + "_window_scene.fxml";
                FXMLLoader fxmlLoader = new FXMLLoader();
                InputStream sceneInputStream = App.class.getClassLoader().getResourceAsStream(sceneToLoad);
                applicationScenes.put(scenesName, fxmlLoader.load(sceneInputStream));
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }

        return applicationScenes;
    }

    /**
     * Method to validate login credentials provided by user
     *
     * @param username username input from GUI
     * @param password password input from GUI
     * @return true if login credentials are valid, false otherwise
     */
    public static boolean validateLoginCredentials(String username, String password) {
        userData = loadUserData(username);
        return userData != null && userData.getPassword().equals(password);
    }

    /**
     * Method to activate different window of application by changing
     * root element of main scene
     *
     * @param sceneName application window name to activate (e.q "login")
     */
    public static void activateScene(String sceneName) {
        mainScene.setRoot(applicationScenes.get(sceneName));
    }

    /**
     * Method to load user information stored in JSON file
     *
     * @param username username which data will be loaded
     * @return UserData containing user information (e.g. password, task categories or avatar type) or null if account
     * does not exist
     */
    private static UserData loadUserData(String username) {
        File file = new File(ReadUpdateFile.USER_DATA_PATH + username + ".json");
        if (file.isFile()) {
            return ReadUpdateFile.readDataFromJSON(username);
        }

        return null;
    }
}
