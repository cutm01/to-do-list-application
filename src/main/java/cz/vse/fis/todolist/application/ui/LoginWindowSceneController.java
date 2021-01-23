package cz.vse.fis.todolist.application.ui;

import cz.vse.fis.todolist.application.main.App;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.util.Optional;

/**
 * LoginWindowSceneController class contains methods to check user's login credentials. After user provides valid
 * username and password he is redirected to the main window of application.
 *
 * @version 1.0.0
 */
public class LoginWindowSceneController {
    //scene elements
    public TextField usernameTextField;
    public PasswordField passwordField;

    /**
     * Method to show password hint to user. It shows JavaFX alert to user with
     * password hint which was set during registration or inform him that no
     * password hint is set
     *
     * @param actionEvent
     */
    public void showPasswordHint(ActionEvent actionEvent) {
        String userName = usernameTextField.getText();
        if (userName.isEmpty()) {
            ApplicationAlert.USERNAME_FIELD_IS_EMPTY().showAndWait();
        }
        else {
            if (App.isPasswordHintSet(userName)) {
                ApplicationAlert.SHOW_PASSWORD_HINT(App.getPasswordHintForUser(userName)).showAndWait();
            }
            else {
                ApplicationAlert.PASSWORD_HINT_NOT_SET().showAndWait();
            }
        }
    }

    /**
     * Method to validate login credentials provided by user. User is moved to main window of application
     * after providing valid credentials or pop-up alert is shown in case of invalid credentials or missing
     * username or password
     *
     * @param actionEvent
     */
    public void validateLoginCredentials(ActionEvent actionEvent) {
        String username = usernameTextField.getText();
        String password = passwordField.getText();

        if (username.isEmpty()) {
            ApplicationAlert.USERNAME_FIELD_IS_EMPTY().showAndWait();
        }
        else if (password.isEmpty()) {
            ApplicationAlert.PASSWORD_FIELD_IS_EMPTY().showAndWait();
        }
        else {
            boolean areLoginCredentialsValid = App.validateLoginCredentials(username, password);
            if (areLoginCredentialsValid) {
                usernameTextField.clear();
                passwordField.clear();
                App.activateScene("main");
            }
            else {
                ApplicationAlert.INVALID_USER_CREDENTIALS().showAndWait();
            }
        }
    }

    public void showRegisterWindow(ActionEvent actionEvent) {
    }

    public void closeApplication(ActionEvent actionEvent) {
    }
}
