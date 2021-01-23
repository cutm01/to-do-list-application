package cz.vse.fis.todolist.application.logic;

import java.util.HashMap;
import java.util.concurrent.atomic.AtomicLong;

public class UserData {

    private String firmwareVersion = "1.0.0";
    private String username;
    private String password;
    private String passwordHint;
    private String avatar;
    private AtomicLong lastTaskID; //incremented whenever new task is added to ensure unique task IDs
    private HashMap<String, HashMap<String, Task>> taskCategory = new HashMap<>();

    public UserData(String username, String password, String passwordHint, String avatar, long lastTaskID) {
        this.username = username;
        this.password = password;
        this.passwordHint = passwordHint;
        this.avatar = avatar;
        this.lastTaskID = new AtomicLong(lastTaskID);
    }

    public UserData(String username) {
        ReadUpdateFile rw = new ReadUpdateFile();
        UserData userData = rw.readDataFromJSON(username);

        this.username = userData.getUsername();
        this.password = userData.getPassword();
        this.passwordHint = userData.getPasswordHint();
        this.avatar = userData.getAvatar();
        this.taskCategory = userData.getTaskCategory();
        this.lastTaskID = new AtomicLong(userData.getLastTaskID());
    }

    public void createTaskCategory(String categoryName)
    {
        HashMap<String, Task> category = new HashMap<>();

        taskCategory.putIfAbsent(categoryName, category);
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

    public HashMap<String, HashMap<String, Task>> getTaskCategory() {
        return taskCategory;
    }

    public void setTaskCategory(HashMap<String, HashMap<String, Task>> taskCategory) {
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
}


