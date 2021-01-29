package cz.vse.fis.todolist.application.ui;

import java.util.HashMap;
import java.util.Map;

/**
 * ManualChapterText includes texts for every manual section + dealing with the loading of that text.
 *
 * @version 1.0.0
 */

public enum ManualChapterText {
     REGISTER("<h1 style=\"text-align: center;\">Register</h1><p>The user will be prompted to enter the name, password, password again, and password hint. Based on this information, a new account will be created for him after clicking on the \"Register\" button.</p>"),
     LOGIN("<h1 style=\"text-align: center;\">LogIn</h1><p>The user enters his login name, after clicking on the \"Log in\" button:\n" +
             "a) if it does not have a password set: the main screen of the application will be displayed <br>\n" +
             "b) if it has a password set: it will be prompted to enter it and after entering the correct password it will display the main page of the application<br>\n" +
             "After clicking on the \"Register\" button, the user will be redirected to a new screen where he will be able to create a new user account. (see verbal description of the “Register” scenario)\n" +
             "After clicking on the “Hint” button, the user will see text help for the password he created during registration.</p>"),
     LOGOUT("<h1 style=\"text-align: center;\">LogOut</h1><p>\n" +
             "After clicking on the appropriate button, the user will be logged out and the application login screen will be displayed.</p>"),
     SETTINGS("<h1 style=\"text-align: center;\">Settings</h1><p>When clicking on the appropriate button, the user will see a new window with application settings, where the following operations will be located:<br>\n" +
             "1) user will have a choice of two avatars that will be displayed in the Main screen of the application next to his name<br>\n" +
             "2) there is also an option that the user enters a new password and its confirmation, after clicking on the appropriate button, a new password is set<br>\n" +
             "3) There is also the option to enter a new password hint, which is then updated after clicking the appropriate button<br>\n" +
             "4) The last option and button is to completely delete the user. By entering the password, the user will be moved to the login screen, while all his data will be deleted</p>"),
     ADD_CATEGORY("<h1 style=\"text-align: center;\">Add Category</h1><p>\n" +
             "The user enters the name of the new task category in the field, which will be created after clicking the appropriate button.</p>"),
     EDIT_CATEGORY("<h1 style=\"text-align: center;\">Edit Category</h1><p>The user has the option to change the name of the category - for each category in the drop-down list there will be a button that will be used to change the name of this category.</p>"),
     ADD_TASK("<h1 style=\"text-align: center;\">Add Task</h1><p>\n" +
             "The user, again by clicking on the appropriate button, enters the name of the task, the time by which the task must be completed, the category in which the task will be included and its text description. Based on this information, a new task will be created after clicking the \"Create\" button</p>"),
     SELECT_TASK("<h1 style=\"text-align: center;\">Select Task</h1><p>any number of them on the main page in the tasks section and perform the following operations using the appropriate buttons:<br>\n" +
             "1) the selected tasks will be marked as completed and moved to the \"Completed tasks\" category<br>\n" +
             "2) the selected tasks will be moved to another, already existing, task category<br>\n" +
             "3) The user will be prompted to enter a name for the new job category, to which all selected jobs will be moved<br>\n" +
             "4) the selected tasks from the left part of the screen will be permanently deleted, before the actual deletion a window will be displayed in which the user will have to confirm this operation</p>"),
     EDIT_TASK("<h1 style=\"text-align: center;\">Edit Task</h1><p>\n" +
             "After clicking on the appropriate button, the user will see a window in which he will be able to edit the name of the task, the time when the task must be completed, its category and the text content.</p>"),
     SORT_TASK("<h1 style=\"text-align: center;\">Sort Task</h1><p>\n" +
             "After clicking on the appropriate button, the user will be able to choose the way in which the tasks will be sorted in the left field.</p>");

     private final String chapterText;
     private static final Map<String, String>  MANUAL_CHAPTERS_WITH_TEXT = loadChapterTexts();

    ManualChapterText(final String chapterText) {
        this.chapterText = chapterText;
    }

    /**
     * Method load chapter texts to strings
     *
     * @return chapterTexts
     */
    private static Map<String, String> loadChapterTexts() {
        Map<String, String> chapterTexts = new HashMap<>();

        int i = 0;
        for (ManualMenuItem item : ManualMenuItem.values()) {
            chapterTexts.put(item.toString(), ManualChapterText.values()[i].toString());
            i++;
        }

        return  chapterTexts;
    }

    /**
     * Method gives chapter text based on chapterName
     *
     * @return  chapterText
     */
    public static String getTextForManualChapter(String chapterName) {
        return MANUAL_CHAPTERS_WITH_TEXT.get(chapterName);
    }

    /**
     * Method gives string chapter text
     *
     * @return  chapterText
     */
    @Override
    public String toString() {
        return chapterText;
    }
}
