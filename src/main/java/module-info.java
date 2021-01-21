module main {
    requires javafx.controls;
    requires javafx.fxml;

    opens cz.vse.fis.todolist.application.main to javafx.fxml;
    opens cz.vse.fis.todolist.application.ui to javafx.fxml;

    exports cz.vse.fis.todolist.application.main;
}
