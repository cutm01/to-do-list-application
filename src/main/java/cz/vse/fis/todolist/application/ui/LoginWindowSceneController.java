package cz.vse.fis.todolist.application.ui;

import cz.vse.fis.todolist.application.main.App;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.io.InputStream;
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
            ApplicationAlert.ALERT_WITH_CUSTOM_MESSAGE(ApplicationAlert.USERNAME_FIELD_IS_EMPTY_MESSAGE).showAndWait();
        }
        else {
            if (App.isPasswordHintSet(userName)) {
                ApplicationAlert.SHOW_PASSWORD_HINT_ALERT(App.getPasswordHintForUser(userName)).showAndWait();
            }
            else {
                ApplicationAlert.ALERT_WITH_CUSTOM_MESSAGE(ApplicationAlert.PASSWORD_HINT_NOT_SET_MESSAGE).showAndWait();
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
            ApplicationAlert.ALERT_WITH_CUSTOM_MESSAGE(ApplicationAlert.USERNAME_FIELD_IS_EMPTY_MESSAGE).showAndWait();
        }
        else if (password.isEmpty()) {
            ApplicationAlert.ALERT_WITH_CUSTOM_MESSAGE(ApplicationAlert.PASSWORD_FIELD_IS_EMPTY_MESSAGE).showAndWait();
        }
        else {
            boolean areLoginCredentialsValid = App.validateLoginCredentials(username, password);
            if (areLoginCredentialsValid) {
                usernameTextField.clear();
                passwordField.clear();

                initializeMainWindow();
            }
            else {
                ApplicationAlert.ALERT_WITH_CUSTOM_MESSAGE(ApplicationAlert.INVALID_USER_CREDENTIALS_MESSAGE).showAndWait();
            }
        }
    }

    /**
     * Method moves user to registration window of application where he can create new account
     *
     * @param actionEvent
     */
    public void showRegisterWindow(ActionEvent actionEvent) {
        usernameTextField.clear();
        passwordField.clear();
        App.activateScene("register");
    }

    /**
     * Method to close application from login window of application
     *
     * @param actionEvent
     */
    public void closeApplication(ActionEvent actionEvent) {
        Platform.exit();
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
