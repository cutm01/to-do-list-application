package cz.vse.fis.todolist.application.logic;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.*;

public class ReadUpdateFile {

    public static String USER_DATA_PATH = "src/main/resources/Data/";


    public static UserData readDataFromJSON(String username)
    {
        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().setPrettyPrinting().create();

        try (Reader reader = new FileReader(USER_DATA_PATH + username + ".json")) {
            //convert JSON File to Java Object
            UserData userData = gson.fromJson(reader, UserData.class);

            return userData;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void writeDataToJSON(UserData userData)
    {
        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().setPrettyPrinting().create();

        try (FileWriter writer = new FileWriter(USER_DATA_PATH + userData.getUsername() + ".json")) {
            gson.toJson(userData, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Method to delete JSON file with user data. It is used when user decide to delete
     * his account
     *
     * @param username username of account to delete
     */
    public static void deleteFileWithUserData(String username) {
        File userDataFile = new File(USER_DATA_PATH + username + ".json");
        userDataFile.delete();
    }
}
