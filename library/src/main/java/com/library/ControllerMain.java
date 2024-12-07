package com.library;

import java.sql.Statement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.PasswordField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.scene.control.TextField;    

public class ControllerMain {

    @FXML
    private Button staffLoginPageButton;

    @FXML
    private PasswordField passwordField;

    @FXML
    private TextField passwordTextField;

    @FXML
    private CheckBox showPasswordCheckBox;

    @FXML
    void initialize() 
    {
        if (passwordTextField != null && passwordField != null)
        {
            passwordTextField.setVisible(false);
            passwordTextField.setManaged(false);
            passwordField.setManaged(true); 
            passwordField.setVisible(true); 
            showPasswordCheckBox.setOnAction(event -> showHidePassword());
        }
    }

    
    @FXML
    void showHidePassword() {
        
        if(showPasswordCheckBox.isSelected()){
            passwordTextField.setText(passwordField.getText());
            passwordTextField.setVisible(true);  
            passwordField.setVisible(false);
            passwordField.setManaged(false);
            passwordTextField.setManaged(true);
        }
        else{
            passwordField.setText(passwordTextField.getText());
            passwordTextField.setVisible(false); 
            passwordField.setVisible(true);  
            passwordTextField.setManaged(false);
            passwordField.setManaged(true);
        }
    }


     @FXML
     void toStaffLoginPage()
     {
          try(Connection conn = DriverManager.getConnection(Config.getLoginURL(),Config.getUser(),Config.getPassword()))
          {
               if (!databaseExists(conn))
               {
                    create(conn);
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/library/reset.fxml"));
                    AnchorPane root2 = loader.load();
                    Stage stage = new Stage();
                    stage.setTitle("Reset password");
                    stage.setScene(new Scene(root2));
                    stage.show();
    
                    Stage currentStage = (Stage) staffLoginPageButton.getScene().getWindow();
                    currentStage.close();
                    return ;
               }
               FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/library/Stafflogin.fxml"));
               AnchorPane root2 = loader.load();
               Stage stage = new Stage();
               stage.setTitle("Staff Login Page");
               stage.setScene(new Scene(root2));
               stage.show();
               Stage currentStage = (Stage) staffLoginPageButton.getScene().getWindow();
               currentStage.close();
          }
            catch(Exception e)
            {
                e.printStackTrace();
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

     @FXML
     void toMemberMainPage()
     {
          try(Connection conn = DriverManager.getConnection(Config.getDbURL(),Config.getUser(),Config.getPassword()))
          {
               // Validation operation will be here
               FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/library/MemberMainPage.fxml"));
               AnchorPane root2 = loader.load();
               Stage stage = new Stage();
               stage.setTitle("Member Main Page");
               stage.setScene(new Scene(root2));
               stage.show();

               Stage currentStage = (Stage) staffLoginPageButton.getScene().getWindow();
               currentStage.close();
          }
          catch(Exception e)
          {
               Alert alert = new Alert(Alert.AlertType.ERROR);
               alert.setTitle("MYSQL Server Error");
               alert.setContentText("MYSQL server couldn't find.");
               alert.showAndWait();
          }
     }


     public static boolean databaseExists(Connection conn)
    {
        boolean result = false;
        String query = "SHOW DATABASES LIKE '" + Config.getDbNAME() + "'";
        try (Statement stmt = conn.createStatement()) 
        {
            ResultSet rs = stmt.executeQuery(query);
            if (rs.next())
                result = true;
        }
        catch (Exception e) 
        {
            e.printStackTrace();
        }
        return (result);
    }

     public static void create(Connection conn) 
     {
        String stafftable = "CREATE TABLE "+ Config.getDbNAME() + ".staff ("
        +"username VARCHAR(255) NOT NULL, "
        +"pass VARCHAR(255) NOT NULL, " 
        +"securityKEY VARCHAR(255) NOT NULL)";

        String insertSQL = "INSERT INTO staff (username, pass, securityKEY)"
        + "VALUES (?, ?, ?)";

        try (Statement stmt = conn.createStatement()) 
        {
            stmt.executeUpdate("CREATE DATABASE "+ Config.getDbNAME()); //Create LibraryManagementSystem Database
            stmt.executeUpdate("USE " + Config.getDbNAME());
            stmt.executeUpdate("DROP TABLE IF EXISTS staff");
            stmt.executeUpdate(stafftable); //Crate LibraryManagementSystem.staff table
            PreparedStatement preparedStatement = conn.prepareStatement(insertSQL);
            preparedStatement.setString(1, "admin");
            preparedStatement.setString(2, "admin");
            preparedStatement.setString(3, "");
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("MYSQL Server");
        alert.setContentText("Database Created");
        alert.showAndWait();
    }

  
}
