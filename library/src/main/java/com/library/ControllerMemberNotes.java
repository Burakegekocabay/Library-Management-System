package com.library;

import java.sql.Statement;
import java.sql.ResultSet;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

public class ControllerMemberNotes {

    private ObservableList<Notes> noteList = FXCollections.observableArrayList();
    
    @FXML
    private TableView<Notes> tableView;

    @FXML
    private TableColumn<Notes, String> date;  
    @FXML
    private TableColumn<Notes, String> id;
    @FXML
    private TableColumn<Notes, String> name;  
    @FXML
    private TableColumn<Notes, String> notes;
    
    @FXML
    private Button mainMenuButton;

    @FXML
    private void initialize() {
        // Initialize the table columns
        date.setCellValueFactory(new PropertyValueFactory<>("Date"));
        id.setCellValueFactory(new PropertyValueFactory<>("memberId"));
        name.setCellValueFactory(new PropertyValueFactory<>("Name"));
        notes.setCellValueFactory(new PropertyValueFactory<>("Notes"));
        getUsersDB();
    }

    private void getUsersDB() {
        String sql = "SELECT * FROM notes"; // SQL 
        try {
            Config.getConn().createStatement().executeUpdate("USE " + Config.getDbNAME());
            Statement statement = Config.getConn().createStatement();
            ResultSet resultSet = statement.executeQuery(sql);

            while (resultSet.next()) {
                String date = resultSet.getString("date");
                String id = resultSet.getString("id");
                String name = resultSet.getString("name");
                String notes = resultSet.getString("notes");
                noteList.add(new Notes(date, id, name, notes)); // Add the data to the observable list
            }
            statement.close();
            tableView.setItems(noteList); 
            tableView.refresh();

            // Apply sorting after data is loaded
            tableView.getSortOrder().clear(); // Clear any existing sort order
            tableView.getSortOrder().add(date); // Add the date column to the sort order
            date.setSortType(TableColumn.SortType.DESCENDING); // Set descending sort type
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @FXML
    void toMainMenu() {
        Stage currentStage = (Stage) mainMenuButton.getScene().getWindow();
        Utils.redirect(currentStage, "/com/library/StaffMainPage.fxml", "Library Management System");
    }
}

