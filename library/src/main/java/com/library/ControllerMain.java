package com.library;

import java.sql.ResultSet;
import java.sql.Statement;

import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
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
    private Button member_Login_Button;

    @FXML
    private TextField member_IDField;

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
        Stage currentStage = (Stage) staffLoginPageButton.getScene().getWindow();
        if (!Config.databaseExists())
        {
             Config.create_DB();
             Utils.redirect(currentStage, "/com/library/reset.fxml", "Create Password");
             return ;
        }
        Utils.redirect(currentStage, "/com/library/StaffLogin.fxml", " Staff Login"); 
     }

     @FXML
     void toMemberMainPage()
     {
        Stage currentStage = (Stage) staffLoginPageButton.getScene().getWindow();
        Utils.redirect(currentStage,"/com/library/MemberMainPage.fxml"," Library Management System");
     }

    @FXML
    void toMemberMain()
    {
        String sql = "SELECT * FROM members WHERE ID = '" + Utils.InjectionPreventer(member_IDField.getText()) + "' AND password = '" + Utils.InjectionPreventer(passwordField.getText()) + "'";
        try
        {
            //Check if the username and password are correct
            Config.getConn().createStatement().executeUpdate("USE " + Config.getDbNAME());
            Statement statement = Config.getConn().createStatement();
            ResultSet rs = statement.executeQuery(sql);
            if (rs.next()) 
            {
                //If the username and password are correct, redirect to the member dashboard
                Stage currentStage = (Stage) member_Login_Button.getScene().getWindow();
                Utils.redirect(currentStage, "/com/library/MemberMainPage.fxml", member_IDField.getText());
            }
            else
            {
                //If the username or password are not correct, display an error message
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("Invalid username or password");
                alert.showAndWait();
                return ;
            }
        } 
        catch (Exception e){ e.printStackTrace(); }
    }

}
