package cz.vse.fis.todolist.application.ui;

import cz.vse.fis.todolist.application.logic.Avatar;
import cz.vse.fis.todolist.application.logic.ReadUpdateFile;
import cz.vse.fis.todolist.application.main.App;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.css.Stylesheet;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * SettingsWindowSceneController class contains methods to change user password, password hint, avatar or to delete
 * user account
 *
 * @version 1.0.0
 */
public class SettingsWindowSceneController {
    //scene elements
    public BorderPane borderPane;
    public ListView settingsMenuListView;
    public VBox settingsContent;

    //change password
    public PasswordField oldPasswordField;
    public PasswordField newPasswordField;
    public PasswordField confirmNewPasswordField;
    public Button changePasswordButton;

    //change password hint
    public TextField newPasswordHintTextField;
    public TextField confirmNewPasswordHintTextField;
    public PasswordField passwordField;
    public Button changePasswordHintButton;

    //avatar
    public ImageView maleAvatarImageView;
    public ImageView femaleAvatarImageView;
    public RadioButton selectMaleAvatarRadioButton;
    public RadioButton selectFemaleAvatarRadioButton;
    public Button saveAvatarChangeButton;

    //delete account
    public PasswordField deleteAccountPasswordField;
    public Button deleteAccountButton;

    private static Map<String, Pane> subwindows;
    private static final Pattern WHITE_SPACES_ONLY_REGEX = Pattern.compile("\\s*");
    private ObservableList<String> settingsMenuItems = FXCollections.observableArrayList();

