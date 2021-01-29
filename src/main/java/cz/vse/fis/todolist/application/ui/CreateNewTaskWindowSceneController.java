package cz.vse.fis.todolist.application.ui;

import cz.vse.fis.todolist.application.logic.Task;
import cz.vse.fis.todolist.application.main.App;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.web.HTMLEditor;

import java.io.IOException;
import java.io.InputStream;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Locale;
import java.util.regex.Pattern;

/**
 * CreateNewTaskWindowSceneController class with methods for creating new task
 *
 * @version 1.0.0
 */
public class CreateNewTaskWindowSceneController {
    //scene elements
    public BorderPane rootBorderPane;
    public ComboBox chooseCategoryComboBox;
    public TextField taskNameTextField;
    public DatePicker deadlineDatePicker;
    public TextField deadlineTimeTextField;
    public HTMLEditor newTaskHTMLEditor;
    public Button createNewTaskButton;

    private static final Locale MY_LOCALE = Locale.getDefault(Locale.Category.FORMAT);
    private static final Pattern WHITE_SPACES_ONLY_REGEX = Pattern.compile("\\s*");
    private static final Pattern DEADLINE_TIME_REGEX = Pattern.compile("^(0[0-9]|1[0-9]|2[0-3]):[0-5][0-9]$"); //represents "HH:mm" time format

    /**
     * Method to initialize scene elements
     */
    public void init() {
        //init chooseCategoryComboBox
        ObservableList<String> categories = FXCollections.observableArrayList();
        categories.setAll(App.getCategoriesForAccount());
        //prevent moving to Completed tasks category, user should use "Mark task as done" button instead
        categories.remove("Completed tasks");
        chooseCategoryComboBox.setItems(categories);
        chooseCategoryComboBox.getSelectionModel().select(0);

        //init deadlineDatePicker and deadlineTimeTextField with actual date and time
        initDeadlineWithActualTime();

        //disable create new task button if one of fields is empty or is not in correct format
        bindCreateNewTaskButton();

        //text fields background is changed when they are empty or in invalid format
        addValidationListeners();

        //disable date picker editor so user has to pick up new date from date picker and can not provide it as String input
        deadlineDatePicker.getEditor().disableProperty().setValue(true);
    }

    /**
     * Method to move user back to main window of application
     *
     * @param actionEvent
     */
    public void showMainWindow(ActionEvent actionEvent) {
        ApplicationAlert.CHANGES_WILL_NOT_BE_SAVED_ALERT().showAndWait().ifPresent(response -> {
            if (response.getButtonData().equals(ButtonBar.ButtonData.YES)) {
                initializeMainWindow();
            }
        });
    }

    /**
     * Method to create new task
     *
     * @param actionEvent
     */
    public void createNewTask(ActionEvent actionEvent) {
        String[] deadlineTime = deadlineTimeTextField.getText().split(":"); //hours at index 0, minutes at index 1
        int deadlineHours = Integer.parseInt(deadlineTime[0]);
        int deadlineMinutes = Integer.parseInt(deadlineTime[1]);

        LocalDateTime deadlineLocalDateTime = LocalDateTime.of(deadlineDatePicker.getValue().getYear(),
                                                                  deadlineDatePicker.getValue().getMonth(),
                                                                  deadlineDatePicker.getValue().getDayOfMonth(),
                                                                  deadlineHours,
                                                                  deadlineMinutes)
                                                                .atZone(ZoneId.systemDefault())
                                                                .toLocalDateTime();
        ZonedDateTime newDeadlineZonedDateTime = deadlineLocalDateTime.atZone(ZoneId.systemDefault());
        long taskDeadlineTimestamp = newDeadlineZonedDateTime.toInstant().toEpochMilli();

        if (taskDeadlineTimestamp < System.currentTimeMillis()) {
            ApplicationAlert.ALERT_WITH_CUSTOM_MESSAGE(ApplicationAlert.DEADLINE_IS_SOONER_THAN_ACTUAL_TIME_MESSAGE).showAndWait();
        }
        else {
            String taskCategory = chooseCategoryComboBox.getSelectionModel().getSelectedItem().toString();

            Task createdTask = App.createNewTask(taskCategory,
                                                taskNameTextField.getText(),       //task name
                                                newTaskHTMLEditor.getHtmlText(),    //task text
                                                System.currentTimeMillis(),         //task creation timestamp
                                                taskDeadlineTimestamp,              //task deadline timestamp
                                                false);                            //task completed attribute

            App.setLastOpenedTask(taskCategory, createdTask.getTaskID());
            initializeMainWindow();
        }
    }

