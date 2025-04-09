package com.library;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class ControllerManageRequest {
    @FXML private TextField bookID;     // Book ID
    @FXML private TextField memberID;   // Member ID
    @FXML private TextField borrowDate; // Borrow Date
    @FXML private TextField dueDate;    // Due Date
    @FXML private TextField returnDate; // Return Date

    @FXML private Button updateButton;
    @FXML private Button deleteButton;
    
    private Runnable updateTableCallback;

    public void setRequest(Requests requestRecords, Runnable updateTableCallback) // requestRecords is the record to be updated
    {
        this.updateTableCallback = updateTableCallback;
        bookID.setText(requestRecords.getBookID());
        memberID.setText(requestRecords.getMemberID());
        borrowDate.setText(requestRecords.getBorrowDate());
        dueDate.setText(requestRecords.getDueDate());
    }

    @FXML 
    private void confirm()
    {
        String sql = "Select status from books where ID = '" + bookID.getText() + "';";
        try {
                Statement stmt = Config.getConn().createStatement();
                ResultSet rs = stmt.executeQuery(sql);
                if (rs.next()) {
                    if (!rs.getBoolean("status")) {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Error");
                        alert.setHeaderText(null);
                        alert.setContentText("Book is busy. Reject the request.");
                        alert.showAndWait();
                        return;
                    }
                }
        } catch (Exception e) {
            e.printStackTrace();
        }

        String sql2 = "Select books_left from members where ID = '" + memberID.getText() + "';";
        try {
                Statement stmt = Config.getConn().createStatement();
                ResultSet rs = stmt.executeQuery(sql2);
                if (rs.next()) {
                    if (rs.getInt("books_left") <= 0) {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Error");
                        alert.setHeaderText(null);
                        alert.setContentText("Member has no borrow limit, The user tried to request two books at the same time which is not allowed. Reject the request and Please give appropriate disciplinary action.");
                        alert.showAndWait();
                        return;
                    }
                }
        } catch (Exception e) {
            e.printStackTrace();
        }

        addRecord();

        try {
            Config.getConn().createStatement().executeUpdate("USE " + Config.getDbNAME());
            Config.getConn().createStatement().executeUpdate("DELETE FROM requests WHERE member_id = '" + memberID.getText() + "';");
            Config.getConn().createStatement().executeUpdate("UPDATE members SET pending = 0 WHERE ID = '" + memberID.getText() + "';");
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    @FXML
    private void reject()
    {
        String sql = "DELETE FROM requests WHERE member_id = '" + memberID.getText() + "';";
        String sql2 = "UPDATE members SET pending = 0 WHERE ID = '" + memberID.getText() + "';";
        try {
            Config.getConn().createStatement().executeUpdate("USE " + Config.getDbNAME());
            Config.getConn().createStatement().executeUpdate(sql);
            Config.getConn().createStatement().executeUpdate(sql2);
            updateTableCallback.run();
        } catch (Exception e) {
            e.printStackTrace();
        }
        // Close the current window
        Stage stage = (Stage) memberID.getScene().getWindow();
        stage.close();
    }

    
      private void changeAvailability(String bookID) {
        String query = "UPDATE books SET status = FALSE WHERE ID = ?";
        try (PreparedStatement stmt = Config.getConn().prepareStatement(query)) {
            stmt.setString(1, bookID);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
     

     
       private void decreaseBorrowRights(String memberID) {
        String query = "UPDATE members SET books_left = books_left - 1 WHERE ID = ?";
        try (PreparedStatement stmt = Config.getConn().prepareStatement(query)) {
            stmt.setString(1, memberID);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
      


    void addRecord() {
        // SQL query to insert a new borrow record
        String sql = "INSERT INTO borrowings (borrow_id, book_id, book_title, member_id, member_name, borrow_date, due_date) VALUES (?, ?, ?, ?, ?, ?, ?)";

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
            changeAvailability(bookID.getText()); // Change the book's availability status to busy
        } catch (Exception e) {
            e.printStackTrace();
        }

        closeWindow();
    }

    private String generateBorrowID() {
        String borrowID;
    do {
        borrowID = Utils.makeKEY().substring(0,10);
    } while (isBorrowIDExists(borrowID));
    return borrowID;
    }

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

    @FXML
    private void closeWindow() {
        // Run the callback to update the table after adding the record
        updateTableCallback.run();
        Stage currentStage = (Stage) bookID.getScene().getWindow();
        currentStage.close();
    }
}
