package com.library;

public class Members
{
    private String ID;
    private String Name;
    private String Mail;
    private String Phone;
    private String Status;

    public Members (String ID, String Name, String Mail, String Phone, String Status)
    {
        this.ID = ID;
        this.Name = Name;
        this.Mail = Mail;
        this.Phone = Phone;
        this.Status = Status;
    }

    public String getID() {
        return ID;
    }

    public String getMail() {
        return Mail;
    }

    public String getName() {
        return Name;
    }

    public String getPhone() {
        return Phone;
    }

    public String getStatus() {
        return Status;
    }
}
