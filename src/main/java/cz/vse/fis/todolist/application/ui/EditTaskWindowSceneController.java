package cz.vse.fis.todolist.application.ui;

import javafx.event.ActionEvent;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.web.HTMLEditor;

/**
 * EditTaskWindowSceneController class with methods for editing existing task
 *
 * @version 1.0.0
 */
public class EditTaskWindowSceneController {
    //scene elements
    public ComboBox chooseCategoryComboBox;
    public TextField taskNameTextField;
    public DatePicker deadlineDatePicker;
    public TextField deadlineTimeTextField;
    public HTMLEditor newTaskHTMLEditor;

    /**
     * Method to move user back to main window of application
     *
     * @param actionEvent
     */
    public void showMainWindow(ActionEvent actionEvent) {
    }

    /**
     * Method to save changes which was done during editing the task
     *
     * @param actionEvent
     */
    public void saveChanges(ActionEvent actionEvent) {
    }
}
