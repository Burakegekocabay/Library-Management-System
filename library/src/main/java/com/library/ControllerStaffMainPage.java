package com.library;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
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

}