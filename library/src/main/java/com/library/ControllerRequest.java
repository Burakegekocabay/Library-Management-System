package com.library;


import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;


public class ControllerRequest {
    @FXML private TextField memberIdField;
    @FXML private TextField borrowLimitField;
    @FXML private TextField bookIdField;
    @FXML private TextField bookTitleField;
    @FXML private TextField bookStatusField;
    @FXML private TextField borrowDateField;
    @FXML private TextField dueDateField;


    void setBookID(String bookID) {
        bookIdField.setText(bookID);
    }

    void setBookTitle(String bookTitle) {
        bookTitleField.setText(bookTitle);
    }

    void setMemberID(String memberID) {
        memberIdField.setText(memberID);
    }


    void setBookStatus(String bookStatus) {
        bookStatusField.setText(bookStatus);
    }
   
    void setBorrowLimit(int limit) {
        borrowLimitField.setText(limit + "");
    }

    @FXML
    void cancel() {
        // Close the current window
        Stage stage = (Stage) memberIdField.getScene().getWindow();
        stage.close();
    }

    @FXML
    void complete() {
        String memberId = memberIdField.getText();
        String bookId = bookIdField.getText();
        String sql = "SELECT * FROM books WHERE ID = '" + bookId + "';";
        String sql2 = "SELECT * FROM members WHERE ID = '" + memberId + "';";
        try {
            // Execute the SQL query
            Config.getConn().createStatement().executeUpdate("USE " + Config.getDbNAME());
            ResultSet rs = Config.getConn().createStatement().executeQuery(sql);
            ResultSet rs2 = Config.getConn().createStatement().executeQuery(sql2);
            if (rs.next() && rs2.next()) {
                //Check if account is active
                if (!rs2.getString("status").equals("Active")) {
            
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText(null);
                    alert.setContentText("Your account is passive. Please contact the admin.");
                    alert.showAndWait();
                    return;
                }
                // Check if the book is available
                if (!rs.getBoolean("status")) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText(null);
                    alert.setContentText("The book is not available for borrowing.");
                    alert.showAndWait();
                    return;
                }
                // Check if the member has reached the limit
                int booksLeft = rs2.getInt("books_left");
                if (booksLeft > 0) {

                    String insertSQL = "INSERT INTO requests (member_id, member_name, book_id, book_title, borrow_date, due_date) VALUES (?, ?, ?, ?, ?, ?)";
                    try
                    {
                        PreparedStatement preparedStatement = Config.getConn().prepareStatement(insertSQL);
                        preparedStatement.setString(1, memberId);
                        preparedStatement.setString(2, rs2.getString("name"));
                        preparedStatement.setString(3, bookId);
                        preparedStatement.setString(4, rs.getString("title"));
                        preparedStatement.setString(5, Utils.InjectionPreventer(borrowDateField.getText()));
                        preparedStatement.setString(6, Utils.InjectionPreventer(dueDateField.getText()));
                        preparedStatement.executeUpdate();
                    } catch (Exception e) { e.printStackTrace(); }



                    // Update the database to set the book as borrowed and update member's limit
                    Config.getConn().createStatement().executeUpdate("UPDATE members SET pending = true WHERE ID = '" + memberId + "';");
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Success");
                    alert.setHeaderText(null);
                    alert.setContentText("Borrow request submitted successfully. You can check your request in the profile section.");
                    alert.showAndWait();
                                        
                    // Close the window after completion
                    Stage stage = (Stage) memberIdField.getScene().getWindow();
                    stage.close();
                } else {
                    // Show alert: Member has reached the borrowing limit
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText(null);
                    alert.setContentText("You have reached the borrowing limit. Please return a book before borrowing another one.");
                    alert.showAndWait();
                }
                
            }
        // new database 
        // need to send notification to admin
        // close the window after completion
        }
        catch (Exception e) { e.printStackTrace(); }
    }
}