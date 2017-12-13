package com.milfrost.frek.models;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.DataSnapshot;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by ASUS on 26/11/2017.
 */

public class User implements Serializable{
    private String name;
    private String emailAddress;
    private String mobileNumber;
    private transient LatLng latLng;
    private UserMedia profile;
    private List<String> circleId;

    public User(DataSnapshot dataSnapshot){
        circleId = new ArrayList<>();

        name = dataSnapshot.child("full_name").getValue().toString();
        emailAddress = dataSnapshot.getKey();
        mobileNumber = dataSnapshot.child("mobile").getValue().toString();
        Iterator iterator = dataSnapshot.child("circles").getChildren().iterator();
        while (iterator.hasNext()){
            DataSnapshot data = (DataSnapshot) iterator.next();
            circleId.add(data.getKey().toString());
        }
        latLng = new LatLng(Double.parseDouble(dataSnapshot.child("position").child("Latitude").getValue().toString())
                ,Double.parseDouble(dataSnapshot.child("position").child("Longitude").getValue().toString()));
    }

    public User(String name,LatLng latLng){
        this.name = name;
        this.latLng = latLng;
    }

    public String getEmailAddress() {
        return emailAddress;
    }
    public String getMobileNumber() {
        return mobileNumber;
    }
    public String getName() {
        return name;
    }

    public LatLng getLatLng() {
        return latLng;
    }

    public UserMedia getProfile() {
        return profile;
    }

    public List<String> getCircleId() {
        return circleId;
    }
}
