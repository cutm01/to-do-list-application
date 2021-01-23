package cz.vse.fis.todolist.application.ui;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;

/**
 * Popup alerts class contains all possible JavaFX alerts which can be shown to user
 *
 * @version 1.0.0
 */
public class ApplicationAlert {
    private static final String USERNAME_FIELD_IS_EMPTY_MESSAGE = "Please provide a username";
    private static final String PASSWORD_FIELD_IS_EMPTY_MESSAGE = "Please provide a password";
    private static final String INVALID_USER_CREDENTIALS_MESSAGE = "User credentials are not valid";
    private static final String PASSWORD_HINT_IS_SET_MESSAGE = "Your password hint is: ";
    private static final String PASSWORD_HINT_NOT_SET_MESSAGE = "Password hint is not set for this account or account does not exist";

    //alerts from login window of application
    /**
     * Method to create JavaFX alert in cases when user have not provided username
     *
     * @return Alert asking user to provide username
     */
    public static final Alert USERNAME_FIELD_IS_EMPTY() {
        return alertTypeNone(USERNAME_FIELD_IS_EMPTY_MESSAGE);
    }

    /**
     * Method to create JavaFX alert in cases when user have not provided password
     *
     * @return Alert asking user to provide password
     */
    public static final Alert PASSWORD_FIELD_IS_EMPTY() {
        return alertTypeNone(PASSWORD_FIELD_IS_EMPTY_MESSAGE);
    }

    /**
     * Method to create JavaFX alert in cases when user have not provided
     * valid credentials (i.e username and password)
     *
     * @return Alert informing user that he provided invalid credentials
     */
    public static final Alert INVALID_USER_CREDENTIALS() {
        return alertTypeNone(INVALID_USER_CREDENTIALS_MESSAGE);
    }

    /**
     * Method to create JavaFX alert in cases when user wants to know his password hint
     * and password hint was set
     *
     * @return Alert showing user his password hint which was set during registration
     */
    public static final Alert SHOW_PASSWORD_HINT(String passwordHint) {
        return alertTypeNone(PASSWORD_HINT_IS_SET_MESSAGE + passwordHint);
    }

    /**
     * Method to create JavaFX alert in cases when user wants to know his password hint
     * and password hint was not set
     *
     * @return Alert informing user that no password hint was set during registration
     */
    public static final Alert PASSWORD_HINT_NOT_SET() {
        return alertTypeNone(PASSWORD_HINT_NOT_SET_MESSAGE);
    }

    /**
     * Method to create base alert
     *
     * @param alertMessage String used as alert message
     * @return instance of Alert with AlertType.None and ButtonType.CLOSE
     */
    private static Alert alertTypeNone(String alertMessage) {
        Alert alert = new Alert(AlertType.NONE);
        alert.getDialogPane().setContent(new Label(alertMessage));
        alert.getButtonTypes().add(ButtonType.CLOSE);
        return alert;
    }
}
