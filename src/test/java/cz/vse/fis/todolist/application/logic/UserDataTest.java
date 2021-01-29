package cz.vse.fis.todolist.application.logic;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.security.Timestamp;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class UserDataTest {
    UserData userData1;
    UserData userData2;
    long timestamp = 1167636900; //2007-01-01T07:35:00+00:00 in ISO 8601
    private List<Task> tasks;


    @BeforeEach
    public void setUp() {
        userData1 = new UserData("user1", "password1", "passwordHint1", "male", 0);

        userData2 = new UserData("user2", "password2", "", "female", 0);

    }


    @Test
    @DisplayName("testing existence of categories")
    public void categoryTest()
    {
        userData1.createTaskCategory("Category1");
        assertEquals(userData1.doesCategoryExists("Category1"), true);
        assertEquals(userData1.doesCategoryExists("Category2"), false);

        userData1.deleteTaskCategory("Category1");
        assertEquals(userData1.doesCategoryExists("Category1"), false);
        assertEquals(userData1.doesCategoryExists("Category2"), false);
    }

    @Test
    @DisplayName("creating task and testing its existence in userdata structure")
    public void taskUserDataTest()
    {
        userData1.createTaskCategory("Category1");
        Task task = new Task("0", "name", "text ", timestamp, timestamp+1000, false);

        userData1.addTaskToCategory(task, "Category1");

        assertEquals(userData1.getTaskFromCategory("Category1","0"), task);

        userData1.createTaskCategory("Category2");
        userData1.moveTaskToCategory(task, "Category1", "Category2");

        assertEquals(userData1.isTaskInCategory("0","Category2"), true);
        assertEquals(userData1.isTaskInCategory("0","Category1"), false);

        userData1.renameTask("Category2", "0", "new name");

        assertTrue(userData1.getTaskFromCategory("Category2","0").getName() == "new name");


        userData1.changeTaskText("Category2", "0", "new text text");

        assertTrue(userData1.getTaskFromCategory("Category2","0").getText() == "new text text");



    }

    @Test
    @DisplayName("testing getters and setters of basic UserData data like password or username")
    public void userDataData()
    {
        assertEquals(userData1.getUsername(), "user1");
        assertFalse(userData2.getUsername() == "user1");

        userData1.setUsername("newUsername1");
        assertEquals(userData1.getUsername(), "newUsername1");
        assertFalse(userData2.getUsername() == "newUsername1");

        assertEquals(userData1.getPassword(), "password1");
        assertFalse(userData2.getPassword() == userData1.getPassword());

        userData1.setPassword("newPassword1");
        assertEquals(userData1.getPassword(), "newPassword1");
        assertFalse(userData2.getPassword() == userData1.getPassword());
    }

    @Test
    @DisplayName("login validity")
    public void loginTest() {
        assertTrue(userData1.areLoginCredentialsValid("user1","password1"));
        assertFalse(userData2.areLoginCredentialsValid("user2","password1"));
    }

    @Test
    @DisplayName("checking password functionality")
    public void passwordTest() {
        assertTrue(userData1.areLoginCredentialsValid("user1","password1"));
        assertFalse(userData2.areLoginCredentialsValid("user2","password1"));

        userData1.setPassword("newPassword");
        assertTrue(userData1.areLoginCredentialsValid("user1","newPassword"));

    }
}
