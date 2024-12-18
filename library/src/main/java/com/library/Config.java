package com.library;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import javafx.scene.control.Alert;

public class Config 
{
    private static String dbNAME = "LibraryManagementSystem";
    private static String connector = "jdbc:mysql://";
    private static Connection conn = null;
    
    public static String getLoginURL() {
        return connector+System.getenv("MYSQL_URL");
    }

    public static String getUser() {
        return System.getenv("MYSQL_USER");
    }

    public static String getPassword() {
        if (System.getenv("MYSQL_PASS") == null)
            return "";
        else
            return System.getenv("MYSQL_PASS");
    }

    public static String getDbNAME() {
        return dbNAME;
    }

    public static String getDbURL() {
        return connector + "/" + dbNAME;
    }

    public static void Connect() {
        try {
            conn = DriverManager.getConnection(getLoginURL(), getUser(), getPassword());
        } catch (SQLException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("Please ensure that your MySQL server is running. Additionally,\n"
                +"Environment variables should be set as follows:\n"
                +"MYSQL_URL = yourServerHost:3306\n"
                +"MYSQL_USER = username\n"
                +"MYSQL_PASS = password(for no password do not create MYSQL_PASS variable)\n");
                alert.showAndWait();
        }
    }

    public static Connection getConn() {
        return conn;
    }
}
