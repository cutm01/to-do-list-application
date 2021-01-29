package cz.vse.fis.todolist.application.logic;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ReadUpdateFileTest {

    private UserData userData;
    private long timestamp;
    private List<Task> tasks = new ArrayList<>();

    @BeforeEach
    public void setUp() {
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

    @Test
    @DisplayName("test uniqueness of created task IDs")
    public void uniqueTaskIDs() {
        assertEquals(tasks.get(0).getName(), userData.getTasksFromCategory("Category","1").get(0).getName());
        assertEquals(tasks.get(1).getName(), userData.getTasksFromCategory("Category","1").get(1).getName());
        assertEquals(tasks.get(2).getName(), userData.getTasksFromCategory("Category","1").get(2).getName());
    }


    /* see https://stackoverflow.com/questions/41265266/how-to-solve-inaccessibleobjectexception-unable-to-make-member-accessible-m
    @Test
    public void writeReadUserData()
    {
        ReadUpdateFile.writeDataToJSON(userData);
        assertEquals(userData, ReadUpdateFile.readDataFromJSON(userData.getUsername()));

    }

    @Test
    @DisplayName("test timestamp read from file conversion to date")
    public void timestampConvert() {
        ReadUpdateFile.writeDataToJSON(userData);

        Date expectedDate = new Date(timestamp);
        Date actualDate = new Date(ReadUpdateFile.readDataFromJSON("username")
                                   .getTasksFromCategory("Category","1").get(0)
                                   .getTaskCreationTimestamp());

        assertEquals(expectedDate, actualDate);
    }

     */
}
