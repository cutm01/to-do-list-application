package cz.vse.fis.todolist.application.logic;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TaskTest {

    private UserData userData = new UserData();
    private long timestamp;
    private List<Task> tasks = new ArrayList<>();

    @BeforeEach
    public void setUp() {
        timestamp = 1167636900; //2007-01-01T07:35:00+00:00 in ISO 8601

        userData = new UserData("username", "password", "passwordHint", "male", 0);
        userData.createTaskCategory("Category");



        for (int i = 0; i < 3; ++i) {
            Task task = new Task(userData.createTaskUniqueID(), "name " + i, "text " + i, timestamp, timestamp+300, false);
            tasks.add(task);
            userData.addTaskToCategory(task, "Category");
        }
    }


    @Test
    @DisplayName("getting task texts and setting new texts")
    public void getSetTextTest()
    {
        assertEquals(userData.getTaskFromCategory("Category","1" ).getText(), "text 0");
        assertEquals(userData.getTaskFromCategory("Category","2" ).getText(), "text 1");
        assertEquals(userData.getTaskFromCategory("Category","3" ).getText(), "text 2");

        userData.getTaskFromCategory("Category","1" ).setText("new text");
        assertEquals(userData.getTaskFromCategory("Category","1" ).getText(), "new text");
        assertEquals(userData.getTaskFromCategory("Category","2" ).getText(), "text 1");
    }

    @Test
    @DisplayName("marking tasks as completed")
    public void completedTest()
    {
        userData.getTaskFromCategory("Category", "1").setCompleted(true);
        assertEquals(userData.getTaskFromCategory("Category", "1").getCompleted(), true);
        assertEquals(userData.getTaskFromCategory("Category", "2").getCompleted(), false);
    }

    @Test
    @DisplayName("checking creation and deadline timestamp and changing them")
    public void timestampTest()
    {
        assertEquals(userData.getTaskFromCategory("Category","1" ).getTaskCreationTimestamp(), 1167636900);
        assertEquals(userData.getTaskFromCategory("Category","1" ).getTaskDeadlineTimestamp(), 1167637200);
        userData.getTaskFromCategory("Category", "1").setTaskDeadlineTimestamp(1167638500);
        assertEquals(userData.getTaskFromCategory("Category","1" ).getTaskDeadlineTimestamp(), 1167638500);

    }

    @Test
    @DisplayName("testing isSelected property")
    public void isSelectedTest()
    {
        assertFalse(userData.getTaskFromCategory("Category","1" ).isSelected());
        userData.getTaskFromCategory("Category","1" ).setSelected(true);
        assertTrue(userData.getTaskFromCategory("Category","1" ).isSelected());
    }
}