    /**
     * Method to bind disable property of create new task button to all possible changes which can occur
     * during user's interaction with application. New task can not be created if one of fields (e.g. task name)
     * is empty (or contains only white spaces) or does not meet correct format such as HH:mm
     */
    private void bindCreateNewTaskButton() {
        createNewTaskButton.disableProperty().bind(Bindings.when(
                //task name text field should not be empty or contains only whitespaces
                taskNameTextField.textProperty().isEqualTo("")
                        .or(patternTextFieldBinding(taskNameTextField, WHITE_SPACES_ONLY_REGEX))
                        //deadline time text field should match HH:mm pattern and can not be empty or contains only whitespaces
                        .or(deadlineTimeTextField.textProperty().isEqualTo(""))
                        .or(patternTextFieldBinding(deadlineTimeTextField, WHITE_SPACES_ONLY_REGEX))
                        .or(patternTextFieldBinding(deadlineTimeTextField, DEADLINE_TIME_REGEX).not()))
                                                         .then(true)
                                                         .otherwise(false));
    }

    /**
     * Method to add change listeners to all text fields. These listeners will set different background color when
     * text field input is empty or invalid (i.e. does not meet correct format)
     */
    private void addValidationListeners() {
        rootBorderPane.getStylesheets().add("/validation_error_style.css");

        //task name text field is empty right after initialization, therefore validation-error style class is set
        taskNameTextField.getStyleClass().add("validation-error");

        taskNameTextField.textProperty().addListener((observableValue, oldValue, newValue) -> {
            taskNameTextField.getStyleClass().remove("validation-error");

            if (newValue.matches("\\s*") || newValue.isEmpty()) {
                taskNameTextField.getStyleClass().add("validation-error");
            }
            else {
                taskNameTextField.getStyleClass().remove("validation-error");
            }
        });

        deadlineTimeTextField.textProperty().addListener((observableValue, oldValue, newValue) -> {
            deadlineTimeTextField.getStyleClass().remove("validation-error");

            if (newValue.matches("(0[0-9]|1[0-9]|2[0-3]):[0-5][0-9]")) {
                deadlineTimeTextField.getStyleClass().remove("validation-error");
            }
            else {
                deadlineTimeTextField.getStyleClass().add("validation-error");
            }
        });
    }

    /**
     * Method init deadlineDatePicker and deadlineTimeTextField with actual system date and time
     */
    private void initDeadlineWithActualTime() {
        LocalDateTime actualLocalDateTime = Instant.ofEpochMilli(System.currentTimeMillis()).atZone(ZoneId.systemDefault()).toLocalDateTime();

        //set localisation for deadlineDatePicker to English
        deadlineDatePicker.setOnShowing(e-> Locale.setDefault(Locale.Category.FORMAT,Locale.ENGLISH));
        deadlineDatePicker.setOnHiding(e-> Locale.setDefault(Locale.Category.FORMAT, MY_LOCALE));
        deadlineDatePicker.setOnAction(e-> Locale.setDefault(Locale.Category.FORMAT, MY_LOCALE));

        //set value to correspond with actual system time
        deadlineDatePicker.setValue(actualLocalDateTime.toLocalDate());
        deadlineTimeTextField.setText(String.format("%02d:%02d", actualLocalDateTime.getHour(), actualLocalDateTime.getMinute()));
    }

    /**
     * Method to validate text field input against regular expression. It creates BooleanBinding based on
     * whether input from TextField matches regular expression pattern or not
     *
     * @param textField text field which will be validated against regular expression
     * @param pattern Pattern representing regular expression which will be used for validation
     * @return BooleanBinding which can be used to disable buttons from scene
     */
    private BooleanBinding patternTextFieldBinding(TextField textField, Pattern pattern) {
        BooleanBinding binding = Bindings.createBooleanBinding(() ->
            pattern.matcher(textField.getText()).matches(), textField.textProperty());

        return binding;
    }

    /**
     * Method for moving to main window of application and its initialization with currently logged in user
     */
    private void initializeMainWindow() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            InputStream sceneInputStream = App.class.getClassLoader().getResourceAsStream("main_window_scene.fxml");
            Parent root = fxmlLoader.load(sceneInputStream);

            MainWindowSceneController controller = fxmlLoader.getController();
            controller.init();

            App.setNewMainSceneParentElement(root);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
