package cz.vse.fis.todolist.application.ui;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;

/**
 * Popup alerts class contains all possible JavaFX alerts which can be shown to user
 *
 * @version 1.0.0
 */
public class ApplicationAlert {
    private static final String USERNAME_FIELD_IS_EMPTY_MESSAGE = "Please provide a username";
    private static final String PASSWORD_FIELD_IS_EMPTY_MESSAGE = "Please provide a password";
    private static final String INVALID_USER_CREDENTIALS_MESSAGE = "User credentials are not valid";

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
     * Method to create base alert
     *
     * @param alertMessage String used as alert message
     * @return instance of Alert with AlertType.None and ButtonType.CLOSE
     */
    private static Alert alertTypeNone(String alertMessage) {
        return new Alert(Alert.AlertType.NONE, alertMessage, ButtonType.CLOSE);
    }
}
