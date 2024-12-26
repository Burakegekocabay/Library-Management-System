package com.library;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javafx.fxml.FXML;

import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
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

    @FXML
    private Button Cancel;

    @FXML
    void initialize() 
    {
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

    void FirstloginInitialize() {
        if (Config.isFirstLogin()) {
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

    @FXML
    void ResetMethod()
    {
        if ((UsernameField.getText().equals("")) || (passwordField.getText().equals("") && passwordTextField.getText().equals("")))
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Please fill in all fields.");
            alert.showAndWait();
            return ;
        }
        if (!showKeyCheckBox.isDisabled()) // Staff has already created a key
        {
            
            String query = "SELECT securityKEY FROM staff WHERE securityKEY IS NOT NULL LIMIT 1";
            try 
            {
                Config.getConn().createStatement().executeUpdate("USE LibraryManagementSystem");
                try (PreparedStatement stmt = Config.getConn().prepareStatement(query);
                ResultSet rs = stmt.executeQuery()) 
                {
                    if (rs.next()) 
                    {
                        String key = rs.getString("securityKEY");
                        //Controlling if key in database is equal to the one in the field
                        if (!(Utils.InjectionPreventer(KeyPasswordField.getText()).equals(key) || Utils.InjectionPreventer(KeyTextField.getText()).equals(key)))
                        {
                            Alert alert = new Alert(Alert.AlertType.ERROR);
                            alert.setTitle("Error");
                            alert.setHeaderText(null);
                            alert.setContentText("Invalid Security Key");
                            alert.showAndWait();
                            return;
                        }
                    }
                }
            } catch (Exception e) {e.printStackTrace();}
        }
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Security Key");
        alert.setHeaderText(null);
        String key = Utils.makeKEY();
        alert.setContentText("MAKE SURE TO STORE THIS KEY SECURELY \n "+ key);
        alert.showAndWait();
        updateDetails(key);
        key = null;

        Stage currentStage = (Stage) resetButton.getScene().getWindow();
        Utils.redirect(currentStage,"/com/library/StaffMainPage.fxml","Staff Dashboard");
    }

    void updateDetails(String key) //updating username pass and key
    {
        String updateQuery = "UPDATE staff SET username = ?, pass = ?, securityKEY = ?";
        try 
        {
            PreparedStatement stmt = Config.getConn().prepareStatement(updateQuery);

            stmt.setString(1, Utils.InjectionPreventer(UsernameField.getText()));

            if (showPasswordCheckBox.isSelected())
                stmt.setString(2, Utils.InjectionPreventer(passwordTextField.getText()));
            else
                stmt.setString(2, Utils.InjectionPreventer(passwordField.getText()));

            stmt.setString(3, key);
            stmt.executeUpdate();
            stmt.close();

        }catch(Exception e){};
            
    }

    @FXML
    void CancelMethod()
    {
        Stage currentStage = (Stage) resetButton.getScene().getWindow();
        Utils.redirect(currentStage,"/com/library/StaffLogin.fxml","Staff Login");
    }
}