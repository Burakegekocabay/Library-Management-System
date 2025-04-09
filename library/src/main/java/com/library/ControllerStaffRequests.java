package com.library;

import java.sql.ResultSet;
import java.sql.Statement;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class ControllerStaffRequests {
    @FXML private TableView<Requests> tableView;
    @FXML private TableColumn<Requests, String> dateColumn;
    @FXML private TableColumn<Requests, String> memberIDColumn;
    @FXML private TableColumn<Requests, String> bookIDColumn;
    @FXML private TableColumn<Requests, String> borrowDateColumn;
    @FXML private TableColumn<Requests, String> dueDateColumn;
    

    @FXML private Button mainMenuButton;
    @FXML private Button manageButton;
    
    private ObservableList<Requests> requestData = FXCollections.observableArrayList();

     @FXML
    public void initialize() {
        // Initialize columns for TableView
        memberIDColumn.setCellValueFactory(new PropertyValueFactory<>("memberID"));
        bookIDColumn.setCellValueFactory(new PropertyValueFactory<>("bookID"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        borrowDateColumn.setCellValueFactory(new PropertyValueFactory<>("borrowDate"));
        dueDateColumn.setCellValueFactory(new PropertyValueFactory<>("dueDate"));
        getRequestsDB();
    }

    void getRequestsDB() {
        String sql1 = "SELECT * FROM requests;"; // SQL query to get all requests
        try {
            Config.getConn().createStatement().executeUpdate("USE " + Config.getDbNAME());
            Statement statement = Config.getConn().createStatement();
            ResultSet resultSet = statement.executeQuery(sql1);

            while (resultSet.next()) {
                String member_id = resultSet.getString("member_id");
                String book_id = resultSet.getString("book_id");
                String date = resultSet.getString("request_date");
                String borrow_date = resultSet.getString("borrow_date");
                String due_date = resultSet.getString("due_date");
                requestData.add(new Requests( date, member_id, book_id,borrow_date, due_date)); 
            }
            statement.close();

            tableView.setItems(requestData);
            tableView.refresh();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void updateTable() {
        requestData.clear(); // Clear the existing data in the table
        tableView.refresh();
        getRequestsDB();
    }

    @FXML
    void toMainMenu() {
        Stage currentStage = (Stage) mainMenuButton.getScene().getWindow();
        Utils.redirect(currentStage,"/com/library/StaffMainPage.fxml"," Library Management System");
    }

    @FXML
    void toManage() {
         if (tableView.getSelectionModel().getSelectedItem() != null) {
            // Get the selected borrowing record
            Requests selectedRecord = tableView.getSelectionModel().getSelectedItem();

            // Load the update scene (FXML) for borrowing record
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/library/ManageRequest.fxml"));
            StackPane root2;
            try {
                // Load the update window
                root2 = loader.load();
                Stage stage = new Stage();
                stage.setTitle("Manage Request");

                // Get the controller for the update screen
                ControllerManageRequest controller = loader.getController();
                controller.setRequest(selectedRecord, this::updateTable); 

                // Set the scene and show the window
                stage.setScene(new Scene(root2));
                stage.show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            // If no record is selected, show an error message
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setTitle("Error");
            alert.setContentText("Please select a borrowing record to update.");
            alert.showAndWait();
        }
    }
    

}

