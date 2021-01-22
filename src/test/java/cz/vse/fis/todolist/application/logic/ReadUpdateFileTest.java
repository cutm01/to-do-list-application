package cz.vse.fis.todolist.application.logic;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class ReadUpdateFileTest {

    private UserData userData;
    private long timestamp;

    @BeforeEach
    void setUp() {
        timestamp = 1167636900; //2007-01-01T07:35:00+00:00 in ISO 8601

        userData = new UserData("username", "password", "passwordHint", "male");
        userData.createTaskCategory("Category");

        Task task = new Task("1","name", "text", timestamp, timestamp, false);
        userData.addTaskToCategory(task, "Category");
    }

    /**
     * Test if timestamp read from file is correctly covnerted to date
     */
    @Test
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
}