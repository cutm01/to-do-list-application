package cz.vse.fis.todolist.application.logic;

import com.google.gson.annotations.Expose;
import javafx.scene.chart.CategoryAxis;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

public class UserData {
    //following attributes are written into JSON file containing user data
    @Expose
    private String firmwareVersion = "1.0.0";
    @Expose
    private String username;
    @Expose
    private String password;
    @Expose
    private String passwordHint;
    @Expose
    private String avatar;
    //incremented whenever new task is added to ensure unique task IDs
    @Expose
    private AtomicLong lastTaskID;
    @Expose
    private Map<String, Category> userTaskCategories = new LinkedHashMap<>();

    public UserData(String username, String password, String passwordHint, String avatar, long lastTaskID) {
        this.username = username;
        this.password = password;
        this.passwordHint = passwordHint;
        this.avatar = avatar;
        this.lastTaskID = new AtomicLong(lastTaskID);
    }

    /**
     * Method for creating new instance of UserData filled with data obtained from JSON file
     *
     * @param username name of the JSON file which be will be data obtained from
     */
    public UserData(String username) {
        UserData userData = ReadUpdateFile.readDataFromJSON(username);

        this.username = userData.getUsername();
        this.password = userData.getPassword();
        this.passwordHint = userData.getPasswordHint();
        this.avatar = userData.getAvatar();
        this.lastTaskID = new AtomicLong(userData.getLastTaskID());
    }

    /**
     * Method to create new category for user's tasks
     *
     * @param categoryName name of the new category to add
     */
    public void createTaskCategory(String categoryName)
    {
        Category newCategory = new Category(categoryName, null);
        userTaskCategories.put(categoryName, newCategory);
    }

    /**
     * Method to check whether category with same name already exists
     *
     * @param categoryName name of category to check
     * @return true if category with same name already exists, false otherwise
     */
    public boolean doesCategoryExists(String categoryName) {
        return userTaskCategories.containsKey(categoryName);
    }

    /**
     * Method to remove existing category. All tasks in this category will be remove too
     *
     * @param categoryName name of category to remove
     */
    public void deleteTaskCategory(String categoryName)
    {
        userTaskCategories.remove(categoryName);
    }

    /**
     * Method to add task to existing category
     *
     * @param task task instance which will be added to category
     * @param categoryName name of category where task will be added
     */
    public void addTaskToCategory(Task task, String categoryName)
    {
        userTaskCategories.get(categoryName).addTask(task);
    }

    /**
     * Method to remove task from existing category
     *
     * @param task task instance to remove
     * @param categoryName name of category which task should be removed from
     */
    public void deleteTaskFromCategory(Task task, String categoryName)
    {
        userTaskCategories.get(categoryName).removeTask(task.getTaskID());
    }

    /**
     * Method to check whether task specified by its ID is currently placed in category
     *
     * @param taskID unique ID of task
     * @param categoryName category name which be checked whether it contains task
     * @return true if category contains task specified by its ID, false otherwise
     */
    public boolean isTaskInCategory(String taskID, String categoryName)
    {
        return userTaskCategories.get(categoryName).isTaskInCategory(taskID);
    }

    public String getFirmwareVersion() {
        return firmwareVersion;
    }


    public void setFirmwareVersion(String firmwareVersion) {
        this.firmwareVersion = firmwareVersion;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPasswordHint() {
        return passwordHint;
    }

    public void setPasswordHint(String passwordHint) {
        this.passwordHint = passwordHint;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    /**
     * Method to get category of tasks specified by its name
     *
     * @param categoryName name of the category which will be obtained
     * @return instance of Category if category specified by name exists, null otherwise
     */
    public Category getUserCategory(String categoryName) {
        return userTaskCategories.get(categoryName);
    }

    public long getLastTaskID() {
        return lastTaskID.longValue();
    }

    /**
     * Method for creating unique task IDs.
     *
     * @return numeric String representing unique ID for new task
     */
    public String createTaskUniqueID() {
        return String.valueOf(lastTaskID.incrementAndGet());
    }

    /**
     * Method to validate login credentials provided by user
     *
     * @param username username provided by user
     * @param password password provided by user
     * @return true if login credentials are valid, false otherwise
     */
    public boolean areLoginCredentialsValid(String username, String password) {
        return this.username.equals(username) && this.password.equals(password);
    }

    /**
     * Method to get whether password hint is set for user account
     *
     * @return true if password hint is set, false otherwise
     */
    public boolean isPasswordHintSet() {
        return passwordHint != null || passwordHint.equals("");
    }

    /**
     * Method to get names of categories which are currently created for account
     *
     * @return ArrayList containing names of all user account categories
     */
    public List<String> getUserCategoryNames() {
        return new ArrayList<>(userTaskCategories.keySet());
    }

    /**
     * Method to get all tasks from category ordered by one of sorting option which
     * is specified in SortingOptions class
     *
     * @param categoryName name of category which will tasks be obtained from
     * @param sortingOption ordering option as specified in SortingOptions class
     * @return List of tasks in order specified by sorting option (e.g. from A->Z, newest->oldest)
     */
    public List<Task> getTasksFromCategory(String categoryName, String sortingOption) {
        List<Task> obtainedTasks = new ArrayList<>();

        switch (sortingOption) {
            case SortingOptions.BY_NAME_FROM_A_TO_Z:
                obtainedTasks = userTaskCategories.get(categoryName).getListOfTasksInAlphabeticalOrder();
                break;
            case SortingOptions.BY_NAME_FROM_Z_TO_A:
                obtainedTasks = userTaskCategories.get(categoryName).getListOfTasksInUnalphabeticalOrder();
                break;
            case SortingOptions.BY_CREATION_TIME_FROM_NEWEST_TO_OLDEST:
                obtainedTasks = userTaskCategories.get(categoryName).getListOfTasksInDescendingCreationDateOrder();
                break;
            case SortingOptions.BY_CREATION_TIME_FROM_OLDEST_TO_NEWEST:
                obtainedTasks = userTaskCategories.get(categoryName).getListOfTasksInAscendingCreationDateOrder();
                break;
            case SortingOptions.BY_DEADLINE_FROM_EARLIEST_TO_LATEST:
                obtainedTasks = userTaskCategories.get(categoryName).getListOfTasksInAscendingDeadlineDateOrder();
                break;
            case SortingOptions.BY_DEADLINE_FROM_LATEST_TO_OLDEST:
                obtainedTasks = userTaskCategories.get(categoryName).getListOfTasksInDescendingDeadlineDateOrder();
                break;
            case SortingOptions.NONE:
                obtainedTasks = userTaskCategories.get(categoryName).getListOfTasks();
                break;
            default:
                obtainedTasks = userTaskCategories.get(categoryName).getListOfTasks();
        }

        return obtainedTasks;
    }
}


