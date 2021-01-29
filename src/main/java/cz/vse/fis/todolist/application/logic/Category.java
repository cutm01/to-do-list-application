package cz.vse.fis.todolist.application.logic;

import com.google.gson.annotations.Expose;

import java.util.*;

/**
 * Category class is used to store all related user's tasks and provides methods for sorting them
 * in ascending or descending order based on their task name or creation (deadline) date
 *
 * @version 1.0.0
 */
public class Category {
    @Expose
    private String categoryName;
    @Expose
    private Map<String, Task> categoryTasks; //where key is task's uniqueID

    public Category(String categoryName, Map<String, Task> categoryTasks) {
        this.categoryName = categoryName;
        this.categoryTasks = categoryTasks == null? new LinkedHashMap<>() : categoryTasks;
    }

    //Getters and setters
    public String getCategoryName() {
        return categoryName;
    }

    public Map<String, Task> getCategoryTasks() {
        return categoryTasks;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    //Class methods
    /**
     * Method to check whether task specified by its ID is currently placed in category
     *
     * @param taskID unique ID of task
     * @return true if category contains task specified by its ID, false otherwise
     */
    public boolean isTaskInCategory(String taskID) {
        return categoryTasks.containsKey(taskID);
    }

    /**
     * Method to add new task to category. Task is put in hash map with its unique ID as a key
     *
     * @param task task to add
     */
    public void addTask(Task task) {
        categoryTasks.put(task.getTaskID(), task);
    }

    /**
     * Method to remove task specified by its unique ID from category
     *
     * @param taskID unique ID of task which will be removed
     */
    public void removeTask(String taskID) {
        categoryTasks.remove(taskID);
    }

    /**
     * Method to remove all task from category
     */
    public void removeAllTasks() {
        categoryTasks.clear();
    }

    /**
     * Method to rename task specified by its unique ID
     *
     * @param newTaskName String representing new name of task
     */
    public void renameTask(String taskID, String newTaskName) {
        categoryTasks.get(taskID).setName(newTaskName);
    }

    /**
     * Method to change text of task specified by its unique ID
     *
     * @param taskID unique ID of task which text will be changed
     * @param newTaskText String representing new text of task
     */
    public void changeTaskText(String taskID, String newTaskText) {
        categoryTasks.get(taskID).setText(newTaskText);
    }

    /**
     * Method to change deadline of task specified by its unique ID
     *
     * @param taskID unique ID of task which deadline will be changed
     * @param newDeadlineTimestamp long representing timestamp of the new task's deadline
     */
    public void changeTaskDeadlineDate(String taskID, long newDeadlineTimestamp) {
        categoryTasks.get(taskID).setTaskDeadlineTimestamp(newDeadlineTimestamp);
    }

    /**
     * Method to mark task specified by its unique ID as completed
     *
     * @param taskID unique ID of task which will be marked as completed
     */
    public void setTaskAsCompleted(String taskID) {
        categoryTasks.get(taskID).setCompleted(true);
    }

    /**
     * Method to obtain task from category by its unique ID
     *
     * @param taskID unique ID of task which will be obtained from category
     */
    public Task getTaskByUniqueID(String taskID) {
        return categoryTasks.get(taskID);
    }

    /**
     * Method to obtain all tasks in category as list
     *
     * @return list of all tasks in category
     */
    public List<Task> getListOfTasks() {
        return new ArrayList<>(categoryTasks.values());
    }

    /**
     * Method to get all task from category ordered by their name from A to Z
     *
     * @return List of all tasks from category ordered by task's name in alphabetical order
     */
    public List<Task> getListOfTasksInAlphabeticalOrder() {
        List<Task> alphabeticalOrder = new ArrayList<>(categoryTasks.values());

        Collections.sort(alphabeticalOrder, new Comparator<Task>() {
            @Override
            public int compare(Task task1, Task task2) {
                return extractInt(task1.getName()) - extractInt(task2.getName());
            }

            int extractInt(String taskName) {
                String number = taskName.replaceAll("\\D", "");

                return number.isEmpty() ? 0 : Integer.parseInt(number);
            }
        });

        return alphabeticalOrder;
    }

    /**
     * Method to get all task from category ordered by their name from Z to A
     *
     * @return List of all tasks from category ordered by task's name in alphabetical order
     */
    public List<Task> getListOfTasksInUnalphabeticalOrder() {
        List<Task> unalphabeticalOrder = getListOfTasksInAlphabeticalOrder();
        Collections.reverse(unalphabeticalOrder);

        return unalphabeticalOrder;
    }

    /**
     * Method to get all task from category ordered by their creation time from oldest to newest
     *
     * @return List of all tasks from category ordered by creation time from oldest to newest
     */
    public List<Task> getListOfTasksInAscendingCreationDateOrder() {
        List<Task> ascendingCreationDate = new ArrayList<>(categoryTasks.values());
        ascendingCreationDate.sort(Comparator.comparing(Task::getTaskCreationTimestamp));

        return ascendingCreationDate;
    }

    /**
     * Method to get all task from category ordered by their creation time from newest to oldest
     *
     * @return List of all tasks from category ordered by creation time from newest to oldest
     */
    public List<Task> getListOfTasksInDescendingCreationDateOrder() {
        List<Task> descendingCreationDate = new ArrayList<>(categoryTasks.values());
        descendingCreationDate.sort(Comparator.comparing(Task::getTaskCreationTimestamp).reversed());

        return descendingCreationDate;
    }

    /**
     * Method to get all task from category ordered by their deadline time from oldest to newest
     *
     * @return List of all tasks from category ordered by deadline time from oldest to newest
     */
    public List<Task> getListOfTasksInAscendingDeadlineDateOrder() {
        List<Task> ascendingDeadlineDate = new ArrayList<>(categoryTasks.values());
        ascendingDeadlineDate.sort(Comparator.comparing(Task::getTaskDeadlineTimestamp));

        return ascendingDeadlineDate;
    }

    /**
     * Method to get all task from category ordered by their deadline time from newest to oldest
     *
     * @return List of all tasks from category ordered by deadline time from newest to oldest
     */
    public List<Task> getListOfTasksInDescendingDeadlineDateOrder() {
        List<Task> descendingDeadlineDate = new ArrayList<>(categoryTasks.values());
        descendingDeadlineDate.sort(Comparator.comparing(Task::getTaskDeadlineTimestamp).reversed());

        return descendingDeadlineDate;
    }
}
