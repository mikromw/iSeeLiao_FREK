package com.milfrost.frek.models;

import com.google.firebase.database.DataSnapshot;

import java.io.Serializable;
import java.util.List;

/**
 * Created by ASUS on 04/12/2017.
 */

public class FirstAidTutorial implements Serializable {
    public String title;
    public String coverUrl;
    public String category;
    public List<TutorialStep> tutorialSteps;

    public FirstAidTutorial(String title,String coverUrl){
        this.title = title;
        this.coverUrl = coverUrl;
    }
    public FirstAidTutorial(DataSnapshot dataSnapshot){
        title = dataSnapshot.child("title").getValue().toString();
        coverUrl = dataSnapshot.child("cover").getValue().toString();
        category = dataSnapshot.child("category").getValue().toString();
    }
}
