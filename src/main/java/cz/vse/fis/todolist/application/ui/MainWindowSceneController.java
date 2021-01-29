package cz.vse.fis.todolist.application.ui;

import cz.vse.fis.todolist.application.logic.Avatar;
import cz.vse.fis.todolist.application.logic.SortingOptions;
import cz.vse.fis.todolist.application.logic.Task;
import cz.vse.fis.todolist.application.main.App;
import javafx.application.Platform;
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
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxListCell;
import javafx.scene.control.skin.ComboBoxListViewSkin;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.StringConverter;

import java.io.IOException;
import java.io.InputStream;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.*;

/**
 * MainWindowSceneController class with methods to handle changes in main window od To-Do List application.
 *
 * @version 1.0.0
 */
public class MainWindowSceneController {
    //scene elements, bottom panel of root borderpane is absent
    public BorderPane rootBorderPane;
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

    //right panel
    public Button markTaskAsCompletedButton;
    public Button editTaskButton;
    public Button moveTaskButton;
    public Button deleteTaskButton;

    //observable attributes to react to changes made in left application panel
    private ObservableList<String> categories = FXCollections.observableArrayList();
    private ObservableList<Task> displayedTasks = FXCollections.observableArrayList();

    //currently displayed task in center panel, its attributes can be changed from left panel
    //(e.g. category name when user moves this task to different category) and center panel has to react to this changes
    private StringProperty uniqueIDOfDisplayedTask;
    private StringProperty nameOfDisplayedTask;
    private StringProperty categoryOfDisplayedTask;
    private StringProperty deadlineOfDisplayedTask;
    private BooleanProperty isDisplayedTaskCompleted;

    public void init() {
        initTopPanel();
        initLeftPanel();
        initCenterPanel();
        initRightPanel();
    }

    /**
     * Method moves user to create new task window scene
     *
     * @param actionEvent
     */
    public void showCreateNewTaskWindow(ActionEvent actionEvent) {
        initializeCreateTaskWindow();
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
        initializeManualWindow();
    }

    /**
     * Method to moves user to settings window scene
     *
     * @param actionEvent
     */
    public void showSettingsWindow(ActionEvent actionEvent) {
        initializeSettingsWindow();
    }

    /**
     * Method to log out user and moves him back to main window scene of application.
     * Performed changes (e.g. creating new category or tasks) are saved before logging out
     *
     * @param actionEvent
     */
    public void logOut(ActionEvent actionEvent) {
        String lastOpenedTaskID = uniqueIDOfDisplayedTask.getValue().equals("") || uniqueIDOfDisplayedTask.getValue().equals("-1") ? "-1" : uniqueIDOfDisplayedTask.getValue();
        App.setLastOpenedTask(categoryOfDisplayedTask.getValue(), lastOpenedTaskID);

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
                    isDisplayedTaskCompleted.setValue(true);
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
        String fromCategory = categoryOfDisplayedTask.getValue();
        String toCategory = "Completed tasks";

        Task currentlyOpenedTask = App.getTaskByID(fromCategory, uniqueIDOfDisplayedTask.getValue());
        currentlyOpenedTask.setCompleted(true);
        isDisplayedTaskCompleted.setValue(true);
        App.moveTasksToCategory(currentlyOpenedTask, fromCategory, toCategory);

        categoryOfDisplayedTask.setValue(toCategory);

        //if present, delete currently opened task from left panel list view
        displayedTasks.remove(currentlyOpenedTask);
    }

    /**
     * Method to edit currently displayed task in center panel of main window.
     * User is moved to edit task window scene where he can perform changes to task name, deadline date, etc.
     *
     * @param actionEvent
     */
    public void editCurrentlyOpenedTask(ActionEvent actionEvent) {
        App.setLastOpenedTask(categoryOfDisplayedTask.getValue(), uniqueIDOfDisplayedTask.getValue());
        initializeEditTaskWindow();
    }

    /**
     * Method to move currently displayed task in center panel of main window to existing category
     *
     * @param actionEvent
     */
    public void moveCurrentlyOpenedTaskToExistingCategory(ActionEvent actionEvent) {
        String fromCategory = categoryOfDisplayedTask.getValue();
        ApplicationAlert.CHOOSE_CATEGORY_TO_MOVE_TASKS_TO_DIALOG(fromCategory).showAndWait().ifPresent(response -> {
            String toCategory = response.toString();
            Task currentlyOpenedTask = App.getTaskByID(fromCategory, uniqueIDOfDisplayedTask.getValue());

            App.moveTasksToCategory(currentlyOpenedTask, fromCategory, toCategory);
            categoryOfDisplayedTask.setValue(toCategory);

            //if present, delete currently opened task from left panel list view
            displayedTasks.remove(currentlyOpenedTask);
        });
    }

