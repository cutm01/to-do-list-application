package cz.vse.fis.todolist.application.ui;

import cz.vse.fis.todolist.application.logic.Task;
import cz.vse.fis.todolist.application.main.App;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.StringProperty;
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
import javafx.util.StringConverter;

import java.io.IOException;
import java.io.InputStream;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.regex.Pattern;

/**
 * EditTaskWindowSceneController class with methods for editing existing task
 *
 * @version 1.0.0
 */
public class EditTaskWindowSceneController {
    //scene elements
    public BorderPane rootBorderPane;
    public ComboBox chooseCategoryComboBox;
    public TextField taskNameTextField;
    public DatePicker deadlineDatePicker;
    public TextField deadlineTimeTextField;
    public HTMLEditor taskHTMLEditor;
    public Button saveChangesButton;

    private static final Locale MY_LOCALE = Locale.getDefault(Locale.Category.FORMAT);
    private static final Pattern WHITE_SPACES_ONLY_REGEX = Pattern.compile("\\s*");
    private static final Pattern DEADLINE_TIME_REGEX = Pattern.compile("^(0[0-9]|1[0-9]|2[0-3]):[0-5][0-9]$"); //represents "HH:mm" time format
    private static final Pattern DEADLINE_DATE_REGEX = Pattern.compile(""); //represent "d. M. yyyy" date format
    /**
     * Method to initialize scene elements to correspond with attributes of task which is currently being edited
     */
    public void init() {
        //fill in attributes of edited task such as category, name, deadline or text
        initTaskView();
        //disable save changes button if one of fields is empty or is not in correct format
        bindSaveChangesButton();
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
     * Method to save changes which was done during editing the task
     *
     * @param actionEvent
     */
    public void saveChanges(ActionEvent actionEvent) {

    }

    /**
     * Method to init all scene elements to correspond with task which is currently being edited. ComboBox is set to
     * to task category, text fields are set to task name, deadline date and time and HTML editor is set to
     * task text
     */
    private void initTaskView() {
        Task lastOpenedTask = App.getLastOpenedTask();
        String lastOpenedTaskCategory = App.getLastOpenedTaskCategory();

        //init chooseCategoryComboBox
        ObservableList<String> categories = FXCollections.observableArrayList();
        categories.setAll(App.getCategoriesForAccount());
        //prevent moving to Completed tasks category, user should use "Mark task as done" button instead
        categories.remove("Completed tasks");
        chooseCategoryComboBox.setItems(categories);
        chooseCategoryComboBox.getSelectionModel().select(lastOpenedTaskCategory);

        //init taskNameTextField
        taskNameTextField.setText(lastOpenedTask.getName());

        //init deadlineDatePicker and deadlineTimeTextField
        //parse deadline timestamp to obtain day, month and time from it
        LocalDateTime localDateTime = Instant.ofEpochMilli(lastOpenedTask.getTaskDeadlineTimestamp()).atZone(ZoneId.systemDefault()).toLocalDateTime();

        //set localisation for deadlineDatePicker to English
        deadlineDatePicker.setOnShowing(e-> Locale.setDefault(Locale.Category.FORMAT,Locale.ENGLISH));
        deadlineDatePicker.setOnHiding(e-> Locale.setDefault(Locale.Category.FORMAT, MY_LOCALE));
        deadlineDatePicker.setOnAction(e-> Locale.setDefault(Locale.Category.FORMAT, MY_LOCALE));

        //set value to correspond with actually set deadline
        deadlineDatePicker.setValue(localDateTime.toLocalDate());
        deadlineTimeTextField.setText(localDateTime.getHour() + ":" + localDateTime.getMinute());

        //init html editor with task text
        taskHTMLEditor.setHtmlText(lastOpenedTask.getText());
    }

    /**
     * Method to bind disable property of save changes button to all possible changes which can occur
     * during user's interaction with application. Edited task can not be saved if one of fields (e.g. task name)
     * is empty (or contains only white spaces) or does not meet correct format such as HH:mm
     */
    private void bindSaveChangesButton() {
        saveChangesButton.disableProperty().bind(Bindings.when(
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

    /**
     * Method to add change listeners to all text fields. These listeners will set different background color when
     * text field input is empty or invalid (i.e. does not meet correct format)
     */
    private void addValidationListeners() {
        rootBorderPane.getStylesheets().add("/validation_error_style.css");

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
}

