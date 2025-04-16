package com.library;

import java.util.UUID;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class Utils
{
    public static String makeKEY() //Generates a key from UUID
    {
        return (UUID.randomUUID().toString().replace("-", "").substring(0,20).toUpperCase());
    }

    public static String InjectionPreventer(String s)
    {
        /*
         * This method sanitizes the input string to prevent SQL injection attacks
         * by deleting potentially dangerous characters like '?', '=', '$', '%', '&' and '|'
        */

        s = s.replace("?","");
        s = s.replace("=","");
        s = s.replace("$","");
        s = s.replace("%","");
        s = s.replace("&","");
        s = s.replace("|","");
        return s;
    }

    public static void redirect(Stage currentStage,String fxmlPath,String title)
    {
        FXMLLoader loader = new FXMLLoader(Utils.class.getResource(fxmlPath));
        StackPane root2;
        try 
        {
            root2 = loader.load();
            Stage stage = new Stage();
            stage.setTitle(title);
            stage.setScene(new Scene(root2));
            stage.setResizable(false);
            stage.show();
            currentStage.close();
        } catch (Exception e){e.printStackTrace();}
    }

}
