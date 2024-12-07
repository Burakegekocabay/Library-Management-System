package com.library;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Connection;


import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.scene.control.Label;

public class ControllerReset 
{
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

    boolean isFirstLogin()
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
        }
    }
}