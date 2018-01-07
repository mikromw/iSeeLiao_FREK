package com.milfrost.frek.models;

/**
 * Created by ASUS on 11/12/2017.
 */

public class CircleLoadingStatus {
    public boolean isUserLoaded = false;
    public boolean isChatLoaded = false;

    public CircleLoadingStatus(){
        isUserLoaded = false;
        isChatLoaded = false;
    }

    public boolean isAllLoaded(){
        return isChatLoaded&&isUserLoaded;
    }
}
