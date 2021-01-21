package cz.vse.fis.todolist.application.logic;

public class Main {
    public static void main(String[] args) {

        String username = "david";
        String password = "cafourek";
        String passwordHint = "city of birth";
        String avatar = "male";



        ReadUpdateFile rw = new ReadUpdateFile();

        UserData userData = new UserData(username, password, passwordHint, avatar);
        UserData loadUserData = new UserData(username);

        /*userData.createTaskCategory("Work");
        userData.createTaskCategory("School");

        Task task = new Task("2","homework from java", "dont forget do complete your hw from java", "1.1.1970", "1.1.1970", false);
        userData.addTaskToCategory(task, "School");

        Task task1 = new Task("1","homework from java", "dont forget do complete your hw from java", "1.1.1970", "1.1.1970", false);
        userData.addTaskToCategory(task, "Work");*/

        System.out.println(loadUserData.isTaskInCategory("1", "School"));

        rw.writeDataToJSON(loadUserData);
    }
}