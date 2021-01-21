package cz.vse.fis.todolist.application.logic;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;

public class ReadUpdateFile {

    String path = "src/main/resources/Data/";


    public UserData readDataFromJSON(String username)
    {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        try (Reader reader = new FileReader(path + username + ".json")) {

            // Convert JSON File to Java Object
            return gson.fromJson(reader, UserData.class);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void writeDataToJSON(UserData userData)
    {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        try (FileWriter writer = new FileWriter(path + userData.getUsername() + ".json")) {
            gson.toJson(userData, writer);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
