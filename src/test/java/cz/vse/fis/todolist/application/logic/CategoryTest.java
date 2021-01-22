package cz.vse.fis.todolist.application.logic;

import org.junit.jupiter.api.BeforeEach;
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
        category = new Category("Category", null, 0);

        //following list will be used as expected output in later testing methods
        tasksInAlphabeticalOrderFromOldestToNewest = new ArrayList<>();
        tasksInAlphabeticalOrderFromOldestToNewest.add(new Task("1","A task from 1.1.2018", "text", 1514764800, 1514764800, false));
        tasksInAlphabeticalOrderFromOldestToNewest.add(new Task("1","B task from 1.1.2019 ", "text", 1546300800, 1546300800, false));
        tasksInAlphabeticalOrderFromOldestToNewest.add(new Task("1","C task from 1.1.2020", "text", 1577836800, 1577836800, false));

        //add task to category in shuffled order
        ArrayList<Task> shuffledTasks = new ArrayList<>(tasksInAlphabeticalOrderFromOldestToNewest);
        Collections.shuffle(shuffledTasks);

        for (Task t : shuffledTasks) {
            category.addTask(t);
        }
    }

    @Test
    void getListOfTasksInAlphabeticalOrder() {
        assertEquals(tasksInAlphabeticalOrderFromOldestToNewest, category.getListOfTasksInAlphabeticalOrder());
    }

    @Test
    void getListOfTasksInUnalphabeticalOrder() {
        Collections.reverse(tasksInAlphabeticalOrderFromOldestToNewest);
        assertEquals(tasksInAlphabeticalOrderFromOldestToNewest, category.getListOfTasksInUnalphabeticalOrder());
    }

    @Test
    void getListOfTasksInAscendingCreationDateOrder() {
        assertEquals(tasksInAlphabeticalOrderFromOldestToNewest, category.getListOfTasksInAscendingCreationDateOrder());
    }

    @Test
    void getListOfTasksInDescendingCreationDateOrder() {
        Collections.reverse(tasksInAlphabeticalOrderFromOldestToNewest);
        assertEquals(tasksInAlphabeticalOrderFromOldestToNewest, category.getListOfTasksInDescendingCreationDateOrder());
    }

    @Test
    void getListOfTasksInAscendingDeadlineDateOrder() {
        assertEquals(tasksInAlphabeticalOrderFromOldestToNewest, category.getListOfTasksInAscendingDeadlineDateOrder());
    }

    @Test
    void getListOfTasksInDescendingDeadlineDateOrder() {
        Collections.reverse(tasksInAlphabeticalOrderFromOldestToNewest);
        assertEquals(tasksInAlphabeticalOrderFromOldestToNewest, category.getListOfTasksInDescendingDeadlineDateOrder());
    }
}