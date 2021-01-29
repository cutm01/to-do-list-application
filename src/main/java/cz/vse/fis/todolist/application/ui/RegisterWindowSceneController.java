package cz.vse.fis.todolist.application.ui;

import cz.vse.fis.todolist.application.main.App;
import javafx.event.ActionEvent;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;

/**
 * RegisterWindowSceneController class contains methods to create new user account
 *
 * @version 1.0.0
 */
public class RegisterWindowSceneController {
    //scene elements
    public TextField usernameTextField;
    public PasswordField passwordField;
    public PasswordField confirmPasswordField;
    public TextField passwordHintTextField;

    /**
     * Method to create new user account
     *
     * @param actionEvent
     */
    public void createNewUserAccount(ActionEvent actionEvent) {
        String username = usernameTextField.getText().strip();
        String password = passwordField.getText();
        String confirmPassword = confirmPasswordField.getText();
        String passwordHint = passwordHintTextField.getText().strip();

        if (username.isEmpty()) {
            ApplicationAlert.ALERT_WITH_CUSTOM_MESSAGE(ApplicationAlert.USERNAME_FIELD_IS_EMPTY_MESSAGE).showAndWait();
        }
        else if (password.isEmpty()) {
            ApplicationAlert.ALERT_WITH_CUSTOM_MESSAGE(ApplicationAlert.PASSWORD_FIELD_IS_EMPTY_MESSAGE).showAndWait();
        }
        else if (confirmPassword.isEmpty()) {
            ApplicationAlert.ALERT_WITH_CUSTOM_MESSAGE(ApplicationAlert.CONFIRM_PASSWORD_FIELD_IS_EMPTY_MESSAGE).showAndWait();
        }
        else {
            if (areRegistrationFieldsValid(username, password, confirmPassword, passwordHint)) {
                App.createNewAccount(username, password, confirmPassword, passwordHint);
                ApplicationAlert.ALERT_WITH_CUSTOM_MESSAGE(ApplicationAlert.NEW_ACCOUNT_SUCCESSFULLY_CREATED_MESSAGE).showAndWait();

                usernameTextField.clear();
                passwordField.clear();
                confirmPasswordField.clear();
                passwordHintTextField.clear();

                App.activateScene("login");
            }
        }
    }

    /**
     * Method to move user back to login window of application
     *
     * @param actionEvent
     */
    public void showLoginWindow(ActionEvent actionEvent) {
        usernameTextField.clear();
        passwordField.clear();
        confirmPasswordField.clear();
        passwordHintTextField.clear();

        App.activateScene("login");
    }

    /**
     * Method to validate all registration fields from user when creating new account. Username has to be alphanumeric String between
     * 6 and 25 characters long and account with same username can not already exist, input from password and confirm password
     * fields has to be same and at least 10 characters long, password hint (if set) has to be different from password
     *
     * @param username input from username field
     * @param password input from password field
     * @param confirmPassword input from confirm password field
     * @param passwordHint input from password hint field
     * @return true if registration fields are valid, false otherwise
     */
    private boolean areRegistrationFieldsValid(String username, String password, String confirmPassword, String passwordHint) {
        //check for username length between 6 and 25 characters
        if (username.length() < 6 || username.length() > 25) {
            ApplicationAlert.ALERT_WITH_CUSTOM_MESSAGE(ApplicationAlert.INVALID_USERNAME_LENGTH_MESSAGE).showAndWait();
            return false;
        }

        //check if account with same username does not already exist
        if (App.doesAccountAlreadyExist(username)) {
            ApplicationAlert.ALERT_WITH_CUSTOM_MESSAGE(ApplicationAlert.ACCOUNT_WITH_SAME_USERNAME_ALREADY_EXIST_MESSAGE).showAndWait();
            return false;
        }

        //check if username is alphanumeric
        if (!username.matches("[a-zA-Z0-9]*")) {
            ApplicationAlert.ALERT_WITH_CUSTOM_MESSAGE(ApplicationAlert.USERNAME_IS_NOT_ALPHANUMERIC_MESSAGE).showAndWait();
            return false;
        }

        //check if password length is at least 10
        if (password.length() < 10) {
            ApplicationAlert.ALERT_WITH_CUSTOM_MESSAGE(ApplicationAlert.INVALID_PASSWORD_LENGTH_MESSAGE).showAndWait();
            return false;
        }

        //check if passwords match
        if (!password.equals(confirmPassword)) {
            ApplicationAlert.ALERT_WITH_CUSTOM_MESSAGE(ApplicationAlert.PASSWORDS_DO_NOT_MATCH_MESSAGE).showAndWait();
            return false;
        }

        //check if password hint is not equal to password
        if (passwordHint.equals(password)) {
            ApplicationAlert.ALERT_WITH_CUSTOM_MESSAGE(ApplicationAlert.PASSWORD_HINT_SAME_AS_PASSWORD_MESSAGE).showAndWait();
            return false;
        }

        return true;
    }

}
