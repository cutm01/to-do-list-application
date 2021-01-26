package cz.vse.fis.todolist.application.ui;

import cz.vse.fis.todolist.application.logic.Avatar;
import cz.vse.fis.todolist.application.logic.SortingOptions;
import cz.vse.fis.todolist.application.logic.Task;
import cz.vse.fis.todolist.application.main.App;
import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxListCell;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.web.WebView;
import javafx.util.Callback;
import javafx.util.StringConverter;
import org.w3c.dom.events.MouseEvent;

import java.util.*;

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

    //observable attributes to react to changes made in left application panel
    private ObservableList<String> categories = FXCollections.observableArrayList();
    private ObservableList<Task> displayedTasks = FXCollections.observableArrayList();

    //currently displayed task in center panel, its attributes can be changed from left panel
    //(e.g. category name when user moves this task to different category) and center panel has to react to this changes
    private StringProperty uniqueIDOfDisplayedTask;
    private StringProperty nameOfDisplayedTask;
    private StringProperty categoryOfDisplayedTask;
    private StringProperty deadlineOfDisplayedTask;

    public void init() {
        initTopPanel();
        initLeftPanel();
        initCenterPanel();
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
     * Method to mark all selected tasks from left panel of main window as completed.
     * These tasks are moved to category "Completed Tasks"
     *
     * @param actionEvent
     */
    public void markSelectedTasksAsCompleted(ActionEvent actionEvent) {
        //no task were selected from left panel
        if (displayedTasks.filtered(Task::isSelected).size() == 0) {
            ApplicationAlert.ALERT_WITH_CUSTOM_MESSAGE(ApplicationAlert.NO_TASK_WAS_SELECTED_TO_MARK_AS_COMPLETED_MESSAGE).showAndWait();
            return;
        }

        String fromCategory = categoriesComboBox.getSelectionModel().getSelectedItem().toString();
        String toCategory = "Completed tasks";

        List<Task> tasksToRemoveFromDisplayedTasks = new ArrayList<>();
        for (Task task : displayedTasks) {
            if (task.isSelected()) {
                task.setCompleted(true);
                App.moveTasksToCategory(task, fromCategory, toCategory);
                tasksToRemoveFromDisplayedTasks.add(task);

                //check if currently opened task from center panel is not between selected tasks
                //and change her category String property if so
                if (task.getTaskID().equals(uniqueIDOfDisplayedTask.getValue())) {
                    categoryOfDisplayedTask.setValue(toCategory);
                }
            }
        }

        displayedTasks.removeAll(tasksToRemoveFromDisplayedTasks);
    }

    /**
     * Method to move all selected tasks from left panel of main window to existing category.
     *
     * @param actionEvent
     */
    public void moveSelectedTasksToExistingCategory(ActionEvent actionEvent) {
        //no task were selected from left panel
        if (displayedTasks.filtered(Task::isSelected).size() == 0) {
            ApplicationAlert.ALERT_WITH_CUSTOM_MESSAGE(ApplicationAlert.NO_TASK_WAS_SELECTED_TO_MOVE_TO_EXISTING_CATEGORY_MESSAGE).showAndWait();
            return;
        }

        String currentlySelectedCategory = categoriesComboBox.getSelectionModel().getSelectedItem().toString();
        ApplicationAlert.CHOOSE_CATEGORY_TO_MOVE_TASKS_TO_DIALOG(currentlySelectedCategory).showAndWait().ifPresent(response -> {
            String fromCategory = categoriesComboBox.getSelectionModel().getSelectedItem().toString();
            String toCategory = response.toString();

            List<Task> tasksToRemoveFromDisplayedTasks = new ArrayList<>();
            for (Task task : displayedTasks) {
                if (task.isSelected()) {
                    App.moveTasksToCategory(task, fromCategory, toCategory);
                    tasksToRemoveFromDisplayedTasks.add(task);

                    //check if currently opened task from center panel is not between selected tasks
                    //and change her category String property if so
                    if (task.getTaskID().equals(uniqueIDOfDisplayedTask.getValue())) {
                        categoryOfDisplayedTask.setValue(toCategory);
                    }
                }
            }

            displayedTasks.removeAll(tasksToRemoveFromDisplayedTasks);
        });
    }

    /**
     * Method to move all selected tasks from left panel of main window to new category.
     * Dialog is shown to user and name of new category where tasks will be moved has to be provided
     * by user
     *
     * @param actionEvent
     */
    public void moveSelectedTasksToNewCategory(ActionEvent actionEvent) {
        //no task were selected from left panel
        if (displayedTasks.filtered(Task::isSelected).size() == 0) {
            ApplicationAlert.ALERT_WITH_CUSTOM_MESSAGE(ApplicationAlert.NO_TASK_WAS_SELECTED_TO_MOVE_TO_NEW_CATEGORY_MESSAGE).showAndWait();
            return;
        }

        ApplicationAlert.CREATE_CATEGORY_TO_MOVE_TASKS_TO_DIALOG().showAndWait().ifPresent(response -> {
            String toCategory = response.toString();

            if (App.doesCategoryAlreadyExist(toCategory)) {
                ApplicationAlert.ALERT_WITH_CUSTOM_MESSAGE(ApplicationAlert.CATEGORY_WITH_SAME_NAME_ALREADY_EXISTS_NO_TASKS_MOVED_MESSAGE).showAndWait();
            }
            else {
                String fromCategory = categoriesComboBox.getSelectionModel().getSelectedItem().toString();

                List<Task> tasksToRemoveFromDisplayedTasks = new ArrayList<>();
                App.createNewCategory(toCategory);
                categories.add(toCategory);
                for (Task task : displayedTasks) {
                    if (task.isSelected()) {
                        App.moveTasksToCategory(task, fromCategory, toCategory);
                        tasksToRemoveFromDisplayedTasks.add(task);

                        //check if currently opened task from center panel is not between selected tasks
                        //and change her category String property if so
                        if (task.getTaskID().equals(uniqueIDOfDisplayedTask.getValue())) {
                            categoryOfDisplayedTask.setValue(toCategory);
                        }
                    }
                }

                displayedTasks.removeAll(tasksToRemoveFromDisplayedTasks);
                ApplicationAlert.ALERT_WITH_CUSTOM_MESSAGE(ApplicationAlert.TASK_SUCCESSFULLY_MOVED_TO_NEWLY_CREATED_CATEGORY_MESSAGE).showAndWait();
            }
        });
    }

    /**
     * Method to deleted all selected tasks from left panel of main window
     *
     * @param actionEvent
     */
    public void deleteSelectedTasks(ActionEvent actionEvent) {
        if (displayedTasks.filtered(Task::isSelected).size() == 0) {
            ApplicationAlert.ALERT_WITH_CUSTOM_MESSAGE(ApplicationAlert.NO_TASK_WAS_SELECTED_TO_DELETE_MESSAGE).showAndWait();
            return;
        }

        ApplicationAlert.CONFIRM_TASKS_DELETION_ALERT().showAndWait().ifPresent(response -> {
            if (response.getButtonData().equals(ButtonBar.ButtonData.YES)) {
                String fromCategory = categoriesComboBox.getSelectionModel().getSelectedItem().toString();

                List<Task> tasksToRemoveFromDisplayedTasks = new ArrayList<>();
                for (Task task : displayedTasks) {
                    if (task.isSelected()) {
                        App.deleteTaskFromCategory(task, fromCategory);
                        tasksToRemoveFromDisplayedTasks.add(task);

                        //check if currently opened task from center panel is not between selected tasks
                        //if so, ID of currently displayed task and content of center panel will be changed
                        if (task.getTaskID().equals(uniqueIDOfDisplayedTask.getValue())) {
                            uniqueIDOfDisplayedTask.setValue("");
                        }
                    }
                }

                displayedTasks.removeAll(tasksToRemoveFromDisplayedTasks);
            }
        });
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

        //lister to checkbox to mark or unmark all currently displayed tasks
        markAllTasksCheckBox.selectedProperty().addListener((observableValue, oldValue, newValue) -> {
            if(newValue) {
                markAllCurrentlyDisplayedTasks();
            } else {
                unmarkAllCurrentlyDisplayedTasks();
            }
        });

        //listener to disable checkbox when no tasks are displayed
        markAllTasksCheckBox.disableProperty().bind(Bindings.isEmpty(displayedTasks));
    }

    /**
     * Method to mark all tasks which are currently shown in left panel of main window.
     * User can perform actions with these selected task such as delete them or move to another category
     */
    private void markAllCurrentlyDisplayedTasks() {
        for (Task task : displayedTasks) {
            task.setSelected(true);
        }
    }

    /**
     * Method to unmark all tasks which are currently shown in left panel of main window.
     * User can perform actions with these selected task such as delete them or move to another category
     */
    private void unmarkAllCurrentlyDisplayedTasks() {
        for (Task task : displayedTasks) {
            task.setSelected(false);
        }
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
        StringConverter<Task> converter = new StringConverter<Task>() {
            @Override
            public String toString(Task task) {
                //get task text which will be displayed in ListView cell,
                //if task name or text is longer than 25 characters, show just first 25 characters followed by dots
                String taskName = task.getName().length() > 25 ? task.getName().toUpperCase().substring(0,25).concat("...")
                                                               : task.getName().toUpperCase();
                String taskText = task.getText().length() > 25 ? task.getText().substring(0,25).concat("...")
                                                               : task.getText();
                String delimiter = "-".repeat(45);
                String creationTime =  "CREATED: " + new Date(task.getTaskCreationTimestamp()).toString();
                String deadline = "DEADLINE: " + new Date(task.getTaskDeadlineTimestamp()).toString();

                return taskName + "\n"
                       + taskText + "\n"
                       + delimiter + "\n"
                       + creationTime + "\n"
                       + deadline + "\n"
                       + "\n";
            }

            @Override
            public Task fromString(String s) {
                return null;
            }
        };

        tasksListView.setCellFactory(CheckBoxListCell.forListView(Task::selectedProperty, converter));
        tasksListView.setItems(displayedTasks);

        //add listener to update center panel when new task is selected from ListView
        tasksListView.getSelectionModel().selectedItemProperty().addListener((observableValue, oldSelectedTask, newSelectedTask) -> {
            if (newSelectedTask != null) {
                uniqueIDOfDisplayedTask.setValue(((Task) newSelectedTask).getTaskID());
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

        unmarkAllCurrentlyDisplayedTasks();
        markAllTasksCheckBox.setSelected(false);
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

        unmarkAllCurrentlyDisplayedTasks();
        markAllTasksCheckBox.setSelected(false);
    }

    /**
     * Method to init center panel of GUI. Content is set to correspond to last opened tasks before
     * logged out or closing application
     */
    private void initCenterPanel() {
        Task lastOpenedTask = App.getLastOpenedTask();

        //initialize StringProperties of currently displayed task in center panel
        uniqueIDOfDisplayedTask = new SimpleStringProperty(lastOpenedTask.getTaskID());
        nameOfDisplayedTask = new SimpleStringProperty(lastOpenedTask.getName());
        categoryOfDisplayedTask = new SimpleStringProperty("");
        deadlineOfDisplayedTask = new SimpleStringProperty(new Date(lastOpenedTask.getTaskDeadlineTimestamp()).toString());

        //add listener to currently displayed task StringProperties so center panel can be dynamically changed
        uniqueIDOfDisplayedTask.addListener((observableValue, oldID, newID) -> {
            updateTaskViewInCenterPanel();
        });

        nameOfDisplayedTask.addListener((observableValue, oldName, newName) -> {
            taskNameLabel.setText(newName);
        });

        categoryOfDisplayedTask.addListener((observableValue, oldCategoryName, newCategoryName) -> {
            taskCategoryLabel.setText(newCategoryName);
        });

        deadlineOfDisplayedTask.addListener((observableValue, oldDeadline, newDeadline) -> {
            taskDeadlineDateLabel.setText(newDeadline);
        });

        //initialize center panel
        taskNameLabel.setText(lastOpenedTask.getName());
        taskCategoryLabel.setText("");
        taskCreationDateLabel.setText(new Date(lastOpenedTask.getTaskCreationTimestamp()).toString());
        taskDeadlineDateLabel.setText(new Date(lastOpenedTask.getTaskDeadlineTimestamp()).toString());
        taskView.getEngine().loadContent(lastOpenedTask.getText());
    }

    /**
     * Method to update center panel of main window which contains currently displayed task.
     * If previous currently displayed task was removed (and therefore uniqueIDOfDisplayedTask set to empty String),
     * task view in center panel will be update to inform user about this action.
     * Center panel is also updated when user selects another task from left panel, labels in center panel are then
     * updated to match newly selected task.
     */
    private void updateTaskViewInCenterPanel() {
        if (uniqueIDOfDisplayedTask.getValue().isEmpty()) {
            taskNameLabel.setText("No task is currently selected :(");
            taskCategoryLabel.setText("-----");
            taskCreationDateLabel.setText("-----");
            taskDeadlineDateLabel.setText("-----");
            taskView.getEngine().loadContent("<p style=\"text-align: center;\">It looks like you deleted the previously displayed task</p>\n"
                                             + "<p style=\"text-align: center;\">Feel free to selected another task from the left panel of the"
                                             + "application or create a completely new one to keep track of your duties :)</p>");
        }
        else {
            Task currentlySelectedTask = (Task)tasksListView.getSelectionModel().getSelectedItem();

            //update values of StringProperty attributes so changes can be made automatically
            uniqueIDOfDisplayedTask.setValue(currentlySelectedTask.getTaskID());
            nameOfDisplayedTask.setValue(currentlySelectedTask.getName());
            categoryOfDisplayedTask.setValue(categoriesComboBox.getSelectionModel().getSelectedItem().toString());
            deadlineOfDisplayedTask.setValue(new Date(currentlySelectedTask.getTaskDeadlineTimestamp()).toString());

            //update rest of elements from center panel
            taskCreationDateLabel.setText(new Date(currentlySelectedTask.getTaskCreationTimestamp()).toString());
            taskView.getEngine().loadContent(currentlySelectedTask.getText());
        }
    }
}
