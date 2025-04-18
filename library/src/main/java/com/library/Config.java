package com.library;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

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

    public static boolean Connect() {
        try {
            conn = DriverManager.getConnection(getLoginURL(), getUser(), getPassword());
            return true;
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
        return false;
    }

    public static Connection getConn() {
        return conn;
    }

    public static boolean databaseExists()
    {
        boolean result = false;
        String query = "SHOW DATABASES LIKE '" + Config.getDbNAME() + "'";
        try (Statement stmt = Config.getConn().createStatement()) 
        {
            ResultSet rs = stmt.executeQuery(query);
            if (rs.next())
                result = true;
        }
        catch (SQLException e) 
        {
            e.printStackTrace();
        }
        return (result);
    }

    public static void create_DB()
    {
        String staff_table = "CREATE TABLE "+ Config.getDbNAME() + ".staff ("
        +"username VARCHAR(255) NOT NULL, "
        +"pass VARCHAR(255) NOT NULL, " 
        +"securityKEY VARCHAR(255) NOT NULL)";

        
        String insertSQL = "INSERT INTO staff (username, pass, securityKEY)"
        + "VALUES (?, ?, ?)";
        
        String members_table = "CREATE TABLE "+ Config.getDbNAME() + ".members ("
        +"ID VARCHAR(255) NOT NULL," 
        +"name VARCHAR(255) NOT NULL, "
        +"mail VARCHAR(255) NOT NULL, " 
        +"phone VARCHAR(255) NOT NULL,"
        +"password VARCHAR(255) NOT NULL,"
        +"books_left INT NOT NULL DEFAULT 3,"  //Maximum 3 books can be borrowed by a member at a time.
        +"pending BOOLEAN NOT NULL DEFAULT FALSE," //TRUE = Pending, FALSE = Not Pending
        + "status VARCHAR(255) NOT NULL DEFAULT 'Active')";

        String books_table = "CREATE TABLE "+ Config.getDbNAME() + ".books ("
        +"ID VARCHAR(255) NOT NULL," 
        +"title VARCHAR(255) NOT NULL, "
        +"author VARCHAR(255) NOT NULL, "
        +"genre VARCHAR(255) NOT NULL, "
        +"status BOOLEAN NOT NULL DEFAULT TRUE)"; //TRUE = Available, FALSE = Not Available

        String borrowing_table = "CREATE TABLE "+ Config.getDbNAME() + ".borrowings ("
        +"borrow_id VARCHAR(255) NOT NULL,"
        +"book_id VARCHAR(255) NOT NULL," 
        +"book_title VARCHAR(255) NOT NULL, "
        +"member_id VARCHAR(255) NOT NULL, "
        +"member_name VARCHAR(255) NOT NULL, "
        +"borrow_date DATE NOT NULL, "
        +"due_date DATE NOT NULL,"
        +"return_date VARCHAR(255) NOT NULL DEFAULT '-' ,"
        +"status VARCHAR(255) NOT NULL DEFAULT 'Borrowed')"; //Borrowed, Returned, Overdue

        String notes_table = "CREATE TABLE "+ Config.getDbNAME() + ".notes ("
        +"date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP," 
        +"id VARCHAR(255) NOT NULL, "
        +"name VARCHAR(255) NOT NULL, "
        +"notes LONGTEXT NOT NULL)";

        String request_table = "CREATE TABLE "+ Config.getDbNAME() + ".requests ("
        +"request_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,"
        +"member_id VARCHAR(255) NOT NULL,"
        +"member_name VARCHAR(255) NOT NULL,"
        +"book_id VARCHAR(255) NOT NULL,"
        +"book_title VARCHAR(255) NOT NULL,"
        +"borrow_date DATE NOT NULL,"
        +"due_date DATE NOT NULL)";

        try (Statement stmt = Config.getConn().createStatement()) 
        {
            stmt.executeUpdate("CREATE DATABASE "+ Config.getDbNAME()); //Create LibraryManagementSystem Database
            stmt.executeUpdate("USE " + Config.getDbNAME());
            stmt.executeUpdate("DROP TABLE IF EXISTS staff");
            stmt.executeUpdate(staff_table); //Crate LibraryManagementSystem.staff table
            PreparedStatement preparedStatement = Config.getConn().prepareStatement(insertSQL);
            preparedStatement.setString(1, "admin");
            preparedStatement.setString(2, "admin");
            preparedStatement.setString(3, "");
            preparedStatement.executeUpdate();
            stmt.executeUpdate(members_table); //Create LibraryManagementSystem.members table
            stmt.executeUpdate(books_table); //Create LibraryManagementSystem.books table
            stmt.executeUpdate(borrowing_table); //Create LibraryManagementSystem.borrowings table
            stmt.executeUpdate(notes_table); //Create LibraryManagementSystem.notes table
            stmt.executeUpdate(request_table); //Create LibraryManagementSystem.requests table
        } catch (SQLException e) {e.printStackTrace();}
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("MYSQL Server");
        alert.setContentText("Database Created");
        alert.showAndWait();
    }

    public static boolean isFirstLogin() //Checking if it's the first login
    {
        String query = "SELECT securityKEY FROM staff LIMIT 1";

        try
        {
            Config.getConn().createStatement().executeUpdate("USE LibraryManagementSystem");
            PreparedStatement stmt = Config.getConn().prepareStatement(query);
            ResultSet rs = stmt.executeQuery();
            if (rs.next())
            {
                String key = rs.getString("securityKEY");
                if (key == null || key.isEmpty())
                    return (true);
            }
            return(false);
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
