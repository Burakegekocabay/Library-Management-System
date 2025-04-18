package com.library;

import java.sql.ResultSet;
import java.sql.Statement;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class ControllerBorrowingSystem {
     // FXML elements
    @FXML private TextField borrowCode;
    @FXML private TextField bookID;
    @FXML private TextField bookTitle;
    @FXML private TextField memberID;
    @FXML private TextField memberName;
    @FXML private TextField borrowDate;
    @FXML private TextField dueDate;
    @FXML private TextField returnDate;
    @FXML private TextField status;

    @FXML private TableView<BorrowRecords> tableView;
    @FXML private TableColumn<BorrowRecords, String> borrowID;
    @FXML private TableColumn<BorrowRecords, String> bookIDColumn;
    @FXML private TableColumn<BorrowRecords, String> bookTitleColumn;
    @FXML private TableColumn<BorrowRecords, String> memberIDColumn;
    @FXML private TableColumn<BorrowRecords, String> memberNameColumn;
    @FXML private TableColumn<BorrowRecords, String> borrowDateColumn;
    @FXML private TableColumn<BorrowRecords, String> dueDateColumn;
    @FXML private TableColumn<BorrowRecords, String> returnDateColumn;
    @FXML private TableColumn<BorrowRecords, String> statusColumn;

    @FXML private Button searchButton;
    @FXML private Button editButton;
    @FXML private Button addButton;
    @FXML private Button mainMenuButton;

    // Observable List for the TableView
    private ObservableList<BorrowRecords> bookBorrowData = FXCollections.observableArrayList();

    // Initialize method to set up TableView
    @FXML
    public void initialize() {
        // Initialize columns for TableView
        borrowID.setCellValueFactory(new PropertyValueFactory<>("borrowID"));
        bookIDColumn.setCellValueFactory(new PropertyValueFactory<>("bookID"));
        bookTitleColumn.setCellValueFactory(new PropertyValueFactory<>("bookTitle"));
        memberIDColumn.setCellValueFactory(new PropertyValueFactory<>("memberID"));
        memberNameColumn.setCellValueFactory(new PropertyValueFactory<>("memberName"));
        borrowDateColumn.setCellValueFactory(new PropertyValueFactory<>("borrowDate"));
        dueDateColumn.setCellValueFactory(new PropertyValueFactory<>("dueDate"));
        returnDateColumn.setCellValueFactory(new PropertyValueFactory<>("returnDate"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("Status"));
        getBooksDB();
    }

    void getBooksDB() {
        String sql = "SELECT * FROM borrowings";
        try {
            Config.getConn().createStatement().executeUpdate("USE " + Config.getDbNAME());
            Statement statement = Config.getConn().createStatement();
            ResultSet resultSet = statement.executeQuery(sql);

            while (resultSet.next()) {
                String borrow_id = resultSet.getString("borrow_id");
                String book_id = resultSet.getString("book_id");
                String book_title = resultSet.getString("book_title");
                String member_id = resultSet.getString("member_id");
                String member_name = resultSet.getString("member_name");
                String borrow_date = resultSet.getString("borrow_date");
                String due_date = resultSet.getString("due_date");
                String return_date = resultSet.getString("return_date");
                String status = resultSet.getString("status");
                bookBorrowData.add(new BorrowRecords(borrow_id,book_id, book_title, member_id, member_name, borrow_date, due_date, return_date, status)); // add book to list
            }
            statement.close();

            tableView.setItems(bookBorrowData);
            tableView.refresh();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void search() { // Filter book borrowing records based on various fields
    // Create a FilteredList based on the original data
    FilteredList<BorrowRecords> filteredData = new FilteredList<>(bookBorrowData, p -> true);

    // Set a predicate to filter the data based on the search criteria
    filteredData.setPredicate(record -> {
        
        boolean matchesBorrowCode = borrowCode.getText().isEmpty() || record.getBorrowID().toLowerCase().contains(borrowCode.getText().toLowerCase());
        boolean matchesBookID = bookID.getText().isEmpty() || record.getBookID().toLowerCase().contains(bookID.getText().toLowerCase());
        boolean matchesBookTitle = bookTitle.getText().isEmpty() || record.getBookTitle().toLowerCase().contains(bookTitle.getText().toLowerCase());
        boolean matchesMemberID = memberID.getText().isEmpty() || record.getMemberID().toLowerCase().contains(memberID.getText().toLowerCase());
        boolean matchesMemberName = memberName.getText().isEmpty() || record.getMemberName().toLowerCase().contains(memberName.getText().toLowerCase());
        boolean matchesBorrowDate = borrowDate.getText().isEmpty() || record.getBorrowDate().toLowerCase().contains(borrowDate.getText().toLowerCase());
        boolean matchesDueDate = dueDate.getText().isEmpty() || record.getDueDate().toLowerCase().contains(dueDate.getText().toLowerCase());
        boolean matchesReturnDate = returnDate.getText().isEmpty() || record.getReturnDate().toLowerCase().contains(returnDate.getText().toLowerCase());
        boolean matchesStatus = status.getText().isEmpty() || record.getStatus().toLowerCase().contains(status.getText().toLowerCase());

        // Return true if all conditions are met (matches all search criteria)
        return matchesBorrowCode && matchesBookID && matchesBookTitle && matchesMemberID && matchesMemberName &&
               matchesBorrowDate && matchesDueDate && matchesReturnDate && matchesStatus;
    });

    // Set the filtered data as the items for the table view
    tableView.setItems(filteredData);
    tableView.refresh(); // Refresh the table to show updated filtered results
    }


    @FXML
    void updateBorrowRecord() { // Update book borrowing record in database
        if (tableView.getSelectionModel().getSelectedItem() != null) {
            // Get the selected borrowing record
            BorrowRecords selectedRecord = tableView.getSelectionModel().getSelectedItem();

            // Load the update scene (FXML) for borrowing record
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/library/StaffUpdateBorrow.fxml"));
            StackPane root2;
            try {
                // Load the update window
                root2 = loader.load();
                Stage stage = new Stage();
                stage.setTitle("Update Borrow Record");

                // Get the controller for the update screen
                ControllerUpdateBorrowRecords controller = loader.getController();
                controller.setBorrowRecords(selectedRecord, this::updateTable); // Pass the selected record and method to update the table

                // Set the scene and show the window
                stage.setScene(new Scene(root2));
                stage.setResizable(false);
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

    void updateTable() {
        bookBorrowData.clear();
        tableView.refresh();
        getBooksDB();
    }

    @FXML
    void addBorrowRecord() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/library/StaffAddRecord.fxml"));
        StackPane root2;
        try {
            root2 = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Add Borrow Record");
            
            // Get the controller of the StaffAddBorrowRecord.fxml
            ControllerAddRecord controller = loader.getController(); 
            controller.setController(this::updateTable); // Pass the method to update the table after adding
            
            stage.setScene(new Scene(root2));
            stage.setResizable(false);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void toMainMenu() {
        Stage currentStage = (Stage) mainMenuButton.getScene().getWindow();
        Utils.redirect(currentStage,"/com/library/StaffMainPage.fxml"," Library Management System");
    }
}
