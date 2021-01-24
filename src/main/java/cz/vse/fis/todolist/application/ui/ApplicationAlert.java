package cz.vse.fis.todolist.application.ui;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
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
    private static final String CREATE_NEW_CATEGORY_DIALOG_TITLE = "Create new category";
    private static final String CREATE_NEW_CATEGORY_HEADER_TEXT = "Please enter name of the new category";
    private static final String NEW_CATEGORY_NAME_RESTRICTIONS = "Category name has to be between 1 and 30 characters long and can contain only alphanumeric characters, whitespaces or underscores";

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
     * Method to create JavaFX alert in cases when user wants to know his password hint
     * and password hint was set
     *
     * @return Alert showing user his password hint which was set during registration
     */
    public static final Alert SHOW_PASSWORD_HINT_ALERT(String passwordHint) {
        return ALERT_WITH_CUSTOM_MESSAGE(PASSWORD_HINT_IS_SET_MESSAGE + passwordHint);
    }
}
