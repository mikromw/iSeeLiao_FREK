package com.milfrost.frek.models;

import com.google.firebase.database.DataSnapshot;
import com.milfrost.frek.utils.DateParser;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

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

    public void sortChats(){
        if(chats!=null){
            for(int i=0;i<chats.length;i++){
                for(int j=i+1;j<chats.length;j++){
                    if(DateParser.getTimeInMillis(chats[i].timestamp)>DateParser.getTimeInMillis(chats[j].timestamp)){
                        Chat chat= chats[i];
                        chats[i] = chats[j];
                        chats[j] = chat;
                    }
                }
            }
        }
    }
}
