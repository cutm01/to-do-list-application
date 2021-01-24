package cz.vse.fis.todolist.application.ui;

import cz.vse.fis.todolist.application.main.App;
import javafx.event.ActionEvent;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.web.WebView;

/**
 * MainWindowSceneController class with methods to handle changes in main window od To-Do List application.
 *
 * @version 1.0.0
 */
public class MainWindowSceneController {
    //scene elements, bottom panel of root borderpane is absent
    //top panel
    public ImageView avatarImageView;
    public Label usernameLabel;
    //left panel
    public ComboBox categoriesComboBox;
    public ComboBox sortTasksComboBox;
    public ListView tasksListView;
    public CheckBox markAllTasksCheckBox;
    //center panel
    public Label taskNameLabel;
    public Label taskCategoryLabel;
    public Label taskCreationDateLabel;
    public Label taskDeadlineDateLabel;
    public WebView taskView;


    /**
     * Method moves user to create new task window scene
     *
     * @param actionEvent
     */
    public void showCreateNewTaskWindow(ActionEvent actionEvent) {
        App.activateScene("create_new_task");
    }

    /**
     * Method to show new category dialog. User has to input new category name
     *
     * @param actionEvent
     */
    public void showNewCategoryDialog(ActionEvent actionEvent) {
    }

    /**
     * Method to moves user to manual window scene
     *
     * @param actionEvent
     */
    public void showManualWindow(ActionEvent actionEvent) {
        App.activateScene("manual");
    }

    /**
     * Method to moves user to settings window scene
     *
     * @param actionEvent
     */
    public void showSettingsWindow(ActionEvent actionEvent) {
        App.activateScene("settings");
    }

    /**
     * Method to log out user and moves him back to main window scene of application.
     * Performed changes (e.g. creating new category or tasks) are saved before logging out
     *
     * @param actionEvent
     */
    public void logOut(ActionEvent actionEvent) {
    }

    /**
     * Method to mark all tasks which are currently shown in left panel of main window.
     * User can perform actions with these selected task such as delete them or move to another category
     *
     * @param actionEvent
     */
    public void markAllCurrentlyDisplayedTasks(ActionEvent actionEvent) {
    }

    /**
     * Method to mark all selected tasks from left panel of main window as completed.
     * These tasks are moved to category "Completed Tasks"
     *
     * @param actionEvent
     */
    public void markSelectedTasksAsCompleted(ActionEvent actionEvent) {
    }

    /**
     * Method to move all selected tasks from left panel of main window to existing category.
     *
     * @param actionEvent
     */
    public void moveSelectedTasksToExistingCategory(ActionEvent actionEvent) {
    }

    /**
     * Method to move all selected tasks from left panel of main window to new category.
     * Dialog is shown to user and name of new category where tasks will be moved has to be provided
     * by user
     *
     * @param actionEvent
     */
    public void moveSelectedTasksToNewCategory(ActionEvent actionEvent) {
    }

    /**
     * Method to deleted all selected tasks from left panel of main window
     *
     * @param actionEvent
     */
    public void deleteSelectedTasks(ActionEvent actionEvent) {
    }

    /**
     * Method to mark currently displayed task in center panel of main window as completed.
     * This task is moved to category "Completed Tasks"
     *
     * @param actionEvent
     */
    public void markCurrentlyOpenedTaskAsCompleted(ActionEvent actionEvent) {
    }

    /**
     * Method to edit currently displayed task in center panel of main window.
     * User is moved to edit task window scene where he can perform changes to task name, deadline date, etc.
     *
     * @param actionEvent
     */
    public void editCurrentlyOpenedTask(ActionEvent actionEvent) {
    }

    /**
     * Method to move currently displayed task in center panel of main window to existing category
     *
     * @param actionEvent
     */
    public void moveCurrentlyOpenedTaskToExistingCategory(ActionEvent actionEvent) {
    }

    /**
     * Method to delete currently displayed task in center panel of main window.
     *
     * @param actionEvent
     */
    public void deleteCurrentlyOpenedTask(ActionEvent actionEvent) {
    }
}
