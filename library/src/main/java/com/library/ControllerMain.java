package com.library;

import java.sql.Statement;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.PasswordField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
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
    public StackPane root;
    @FXML
    public AnchorPane anchorPane;

    @FXML
    void initialize() 
    {
        Config.Connect();
        if (root != null && anchorPane != null) {
            var scaleX = Bindings.min(Bindings.divide(root.widthProperty(), 450.0), 1.5); 
            var scaleY = Bindings.min(Bindings.divide(root.heightProperty(), 600.0), 1.5); 
            anchorPane.scaleXProperty().bind(Bindings.min(scaleX, scaleY));
            anchorPane.scaleYProperty().bind(Bindings.min(scaleX, scaleY));
        } else {
            System.err.println("root veya anchorPane null!");
        }


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
          try
          {
               if (!databaseExists())
               {
                    create();
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/library/reset.fxml"));
                    AnchorPane root2 = loader.load();
                    Stage stage = new Stage();
                    stage.setTitle("Create Password");
                    stage.setScene(new Scene(root2));
                    stage.show();
    
                    Stage currentStage = (Stage) staffLoginPageButton.getScene().getWindow();
                    currentStage.close();
                    return ;
               }
               FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/library/StaffLogin.fxml"));
               AnchorPane root2 = loader.load();
               Stage stage = new Stage();
               stage.setTitle("Staff Login Page");
               stage.setScene(new Scene(root2));
               stage.show();
               Stage currentStage = (Stage) staffLoginPageButton.getScene().getWindow();
               currentStage.close();
          }
            catch(IOException e)
            {
                e.printStackTrace();
            }
     }

     @FXML
     void toMemberMainPage()
     {
          try
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
          catch(IOException e)
          {
            e.printStackTrace();
          }
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

     public static void create() 
     {
        String stafftable = "CREATE TABLE "+ Config.getDbNAME() + ".staff ("
        +"username VARCHAR(255) NOT NULL, "
        +"pass VARCHAR(255) NOT NULL, " 
        +"securityKEY VARCHAR(255) NOT NULL)";

        String insertSQL = "INSERT INTO staff (username, pass, securityKEY)"
        + "VALUES (?, ?, ?)";

        try (Statement stmt = Config.getConn().createStatement()) 
        {
            stmt.executeUpdate("CREATE DATABASE "+ Config.getDbNAME()); //Create LibraryManagementSystem Database
            stmt.executeUpdate("USE " + Config.getDbNAME());
            stmt.executeUpdate("DROP TABLE IF EXISTS staff");
            stmt.executeUpdate(stafftable); //Crate LibraryManagementSystem.staff table
            PreparedStatement preparedStatement = Config.getConn().prepareStatement(insertSQL);
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
