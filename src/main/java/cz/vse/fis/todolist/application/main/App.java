package cz.vse.fis.todolist.application.main;

import cz.vse.fis.todolist.application.logic.Avatar;
import cz.vse.fis.todolist.application.logic.ReadUpdateFile;
import cz.vse.fis.todolist.application.logic.Task;
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
import java.util.List;
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
     * Method for creating new user account
     *
     * @param username
     * @param password
     * @param confirmPassword
     * @param passwordHint
     */
    public static void createNewAccount(String username, String password, String confirmPassword, String passwordHint) {
        UserData newAccountData = new UserData(username, password, passwordHint, Avatar.MALE.getAvatarIdentifier(), 0);
        ReadUpdateFile.writeDataToJSON(newAccountData);
    }

    /**
     * Method to create new empty category for user tasks
     *
     * @param categoryName
     */
    public static void createNewCategory(String categoryName) {
        userData.createTaskCategory(categoryName);
    }

    /**
     * Method to check whether category with same name already exists
     *
     * @param categoryName
     * @return true if category with same name already exists, false otherwise
     */
    public static boolean doesCategoryAlreadyExist(String categoryName) {
        return userData.doesCategoryExists(categoryName);
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
        return userData != null && userData.areLoginCredentialsValid(username, password);
    }

    /**
     * Method to check if password hint is set for user account
     *
     * @param username username input from GUI
     * @return true if password is set for user account, false
     */
    public static boolean isPasswordHintSet(String username) {
        userData = loadUserData(username);
        return userData != null && userData.isPasswordHintSet();
    }

    /**
     * Getter for user account's password hint
     *
     * @param username username input from GUI
     * @return String representing password hint which was set for account during registration
     */
    public static String getPasswordHintForUser(String username) {
        userData = loadUserData(username);
        return userData.getPasswordHint();
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
     * Method to set new Parent element to application main scene
     *
     * @param root Parent element to set
     */
    public static void setNewMainSceneParentElement(Parent root) {
        mainScene.setRoot(root);
    }

    /**
     * Method to check whether account with given username already exists
     *
     * @param username
     * @return true if account with given username already exists, false otherwise
     */
    public static boolean doesAccountAlreadyExist(String username) {
        return new File(ReadUpdateFile.USER_DATA_PATH + username + ".json").exists();
    }

    public static void savePerformedChanges() {
        ReadUpdateFile.writeDataToJSON(userData);
    }

    /**
     * Method to get avatar identifier which is currently set for user account
     *
     * @return String representing avatar identifier as defined in Avatar enum
     */
    public static String getUserAvatarIdentifier() {
        return userData.getAvatar();
    }

    /**
     * Method to get username for currently logged in account
     *
     * @return String representing username of actually logged in account
     */
    public static String getUsername() {
        return userData.getUsername();
    }

    /**
     * Method to get names of categories which are currently created for account
     *
     * @return ArrayList containing names of all user account categories
     */
    public static List<String> getCategoriesForAccount() {
        return userData.getUserCategoryNames();
    }

    /**
     * Method to get all task from category ordered by one of sorting option which
     * is specified in SortingOptions class
     *
     * @param categoryName name of category which will tasks be obtained from
     * @param sortingOption ordering option as specified in SortingOptions class
     * @return List of tasks in order specified by sorting option (e.g. from A->Z, newest->oldest)
     */
    public static List<Task> getTaskFromCategory(String categoryName, String sortingOption) {
        return userData.getTasksFromCategory(categoryName, sortingOption);
    }

    /**
     * Method to move task to another category
     *
     * @param task task instance to move
     * @param fromCategory category name where task will be moved from
     * @param  toCategory category name where task will be moved to
     */
    public static void moveTasksToCategory(Task task, String fromCategory, String toCategory) {
        userData.moveTaskToCategory(task, fromCategory, toCategory);
    }

    /**
     * Method to delete selected task from category
     *
     * @param task task which will be deleted
     * @param fromCategory category name which task will be deleted from
     */
    public static void deleteTaskFromCategory(Task task, String fromCategory) {
        userData.deleteTaskFromCategory(task, fromCategory);
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

    /**
     * Method to load all application scenes which user can switch between within GUI
     *
     * @return HashMap with scene name as key and FXML scene file as value
     */
    private Map<String, Pane> loadApplicationScenes() {
        applicationScenes = new HashMap<>();
        String[] scenesNames = {"login", "main", "manual", "register", "settings", "create_new_task", "edit_task"};

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
}
