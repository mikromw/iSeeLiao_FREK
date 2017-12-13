package com.milfrost.frek.models;

import com.google.firebase.database.DataSnapshot;

/**
 * Created by ASUS on 01/12/2017.
 */

public class Comment {
    public User author;
    public String content;
    public String dateTime;
    public int show;

    public Comment (DataSnapshot dataSnapshot){
        content = dataSnapshot.child("content").getValue().toString();
        dateTime = dataSnapshot.child("datetime").getValue().toString();
        show = Integer.parseInt(dataSnapshot.child("show").getValue().toString());
    }

    public void setAuthor (User author){
        this.author = author;
    }
}
