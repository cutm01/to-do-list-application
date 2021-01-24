module main {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;
    requires com.google.gson;

    opens cz.vse.fis.todolist.application.main to javafx.fxml;
    opens cz.vse.fis.todolist.application.ui to javafx.fxml;

    exports cz.vse.fis.todolist.application.main;
}
