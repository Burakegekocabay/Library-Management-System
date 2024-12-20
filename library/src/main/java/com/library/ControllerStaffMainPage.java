package com.library;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.scene.Node;

import java.io.IOException;

public class ControllerStaffMainPage {

    @FXML
    private Button bookManagementButton;

    @FXML
    private Button borrowingSystemButton;

    @FXML
    private Button memberManagementButton;

    @FXML
    private Button memberNotesButton;

    @FXML
    private Button homeButton;

    @FXML
    public void initialize() {
        // setButtonImage metodu kaldırıldı
    }

    @FXML
    private void handleButtonAction(ActionEvent event) throws IOException {
        String fxmlFile = "";
        if (event.getSource() == memberManagementButton) {
            fxmlFile = "/com/library/StaffMemberManagement.fxml";
        } else if (event.getSource() == bookManagementButton) {
            fxmlFile = "/com/library/StaffBookManagement.fxml";
        } else if (event.getSource() == borrowingSystemButton) {
            fxmlFile = "/com/library/StaffBorrowingSystem.fxml";
        } else if (event.getSource() == memberNotesButton) {
            fxmlFile = "/com/library/StaffMemberNotes.fxml";
        } else if (event.getSource() == homeButton) {
            fxmlFile = "/com/library/StaffLogin.fxml";
        }

        if (!fxmlFile.isEmpty()) {
            Parent root = FXMLLoader.load(getClass().getResource(fxmlFile));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        }
    }
}