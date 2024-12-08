package com.library;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * JavaFX App
 */
public class Main extends Application 
{
    @Override
    public void start(Stage stage) throws IOException 
    {
        Parent root = FXMLLoader.load(getClass().getResource("/com/library/main.fxml"));
        Scene scene = new Scene(root);
        stage.setTitle("Library Management System");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }

}