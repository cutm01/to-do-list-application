package cz.vse.fis.todolist.application.logic;

import java.util.Comparator;
import java.util.Date;

public class Task {
    private String taskID;
    private String name;
    private String text;
    private long taskCreationTimestamp;
    private long taskDeadlineTimestamp;
    private Boolean completed;

    public Task(String taskID, String name, String text, long taskCreationTimestamp, long taskDeadlineTimestamp, Boolean completed) {
        this.taskID = taskID;
        this.name = name;
        this.text = text;
        this.taskCreationTimestamp = taskCreationTimestamp;
        this.taskDeadlineTimestamp = taskDeadlineTimestamp;
        this.completed = completed;
    }

    public String getTaskID() {
        return taskID;
    }

    public void setTaskID(String taskID) {
        this.taskID = taskID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public long getTaskCreationTimestamp() {
        return taskCreationTimestamp;
    }

    public void setTaskCreationTimestamp(long taskCreationTimestamp) {
        this.taskCreationTimestamp = taskCreationTimestamp;
    }

    public long getTaskDeadlineTimestamp() {
        return taskDeadlineTimestamp;
    }

    public void setTaskDeadlineTimestamp(long taskDeadlineTimestamp) {
        this.taskDeadlineTimestamp = taskDeadlineTimestamp;
    }

    public Boolean getCompleted() {
        return completed;
    }

    public void setCompleted(Boolean completed) {
        this.completed = completed;
    }

    @Override
    public String toString() {
        return "Task{" +
                "name='" + name + '\'' +
                ", creation time=" + new Date(taskCreationTimestamp*1000).toString() +
                ", deadline=" + new Date(taskDeadlineTimestamp*1000).toString() +
                '}';
    }
}
