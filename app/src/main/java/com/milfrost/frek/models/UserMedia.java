package com.milfrost.frek.models;

import com.google.firebase.database.DataSnapshot;

import java.io.Serializable;

/**
 * Created by ASUS on 01/12/2017.
 */

public class UserMedia implements Serializable {
    public String name;
    public String type;
    public String downloadUrl;

    public UserMedia(DataSnapshot dataSnapshot){
        name = dataSnapshot.child("name").getValue().toString();
        if(dataSnapshot.child("type").getValue()!=null) {
            type = dataSnapshot.child("type").getValue().toString();
        }
        downloadUrl = dataSnapshot.child("url").getValue().toString();
    }
}
