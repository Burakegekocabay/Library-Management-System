package com.library;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class ControllerStaffLogin
{
    @FXML
    private TextField StaffUsername;

    @FXML
    private TextField StaffPasswordfield;

    @FXML
    private TextField StaffTextfield;

    @FXML
    private Button member_Login_Button;

    @FXML
    private Button staff_LoginPage;

    @FXML
    private CheckBox showPasswordCheckbox;

    @FXML
    private Hyperlink forgotPassword;

    @FXML
    void initialize()
    {
        if (StaffTextfield != null && StaffPasswordfield != null)
        {
            StaffTextfield.setVisible(false);
            StaffTextfield.setManaged(false);
            StaffPasswordfield.setManaged(true);
            StaffPasswordfield.setVisible(true); 
            showPasswordCheckbox.setOnAction(event -> showHidePassword());
        }
    }

    @FXML
    void showHidePassword() {
        
        if(showPasswordCheckbox.isSelected()){
            StaffTextfield.setText(StaffPasswordfield.getText());
            StaffTextfield.setVisible(true);  
            StaffPasswordfield.setVisible(false);
            StaffPasswordfield.setManaged(false);
            StaffTextfield.setManaged(true);
        }
        else{
            StaffPasswordfield.setText(StaffTextfield.getText());
            StaffTextfield.setVisible(false); 
            StaffPasswordfield.setVisible(true);  
            StaffTextfield.setManaged(false);
            StaffPasswordfield.setManaged(true);
        }
    }

    @FXML
    void forgot_password()
    {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/library/reset.fxml"));
        AnchorPane root2;
        try {
            root2 = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Forgot Password");
            stage.setScene(new Scene(root2));
            stage.show();
            Stage currentStage = (Stage) forgotPassword.getScene().getWindow();
            currentStage.close();
        } catch (Exception e) {}
    }
}
