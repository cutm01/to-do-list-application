package cz.vse.fis.todolist.application.ui;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;

/**
 * Popup alerts class contains all possible JavaFX alerts which can be shown to user
 *
 * @version 1.0.0
 */
public class ApplicationAlert {
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
     * Method to create JavaFX alert in cases when user wants to know his password hint
     * and password hint was set
     *
     * @return Alert showing user his password hint which was set during registration
     */
    public static final Alert SHOW_PASSWORD_HINT_ALERT(String passwordHint) {
        return ALERT_WITH_CUSTOM_MESSAGE(PASSWORD_HINT_IS_SET_MESSAGE + passwordHint);
    }
}
