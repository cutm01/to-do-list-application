package cz.vse.fis.todolist.application.logic;

/**
 * SortingOptions contains all String constants which can be used for sorting
 * tasks in main application windows. Task can be sorted by their names, creation time
 * or deadline
 */
public class SortingOptions {
    public static final String BY_NAME_FROM_A_TO_Z = "Name from A to Z";
    public static final String BY_NAME_FROM_Z_TO_A = "Name from Z to A";
    public static final String BY_CREATION_TIME_FROM_NEWEST_TO_OLDEST = "Creation time from newest to oldest";
    public static final String BY_CREATION_TIME_FROM_OLDEST_TO_NEWEST = "Creation time from oldest to newest";
    public static final String BY_DEADLINE_FROM_EARLIEST_TO_LATEST = "Deadline from earliest to latest";
    public static final String BY_DEADLINE_FROM_LATEST_TO_OLDEST = "Deadline from latest to earliest";
    //no sorting option is currently selected in GUI and therefore tasks will be sorted in insertion order to category
    public static final String NONE = "None";
}
