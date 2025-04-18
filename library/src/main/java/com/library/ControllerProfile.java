package com.library;


import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.scene.control.Label;

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
    private TextField limitField;

    @FXML
    private TextField statusField;

    @FXML
    private Button exit;

    @FXML
    private Button cancelRequestButton;

    @FXML
    private CheckBox showPasswordCheckBox;
    
    @FXML
    private Button saveChangesButton;

    @FXML
    private Label label;

    @FXML
    private TextArea area;

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
                member_NameField.setText(rs.getString("name"));
                passwordField.setText(rs.getString("password"));
                passwordTextField.setText(rs.getString("password"));
                limitField.setText(rs.getString("books_left"));
                statusField.setText(rs.getString("status"));
                if (rs.getBoolean("pending") == true) {
                    cancelRequestButton.setVisible(true);
                    area.setVisible(true);
                    label.setText("You have a pending request.");
                } 
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

    @FXML
    void toExit() {
        Stage currentStage = (Stage) cancelRequestButton.getScene().getWindow();
        currentStage.close();
    }

    @FXML
    void cancelRequest() {
        String sql1 = "UPDATE members SET pending = false WHERE ID = '" + member_IDField.getText() + "';";
        String sql2 = "DELETE FROM requests WHERE member_id = '" + member_IDField.getText() + "';";
        try {
            Config.getConn().createStatement().executeUpdate("USE " + Config.getDbNAME());
            Config.getConn().createStatement().executeUpdate(sql1);
            Config.getConn().createStatement().executeUpdate(sql2);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Success");
            alert.setHeaderText(null);
            alert.setContentText("Request cancelled successfully.");
            alert.showAndWait();
        } catch (Exception e) {e.printStackTrace();}
        Stage currentStage = (Stage) cancelRequestButton.getScene().getWindow();
        currentStage.close();
    }
}
