package com.library;

public class Notes {
    private String date;
    private String memberId;
    private String name;
    private String notes;

    public Notes(String date, String memberId, String name, String notes) {
        this.date = date;
        this.memberId = memberId;
        this.name = name;
        this.notes = notes;
    }

    // Getter ve Setter metodları
    
    public String getDate() {
        return date;
    }

    public String getMemberId() {
        return memberId;
    }
    public String getName() {
        return name;
    }

    public String getNotes() {
        return notes;
    }

}