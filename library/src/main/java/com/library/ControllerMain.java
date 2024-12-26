package com.library;

import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
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
  
}
