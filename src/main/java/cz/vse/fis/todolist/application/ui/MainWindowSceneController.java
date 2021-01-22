package cz.vse.fis.todolist.application.ui;

import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;

/**
 * MainWindowSceneController class with method to handle changes in main window od To-Do List application.
 *
 * @version 1.0.0
 */
public class MainWindowSceneController {
    //scene elements, bottom panel of root borderpane is absent
    //top panel
    public ImageView avatarImageView;
    public Label usernameLabel;
    //left panel
    public ComboBox categoriesComboBox;
    public ComboBox sortTasksComboBox;
    public ListView tasksListView;
    public CheckBox markAllTasksCheckBox;
    //center panel
    public Label taskNameLabel;
    public Label taskCategoryLabel;
    public Label taskCreationDateLabel;
    public Label taskDeadlineDateLabel;


}
