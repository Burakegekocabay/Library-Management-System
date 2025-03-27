package com.library;

public class Books {
    private String ID;
    private String title;
    private String author;
    private String genre;
    private boolean status; // TRUE = Available, FALSE = Not Available

    public Books(String ID, String title, String author, String genre, boolean status) {
        this.ID = ID;
        this.title = title;
        this.author = author;
        this.genre = genre;
        this.status = status;
    }

    // Getters and Setters
    public String getID() {
        return ID;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getGenre() {
        return genre;
    }

    public boolean getStatus() {
        return status;
    }
}
