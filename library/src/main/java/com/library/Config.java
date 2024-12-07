package com.library;

public class Config 
{
    private static String dbNAME = "LibraryManagementSystem";
    private static String connector = "jdbc:mysql://";
    
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

    //Connection  method will be added here
}
