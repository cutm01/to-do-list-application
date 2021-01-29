package cz.vse.fis.todolist.application.ui;

import cz.vse.fis.todolist.application.main.App;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.text.Text;
import javafx.scene.web.WebView;

import java.io.IOException;
import java.io.InputStream;

/**
 * ManualWindowSceneController class contains methods to handle events in Manual window scene, e.g. show
 * text to manual's chapter chosen by user
 *
 * @version 1.0.0
 */
public class ManualWindowSceneController {
    //scene elements
    public ListView manualMenuListView;
    public WebView manualChapterView;

    public void init() {
        //filling left panel
        manualMenuListView.getItems().setAll(ManualMenuItem.values());

        manualMenuListView.setCellFactory(listCell -> new ListCell<ManualMenuItem>() {
            @Override
            protected void updateItem(ManualMenuItem item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setText(null);
                    setGraphic(null);
                } else {
                    setGraphic(new Label(item.toString()));
                }
            }
        });

        //select first item from left menu as default value
        manualMenuListView.getSelectionModel().select(0);
        manualChapterView.getEngine().loadContent(ManualChapterText.REGISTER.toString());

        //change listeners for buttons
        manualMenuListView.getSelectionModel().selectedItemProperty().addListener((obsVal, oldVal, newVal) -> {
            if (newVal != null) {changeChapter(newVal.toString());}
        });
    }

    public void showMainWindow(ActionEvent actionEvent) {
        initializeMainWindow();
    }

    private void changeChapter(String chapterName) {
        manualChapterView.getEngine().loadContent(ManualChapterText.getTextForManualChapter(chapterName));
    }

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
