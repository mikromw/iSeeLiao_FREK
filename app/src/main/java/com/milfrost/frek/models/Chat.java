package com.milfrost.frek.models;

import com.google.firebase.database.DataSnapshot;

import java.io.Serializable;

/**
 * Created by ASUS on 06/12/2017.
 */

public class Chat implements Serializable {
    public static final int RECEIVED_CHAT =0;
    public static final int SENT_CHAT =1;

    public String content;
    public User sender;
    public String timestamp;

    public Chat(User sender,String content,String timestamp){
        this.content = content;
        this.sender = sender;
        this.timestamp = timestamp;
    }

    public Chat(DataSnapshot dataSnapshot){
        this.content = dataSnapshot.child("content").getValue().toString();
        this.timestamp = dataSnapshot.child("time_created").getValue().toString();
    }

    public void setUser(User user){
        this.sender = user;
    }
}
