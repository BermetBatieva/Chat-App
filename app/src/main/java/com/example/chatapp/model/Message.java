package com.example.chatapp.model;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.ServerTimestamp;

public class Message {
    private String id;
    private String displayName;
    private String text;
    private String senderId;
    @ServerTimestamp
    private Timestamp timestamp;



    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public void setTime(Timestamp timestamp) {
        this.timestamp = timestamp;
    }


    public Timestamp getTime() {
        return timestamp;
    }



    public String getText() {
        return text;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }
}