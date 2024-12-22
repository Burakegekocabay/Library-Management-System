package com.library;

public class Members
{
    private String ID;
    private String Name;
    private String Mail;
    private String Phone;

    public Members (String ID, String Name, String Mail, String Phone)
    {
        this.ID = ID;
        this.Name = Name;
        this.Mail = Mail;
        this.Phone = Phone;
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

}
