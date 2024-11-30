package com.library;

import java.sql.Statement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.PreparedStatement;

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
import javafx.scene.control.Label;


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
    private Label errorMessageLabel;

    @FXML
    private TextField member_IDField;

    // Validate the member's credentials
    private boolean validateMemberCredentials(Connection conn, String memberID, String password) {
        String query = "SELECT * FROM members WHERE member_id = ? AND password = ?";
        
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, memberID);
            stmt.setString(2, password);
            
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next(); // If the user exists, return true
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Handle the member login action
    @FXML
    void handleMemberLogin() {
        String memberID = member_IDField.getText();
        String password = passwordField.getText();

        if (memberID.isEmpty() || password.isEmpty()) {
            showErrorMessage("Please fill in both fields.");
            return;
        }

        try (Connection conn = DriverManager.getConnection(Config.getConnectionString(), Config.getUser(), Config.getPassword())) {
            if (validateMemberCredentials(conn, memberID, password)) {
                navigateToMemberPage();
            } else {
                showErrorMessage("Invalid Member ID or Password. Please try again.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showErrorMessage("Database connection error. Please contact staff.");
        }
    }

    // Show the error message on the label
    private void showErrorMessage(String message) {
        errorMessageLabel.setText(message);        // Set the error message
        errorMessageLabel.setVisible(true);        // Make it visible
        errorMessageLabel.setManaged(true);        // Include in layout management
    }

    // Clear the error message and hide the label
    private void clearErrorMessage() {
        errorMessageLabel.setText("");             // Clear the message
        errorMessageLabel.setVisible(false);       // Make it invisible
        errorMessageLabel.setManaged(false);       // Exclude from layout management
    }

    // Navigate to the member page upon successful login
    private void navigateToMemberPage() {
        clearErrorMessage();                     // Clear the error message upon successful login
        // Load the member page scene
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("MemberMainPage.fxml"));
            AnchorPane root2 = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Member Main Page");
            stage.setScene(new Scene(root2));
            stage.show();

            Stage currentStage = (Stage) staffLoginPageButton.getScene().getWindow();
            currentStage.close();
        } catch (Exception e) {
            e.printStackTrace();
            showErrorMessage("Error navigating to Member page.");
        }
    }
    @FXML
    void initialize() {
    passwordTextField.setVisible(false);
    passwordTextField.managedProperty().bind(passwordTextField.visibleProperty());
    passwordField.managedProperty().bind(passwordField.visibleProperty());

   
    showPasswordCheckBox.setOnAction(event -> showHidePassword());
}

    // Show or hide the password based on the checkbox
    @FXML
    void showHidePassword() {
        if (showPasswordCheckBox.isSelected()) {
            passwordTextField.setText(passwordField.getText());
            passwordTextField.setVisible(true);
            passwordField.setVisible(false);
            passwordField.setManaged(false);
            passwordTextField.setManaged(true);
        } else {
            passwordField.setText(passwordTextField.getText());
            passwordTextField.setVisible(false);
            passwordField.setVisible(true);
            passwordTextField.setManaged(false);
            passwordField.setManaged(true);
        }
    }

    // Navigate to the staff login page
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

    // Check if the database exists
    public static boolean databaseExists(Connection conn) {
        boolean result = false;
        String query = "SHOW DATABASES LIKE '" + Config.getDbNAME() + "'";
        try (Statement stmt = conn.createStatement()) {
            ResultSet rs = stmt.executeQuery(query);
            if (rs.next())
                result = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    // Create the database and tables if they do not exist
    public static void create(Connection conn) {
        String database = "CREATE DATABASE " + Config.getDbNAME();

        String staffTable = "CREATE TABLE IF NOT EXISTS " + Config.getDbNAME() + ".staff (" +
                "username VARCHAR(255) NOT NULL, " +
                "pass VARCHAR(255) NOT NULL, " +
                "securityKEY VARCHAR(255) NOT NULL)";

        try (Statement stmt = conn.createStatement()) {
            stmt.executeUpdate(database); // Create the LibraryManagementSystem database
            stmt.executeUpdate(staffTable); // Create the LibraryManagementSystem.staff table

        } catch (SQLException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("MYSQL Server Error");
            alert.setContentText("MYSQL server couldn't find.");
            alert.showAndWait();
        }
    }
}
