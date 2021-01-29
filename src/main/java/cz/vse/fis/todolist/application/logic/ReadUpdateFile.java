package cz.vse.fis.todolist.application.logic;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.*;

/**
 * This class consists of methods implementing logic for reading from JSON file and writing into JSON file.
 * UserData object is created from JSON file.
 *
 * @author  Petr Cafourek
 * @version 1.0.0
 * @since   2021-28-01
 */
public class ReadUpdateFile {

    //path to dictionary containing JSON files
    public static String USER_DATA_PATH = "src/main/resources/Data/";


    /**
     * Returns an UserData instance created by Reader from user JSON file.
     *
     * @param  username  an absolute URL giving the base location of the image
     * @return UserData  istance representing data of one user from JSON file
     */

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

    /**
     * This method creates or overwrites (update) JSON files containing data from users with UserData instance.
     *
     * @param  userData  instance representing data of one user which will overwrite / create JSON file
     */
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
