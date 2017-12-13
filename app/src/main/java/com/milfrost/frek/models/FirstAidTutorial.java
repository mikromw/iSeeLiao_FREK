package com.milfrost.frek.models;

import com.google.firebase.database.DataSnapshot;

import java.util.List;

/**
 * Created by ASUS on 04/12/2017.
 */

public class FirstAidTutorial {
    public String title;
    public String content;
    public String coverUrl;
    public List<TutorialStep> tutorialSteps;

    public FirstAidTutorial(String title,String content){
        this.title = title;
        this.content = content;
    }
    public FirstAidTutorial(DataSnapshot dataSnapshot){
        title = dataSnapshot.child("title").getValue().toString();
        content = dataSnapshot.child("content").getValue().toString();
    }
}
