package cz.vse.fis.todolist.application.ui;

import cz.vse.fis.todolist.application.logic.Avatar;
import cz.vse.fis.todolist.application.logic.SortingOptions;
import cz.vse.fis.todolist.application.logic.Task;
import cz.vse.fis.todolist.application.main.App;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxListCell;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebView;
import org.w3c.dom.events.MouseEvent;

import java.util.Date;
import java.util.Objects;
import java.util.Optional;

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

    //observable attributes
    private ObservableList<String> categories = FXCollections.observableArrayList();
    private ObservableList<Task> displayedTasks = FXCollections.observableArrayList();

    public void init() {
        initTopPanel();
        initLeftPanel();
    }

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
        ApplicationAlert.CREATE_NEW_CATEGORY_DIALOG().showAndWait().ifPresent(response -> {
            if (App.doesCategoryAlreadyExist(response.toString())) {
                ApplicationAlert.ALERT_WITH_CUSTOM_MESSAGE(ApplicationAlert.CATEGORY_WITH_SAME_NAME_ALREADY_EXISTS_MESSAGE).showAndWait();
            }
            else {
                String categoryName = response.toString();
                App.createNewCategory(categoryName);
                categories.add(categoryName);
                ApplicationAlert.ALERT_WITH_CUSTOM_MESSAGE(ApplicationAlert.NEW_CATEGORY_SUCCESSFULLY_CREATED_MESSAGE).showAndWait();
            }
        });
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
        App.savePerformedChanges();
        App.activateScene("login");
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
        App.activateScene("edit_task");
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

    /**
     * Method for initializing the top panel of main application window. It sets user avatar and username
     */
    private void initTopPanel() {
        avatarImageView.setImage(Avatar.getImageForAvatar(App.getUserAvatarIdentifier()));
        usernameLabel.setText(App.getUsername());
    }

    private void initLeftPanel() {
        populateCategoriesComboBox();
        populateSortTasksComboBox();
        populateTasksListView();

        //listener to change displayed tasks according to user selection of category and sorting option
        categoriesComboBox.getSelectionModel().selectedItemProperty().addListener((observableValue, oldSelectedCategory, newSelectedCategory) -> {
            updateDisplayedTaskAfterNewCategorySelection(newSelectedCategory.toString());
        });
        sortTasksComboBox.getSelectionModel().selectedItemProperty().addListener((observableValue, oldSelectedSortingOption, newSelectedSortingOption) -> {
            updateDisplayedTaskAfterNewSortingOptionSelection(newSelectedSortingOption.toString());
        });
    }

    private void populateCategoriesComboBox() {
        categories.setAll(App.getCategoriesForAccount());
        categoriesComboBox.setItems(categories);
        categoriesComboBox.setPromptText("Categories");
    }

    private void populateSortTasksComboBox() {
        ObservableList<String> sortingOptions = FXCollections.observableArrayList();
        sortingOptions.addAll(SortingOptions.BY_NAME_FROM_A_TO_Z,
                              SortingOptions.BY_NAME_FROM_Z_TO_A,
                              SortingOptions.BY_CREATION_TIME_FROM_NEWEST_TO_OLDEST,
                              SortingOptions.BY_CREATION_TIME_FROM_OLDEST_TO_NEWEST,
                              SortingOptions.BY_DEADLINE_FROM_EARLIEST_TO_LATEST,
                              SortingOptions.BY_DEADLINE_FROM_LATEST_TO_OLDEST);

        sortTasksComboBox.setItems(sortingOptions);
        sortTasksComboBox.setPromptText("Order by");
    }

    private void populateTasksListView() {
        tasksListView.setItems(displayedTasks);
        tasksListView.setCellFactory(param -> new ListCell<Task>() {
            @Override
            public void updateItem(Task task, boolean empty) {
                super.updateItem(task, empty);

                if (empty || task == null) {
                    setText(null);
                    setGraphic(null);
                }
                else {
                    setGraphic(new VBox(new Label(task.getName()),
                                        new Label("Creation: " + new Date(task.getTaskCreationTimestamp()*1000).toString()),
                                        new Label("Deadline: " + new Date(task.getTaskDeadlineTimestamp()*1000).toString())));
                }
            }
        });

        tasksListView.setPlaceholder(new Label("Select one of the categories"));
    }

    private void updateDisplayedTaskAfterNewCategorySelection(String selectedCategoryName) {
        //get currently selected sorting option from GUI to obtain tasks in this order
        String sortingOption = sortTasksComboBox.getSelectionModel().getSelectedItem() == null ? null : sortTasksComboBox.getSelectionModel().getSelectedItem().toString();

        //no sorting option is currently selected, tasks will be obtained in their insertion order
        if (sortingOption == null) {
            displayedTasks.setAll(App.getTaskFromCategory(selectedCategoryName, SortingOptions.NONE));
        }
        else {
            displayedTasks.setAll(App.getTaskFromCategory(selectedCategoryName, sortingOption));
        }

        //show placeholder when category is empty
        if (displayedTasks.size() == 0) {
            tasksListView.setPlaceholder(new Label("Category is empty"));
        }
        else {
            tasksListView.setItems(displayedTasks);
        }
    }

    private void updateDisplayedTaskAfterNewSortingOptionSelection(String selectedSortingOption) {
        //get currently selected category from GUI to obtain tasks from this category
        String currentlySelectedCategory = categoriesComboBox.getSelectionModel().getSelectedItem() == null ? null : categoriesComboBox.getSelectionModel().getSelectedItem().toString();

        //no category is currently selected, list view will show placeholder
        if (currentlySelectedCategory == null) {
            tasksListView.setPlaceholder(new Label("Select one of the categories"));
        }
        else {
            displayedTasks.setAll(App.getTaskFromCategory(currentlySelectedCategory, selectedSortingOption));
        }

        tasksListView.setItems(displayedTasks);
    }
}
