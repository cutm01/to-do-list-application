package cz.vse.fis.todolist.application.ui;

import cz.vse.fis.todolist.application.main.App;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.util.Pair;

import java.util.Optional;

/**
 * Popup alerts class contains all possible JavaFX alerts which can be shown to user
 *
 * @version 1.0.0
 */
public class ApplicationAlert {
    private static final String NEW_CATEGORY_DIALOG_ICON = "/new_category_dialog_icon.png";

    //login window alert messages
    public static final String USERNAME_FIELD_IS_EMPTY_MESSAGE = "Please provide a username";
    public static final String PASSWORD_FIELD_IS_EMPTY_MESSAGE = "Please provide a password";
    public static final String INVALID_USER_CREDENTIALS_MESSAGE = "User credentials are not valid";
    public static final String PASSWORD_HINT_IS_SET_MESSAGE = "Your password hint is: ";
    public static final String PASSWORD_HINT_NOT_SET_MESSAGE = "Password hint is not set for this account or account does not exist";
    //register window alert messages
    public static final String CONFIRM_PASSWORD_FIELD_IS_EMPTY_MESSAGE = "Please confirm your password";
    public static final String NEW_ACCOUNT_SUCCESSFULLY_CREATED_MESSAGE = "New account was successfully created! You can now log in";
    public static final String INVALID_USERNAME_LENGTH_MESSAGE = "Username has to be between 6 and 25 characters long";
    public static final String ACCOUNT_WITH_SAME_USERNAME_ALREADY_EXIST_MESSAGE = "Account with same username already exists";
    public static final String USERNAME_IS_NOT_ALPHANUMERIC_MESSAGE = "Username can not contain whitespaces or non-alphanumerical characters such characters with diacritical marks";
    public static final String INVALID_PASSWORD_LENGTH_MESSAGE = "Password has to be at least 10 characters long";
    public static final String PASSWORDS_DO_NOT_MATCH_MESSAGE = "Passwords do not match, please try again";
    public static final String PASSWORD_HINT_SAME_AS_PASSWORD_MESSAGE = "Password hint has to be different from account password";
    //main window alert messages
    public static final String CATEGORY_WITH_SAME_NAME_ALREADY_EXISTS_MESSAGE = "Category with same name already exists";
    public static final String NEW_CATEGORY_SUCCESSFULLY_CREATED_MESSAGE = "New category was successfully created!";
    public static final String NO_TASK_WAS_SELECTED_TO_MARK_AS_COMPLETED_MESSAGE = "Please select at least one task which will be marked as completed";
    public static final String NO_TASK_WAS_SELECTED_TO_MOVE_TO_EXISTING_CATEGORY_MESSAGE = "Please select at least one task which will be moved to another existing category";
    public static final String NO_TASK_WAS_SELECTED_TO_MOVE_TO_NEW_CATEGORY_MESSAGE = "Please select at least one task which will be moved to new category";
    public static final String CATEGORY_WITH_SAME_NAME_ALREADY_EXISTS_NO_TASKS_MOVED_MESSAGE = "Category with same name already exists. No tasks were moved";
    public static final String TASK_SUCCESSFULLY_MOVED_TO_NEWLY_CREATED_CATEGORY_MESSAGE = "All selected tasks were successfully moved to newly created category";
    public static final String NO_TASK_WAS_SELECTED_TO_DELETE_MESSAGE = "Please select at least one task which will be deleted";
    public static final String CONFIRM_TASKS_DELETION_ALERT_MESSAGE = "Selected tasks will be definitely deleted. Do you want to continue?";
    private static final String CREATE_NEW_CATEGORY_DIALOG_TITLE = "Create new category";
    private static final String CREATE_NEW_CATEGORY_HEADER_TEXT = "Please enter name of the new category";
    private static final String NEW_CATEGORY_NAME_RESTRICTIONS = "Category name has to be between 1 and 30 characters long and can contain only alphanumeric characters, whitespaces or underscores";
    private static final String CHOOSE_CATEGORY_DIALOG_TITLE = "Move tasks to existing category";
    private static final String CHOOSE_CATEGORY_DIALOG_TEXT = "Please choose one category where tasks will be moved to";
    private static final String CREATE_NEW_CATEGORY_TO_MOVE_TASKS_DIALOG_TITLE = "Move tasks to new category";
    private static final String CREATE_NEW_CATEGORY_TO_MOVE_TASKS_HEADER_TEXT = "Please enter name of the new category where selected tasks will be moved to";

