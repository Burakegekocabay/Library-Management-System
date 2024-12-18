package com.library;

import java.sql.ResultSet;
import java.sql.Statement;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
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
                if (!((rs.getString("username").equals(InjectionPreventer(StaffUsername.getText())))) || (!(rs.getString("pass").equals(getStaffPassword()))))
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
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/library/StaffMainPage.fxml"));
                AnchorPane root2;
                try 
                {
                    root2 = loader.load();
                    Stage stage = new Stage();
                    stage.setTitle("Staff Main Page");
                    stage.setScene(new Scene(root2));
                    stage.show();
                    Stage currentStage = (Stage) StaffUsername.getScene().getWindow();
                    currentStage.close();
                } catch (Exception e){}

            }
        } 
        catch (Exception e){ e.printStackTrace(); }
    }

    String InjectionPreventer(String s)
    {
        /*
         * This method sanitizes the input string to prevent SQL injection attacks
         * by deleting potentially dangerous characters like '?', '=', '$', '%', '&' and '|'
        */

        s = s.replace("?","");
        s = s.replace("=","");
        s = s.replace("$","");
        s = s.replace("%","");
        s = s.replace("&","");
        s = s.replace("|","");
        return s;
    }


    String getStaffPassword()
    {
        //This method gets the password from the password field or text field

        if (showPasswordCheckbox.isSelected())
        {
            return InjectionPreventer(StaffTextfield.getText());    
        }
        else
            return InjectionPreventer(StaffPasswordfield.getText());
    }

}
