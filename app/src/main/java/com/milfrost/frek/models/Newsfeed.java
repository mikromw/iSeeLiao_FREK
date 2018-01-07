package com.milfrost.frek.models;

import com.google.firebase.database.DataSnapshot;
import com.milfrost.frek.MyApplication;
import com.milfrost.frek.R;
import com.milfrost.frek.utils.DateParser;

import java.text.SimpleDateFormat;

/**
 * Created by ASUS on 01/12/2017.
 */

public class Newsfeed {
    public String key;
    public String title;
    public String content;
    public String publishDate;
    public User author;
    public String mediaUrl;
    //public UserMedia media;
    public Comment[] comments;
    public int commentAmount;

    public Newsfeed (DataSnapshot dataSnapshot){
        //title = dataSnapshot.child("title").getValue().toString();
        key = dataSnapshot.getKey();
        content = dataSnapshot.child("content").getValue().toString();
        publishDate = dataSnapshot.child("datetime").getValue().toString();
        commentAmount = (int)dataSnapshot.child("comments").getChildrenCount();
        mediaUrl = dataSnapshot.child("media").getValue().toString();
        System.out.println("commentAmount = "+ commentAmount);
    }

    public void setComments (Comment[] comments){
        this.comments = comments;
    }
    public void setAuthor (User author){
        this.author = author;
    }

    /*public void setMedia(UserMedia media) {
        this.media = media;
    }*/

    public String getPostingTime(){
        long currentMillis = System.currentTimeMillis();
        long postingMillis = DateParser.getTimeInMillis(publishDate);
        long diff = currentMillis - postingMillis;
        long minute = diff / (60*1000);
        long hour = diff / (60*60*1000);
        if(hour<24 && hour>=1)
            return Long.toString(hour) + " h";
        else if(hour<1)
            return Long.toString(minute) + " min";
        else
            return DateParser.getParsedFormat("dd MMM yyyy",publishDate);
    }

    public String getCommentAmount(){
            if(commentAmount>1){
                String commentStr = MyApplication.getInstance().getApplicationContext().getResources().getString(R.string.amount_comments);
                return String.format(commentStr,commentAmount);
            }else{
                String commentStr =MyApplication.getInstance().getApplicationContext().getResources().getString(R.string.amount_comment);
                return String.format(commentStr,commentAmount);
            }
    }
}