    /**
     * Method to create alert with AlertType.NONE and custom message to inform user about
     * wrong input field format, success of operation, etc.
     *
     * @param alertMessage String used as alert message
     * @return instance of Alert with AlertType.None and ButtonType.CLOSE
     */
    public static final Alert ALERT_WITH_CUSTOM_MESSAGE(String alertMessage) {
        Alert alert = new Alert(AlertType.NONE);
        alert.getDialogPane().setContent(new Label(alertMessage));
        alert.getButtonTypes().add(ButtonType.CLOSE);
        return alert;
    }

    /**
     * Method to create dialog for creating new category. User has to input name between 1 and 30 characters long
     * containing only alphanumeric characters, whitespaces or underscores
     *
     * @return dialog where user has to enter name of the new task category
     */
    public static final Dialog CREATE_NEW_CATEGORY_DIALOG() {
        Dialog<String> dialog = new Dialog<>();

        dialog.setTitle(CREATE_NEW_CATEGORY_DIALOG_TITLE);
        dialog.setHeaderText(CREATE_NEW_CATEGORY_HEADER_TEXT);
        dialog.setGraphic(new ImageView(ApplicationAlert.class.getResource(NEW_CATEGORY_DIALOG_ICON).toString()));

        //add buttons
        ButtonType createButtonType = new ButtonType("Create", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(createButtonType, ButtonType.CANCEL);

        //create grid pane with content
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField categoryName = new TextField();
        grid.add(new Label(NEW_CATEGORY_NAME_RESTRICTIONS), 0, 0);
        grid.add(categoryName, 0,1);

        //disable create button when no input was provided or category name does not meet criteria for length or format
        Node createButton = dialog.getDialogPane().lookupButton(createButtonType);
        createButton.setDisable(true);

        categoryName.textProperty().addListener((observable, oldValue, newValue) -> {
            createButton.setDisable(newValue.trim().isEmpty()
                                    || !newValue.trim().matches("[a-zA-Z0-9\\s_]{1,30}"));
        });

        dialog.getDialogPane().setContent(grid);

        //focus on category name text field
        Platform.runLater(categoryName::requestFocus);

        //get the result
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == createButtonType) {
                return categoryName.getText().trim();
            }
            return null;
        });

        return dialog;
    }

    /**
     * Method to create dialog for choosing category where selected tasks will be moved to. User has to choose
     * one existing category from dropdown list
     *
     * @param currentlySelectedCategory category which is currently opened and therefore task(s) can be moved there
     * @return dialog where user has to choose category where selected task will be moved to
     */
    public static final Dialog CHOOSE_CATEGORY_TO_MOVE_TASKS_TO_DIALOG(String currentlySelectedCategory) {
        Dialog<String> dialog = new Dialog<>();

        dialog.setTitle(CHOOSE_CATEGORY_DIALOG_TITLE);
        dialog.setHeaderText(CHOOSE_CATEGORY_DIALOG_TEXT);
        dialog.setGraphic(new ImageView(ApplicationAlert.class.getResource(NEW_CATEGORY_DIALOG_ICON).toString()));

        //add buttons
        ButtonType confirmSelectionButtonType = new ButtonType("Move tasks", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(confirmSelectionButtonType, ButtonType.CANCEL);

        //create grid pane with content
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        ObservableList<String> categories = FXCollections.observableArrayList(App.getCategoriesForAccount());
        categories.remove(currentlySelectedCategory);
        ComboBox chooseCategory = new ComboBox(categories);
        chooseCategory.setPromptText("Categories");
        grid.add(chooseCategory, 0, 0);

        //disable move tasks button when no category was selected so far
        Node moveTasksButton = dialog.getDialogPane().lookupButton(confirmSelectionButtonType);
        moveTasksButton.setDisable(true);

        chooseCategory.getSelectionModel().selectedItemProperty().addListener((observableValue, oldValue, newValue) -> {
            moveTasksButton.setDisable(observableValue == null);
        });

        dialog.getDialogPane().setContent(grid);

        //focus on choose category combo box
        Platform.runLater(chooseCategory::requestFocus);

        //get the result
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == confirmSelectionButtonType) {
                return chooseCategory.getSelectionModel().getSelectedItem().toString();
            }
            return null;
        });

        return dialog;
    }

    /**
     * Method to create dialog to provide new category name. This category will be created and all selected tasks will
     * be moved into it. User has to input name between 1 and 30 characters long containing only alphanumeric characters,
     * whitespaces or underscores
     *
     * @return dialog where user has to input new category name where selected tasks will be moved to
     */
    public static final Dialog CREATE_CATEGORY_TO_MOVE_TASKS_TO_DIALOG() {
        Dialog<String> dialog = new Dialog<>();

        dialog.setTitle(CREATE_NEW_CATEGORY_TO_MOVE_TASKS_DIALOG_TITLE);
        dialog.setHeaderText(CREATE_NEW_CATEGORY_TO_MOVE_TASKS_HEADER_TEXT);
        dialog.setGraphic(new ImageView(ApplicationAlert.class.getResource(NEW_CATEGORY_DIALOG_ICON).toString()));

        //add buttons
        ButtonType createButtonType = new ButtonType("Create category and move tasks", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(createButtonType, ButtonType.CANCEL);

        //create grid pane with content
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField categoryName = new TextField();
        grid.add(new Label(NEW_CATEGORY_NAME_RESTRICTIONS), 0, 0);
        grid.add(categoryName, 0,1);

        //disable create button when no input was provided or category name does not meet criteria for length or format
        Node createButton = dialog.getDialogPane().lookupButton(createButtonType);
        createButton.setDisable(true);

        categoryName.textProperty().addListener((observable, oldValue, newValue) -> {
            createButton.setDisable(newValue.trim().isEmpty()
                                    || !newValue.trim().matches("[a-zA-Z0-9\\s_]{1,30}"));
        });

        dialog.getDialogPane().setContent(grid);

        //focus on category name text field
        Platform.runLater(categoryName::requestFocus);

        //get the result
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == createButtonType) {
                return categoryName.getText().trim();
            }
            return null;
        });

        return dialog;
    }

    /**
     * Method to create alert to inform user that selected tasks are about to delete. User has to confirm
     * that he wants to delete selected, otherwise no action will be performed
     *
     * @return alert where user has to confirm that he want to delete all selected tasks
     */
    public static final Alert CONFIRM_TASKS_DELETION_ALERT() {
        Alert alert = new Alert(AlertType.NONE);
        alert.getDialogPane().setContent(new Label(CONFIRM_TASKS_DELETION_ALERT_MESSAGE));

        ButtonType confirmButtonType = new ButtonType("Delete selected tasks", ButtonBar.ButtonData.YES);
        alert.getButtonTypes().add(confirmButtonType);
        alert.getButtonTypes().add(ButtonType.CLOSE);

        return alert;
    }

    /**
     * Method to create JavaFX alert in cases when user wants to know his password hint
     * and password hint was set
     *
     * @return Alert showing user his password hint which was set during registration
     */
    public static final Alert SHOW_PASSWORD_HINT_ALERT(String passwordHint) {
        return ALERT_WITH_CUSTOM_MESSAGE(PASSWORD_HINT_IS_SET_MESSAGE + passwordHint);
    }
}