    /**
     * Method creates all scene for settings.
     */
    public void init() {
        //load all scenes for easier switching between them later
        subwindows = loadSubwindows();

        //filling left panel
        for (SettingsMenuItem item : SettingsMenuItem.values()) {
            settingsMenuItems.add(item.toString());
        }

        settingsMenuListView.setItems(settingsMenuItems);

        settingsMenuListView.setCellFactory(listCell -> new ListCell<String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setText(null);
                    setGraphic(null);
                } else {
                    setGraphic(new Label(item));
                }
            }
        });

        //select firs item from left menu as default value
        changeContent("Change password");

        //change listeners for list view in left panel
        settingsMenuListView.getSelectionModel().selectedItemProperty().addListener((obsVal, oldVal, newVal) -> {
            if (newVal != null) {changeContent(newVal.toString());}
        });
    }

    public void showMainWindow(ActionEvent actionEvent) {
        initializeMainWindow();
    }

    /**
     * Method represents the logic of changing the password.
     * @param actionEvent
     */
    public void changePassword(ActionEvent actionEvent) {
        //wrong password
        if (!App.validateLoginCredentials(App.getUsername(), oldPasswordField.getText())) {
            ApplicationAlert.ALERT_WITH_CUSTOM_MESSAGE(ApplicationAlert.PROVIDED_PASSWORD_IS_INCORRECT_MESSAGE).showAndWait();
            return;
        }

        //new password shorter than 10 characters
        if (newPasswordField.getText().length() < 10) {
            ApplicationAlert.ALERT_WITH_CUSTOM_MESSAGE(ApplicationAlert.INVALID_PASSWORD_LENGTH_MESSAGE).showAndWait();
            return;
        }

        //passwords do not match
        if (!(newPasswordField.getText().equals(confirmNewPasswordField.getText()))) {
            ApplicationAlert.ALERT_WITH_CUSTOM_MESSAGE(ApplicationAlert.PASSWORDS_DO_NOT_MATCH_MESSAGE).showAndWait();
            return;
        }

        String newPassword = newPasswordField.getText();
        App.changePassword(newPassword);
        ApplicationAlert.ALERT_WITH_CUSTOM_MESSAGE(ApplicationAlert.PASSWORD_SUCCESSFULLY_CHANGED_MESSAGE).showAndWait();
    }

    /**
     * Method represents the logic of changing the password hint.
     * @param actionEvent
     */
    public void changePasswordHint(ActionEvent actionEvent) {
        //password hint has to be different from actual password
        if (newPasswordHintTextField.getText().equals(App.getPasswordForCurrentlyLogedInUser())) {
            ApplicationAlert.ALERT_WITH_CUSTOM_MESSAGE(ApplicationAlert.PASSWORD_HINT_SAME_AS_PASSWORD_MESSAGE).showAndWait();
            return;
        }

        //wrong password
        if (!App.validateLoginCredentials(App.getUsername(), passwordField.getText())) {
            ApplicationAlert.ALERT_WITH_CUSTOM_MESSAGE(ApplicationAlert.PROVIDED_PASSWORD_IS_INCORRECT_MESSAGE).showAndWait();
            return;
        }

        //new password hint and its confirmation has to be same
        if (!newPasswordHintTextField.getText().equals(confirmNewPasswordHintTextField.getText())) {
            ApplicationAlert.ALERT_WITH_CUSTOM_MESSAGE(ApplicationAlert.PASSWORD_HINTS_DO_NOT_MATCH_MESSAGE).showAndWait();
            return;
        }

        String newPasswordHint = newPasswordHintTextField.getText();
        App.changePasswordHint(newPasswordHint);
        ApplicationAlert.ALERT_WITH_CUSTOM_MESSAGE(ApplicationAlert.NEW_PASSWORD_HINT_SUCCESSFULLY_CHANGED_MESSAGE).showAndWait();
    }

    /**
     * Method represents the logic of selection and setting the avatar.
     * @param actionEvent
     */
    public void saveAvatarChange(ActionEvent actionEvent) {
        if (selectMaleAvatarRadioButton.isSelected()) {
            App.changeAvatar(Avatar.MALE.toString());
        }
        else {
            App.changeAvatar(Avatar.FEMALE.toString());
        }
        ApplicationAlert.ALERT_WITH_CUSTOM_MESSAGE(ApplicationAlert.AVATAR_CHANGE_SUCCESS_MESSAGE).showAndWait();
    }

    /**
     * Method represents the logic of deleting account.
     * @param actionEvent
     */
    public void deleteAccount(ActionEvent actionEvent) {
        //wrong password
        if (!App.validateLoginCredentials(App.getUsername(), deleteAccountPasswordField.getText())) {
            ApplicationAlert.ALERT_WITH_CUSTOM_MESSAGE(ApplicationAlert.PROVIDED_PASSWORD_IS_INCORRECT_MESSAGE).showAndWait();
            return;
        }

        ApplicationAlert.ALERT_WITH_CUSTOM_MESSAGE(ApplicationAlert.ACCOUNT_WAS_SUCCESSFULLY_DELETED_MESSAGE).showAndWait();
        ReadUpdateFile.deleteFileWithUserData(App.getUsername());
        App.activateScene("login");
    }

    /**
     * Method represents logic of inicializeing window in fxml.
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
     * Method changing items in window.
     * @param selectedSettingsItem
     */
    private void changeContent(String selectedSettingsItem) {
        try {
            String sceneToLoad = selectedSettingsItem.toLowerCase().replace(' ', '_');

            FXMLLoader loader = new FXMLLoader();
            InputStream centerPanelInputStream = App.class.getClassLoader().getResourceAsStream(sceneToLoad + ".fxml");
            Parent root = loader.load(centerPanelInputStream);

            SettingsWindowSceneController controller = loader.getController();
            controller.initSubwindow(selectedSettingsItem);

            root.getStylesheets().add("/validation_error_style.css");

            borderPane.setCenter(root);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Method creating map of scenes.
     * @return scene
     */
    private Map<String, Pane> loadSubwindows() {
        Map<String, Pane> subwindows = new HashMap<>();
        String[] scenesNames = {"change_password", "change_password_hint", "avatar", "delete_account"};

        for (String scenesName : scenesNames) {
            try {
                String sceneToLoad = scenesName + ".fxml";
                FXMLLoader fxmlLoader = new FXMLLoader();
                InputStream sceneInputStream = App.class.getClassLoader().getResourceAsStream(sceneToLoad);
                subwindows.put(scenesName, fxmlLoader.load(sceneInputStream));
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }

        return subwindows;
    }

    /**
     * Method represent logic of actual scene at center panel.
     * If we click at avatar at the left panel, avatar would be at the center panel.
     * @param subwindowName
     */
    private void initSubwindow(String subwindowName) {
        //based on selected item from left panel, decide what should be displayed in center panel:
        switch (subwindowName) {
            case "Change password":
                initChangePasswordSubwindow();
                break;
            case "Change password hint":
                initChangePasswordHintSubwindow();
                break;
            case "Avatar":
                initChangeAvatarSubwindow();
                break;
            case "Delete account":
                initDeleteAccountSubwindow();
                break;
            default:
                initChangePasswordSubwindow();
        }
    }

    /**
     * Method represent fields of passwords.
     */
    private void initChangePasswordSubwindow() {
        //add css class which will highlight field with red color when it is empty
        oldPasswordField.getStyleClass().add("validation-error");
        newPasswordField.getStyleClass().add("validation-error");
        confirmNewPasswordField.getStyleClass().add("validation-error");

        oldPasswordField.textProperty().addListener((obsVal, oldVal, newVal) -> {
            oldPasswordField.getStyleClass().remove("validation-error");
            if (newVal.isEmpty()) {oldPasswordField.getStyleClass().add("validation-error");}
            else {oldPasswordField.getStyleClass().remove("validation-error");}
        });

        newPasswordField.textProperty().addListener((obsVal, oldVal, newVal) -> {
            newPasswordField.getStyleClass().remove("validation-error");
            if (newVal.isEmpty()) {newPasswordField.getStyleClass().add("validation-error");}
            else {newPasswordField.getStyleClass().remove("validation-error");}
        });

        confirmNewPasswordField.textProperty().addListener((obsVal, oldVal, newVal) -> {
            confirmNewPasswordField.getStyleClass().remove("validation-error");
            if (newVal.isEmpty()) {confirmNewPasswordField.getStyleClass().add("validation-error");}
            else {confirmNewPasswordField.getStyleClass().remove("validation-error");}
        });

        //bind changePasswordButton disabled property, button will be disabled if one of field above is empty
        changePasswordButton.disableProperty().bind(Bindings.when(
            oldPasswordField.textProperty().isEqualTo("")
            .or(newPasswordField.textProperty().isEqualTo(""))
            .or(confirmNewPasswordField.textProperty().isEqualTo("")))
            .then(true)
            .otherwise(false));
    }

    /**
     * Method represent fields of password hints.
     */
    private void initChangePasswordHintSubwindow() {
        //add css class which will highlight field with red color when it is empty
        newPasswordHintTextField.getStyleClass().add("validation-error");
        confirmNewPasswordHintTextField.getStyleClass().add("validation-error");
        passwordField.getStyleClass().add("validation-error");

        newPasswordHintTextField.textProperty().addListener((obsVal, oldVal, newVal) -> {
            newPasswordHintTextField.getStyleClass().remove("validation-error");
            if (newVal.isEmpty() || newVal.matches("\\s*")) {oldPasswordField.getStyleClass().add("validation-error");}
            else {newPasswordHintTextField.getStyleClass().remove("validation-error");}
        });

        confirmNewPasswordHintTextField.textProperty().addListener((obsVal, oldVal, newVal) -> {
            confirmNewPasswordHintTextField.getStyleClass().remove("validation-error");
            if (newVal.isEmpty() || newVal.matches("\\s*")) {confirmNewPasswordHintTextField.getStyleClass().add("validation-error");}
            else {confirmNewPasswordHintTextField.getStyleClass().remove("validation-error");}
        });

        passwordField.textProperty().addListener((obsVal, oldVal, newVal) -> {
            passwordField.getStyleClass().remove("validation-error");
            if (newVal.isEmpty()) {passwordField.getStyleClass().add("validation-error");}
            else {passwordField.getStyleClass().remove("validation-error");}
        });

        //bind changePasswordHintButton disabled property, button will be disabled if one of field above is empty
        //or contains only whitespaces
        changePasswordHintButton.disableProperty().bind(Bindings.when(
                newPasswordHintTextField.textProperty().isEqualTo("")
                .or(confirmNewPasswordHintTextField.textProperty().isEqualTo(""))
                .or(passwordField.textProperty().isEqualTo(""))
                .or(patternTextFieldBinding(newPasswordHintTextField, WHITE_SPACES_ONLY_REGEX))
                .or(patternTextFieldBinding(confirmNewPasswordHintTextField, WHITE_SPACES_ONLY_REGEX)))
                .then(true)
                .otherwise(false));
    }

    /**
     * Method represent image of avatar.
     */
    private void initChangeAvatarSubwindow() {
        maleAvatarImageView.setImage(Avatar.getImageForAvatar("male"));
        femaleAvatarImageView.setImage(Avatar.getImageForAvatar("female"));

        selectMaleAvatarRadioButton.setSelected(App.isMaleAvatarCurrentlySet());
        selectFemaleAvatarRadioButton.setSelected(!App.isMaleAvatarCurrentlySet());
    }

    /**
     * Method represent field of delete account.
     */
    private void initDeleteAccountSubwindow() {
        //add css class which will highlight field with red color when it is empty
        deleteAccountPasswordField.getStyleClass().add("validation-error");

        deleteAccountPasswordField.textProperty().addListener((obsVal, oldVal, newVal) -> {
            deleteAccountPasswordField.getStyleClass().remove("validation-error");
            if (newVal.isEmpty()) {deleteAccountPasswordField.getStyleClass().add("validation-error");}
            else {deleteAccountPasswordField.getStyleClass().remove("validation-error");}
        });

        //bind deleteAccountButton disabled property, button will be disabled if one of password field is empty
        deleteAccountButton.disableProperty().bind(Bindings.when(
            deleteAccountPasswordField.textProperty().isEqualTo(""))
            .then(true)
            .otherwise(false));
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
