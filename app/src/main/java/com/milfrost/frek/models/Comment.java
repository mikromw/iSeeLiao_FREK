package com.milfrost.frek.models;

import com.google.firebase.database.DataSnapshot;
import com.milfrost.frek.utils.DateParser;

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

    public String getPostingTime(){
        long currentMillis = System.currentTimeMillis();
        long postingMillis = DateParser.getTimeInMillis(dateTime);
        long diff = currentMillis - postingMillis;
        long minute = diff / (60*1000);
        long hour = diff / (60*60*1000);
        if(hour<24 && hour>=1)
            return Long.toString(hour) + " h";
        else if(hour<1)
            return Long.toString(minute) + " min";
        else
            return DateParser.getParsedFormat("dd MMM yyyy",dateTime);
    }
}
