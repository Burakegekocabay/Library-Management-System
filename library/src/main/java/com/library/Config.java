package com.library;

public class Config 
{
    private static String url = "jdbc:mysql://localhost:3306"; // Localhost adress
    private static String user = "root"; //default localhost username
    private static String password = ""; //default localhost password
    private static String dbNAME = "LibraryManagementSystem";

    
    public static String getUrl() {
        return url;
    }

    public static String getUser() {
        return user;
    }

    public static String getPassword() {
        return password;
    }

    public static String getDbNAME() {
        return dbNAME;
    }

    public static String getConnectionString() {
        return url + "/" + dbNAME;
    }

    /*  
    Setter methods will be added to this class (Config.java)
     once config.fxml is completed and ready to use.
    */



}
