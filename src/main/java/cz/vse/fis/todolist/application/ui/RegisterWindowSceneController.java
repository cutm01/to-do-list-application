package cz.vse.fis.todolist.application.ui;

import cz.vse.fis.todolist.application.main.App;
import javafx.event.ActionEvent;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

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

    public void createNewUserAccount(ActionEvent actionEvent) {
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
}
