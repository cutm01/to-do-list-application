package cz.vse.fis.todolist.application.logic;

import com.google.gson.annotations.Expose;
import javafx.scene.chart.CategoryAxis;
import javafx.util.Pair;

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
    @Expose
    private AtomicLong lastTaskID;     //incremented whenever new task is added to ensure unique task IDs
    @Expose
    private Map<String, String> lastOpenedTask = new HashMap<>(); //<category name, task id>...GUI center panel is initialized with this task
    @Expose
    private Map<String, Category> userTaskCategories = new LinkedHashMap<>();

    public UserData() {
        lastOpenedTask = new HashMap<>();
        lastOpenedTask.put("category1", "1");
    }

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

    /**
     * Method to move task to another category
     *
     * @param task task instance to move
     * @param fromCategory category name where task will be moved from
     * @param  toCategory category name where task will be moved to
     */
    public void moveTaskToCategory(Task task, String fromCategory, String toCategory) {
        addTaskToCategory(task, toCategory);
        deleteTaskFromCategory(task, fromCategory);
    }

    /**
     * Method to rename task
     *
     * @param categoryName name of category where task is currently placed
     * @param taskID unique ID of task that will be renamed
     * @param newName new name of task
     */
    public void renameTask(String categoryName, String taskID, String newName) {
        userTaskCategories.get(categoryName).renameTask(taskID, newName);
    }

    /**
     * Method to set new deadline timestamp for task
     *
     * @param categoryName name of category where task is currently placed
     * @param taskID unique ID of task which deadline timestamp will be changed
     * @param newDeadlineTimestamp new deadline timestamp
     */
    public void changeTaskDeadlineTimestamp(String categoryName, String taskID, long newDeadlineTimestamp) {
        userTaskCategories.get(categoryName).getTaskByUniqueID(taskID).setTaskDeadlineTimestamp(newDeadlineTimestamp);
    }

    /**
     * Method to change task text
     *
     * @param categoryName name of category where task is currently placed
     * @param taskID unique ID of task which text will be changed
     * @param newText new task text
     */
    public void changeTaskText(String categoryName, String taskID, String newText) {
        userTaskCategories.get(categoryName).getTaskByUniqueID(taskID).setText(newText);
    }

    /**
     * Method to create new task and save it to specified category
     *
     * @param taskCategory category name where task will be placed into
     * @param taskName String representing task name
     * @param taskText String representing task text
     * @param taskCreationTimestamp long representing milliseconds since epoch
     * @param taskDeadlineTimestamp long representing milliseconds since epoch
     * @param isTaskCompleted true if task is completed, false otherwise
     * @return instance of newly created task
     */
    public Task createNewTask(String taskCategory,
                              String taskName,
                              String taskText,
                              long taskCreationTimestamp,
                              long taskDeadlineTimestamp,
                              boolean isTaskCompleted) {
        Task createdTask = new Task(createTaskUniqueID(),
                                    taskName,
                                    taskText,
                                    taskCreationTimestamp,
                                    taskDeadlineTimestamp,
                                    isTaskCompleted);
        userTaskCategories.get(taskCategory).addTask(createdTask);

        return createdTask;
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
     * Method to get last opened tasks before user logged out or closed application. Used to
     * to set content of center panel in GUI
     *
     * @return last opened task instance or null for accounts without any task or when no task was previously displayed
     */
    public Task getLastOpenedTask() {
        String category = "";
        String taskID = "";
        for (Map.Entry<String, String> categoryNameTaskIDEntry : lastOpenedTask.entrySet()) {
            category = categoryNameTaskIDEntry.getKey();
            taskID = categoryNameTaskIDEntry.getValue();
        }

        //account is without any task or no task was previously displayed
        if (taskID.equals("-1")) {
            return null;
        }

        return getTaskFromCategory(category, taskID);
    }

    /**
     * Method to get category name of last opened tasks before user logged out or closed application. Used to
     * to set content of center panel in GUI
     *
     * @return String representing category name of last opened task instance
     */
    public String getLastOpenedTaskCategory() {
        String category = "";

        for (Map.Entry<String, String> categoryNameTaskIDEntry : lastOpenedTask.entrySet()) {
            category = categoryNameTaskIDEntry.getKey();
        }

        return category;
    }

    /**
     * Method to obtain task specified by its ID from given category
     *
     * @param categoryName category name which task will be obtained from
     * @param taskID ID of task which will be obtained
     * @return Task instance
     */
    public Task getTaskFromCategory(String categoryName, String taskID) {
        return userTaskCategories.get(categoryName).getTaskByUniqueID(taskID);
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
     * Method to set information about last opened task before user logged out, closed application or
     * moved to another window of application
     *
     * @param categoryName category name where last opened task is placed
     * @param taskID unique ID of last opened task
     */
    public void setLastOpenedTask(String categoryName, String taskID) {
        lastOpenedTask.clear();
        lastOpenedTask.put(categoryName, taskID);
    }

    /**
     * Method to change name of category
     *
     * @param oldCategoryName name of category which name will be changed
     * @param newCategoryName new category name
     */
    public void renameCategory(String oldCategoryName, String newCategoryName) {
        Category categoryToRename = userTaskCategories.get(oldCategoryName);
        categoryToRename.setCategoryName(newCategoryName);

        userTaskCategories.remove(oldCategoryName);
        userTaskCategories.put(newCategoryName, categoryToRename);
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


