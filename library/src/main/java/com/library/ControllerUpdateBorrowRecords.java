package com.library;

import java.sql.PreparedStatement;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.scene.control.Label;


public class ControllerUpdateBorrowRecords
{
    @FXML private TextField bookID;     // Book ID
    @FXML private TextField memberID;   // Member ID
    @FXML private TextField borrowDate; // Borrow Date
    @FXML private TextField dueDate;    // Due Date
    @FXML private TextField returnDate; // Return Date
    @FXML private ComboBox<String> status;    

    @FXML private Button updateButton;
    @FXML private Button deleteButton;
    
    @FXML private Label bookIDLabel;
    @FXML private Label memberIDLabel;
    @FXML private Label borrowDateLabel;
    @FXML private Label dueDateLabel;
    @FXML private Label returnDateLabel;
    @FXML private Label statusLabel;


    private Runnable updateTableCallback;
    private String borrowID;

    public void setBorrowRecords(BorrowRecords borrowRecords, Runnable updateTableCallback) // borrowRecords is the record to be updated
    {
        this.updateTableCallback = updateTableCallback;
        bookID.setText(borrowRecords.getBookID());
        memberID.setText(borrowRecords.getMemberID());
        borrowDate.setText(borrowRecords.getBorrowDate());
        dueDate.setText(borrowRecords.getDueDate());
        returnDate.setText(borrowRecords.getReturnDate());
        status.setValue(borrowRecords.getStatus());
        this.borrowID = borrowRecords.getBorrowID(); 
    }

    @FXML
    private void updateBorrowRecord()
    {
        if ("Returned".equals(status.getValue())) {
            if (returnDate.getText().isEmpty() || "-".equals(returnDate.getText())) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("Please provide a valid return date.");
                alert.showAndWait();
                return;
            }
       }

        // Updated SQL query without book_title and member_name
        String sql = "UPDATE borrowings SET book_id = ?, member_id = ?, borrow_date = ?, due_date = ?, return_date = ?, status = ? WHERE borrow_id = ?";
        
        String updatedBookID = bookID.getText();
        String updatedMemberID = memberID.getText();
        String updatedBorrowDate = borrowDate.getText();
        String updatedDueDate = dueDate.getText();
        String updatedReturnDate = returnDate.getText();
        String updatedStatus = status.getValue();

        try
        {
            Config.getConn().createStatement().executeUpdate("USE " + Config.getDbNAME());
            PreparedStatement statement = Config.getConn().prepareStatement(sql);
            statement.setString(1, updatedBookID);    // Set Book ID
            statement.setString(2, updatedMemberID);  // Set Member ID
            statement.setString(3, updatedBorrowDate); // Set Borrow Date
            statement.setString(4, updatedDueDate);    // Set Due Date
            statement.setString(5, updatedReturnDate); // Set Return Date
            statement.setString(6, updatedStatus);     // Set Status
            statement.setString(7, borrowID);     // Set Book ID in WHERE clause (for WHERE condition)
            statement.executeUpdate();
        }
        catch (Exception e) { e.printStackTrace(); }
        
        closeWindow();
    }

    @FXML
    private void deleteBorrowRecord()
    {
        String sql = "DELETE FROM borrowings WHERE borrow_id = ?";
        try
        {
            Config.getConn().createStatement().executeUpdate("USE " + Config.getDbNAME());
            PreparedStatement statement = Config.getConn().prepareStatement(sql);
            statement.setString(1, borrowID);
            statement.executeUpdate();
        }
        catch (Exception e) { e.printStackTrace(); }

        closeWindow();
    }

    @FXML
    private void closeWindow()
    {
        updateTableCallback.run(); // updates the table in the main window
        Stage currentStage = (Stage) deleteButton.getScene().getWindow();
        currentStage.close();
    }
}
