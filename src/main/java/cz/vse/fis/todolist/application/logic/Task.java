package cz.vse.fis.todolist.application.logic;

import com.google.gson.annotations.Expose;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

import java.util.Comparator;
import java.util.Date;

/**
 * This class represents user task and contains methods for editing task in Task instance.
 *
 * @author  Petr Cafourek
 * @version 1.0
 * @since   2021-28-01
 */
public class Task {
    @Expose
    private String taskID;
    @Expose
    private String name;
    @Expose
    private String text;
    @Expose
    private long taskCreationTimestamp;
    @Expose
    private long taskDeadlineTimestamp;
    @Expose
    private Boolean completed;

    private BooleanProperty selected = new SimpleBooleanProperty(false);

    /**
     * Constructor to set default values to attributes which are not present in JSON
     * during deserialization
     */
    public Task() {
        selected = new SimpleBooleanProperty(false);
    }

    /**
     * Constructor for creating instance of new task
     *
     * @param  taskID  task ID used for internal reference
     * @param  name  name created by user displayed as name in gui
     * @param  text  main text written by user
     * @param  taskCreationTimestamp  task date of creation represented as timestamp in milliseconds from Epoch
     * @param  taskDeadlineTimestamp  datetime of task deadline represented as timestamp in milliseconds from Epoch
     * @param  completed  boolean containing whether task was completed or not
     */
    public Task(String taskID, String name, String text, long taskCreationTimestamp, long taskDeadlineTimestamp, Boolean completed) {
        this.taskID = taskID;
        this.name = name;
        this.text = text;
        this.taskCreationTimestamp = taskCreationTimestamp;
        this.taskDeadlineTimestamp = taskDeadlineTimestamp;
        this.completed = completed;
        setSelected(false);
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

    public final BooleanProperty selectedProperty() {
        return this.selected;
    }


    public final boolean isSelected() {
        return this.selectedProperty().get();
    }


    public final void setSelected(final boolean selected) {
        this.selectedProperty().set(selected);
    }
}
