package com.library;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
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
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/library/main.fxml"));
        StackPane root2;
        try 
        {
            root2 = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Library Management System");
            stage.setScene(new Scene(root2));
            stage.show();
            Stage currentStage = (Stage) logOutButton.getScene().getWindow();
            currentStage.close();
        } catch (Exception e){e.printStackTrace();}
    }

    @FXML
    void toBorrowing()
    {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/library/StaffBorrowingSystem.fxml"));
        StackPane root2;
        try 
        {
            root2 = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Borrowing System");
            stage.setScene(new Scene(root2));
            stage.show();
            Stage currentStage = (Stage) logOutButton.getScene().getWindow();
            currentStage.close();
        } catch (Exception e){e.printStackTrace();}
    }

    @FXML
    void toBook()
    {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/library/StaffBookManagement.fxml"));
        StackPane root2;
        try 
        {
            root2 = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Book Management System");
            stage.setScene(new Scene(root2));
            stage.show();
            Stage currentStage = (Stage) logOutButton.getScene().getWindow();
            currentStage.close();
        } catch (Exception e){e.printStackTrace();}
    }

    @FXML
    void toMember()
    {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/library/StaffMemberManagement.fxml"));
        StackPane root2;
        try 
        {
            root2 = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Member Management System");
            stage.setScene(new Scene(root2));
            stage.show();
            Stage currentStage = (Stage) logOutButton.getScene().getWindow();
            currentStage.close();
        } catch (Exception e){e.printStackTrace();}
    }


    @FXML
    void toNotes()
    {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/library/StaffMemberNotes.fxml"));
        StackPane root2;
        try 
        {
            root2 = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Member Notes");
            stage.setScene(new Scene(root2));
            stage.show();
            Stage currentStage = (Stage) logOutButton.getScene().getWindow();
            currentStage.close();
        } catch (Exception e){e.printStackTrace();}
    }

}