package com.library;

public class Requests {
    private String date;
    private String bookID;
    private String memberID;
    private String borrowDate;
    private String dueDate;

    public Requests(String date, String memberID, String bookID, String borrowDate, String dueDate) {
        this.date = date;
        this.bookID =bookID;
        this.memberID =memberID;
        this.borrowDate =borrowDate;
        this.dueDate =dueDate;
    }

    // Getter methods
    public String getBookID() { return bookID; }
    public String getMemberID() { return memberID; }
    public String getBorrowDate() { return borrowDate; }
    public String getDueDate() { return dueDate; }
    public String getDate() { return date; }

}
