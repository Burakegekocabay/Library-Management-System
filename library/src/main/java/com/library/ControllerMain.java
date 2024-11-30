package com.library;

import java.sql.Statement;
import java.sql.Connection;
import java.sql.DriverManager;
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
    private Button showPasswordButton;

    @FXML
    private TextField passwordTextField;

    

    @FXML
    private CheckBox showPasswordCheckBox;

    @FXML
void initialize() {
    passwordTextField.setVisible(false);
    passwordTextField.managedProperty().bind(passwordTextField.visibleProperty());
    passwordField.managedProperty().bind(passwordField.visibleProperty());

    
    showPasswordCheckBox.setOnAction(event -> showHidePassword());
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
          try(Connection conn = DriverManager.getConnection(Config.getUrl(),Config.getUser(),Config.getPassword()))
          {
               if (!databaseExists(conn))
               {
                    create(conn);
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("reset.fxml"));
                    AnchorPane root2 = loader.load();
                    Stage stage = new Stage();
                    stage.setTitle("Staff Login Page");
                    stage.setScene(new Scene(root2));
                    stage.show();
    
                    Stage currentStage = (Stage) staffLoginPageButton.getScene().getWindow();
                    currentStage.close();
                    return ;
               }

               FXMLLoader loader = new FXMLLoader(getClass().getResource("Stafflogin.fxml"));
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
               Alert alert = new Alert(Alert.AlertType.ERROR);
               alert.setTitle("MYSQL Server Error");
               alert.setContentText("MYSQL server couldn't find.");
               alert.showAndWait();
          }
     }

     @FXML
     void toMemberMainPage()
     {
          try(Connection conn = DriverManager.getConnection(Config.getUrl(),Config.getUser(),Config.getPassword()))
          {
               // Validation operation will be here
               FXMLLoader loader = new FXMLLoader(getClass().getResource("MemberMainPage.fxml"));
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
          String database = "CREATE DATABASE "+Config.getDbNAME();  
        
          String stafftable = "CREATE TABLE IF NOT EXISTS "+ Config.getDbNAME() + ".staff (" +
                                  "username VARCHAR(255) NOT NULL, " +
                                  "pass VARCHAR(255) NOT NULL, " +
                                  "securityKEY VARCHAR(255) NOT NULL, " +
                                  ")";
        
        try (Statement stmt = conn.createStatement()) 
        {
          stmt.executeUpdate(database); //Create LibraryManagementSystem Database
          stmt.executeUpdate(stafftable); //Crate LibraryManagementSystem.staff table


        } catch (SQLException e) {
               Alert alert = new Alert(Alert.AlertType.ERROR);
               alert.setTitle("MYSQL Server Error");
               alert.setContentText("MYSQL server couldn't find.");
               alert.showAndWait();
        }

    }

  
}
