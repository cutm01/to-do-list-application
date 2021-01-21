package cz.vse.fis.todolist.application.main;

public class Task {
    String taskID;
    String name;
    String text;
    String creationDate;
    String deadline;
    Boolean completed;

    public Task(String taskID, String name, String text, String creationDate, String deadline, Boolean completed) {
        this.taskID = taskID;
        this.name = name;
        this.text = text;
        this.creationDate = creationDate;
        this.deadline = deadline;
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

    public String getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }

    public String getDeadline() {
        return deadline;
    }

    public void setDeadline(String deadline) {
        this.deadline = deadline;
    }

    public Boolean getCompleted() {
        return completed;
    }

    public void setCompleted(Boolean completed) {
        this.completed = completed;
    }
}
