package com.library;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class ControllerAddRecord {

    @FXML private TextField memberID;   // Member ID
    @FXML private TextField bookID;     // Book ID
    @FXML private TextField borrowDate; // Borrow Date
    @FXML private TextField dueDate;    // Due Date

    @FXML private Button addButton;
    @FXML private Button cancelButton;

    private Runnable updateTableCallback;

    void setController(Runnable updateTableCallback) {
        this.updateTableCallback = updateTableCallback;
    }

    @FXML
    void addBorrowRecord() {
        // SQL query to insert a new borrow record
        String sql = "INSERT INTO borrowings (borrow_id, book_id, book_title, member_id, member_name, borrow_date, due_date) VALUES (?, ?, ?, ?, ?, ?, ?)";

        
        // Validate input fields
        if (memberID.getText().isEmpty() || bookID.getText().isEmpty() || borrowDate.getText().isEmpty() || dueDate.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Please fill in all required fields.");
            alert.showAndWait();
            return;
        }

        // Validate if MemberID and BookID exist
        if (!controlMemberID(memberID.getText())) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Member ID does not exist.");
            alert.showAndWait();
            return;
        }

        if (!controlBookID(bookID.getText())) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Book ID does not exist.");
            alert.showAndWait();
            return;
        }

        
/************************************************************************************ */
         // Check if the book has already been borrowed and not returned
    if (isBookAlreadyBorrowed(bookID.getText())) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText("This book is already borrowed and not returned.");
        alert.showAndWait();
        return;
    }

    // Check if the member has any borrow rights left
    if (!hasBorrowRights(memberID.getText())) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText("This member has no borrow rights left.");
        alert.showAndWait();
        return;
    }
/*************************************************************************** */
        try {
            // Execute the insert query to add the borrow record
            PreparedStatement statement = Config.getConn().prepareStatement(sql);
            statement.setString(1, generateBorrowID()); // Generate a unique borrow ID
            statement.setString(2, bookID.getText());
            statement.setString(3, getBookTitleByID(bookID.getText()));
            statement.setString(4, memberID.getText());
            statement.setString(5, getMemberNameByID(memberID.getText()));
            statement.setString(6, borrowDate.getText());
            statement.setString(7, dueDate.getText());

            statement.executeUpdate();

            decreaseBorrowRights(memberID.getText()); // Decrease the borrow rights of the member by 1
        } catch (Exception e) {
            e.printStackTrace();
        }

        closeWindow();
    }

    @FXML
    private void closeWindow() {
        // Run the callback to update the table after adding the record
        updateTableCallback.run();
        Stage currentStage = (Stage) addButton.getScene().getWindow();
        currentStage.close();
    }

    private boolean controlMemberID(String memberID) {
        try {
            PreparedStatement statement = Config.getConn().prepareStatement("SELECT * FROM members WHERE ID = ?");
            statement.setString(1, memberID);
            return statement.executeQuery().next(); // Check if the member exists
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private boolean controlBookID(String bookID) {
        try {
            PreparedStatement statement = Config.getConn().prepareStatement("SELECT * FROM books WHERE ID = ?");
            statement.setString(1, bookID);
            return statement.executeQuery().next(); // Check if the book exists
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }


    private String getBookTitleByID(String bookID) {
        // SQL query to get the book title based on BookID
        String sql = "SELECT Title FROM books WHERE ID = ?";
        try {
            PreparedStatement statement = Config.getConn().prepareStatement(sql);
            statement.setString(1, bookID);
            var resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getString("title");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
    private String getMemberNameByID(String memberID) {
        // SQL query to get the member name based on MemberID
        String sql = "SELECT Name FROM members WHERE ID = ?";
        try {
            PreparedStatement statement = Config.getConn().prepareStatement(sql);
            statement.setString(1, memberID);
            var resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getString("name");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    /************************************************** */
    private boolean isBookAlreadyBorrowed(String bookID) {
        String query = "SELECT return_date FROM borrowings WHERE book_id = ? AND (return_date IS NULL OR return_date = '-')";
        try (PreparedStatement stmt = Config.getConn().prepareStatement(query)) {
            stmt.setString(1, bookID);
            ResultSet resultSet = stmt.executeQuery();
            return resultSet.next(); // If there's a result, it means the book is borrowed and not returned
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Method to check if the member has borrow rights left
    private boolean hasBorrowRights(String memberID) {
        String query = "SELECT books_left FROM members WHERE ID = ?";
        try (PreparedStatement stmt = Config.getConn().prepareStatement(query)) {
            stmt.setString(1, memberID);
            ResultSet resultSet = stmt.executeQuery();
            if (resultSet.next()) {
                int borrowRights = resultSet.getInt("books_left");
                return borrowRights > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Method to decrease the borrow rights of the member by 1 after borrowing
    private void decreaseBorrowRights(String memberID) {
        String query = "UPDATE members SET books_left = books_left - 1 WHERE ID = ?";
        try (PreparedStatement stmt = Config.getConn().prepareStatement(query)) {
            stmt.setString(1, memberID);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Method to generate a unique borrow ID
    private String generateBorrowID() {
        String borrowID;
    do {
        borrowID = Utils.makeKEY().substring(0,10);
    } while (isBorrowIDExists(borrowID));
    return borrowID;
    }
    

    // Method to check if the borrow ID already exists
    private boolean isBorrowIDExists(String borrowID) {
        String query = "SELECT * FROM borrowings WHERE borrow_id = ?";
        try (PreparedStatement stmt = Config.getConn().prepareStatement(query)) {
            stmt.setString(1, borrowID);
            ResultSet resultSet = stmt.executeQuery();
            return resultSet.next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

}