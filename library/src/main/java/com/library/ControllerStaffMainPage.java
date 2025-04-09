package com.library;

import javafx.scene.control.Label;
import java.sql.ResultSet;
import java.sql.Statement;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.stage.Stage;



public class ControllerStaffMainPage {

    @FXML
    private ImageView bookManagementImageView;

    @FXML
    private ImageView borrowingSystemImageView;

    @FXML
    private ImageView memberManagementImageView;

    @FXML
    private ImageView memberNotesImageView;

    @FXML
    private Button logOutButton;

    @FXML
    private Text text1;

    @FXML
    private Text text2;

    @FXML
    private Label label;

    @FXML
    void initialize()
    {
        checkRequests();
    }
    
    @FXML
    void logOut()
    {
        Stage currentStage = (Stage) logOutButton.getScene().getWindow();
        Utils.redirect(currentStage, "/com/library/main.fxml", "Library Management System");
    }

    @FXML
    void toBorrowing()
    {
        Stage currentStage = (Stage) logOutButton.getScene().getWindow();
        Utils.redirect(currentStage, "/com/library/StaffBorrowingSystem.fxml", "Borrowing System");
    }

    @FXML
    void toBook()
    {
        Stage currentStage = (Stage) logOutButton.getScene().getWindow();
        Utils.redirect(currentStage, "/com/library/StaffBookManagement.fxml", "Book Management");
    }

    @FXML
    void toMember()
    {
        Stage currentStage = (Stage) logOutButton.getScene().getWindow();
        Utils.redirect(currentStage, "/com/library/StaffMemberManagement.fxml", "Member Management");
    }


    @FXML
    void toNotes()
    {
        Stage currentStage = (Stage) logOutButton.getScene().getWindow();
        Utils.redirect(currentStage, "/com/library/StaffMemberNotes.fxml", "Member Notes");
    }

    
    @FXML
    void toRequests()
    {
        Stage currentStage = (Stage) logOutButton.getScene().getWindow();
        Utils.redirect(currentStage, "/com/library/StaffRequests.fxml", "Requests");
    }

    void checkRequests()
    {
        String sql = "SELECT * FROM requests;"; // SQL query to get all requests
        try
        {
            Config.getConn().createStatement().executeUpdate("USE " + Config.getDbNAME());
            Statement statement = Config.getConn().createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            int count = 0;
            while (resultSet.next())
                count++;
            
            if (count > 0)
            {
                text1.setVisible(true);
                text2.setVisible(true);
                label.setVisible(true);
                label.setText(count+"");
            }
        }
        catch (Exception e) { e.printStackTrace(); }
    }
}