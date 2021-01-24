package cz.vse.fis.todolist.application.logic;

import com.google.gson.annotations.Expose;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

public class UserData {

    private Map<String, Category> userCategories = new LinkedHashMap<>();

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
    /* taskCategory represents all user categories with their tasks as one JSON object, e.g.:
      "taskCategory": {
        "School": { //category name
          "2": { //task unique ID
            //task data
          }
        },
    */
    @Expose
    private Map<String, Map<String, Task>> taskCategory = new LinkedHashMap<>();

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
     * @param categoryName
     */
    public void createTaskCategory(String categoryName)
    {
        Category newCategory = new Category(categoryName, null);
        userCategories.put(categoryName, newCategory);
    }

    /**
     * Method to check whether category with same name already exists
     *
     * @param categoryName
     * @return true if category with same name already exists, false otherwise
     */
    public boolean doesCategoryExists(String categoryName) {
        return userCategories.containsKey(categoryName);
    }

    public void deleteTaskCategory(String categoryName)
    {
        taskCategory.remove(categoryName);
    }

    public void addTaskToCategory(Task task, String categoryName)
    {
        taskCategory.get(categoryName).put(task.getTaskID(), task);
    }

    public void deleteTask(Task task, String categoryName)
    {
        taskCategory.get(categoryName).remove(task.getTaskID());
    }

    public boolean isTaskInCategory(String taskID, String categoryName)
    {
        return taskCategory.get(categoryName).containsKey(taskID);
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

    public Map<String, Map<String, Task>> getTaskCategory() {
        return taskCategory;
    }

    public void setTaskCategory(Map<String, Map<String, Task>> taskCategory) {
        this.taskCategory = taskCategory;
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
     * Method loads all user categories together with tasks they contain. Data are obtained from taskCategory JSON
     * object placed in JSON file which contains all account information
     */
    void loadUserCategoriesWithTasks() {
        userCategories = new LinkedHashMap<>();

        //parse data from taskCategory JSON object where key is name of category
        //and value is HashMap with task unique ID as key and task instance as value
        for (String categoryName : taskCategory.keySet()) {
            userCategories.put(categoryName, new Category(categoryName, taskCategory.get(categoryName)));
        }
    }

    /**
     * Method updates taskCategory attribute according to changes made by user (e.g. deleting category, adding or
     * removing tasks). This attribute is later written to JSON file which holds all account information
     */
    void updateTaskCategory() {
        taskCategory = new LinkedHashMap<>();
        for (String categoryName : userCategories.keySet()) {
            taskCategory.put(categoryName, userCategories.get(categoryName).getCategoryTasks());
        }
    }
}


