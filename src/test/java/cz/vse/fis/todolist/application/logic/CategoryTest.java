package cz.vse.fis.todolist.application.logic;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * CategoryTest contains unit tests for sorting methods from class Category
 */
class CategoryTest {

    private Category category;
    private List<Task> tasksInAlphabeticalOrderFromOldestToNewest;

    @BeforeEach
    void setUp() {
        category = new Category("Category", null);

        //following list will be used as expected output in later testing methods
        tasksInAlphabeticalOrderFromOldestToNewest = new ArrayList<>();
        tasksInAlphabeticalOrderFromOldestToNewest.add(new Task("1","A task from 1.1.2018", "text", 1514764800, 1514764800, false));
        tasksInAlphabeticalOrderFromOldestToNewest.add(new Task("1","B task from 1.1.2019 ", "text", 1546300800, 1546300800, false));
        tasksInAlphabeticalOrderFromOldestToNewest.add(new Task("1","C task from 1.1.2020", "text", 1577836800, 1577836800, false));

        //add tasks to category in shuffled order
        ArrayList<Task> shuffledTasks = new ArrayList<>(tasksInAlphabeticalOrderFromOldestToNewest);
        Collections.shuffle(shuffledTasks);

        for (Task t : shuffledTasks) {
            category.addTask(t);
        }
    }

    @Test
    @DisplayName("order tasks by name from A to Z")
    void getListOfTasksInAlphabeticalOrder() {
        assertEquals(tasksInAlphabeticalOrderFromOldestToNewest, category.getListOfTasksInAlphabeticalOrder());
    }

    @Test
    @DisplayName("order tasks by name from Z to A")
    void getListOfTasksInUnalphabeticalOrder() {
        Collections.reverse(tasksInAlphabeticalOrderFromOldestToNewest);
        assertEquals(tasksInAlphabeticalOrderFromOldestToNewest, category.getListOfTasksInUnalphabeticalOrder());
    }

    @Test
    @DisplayName("order tasks by creation time from oldest to newest")
    void getListOfTasksInAscendingCreationDateOrder() {
        assertEquals(tasksInAlphabeticalOrderFromOldestToNewest, category.getListOfTasksInAscendingCreationDateOrder());
    }

    @Test
    @DisplayName("order tasks by creation time from newest to oldest")
    void getListOfTasksInDescendingCreationDateOrder() {
        Collections.reverse(tasksInAlphabeticalOrderFromOldestToNewest);
        assertEquals(tasksInAlphabeticalOrderFromOldestToNewest, category.getListOfTasksInDescendingCreationDateOrder());
    }

    @Test
    @DisplayName("order tasks by deadline from oldest to newest")
    void getListOfTasksInAscendingDeadlineDateOrder() {
        assertEquals(tasksInAlphabeticalOrderFromOldestToNewest, category.getListOfTasksInAscendingDeadlineDateOrder());
    }

    @Test
    @DisplayName("order tasks by creation time from oldest to newest")
    void getListOfTasksInDescendingDeadlineDateOrder() {
        Collections.reverse(tasksInAlphabeticalOrderFromOldestToNewest);
        assertEquals(tasksInAlphabeticalOrderFromOldestToNewest, category.getListOfTasksInDescendingDeadlineDateOrder());
    }
}