package com.springmvcapp.model;
import java.util.Date;

public class Message {
    private String sender;
    private String receiver;
    private String text;
    private Date date;
    private String role;

    // Constructor
    public Message( String sender, String receiver, String text, Date date, String role) {
        this.sender = sender;
        this.receiver = receiver;
        this.text = text;
        this.date = date;
        this.role = role;
    }

    // Getters and setters


    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}

