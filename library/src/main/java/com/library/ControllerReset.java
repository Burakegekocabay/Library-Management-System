package com.library;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Connection;

import java.util.UUID;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;

import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.scene.control.Label;

public class ControllerReset 
{
    @FXML
    private TextField UsernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private TextField passwordTextField;

    @FXML
    private CheckBox showPasswordCheckBox;

    @FXML
    private PasswordField KeyPasswordField;

    @FXML
    private TextField KeyTextField;

    @FXML
    private CheckBox showKeyCheckBox;

    @FXML
    private Label label;

    @FXML
    private Button resetButton;

    @FXML
    private TextArea area;

    Connection conn;

    @FXML
    void initialize() 
    {
        try {
            conn = DriverManager.getConnection(Config.getDbURL(),Config.getUser(),Config.getPassword());
        } catch (Exception e) {
        }

        if (passwordTextField != null && passwordField != null)
        {
            passwordTextField.setVisible(false);
            passwordTextField.setManaged(false);
            passwordField.setManaged(true);
            passwordField.setVisible(true); 

            KeyTextField.setVisible(false);
            KeyTextField.setManaged(false);
            KeyPasswordField.setManaged(true); 
            KeyPasswordField.setVisible(true); 
            
            FirstloginInitialize();
            showPasswordCheckBox.setOnAction(event -> showHidePassword());
            showKeyCheckBox.setOnAction(event -> showKeyPassword());
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
    void showKeyPassword() {
        
        if(showKeyCheckBox.isSelected()){
            KeyTextField.setText(KeyPasswordField.getText());
            KeyTextField.setVisible(true);  
            KeyPasswordField.setVisible(false);
            KeyPasswordField.setManaged(false);
            KeyTextField.setManaged(true);
        }
        else{
            KeyPasswordField.setText(KeyTextField.getText());
            KeyTextField.setVisible(false); 
            KeyPasswordField.setVisible(true);  
            KeyTextField.setManaged(false);
            KeyPasswordField.setManaged(true);
        }
    }

    boolean isFirstLogin() //Checking if it's the first login
    {
        String query = "SELECT securityKEY FROM staff WHERE securityKEY IS NULL LIMIT 1";

        try (PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

                return(!rs.next());
            
        } catch (SQLException e) {
            
        }
        return false;
    }

    void FirstloginInitialize() {
        if (isFirstLogin()) {
            resetButton.setText("CREATE");
            label.setText("CREATE PASSWORD");
            area.setText("Leave the 'Security Key' field blank. A Security Key will be generated for you automatically. Make sure to store this key securely, as you will not be able to reset your password without it.");
            KeyPasswordField.setEditable(false);
            KeyTextField.setEditable(false);
            KeyPasswordField.setPromptText("Leave this field blank");
            KeyTextField.setPromptText("Leave this field blank");
            KeyPasswordField.setDisable(true);
            showKeyCheckBox.setDisable(true);
        }
    }


    String makeKEY() //Generates a key from UUID
    {
        return (UUID.randomUUID().toString().replace("-", "").substring(0,20).toUpperCase());
    }

    @FXML
    void ResetMethod()
    {
        if (showKeyCheckBox.isDisable()) //it means the first sign up
        {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Security Key");
            alert.setHeaderText(null);
            String key = makeKEY();
            alert.setContentText("MAKE SURE TO STORE THIS KEY SECURELY \n "+ key);
            alert.showAndWait();
            updateDetails(key);
            key = null;
        }
        else
        {
            //Security key validation operation will be here
        }
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/library/StaffMainPage.fxml"));
        AnchorPane root2;
        try {
            root2 = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Staff Main Page");
            stage.setScene(new Scene(root2));
            stage.show();
            Stage currentStage = (Stage) resetButton.getScene().getWindow();
            currentStage.close();
        } catch (Exception e) {}
    }

    void updateDetails(String key) //updating username pass and key
    {
        String updateQuery = "UPDATE staff SET username = ?, pass = ?, securityKEY = ?";
        try 
        {
            PreparedStatement stmt = conn.prepareStatement(updateQuery);

            stmt.setString(1, InjectionPreventer(UsernameField.getText()));

            if (showPasswordCheckBox.isSelected())
                stmt.setString(2, InjectionPreventer(passwordTextField.getText()));
            else
                stmt.setString(2, InjectionPreventer(passwordField.getText()));

            stmt.setString(3, key);
            stmt.executeUpdate();
            stmt.close();

        }catch(Exception e){};
            
    }

    String InjectionPreventer(String s)
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
}