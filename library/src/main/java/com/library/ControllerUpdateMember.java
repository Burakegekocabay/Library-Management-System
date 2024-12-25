package com.library;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.scene.control.Label;

public class ControllerUpdateMember
{
    @FXML private TextField ID;
    @FXML private TextField Password;
    @FXML private TextField Name;
    @FXML private TextField Mail;
    @FXML private TextField Phone;
    
    
    @FXML private Button updateButton;
    @FXML private Button deleteButton;
    
    @FXML private Label memberIDLabel;
    @FXML private Label memberPasswordLabel;
    @FXML private Label memberNameLabel;
    @FXML private Label memberMailLabel;
    @FXML private Label memberPhoneLabel;

    private Runnable updateTableCallback;

    public void setMember(Members member,Runnable updateTableCallback) // member is the member to be updated , updateTableCallback is the callback for updating the table
    {
        this.updateTableCallback = updateTableCallback;
        ID.setText(member.getID());
        Password.setText(getPass(member.getID()));
        Name.setText(member.getName());
        Mail.setText(member.getMail());
        Phone.setText(member.getPhone());
    }

    @FXML
    private void updateUser() 
    {
        String sql = "UPDATE members SET Password = ?, Name = ?, Mail = ?, Phone = ? WHERE ID = ?";
        String updatedID = ID.getText();
        String updatedPassword = Password.getText();
        String updatedName = Name.getText();
        String updatedMail = Mail.getText();
        String updatedPhone = Phone.getText();

        try
        {
            Config.getConn().createStatement().executeUpdate("USE "+Config.getDbNAME());
            PreparedStatement statement = Config.getConn().prepareStatement(sql);
            statement.setString(1, updatedPassword);
            statement.setString(2, updatedName);
            statement.setString(3, updatedMail);
            statement.setString(4, updatedPhone);
            statement.setString(5, updatedID);
            statement.executeUpdate();
        }
        catch (Exception e) {e.printStackTrace();}
        closeWindow();
    }

    @FXML
    private void deleteUser()
    {
        String sql = "DELETE FROM members WHERE ID = ?";
        try
        {
            Config.getConn().createStatement().executeUpdate("USE "+Config.getDbNAME());
            PreparedStatement statement = Config.getConn().prepareStatement(sql);
            statement.setString(1, ID.getText());
            statement.executeUpdate();
        }
        catch (Exception e) {}
        closeWindow();
    }

    String getPass(String ID) // returns the password of the user with the given ID from the database
    {
        String currentPassword = null;
        String sql = "SELECT Password FROM members WHERE ID = ?";
        try 
        {
            Config.getConn().createStatement().executeUpdate("USE "+Config.getDbNAME());
            PreparedStatement statement = Config.getConn().prepareStatement(sql);
            statement.setString(1, ID);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) 
                currentPassword = resultSet.getString("Password"); 
            
        } 
    catch (Exception e) {}
    return currentPassword;
    }

    @FXML
    private void closeWindow()
    {
        updateTableCallback.run(); // updates the table in the main window
        Stage currentStage = (Stage) deleteButton.getScene().getWindow();
        currentStage.close();
    }
}
