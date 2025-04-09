package com.library;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;


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
   
    void setBorrowLimit() {
        //TODO 
    }
}