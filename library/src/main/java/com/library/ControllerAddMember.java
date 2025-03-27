package com.library;

import java.sql.PreparedStatement;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class ControllerAddMember
{
    
    @FXML private TextField ID;
    @FXML private TextField Password;
    @FXML private TextField Name;
    @FXML private TextField Mail;
    @FXML private TextField Phone;
    
    
    @FXML private Button addButton;
    @FXML private Button cancelButton;
    
    @FXML private Label memberIDLabel;
    @FXML private Label memberPasswordLabel;
    @FXML private Label memberNameLabel;
    @FXML private Label memberMailLabel;
    @FXML private Label memberPhoneLabel;
    
    private Runnable updateTableCallback;
    
    void setController(Runnable updateTableCallback)
    {
        this.updateTableCallback = updateTableCallback;
    }

    @FXML
    void addMember()
    {
        String sql = "INSERT INTO members (ID, Password, Name, Mail, Phone) VALUES ( ?,?,?,?,?)";
        if (ID.getText().isEmpty() || Password.getText().isEmpty() || Name.getText().isEmpty() || Mail.getText().isEmpty() || Phone.getText().isEmpty())
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Please fill in all fields");
            alert.showAndWait();
            return ;
        }

        if (controlID(ID.getText()) == false)
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("ID already exists");
            alert.showAndWait();
            return ;
        }
        try 
        {
            PreparedStatement statement = Config.getConn().prepareStatement(sql);
            statement.setString(1, ID.getText());
            statement.setString(2, Password.getText());
            statement.setString(3, Name.getText());
            statement.setString(4, Mail.getText());
            statement.setString(5, Phone.getText());
            statement.executeUpdate();
        
        }
        catch(Exception e){e.printStackTrace();}    
        closeWindow();
    }

    @FXML
    private void closeWindow()
    {
        updateTableCallback.run(); // updates the table in the main window
        Stage currentStage = (Stage) addButton.getScene().getWindow();
        currentStage.close();
    }

    private boolean controlID(String ID)
    {
        try {
            PreparedStatement statement = Config.getConn().prepareStatement("SELECT * FROM members WHERE ID = ?");
            statement.setString(1, ID);
            return !statement.executeQuery().next();
            }
        catch (Exception e) {e.printStackTrace();}
        return false;
    }
}