    /**
     * Method to delete currently displayed task in center panel of main window.
     *
     * @param actionEvent
     */
    public void deleteCurrentlyOpenedTask(ActionEvent actionEvent) {
        ApplicationAlert.CONFIRM_TASKS_DELETION_ALERT().showAndWait().ifPresent(response -> {
            if (response.getButtonData().equals(ButtonBar.ButtonData.YES)) {
                String fromCategory = categoryOfDisplayedTask.getValue();
                Task currentlyOpenedTask = App.getTaskByID(fromCategory, uniqueIDOfDisplayedTask.getValue());

                App.deleteTaskFromCategory(currentlyOpenedTask, fromCategory);

                //if present, delete currently opened task from left panel list view
                displayedTasks.remove(currentlyOpenedTask);

                //select another task from left panel if not empty, otherwise update center panel with default view
                if (!displayedTasks.isEmpty()) {
                    Platform.runLater(() -> {
                        tasksListView.getSelectionModel().select(0);
                    });
                }
                else {
                    uniqueIDOfDisplayedTask.setValue("");
                }
            }
        });
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
            if (newSelectedCategory == null) {
                if (!categories.isEmpty()) {
                    categoriesComboBox.getSelectionModel().select(0);
                    String selectedCategory = categoriesComboBox.getSelectionModel().getSelectedItem().toString();
                    updateDisplayedTaskAfterNewCategorySelection(selectedCategory);
                }
                else {
                    Platform.runLater(() -> {
                        categoriesComboBox.getSelectionModel().clearSelection();
                    });
                }
            }
            else {
                updateDisplayedTaskAfterNewCategorySelection(newSelectedCategory.toString());
            }
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
        rootBorderPane.getStylesheets().add("/buttons.css");
        categories.setAll(App.getCategoriesForAccount());

        categoriesComboBox.setItems(categories);
        categoriesComboBox.setPromptText("Categories");

        categoriesComboBox.setCellFactory(lv -> new ListCell<String>() {
            private HBox graphic;

            //this is the constructor for the anonymous class
            {
                Label label = new Label();
                label.textProperty().bind(itemProperty());
                //set max width to infinity so the buttons are displayed at the right of ListCell
                label.setMaxWidth(Double.POSITIVE_INFINITY);
                //modify the hiding behavior of the ComboBox to allow clicking on the button,
                //ComboBox will hide when the label is clicked (i.e. item selected)
                label.setOnMouseClicked(event -> categoriesComboBox.hide());

                Button renameTaskButton = new Button();
                Button deleteTaskButton = new Button();
                renameTaskButton.setGraphic(new ImageView(new Image("/edit.png")));
                deleteTaskButton.setGraphic(new ImageView(new Image("/delete.png")));
                renameTaskButton.getStyleClass().add("rename-category-button");
                deleteTaskButton.getStyleClass().add("delete-category-button");

                renameTaskButton.setOnAction(event -> {
                    renameCategory(getItem());
                });

                deleteTaskButton.setOnAction(event -> {
                    deleteCategory(getItem());
                });

                //create HBox which will be used as ListCell graphic
                graphic = new HBox(label, renameTaskButton, deleteTaskButton);
                graphic.setHgrow(label, Priority.ALWAYS);
                setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
            }

            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(graphic);
                }
            }
        });

        //set a custom skin, otherwise the ComboBox disappears before the click on button is registered
        ComboBoxListViewSkin<String> skin = new ComboBoxListViewSkin<String>(categoriesComboBox);
        skin.setHideOnClick(false);
        categoriesComboBox.setSkin(skin);

        //since hide on click was disable, ComboBox has to be hide everytime when its selected item changes
        categoriesComboBox.getSelectionModel().selectedItemProperty().addListener((observableValue, oldValue, newValue) -> {
            categoriesComboBox.hide();
        });
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
                LocalDateTime creationLocalDateTime = Instant.ofEpochMilli(task.getTaskCreationTimestamp()).atZone(ZoneId.systemDefault()).toLocalDateTime();
                LocalDateTime deadlineLocalDateTime = Instant.ofEpochMilli(task.getTaskDeadlineTimestamp()).atZone(ZoneId.systemDefault()).toLocalDateTime();

                //get task text which will be displayed in ListView cell,
                //if task name or text is longer than 25 characters, show just first 25 characters followed by dots
                String taskName = task.getName().length() > 25 ? task.getName().toUpperCase().substring(0,25).concat("...")
                                                               : task.getName().toUpperCase();
                String delimiter = "-".repeat(45);
                String creationTime =  "CREATED: " + creationLocalDateTime.toLocalDate().toString() + " " + creationLocalDateTime.toLocalTime().truncatedTo(ChronoUnit.MINUTES).toString();
                String deadline = "DEADLINE: " + deadlineLocalDateTime.toLocalDate().toString() + " " + deadlineLocalDateTime.toLocalTime().truncatedTo(ChronoUnit.MINUTES).toString();

                return taskName + "\n"
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
                categoryOfDisplayedTask.setValue(categoriesComboBox.getSelectionModel().getSelectedItem().toString());
            }
        });

        tasksListView.setPlaceholder(new Label("Select one of the categories"));
    }

    private void updateDisplayedTaskAfterNewCategorySelection(String selectedCategoryName) {
        //get currently selected sorting option from GUI to obtain tasks in this order
        String sortingOption = sortTasksComboBox.getSelectionModel().getSelectedItem() == null ? null : sortTasksComboBox.getSelectionModel().getSelectedItem().toString();

        //no sorting option is currently selected, tasks will be obtained in their insertion order
        if (sortingOption == null) {
            displayedTasks.setAll(App.getTasksFromCategory(selectedCategoryName, SortingOptions.NONE));
        }
        else {
            displayedTasks.setAll(App.getTasksFromCategory(selectedCategoryName, sortingOption));
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
            displayedTasks.setAll(App.getTasksFromCategory(currentlySelectedCategory, selectedSortingOption));
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

        //init center panel with "No task is currently displayed view"
        if (lastOpenedTask == null) {
            initEmptyCenterPanel();
            return;
        }

        String lastOpenedTaskCategory = App.getLastOpenedTaskCategory();
        LocalDateTime creationLocalDateTime = Instant.ofEpochMilli(lastOpenedTask.getTaskCreationTimestamp()).atZone(ZoneId.systemDefault()).toLocalDateTime();
        LocalDateTime deadlineLocalDateTime = Instant.ofEpochMilli(lastOpenedTask.getTaskDeadlineTimestamp()).atZone(ZoneId.systemDefault()).toLocalDateTime();

        //initialize StringProperties of currently displayed task in center panel
        uniqueIDOfDisplayedTask = new SimpleStringProperty(lastOpenedTask.getTaskID());
        nameOfDisplayedTask = new SimpleStringProperty(lastOpenedTask.getName());
        categoryOfDisplayedTask = new SimpleStringProperty(lastOpenedTaskCategory);
        deadlineOfDisplayedTask = new SimpleStringProperty(deadlineLocalDateTime.toLocalDate().toString()  + " " + deadlineLocalDateTime.toLocalTime().truncatedTo(ChronoUnit.MINUTES).toString());
        isDisplayedTaskCompleted = new SimpleBooleanProperty(lastOpenedTask.getCompleted());

        //add listener to currently displayed task StringProperties so center panel can be dynamically changed
        uniqueIDOfDisplayedTask.addListener((observableValue, oldID, newID) -> {
            updateTaskViewInCenterPanel();
        });

        nameOfDisplayedTask.addListener((observableValue, oldName, newName) -> {
            taskNameLabel.setText(newName);
            //updateTaskViewInCenterPanel();
        });

        categoryOfDisplayedTask.addListener((observableValue, oldCategoryName, newCategoryName) -> {
            taskCategoryLabel.setText(newCategoryName);
            //updateTaskViewInCenterPanel();
        });

        deadlineOfDisplayedTask.addListener((observableValue, oldDeadline, newDeadline) -> {
            taskDeadlineDateLabel.setText(newDeadline);
            //updateTaskViewInCenterPanel();
        });

        //initialize center panel
        taskNameLabel.setText(lastOpenedTask.getName());
        taskCategoryLabel.setText(lastOpenedTaskCategory);
        taskCreationDateLabel.setText(creationLocalDateTime.toLocalDate().toString() + " " + creationLocalDateTime.toLocalTime().truncatedTo(ChronoUnit.MINUTES).toString());
        taskDeadlineDateLabel.setText(deadlineLocalDateTime.toLocalDate().toString() + " " + deadlineLocalDateTime.toLocalTime().truncatedTo(ChronoUnit.MINUTES).toString());
        taskView.getEngine().loadContent(lastOpenedTask.getText());

        taskView.setDisable(true);
       // taskView.addEventFilter(KeyEvent.ANY, KeyEvent::consume);
    }

    /**
     * Method to init center panel of main window with "No task is currently displayed" view
     * when account does not contain any taks to display or no task was previously selected
     */
    private void initEmptyCenterPanel() {
        //initialize StringProperties of currently displayed task in center panel with empty values
        uniqueIDOfDisplayedTask = new SimpleStringProperty("");
        nameOfDisplayedTask = new SimpleStringProperty("");
        categoryOfDisplayedTask = new SimpleStringProperty("");
        deadlineOfDisplayedTask = new SimpleStringProperty("");
        isDisplayedTaskCompleted = new SimpleBooleanProperty(true);

        //method will update center panel with "No task is currently displayed" view
        updateTaskViewInCenterPanel();
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
            taskView.getEngine().loadContent("<p style=\"text-align: center;\">It looks like you deleted the previously displayed task or you currently don't have any existing task</p>\n"
                                             + "<p style=\"text-align: center;\">Feel free to selected another task from the left panel of the "
                                             + "application or create a completely new one to keep track of your duties :)</p>");
            taskView.setDisable(true);
            isDisplayedTaskCompleted.setValue(true);
        }
        else {
            Task currentlySelectedTask = (Task)tasksListView.getSelectionModel().getSelectedItem();

            //update values of StringProperty attributes so changes can be made automatically
            LocalDateTime deadlineLocalDateTime = Instant.ofEpochMilli(currentlySelectedTask.getTaskDeadlineTimestamp()).atZone(ZoneId.systemDefault()).toLocalDateTime();
            LocalDateTime creationLocalDateTime = Instant.ofEpochMilli(currentlySelectedTask.getTaskCreationTimestamp()).atZone(ZoneId.systemDefault()).toLocalDateTime();

            uniqueIDOfDisplayedTask.setValue(currentlySelectedTask.getTaskID());
            nameOfDisplayedTask.setValue(currentlySelectedTask.getName());
            categoryOfDisplayedTask.setValue(categoriesComboBox.getSelectionModel().getSelectedItem().toString());
            deadlineOfDisplayedTask.setValue(deadlineLocalDateTime.toLocalDate().toString() + " " + deadlineLocalDateTime.toLocalTime().truncatedTo(ChronoUnit.MINUTES).toString());
            isDisplayedTaskCompleted.setValue(currentlySelectedTask.getCompleted());

            //update rest of elements from center panel
            taskCreationDateLabel.setText(creationLocalDateTime.toLocalDate().toString() + " " + creationLocalDateTime.toLocalTime().truncatedTo(ChronoUnit.MINUTES).toString());
            taskView.getEngine().loadContent(currentlySelectedTask.getText());
            taskView.setDisable(true);

            taskCategoryLabel.setText(categoriesComboBox.getSelectionModel().getSelectedItem().toString());
        }
    }

    /**
     * Method to init right panel of main window of application. This panel contains buttons which can be used to
     * perform actions on currently displayed task (e.g. editing, moving or deleting it). This method set listeners
     * to disable this button when no task is currently selected.
     */
    private void initRightPanel() {
        markTaskAsCompletedButton.disableProperty().bind(isDisplayedTaskCompleted);
        editTaskButton.disableProperty().bind(Bindings.isEmpty(uniqueIDOfDisplayedTask));
        moveTaskButton.disableProperty().bind(Bindings.isEmpty(uniqueIDOfDisplayedTask));
        deleteTaskButton.disableProperty().bind(Bindings.isEmpty(uniqueIDOfDisplayedTask));
    }

    /**
     * Method for moving to edit task window of application and its initialization with currently opened task
     */
    private void initializeEditTaskWindow() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            InputStream sceneInputStream = App.class.getClassLoader().getResourceAsStream("edit_task_window_scene.fxml");
            Parent root = fxmlLoader.load(sceneInputStream);

            EditTaskWindowSceneController controller = fxmlLoader.getController();
            controller.init();

            App.setNewMainSceneParentElement(root);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Method for moving to create task window of application and its initialization
     */
    private void initializeCreateTaskWindow() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            InputStream sceneInputStream = App.class.getClassLoader().getResourceAsStream("create_new_task_window_scene.fxml");
            Parent root = fxmlLoader.load(sceneInputStream);

            CreateNewTaskWindowSceneController controller = fxmlLoader.getController();
            controller.init();

            App.setNewMainSceneParentElement(root);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Method for moving to manual window of application and its initialization
     */
    private void initializeManualWindow() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            InputStream sceneInputStream = App.class.getClassLoader().getResourceAsStream("manual_window_scene.fxml");
            Parent root = fxmlLoader.load(sceneInputStream);

            ManualWindowSceneController controller = fxmlLoader.getController();
            controller.init();

            App.setNewMainSceneParentElement(root);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Method for moving to settings window of application and its initialization
     */
    private void initializeSettingsWindow() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            InputStream sceneInputStream = App.class.getClassLoader().getResourceAsStream("settings_window_scene.fxml");
            Parent root = fxmlLoader.load(sceneInputStream);

            SettingsWindowSceneController controller = fxmlLoader.getController();
            controller.init();

            App.setNewMainSceneParentElement(root);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Method to rename category
     *
     * @param oldCategoryName name of category which name will be changed
     */
    private void renameCategory(String oldCategoryName) {
        if (oldCategoryName.equals("Completed tasks")) {
            ApplicationAlert.ALERT_WITH_CUSTOM_MESSAGE(ApplicationAlert.COMPLETED_TASKS_CATEGORY_CAN_NOT_BE_RENAMED_MESSAGE).showAndWait();
            return;
        }

        ApplicationAlert.RENAME_CATEGORY_DIALOG(oldCategoryName).showAndWait().ifPresent(response -> {
            if (App.doesCategoryAlreadyExist(response.toString())) {
                ApplicationAlert.ALERT_WITH_CUSTOM_MESSAGE(ApplicationAlert.CATEGORY_WITH_SAME_NAME_ALREADY_EXISTS_MESSAGE).showAndWait();
            }
            else {
                String newCategoryName = response.toString();

                //change category name in combo box in left panel
                categories.set(categories.indexOf(oldCategoryName),
                               newCategoryName);

                //check whether currently opened task is not in category which is being renamed
                if (categoryOfDisplayedTask.get().equals(oldCategoryName)) {
                    categoryOfDisplayedTask.setValue(newCategoryName);
                }

                App.renameCategory(oldCategoryName, newCategoryName);
                ApplicationAlert.ALERT_WITH_CUSTOM_MESSAGE(ApplicationAlert.CATEGORY_NAME_SUCCESSFULLY_CHANGED_MESSAGE).showAndWait();
            }
        });
    }

    /**
     * Method to delete category. All tasks which are currently in category will be deleted too
     *
     * @param categoryName name of category which name will be changed
     */
    private void deleteCategory(String categoryName) {
        if (categoryName.equals("Completed tasks")) {
            ApplicationAlert.ALERT_WITH_CUSTOM_MESSAGE(ApplicationAlert.COMPLETED_TASKS_CATEGORY_CAN_NOT_BE_DELETED_MESSAGE).showAndWait();
            return;
        }

        ApplicationAlert.CONFIRM_CATEGORY_DELETION_ALERT().showAndWait().ifPresent(response -> {
            if (response.getButtonData().equals(ButtonBar.ButtonData.YES)) {
                App.deleteCategory(categoryName);

                //check whether currently opened task is not in category which is being deleted
                if (categoryOfDisplayedTask.get().equals(categoryName)) {
                    uniqueIDOfDisplayedTask.setValue("");
                }

                //remove category from observable list
                categories.remove(categoryName);

                //remove displayed tasks if currently selected category is being deleted
                if (categoriesComboBox.getSelectionModel().getSelectedItem() != null
                    && categoriesComboBox.getSelectionModel().getSelectedItem().toString().equals(categoryName)) {
                    displayedTasks.removeAll();
                }
            }
        });
    }
}
