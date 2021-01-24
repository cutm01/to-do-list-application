package cz.vse.fis.todolist.application.ui;

import cz.vse.fis.todolist.application.main.App;
import javafx.event.ActionEvent;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.web.HTMLEditor;

public class CreateNewTaskWindowSceneController {
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
     * Method to create new task
     *
     * @param actionEvent
     */
    public void createNewTask(ActionEvent actionEvent) {
    }
}
