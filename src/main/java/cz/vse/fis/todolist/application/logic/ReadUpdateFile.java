package cz.vse.fis.todolist.application.logic;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;

public class ReadUpdateFile {

    public static String USER_DATA_PATH = "src/main/resources/Data/";


    public static UserData readDataFromJSON(String username)
    {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        try (Reader reader = new FileReader(USER_DATA_PATH + username + ".json")) {
            //convert JSON File to Java Object
            UserData userData = gson.fromJson(reader, UserData.class);
            //load categories with tasks into userCategories attribute, this attribute is used
            //to reflect actual state of user's categories and tasks while using application
            userData.loadUserCategoriesWithTasks();

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
            //update attribute which will be written to JSON file according to performed operations
            //while using application
            userData.updateTaskCategory();
            gson.toJson(userData, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
