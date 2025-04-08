package com.library;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;

public class ControllerUpdateMember
{
    @FXML private TextField ID;
    @FXML private TextField Password;
    @FXML private TextField Name;
    @FXML private TextField Mail;
    @FXML private TextField Phone;
    @FXML private TextField rights;
    
    @FXML private Button updateButton;
    @FXML private Button deleteButton;
    
    @FXML private Label memberIDLabel;
    @FXML private Label memberPasswordLabel;
    @FXML private Label memberNameLabel;
    @FXML private Label memberMailLabel;
    @FXML private Label memberPhoneLabel;

    @FXML ToggleGroup statusGroup; // Group for the radio buttons
    @FXML RadioButton active;
    @FXML RadioButton passive; 
    @FXML RadioButton banned;

    @FXML private TextArea notesArea;

    private Runnable updateTableCallback;

    public void setMember(Members member,Runnable updateTableCallback) // member is the member to be updated , updateTableCallback is the callback for updating the table
    {
        this.updateTableCallback = updateTableCallback;
        ID.setText(member.getID());
        Password.setText(getPass(member.getID()));
        Name.setText(member.getName());
        Mail.setText(member.getMail());
        Phone.setText(member.getPhone());
        rights.setText(String.valueOf(member.getBooksLeft()));
        
         // Associate radio buttons with the status group
         statusGroup = new ToggleGroup(); // Create a new ToggleGroup
         active.setToggleGroup(statusGroup);  
         passive.setToggleGroup(statusGroup); 
         banned.setToggleGroup(statusGroup);

         if (getNotes() != null) // Check if notes are not null
             notesArea.setText(getNotes()); // Set the notes area with the member ID
         
        if (member.getStatus().equals("Active")) active.setSelected(true);
        else if (member.getStatus().equals("Passive")) passive.setSelected(true);
        else if (member.getStatus().equals("Banned")) banned.setSelected(true);
    }

    @FXML
    private void updateUser() 
    {
        String sql = "UPDATE members SET Password = ?, Name = ?, Mail = ?, Phone = ?, books_left = ?, status = ? WHERE ID = ?";
        String updatedID = ID.getText();
        String updatedPassword = Password.getText();
        String updatedName = Name.getText();
        String updatedMail = Mail.getText();
        String updatedPhone = Phone.getText();
        int updatedRights = Integer.parseInt(rights.getText()); // books_left
        String updatedStatus; // status = Active, Passive, Banned
        if (active.isSelected()) 
            updatedStatus = "Active"; 
        else if (passive.isSelected())
            updatedStatus = "Passive"; 
        else 
            updatedStatus = "Banned"; 

        // Check if the notes area is empty and set the notes accordingly
        if (!notesArea.getText().isEmpty()) 
        {
            String updatedNotes = notesArea.getText(); // Get the notes from the text area
            
            // Delete the existing note, if any
            String deleteSql = "DELETE FROM notes WHERE id = ?";
            try (PreparedStatement deleteStmt = Config.getConn().prepareStatement(deleteSql)) {
            deleteStmt.setString(1, updatedID);
            deleteStmt.executeUpdate();
            } catch (Exception e) {
            e.printStackTrace();
            }
            
            // Insert the new note row
            String insertSql = "INSERT INTO notes (id, name, notes) VALUES (?, ?, ?)";
            try (PreparedStatement insertStmt = Config.getConn().prepareStatement(insertSql)) {
            insertStmt.setString(1, updatedID);
            insertStmt.setString(2, updatedName);
            insertStmt.setString(3, updatedNotes);
            insertStmt.executeUpdate();
            } catch (Exception e) {
            e.printStackTrace();
            }
        }
        else
        {
            // Delete the existing note, if any
            String deleteSql = "DELETE FROM notes WHERE id = ?";
            try (PreparedStatement deleteStmt = Config.getConn().prepareStatement(deleteSql)) {
                deleteStmt.setString(1, updatedID);
                deleteStmt.executeUpdate();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        try
        {
            Config.getConn().createStatement().executeUpdate("USE "+Config.getDbNAME());
            PreparedStatement statement = Config.getConn().prepareStatement(sql);
            statement.setString(1, updatedPassword);
            statement.setString(2, updatedName);
            statement.setString(3, updatedMail);
            statement.setString(4, updatedPhone);
            statement.setInt(5, updatedRights);
            statement.setString(6, updatedStatus);
            statement.setString(7, updatedID); // Set Book ID in WHERE clause (for WHERE condition)
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

    private String getNotes()
    {
        String notes = "";
        String sql = "SELECT notes FROM notes WHERE ID = ?";
        try
        {
            Config.getConn().createStatement().executeUpdate("USE "+Config.getDbNAME());
            PreparedStatement statement = Config.getConn().prepareStatement(sql);
            statement.setString(1, ID.getText());
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) 
                notes = resultSet.getString("notes");
        }
        catch (Exception e) {e.printStackTrace();}
        return notes;
    }
}
