package com.milfrost.frek.models;

import com.google.firebase.database.DataSnapshot;

import java.io.Serializable;

/**
 * Created by ASUS on 05/12/2017.
 */

public class Circle implements Serializable{
    public String key;
    public String name;
    public String dateCreated;
    public User[] users;
    public Chat[] chats;

    public Circle(String name,User[] users){
        this.name = name;
        this.users = users;
    }

    public Circle(DataSnapshot dataSnapshot){
        this.key = dataSnapshot.getKey().toString();
        this.name = dataSnapshot.child("name").getValue().toString();
        this.dateCreated = dataSnapshot.child("date_created").getValue().toString();
    }

    public void setUsers (User[] users){
        this.users = users;
    }

    public void setChats(Chat[] chats){
        this.chats = chats;
    }
}
