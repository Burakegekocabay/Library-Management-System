package com.library;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class ControllerProfile {
    
    @FXML
    private TextField member_IDField;

    @FXML
    private TextField member_NameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private TextField passwordTextField;

    @FXML
    private CheckBox showPasswordCheckBox;
    
    @FXML
    private Button saveChangesButton;

    @FXML
    void initialize() 
    {
        if (passwordTextField != null && passwordField != null && showPasswordCheckBox != null)
        {
            passwordTextField.setVisible(false);
            passwordTextField.setManaged(false);
            passwordField.setManaged(true); 
            passwordField.setVisible(true); 
            showPasswordCheckBox.setOnAction(event -> showHidePassword());
        }

        javafx.application.Platform.runLater(() -> {
            if (member_IDField != null && member_IDField.getScene() != null) {
                javafx.stage.Window window = member_IDField.getScene().getWindow();
                if (window instanceof javafx.stage.Stage) {
                    String stageTitle = ((javafx.stage.Stage) window).getTitle();
                    getInformations(stageTitle);
                }
            }
        });
        
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

    void getInformations(String stageTitle) {
        String sql = "SELECT * FROM members WHERE ID = '" + stageTitle + "';";
        try {
            Config.getConn().createStatement().executeUpdate("USE " + Config.getDbNAME());
            java.sql.ResultSet rs = Config.getConn().createStatement().executeQuery(sql);
            if (rs.next()) {
                member_IDField.setText(rs.getString("ID"));
                member_NameField.setText(Utils.InjectionPreventer(rs.getString("name")));
                passwordField.setText(Utils.InjectionPreventer(rs.getString("password")));
                passwordTextField.setText(Utils.InjectionPreventer(rs.getString("password")));
            }
        } catch (Exception e) {e.printStackTrace();}
    }

    @FXML
    void saveChanges() {
        String newPassword = Utils.InjectionPreventer(passwordField.getText());
        if (showPasswordCheckBox.isSelected()) {
            if (passwordField.getText().isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("Please enter a password.");
                alert.showAndWait();
                return;
            }
            newPassword = Utils.InjectionPreventer(passwordTextField.getText());
        
        } 

        String sql = "UPDATE members SET password = '" + newPassword + "' WHERE ID = '" + member_IDField.getText() + "';";
        try {
            Config.getConn().createStatement().executeUpdate("USE " + Config.getDbNAME());
            Config.getConn().createStatement().executeUpdate(sql);
        } catch (Exception e) {e.printStackTrace();}

        Stage currentStage = (Stage) passwordField.getScene().getWindow();
        currentStage.close();
    }
}
