package cz.vse.fis.todolist.application.main;

import java.util.HashMap;

public class UserData {

    private String firmwareVersion = "1.0.0";
    private String username;
    private String password;
    private String passwordHint;
    private String avatar;
    private HashMap<String, HashMap<String, Task>> taskCategory = new HashMap<>();

    public UserData(String username, String password, String passwordHint, String avatar) {
        this.username = username;
        this.password = password;
        this.passwordHint = passwordHint;
        this.avatar = avatar;
    }

    public UserData(String username) {
        ReadUpdateFile rw = new ReadUpdateFile();
        UserData userData = rw.readDataFromJSON(username);

        this.username = userData.getUsername();
        this.password = userData.getPassword();
        this.passwordHint = userData.getPasswordHint();
        this.avatar = userData.getAvatar();
        this.taskCategory = userData.getTaskCategory();
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
}


