package com.library;


public class BorrowRecords {
    private String borrowID;
    private String bookID;
    private String bookTitle;
    private String memberID;
    private String memberName;
    private String borrowDate;
    private String dueDate;
    private String return_date;
    private String status;

    public BorrowRecords(String borrowID, String bookID, String bookTitle, String memberID, String memberName,String borrowDate, String dueDate, String return_date, String status) {
        this.borrowID = borrowID;
        this.bookID =bookID;
        this.bookTitle =bookTitle;
        this.memberID =memberID;
        this.memberName =memberName;
        this.borrowDate =borrowDate;
        this.dueDate =dueDate;
        this.return_date =return_date;
        this.status =status;
    }

    // Getter methods
    public String getBorrowID() { return borrowID; }
    public String getBookID() { return bookID; }
    public String getBookTitle() { return bookTitle; }
    public String getMemberID() { return memberID; }
    public String getMemberName() { return memberName; }
    public String getBorrowDate() { return borrowDate; }
    public String getDueDate() { return dueDate; }
    public String getReturnDate() {
        return (return_date != null) ? return_date : "0000-00-00"; // Return "0000-00-00" if return date is null
    }
    public String getStatus() { return status; }

}
