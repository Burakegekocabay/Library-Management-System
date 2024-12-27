package com.library;

import java.sql.ResultSet;
import java.sql.Statement;

import javafx.fxml.FXML;

import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class ControllerStaffLogin
{
    @FXML
    private TextField StaffUsername;

    @FXML
    private PasswordField StaffPasswordfield;

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
        Stage currentStage = (Stage) forgotPassword.getScene().getWindow();
        Utils.redirect(currentStage, "/com/library/reset.fxml","Forgot Password" );
    }

    @FXML
    void login()
    {
        String sql = "SELECT * FROM staff";
        try
        {
            Config.getConn().createStatement().executeUpdate("USE LibraryManagementSystem");
            Statement statement = Config.getConn().createStatement();
            ResultSet rs = statement.executeQuery(sql);
            if (rs.next()) 
            {
                if (!((rs.getString("username").equals(Utils.InjectionPreventer(StaffUsername.getText())))) || (!(rs.getString("pass").equals(getStaffPassword()))))
                {
                    //If the username or password are not correct, display an error message
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText(null);
                    alert.setContentText("Invalid username or password");
                    alert.showAndWait();
                    return ;
                }
                //If the username and password are correct, redirect to the staff dashboard
                Stage currentStage = (Stage) StaffUsername.getScene().getWindow();
                Utils.redirect(currentStage, "/com/library/StaffMainPage.fxml", "Staff Dashboard");
            }
        } 
        catch (Exception e){ e.printStackTrace(); }
    }



    String getStaffPassword()
    {
        //This method gets the password from the password field or text field

        if (showPasswordCheckbox.isSelected())
        {
            return Utils.InjectionPreventer(StaffTextfield.getText());    
        }
        else
            return Utils.InjectionPreventer(StaffPasswordfield.getText());
    }

    @FXML
    void toMemberLogin()
    {
        Stage currentStage = (Stage) StaffUsername.getScene().getWindow();
        Utils.redirect(currentStage,"/com/library/main.fxml" , "Library Management System");
    }
}
