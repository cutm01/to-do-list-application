package cz.vse.fis.todolist.application.ui;

/**
 * SettingsMenuItem sets items in settings menu.
 * @version 1.0.0
 */
public enum SettingsMenuItem {
    CHANGE_PASSWORD("Change password"),
    CHANGE_PASSWORD_HINT("Change password hint"),
    CHANGE_AVATAR("Avatar"),
    DELETE_ACCOUNT("Delete account");
    /**
     * Method creates string.
     */
    private final String settingsMenuItem;

    SettingsMenuItem(final String settingsMenuItem) {
        this.settingsMenuItem = settingsMenuItem;
    }

    /**
     * Method returns values of string.
     * @return values.
     */
    public static SettingsMenuItem[] getAllSettingsMenuItems() {
        return SettingsMenuItem.values();
    }

    /**
     * Method returns string settingsMenuItem
     * @return string
     */
    @Override
    public String toString() {
        return settingsMenuItem;
    }
}
