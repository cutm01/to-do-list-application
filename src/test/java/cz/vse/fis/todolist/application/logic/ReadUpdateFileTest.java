package cz.vse.fis.todolist.application.logic;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ReadUpdateFileTest {

    private UserData userData;
    private long timestamp;
    private List<Task> tasks;

    @BeforeEach
    void setUp() {
        timestamp = 1167636900; //2007-01-01T07:35:00+00:00 in ISO 8601

        userData = new UserData("username", "password", "passwordHint", "male", 0);
        userData.createTaskCategory("Category");

        tasks = new ArrayList<>();
        for(int i = 0; i < 3; ++i) {
            Task task = new Task(userData.createTaskUniqueID(), "name " + i, "text " + i, timestamp, timestamp, false);
            tasks.add(task);
            userData.addTaskToCategory(task, "Category");
        }
    }
    /*
    @Test
    @DisplayName("test uniqueness of created task IDs")
    void uniqueTaskIDs() {
        assertEquals(tasks.get(0).taskID, userData.getTaskCategory().get("Category").get("1").getTaskID());
        assertEquals(tasks.get(1).taskID, userData.getTaskCategory().get("Category").get("2").getTaskID());
        assertEquals(tasks.get(2).taskID, userData.getTaskCategory().get("Category").get("3").getTaskID());
    }

    @Test
    @DisplayName("test timestamp read from file conversion to date")
    void timestampConvert() {
        ReadUpdateFile.writeDataToJSON(userData);

        Date expectedDate = new Date(timestamp);
        Date actualDate = new Date(ReadUpdateFile.readDataFromJSON("username")
                                   .getTaskCategory()
                                   .get("Category")
                                   .get("1")
                                   .getTaskCreationTimestamp());

        assertEquals(expectedDate, actualDate);
    }

     */
}