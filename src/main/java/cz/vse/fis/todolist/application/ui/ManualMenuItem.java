package cz.vse.fis.todolist.application.ui;

/**
 * ManualMenuItem is setting names for manual chapters
 *
 * @version 1.0.0
 */

public enum ManualMenuItem {
    REGISTER("Register"),
    LOGIN("LogIn"),
    LOGOUT("LogOut"),
    SETTINGS("Settings"),
    ADD_CATEGORY("Add Category"),
    EDIT_CATEGORY("Edit Category"),
    ADD_TASK("Add Task"),
    SELECT_TASK("Select Task"),
    EDIT_TASK("Edit Task"),
    SORT_TASK("Sort Task");

    private final String manualMenuItem;

    /**
     * Method sets manualMenuItem
     *
     */
    ManualMenuItem(final String manualMenuItem) {
        this.manualMenuItem = manualMenuItem;
    }

    /**
     * Method returns values of manual menu items
     *
     * @return ManualMenuItem values
     */
    public static ManualMenuItem[] getAllManualMenuItems() {
        return ManualMenuItem.values();
    }

    /**
     * Method returns manualMenuItem
     *
     * @return manualMenuItem
     */
    @Override
    public String toString() {
        return manualMenuItem;
    }
}
