package com.milfrost.frek.models;

import android.graphics.Bitmap;

import com.google.firebase.database.DataSnapshot;

import java.io.Serializable;

/**
 * Created by ASUS on 04/12/2017.
 */

public class TutorialStep implements Serializable{
    public String title;
    public String content;
    public String media;

    public TutorialStep(DataSnapshot ds){
        this.title = ds.child("title").getValue().toString();
        this.content = ds.child("content").getValue().toString();
        this.media = ds.child("media").getValue().toString();
    }
}
