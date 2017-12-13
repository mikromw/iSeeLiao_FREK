package com.milfrost.frek.models;

/**
 * Created by ASUS on 04/12/2017.
 */

public class NewsFeedLoadingStatus {
    public boolean isAuthorLoaded = false;
    public boolean isCommentLaoded = false;
    public boolean isMediaLoaded = false;

    public boolean isAllLoaded (){
        return isAuthorLoaded && isCommentLaoded && isMediaLoaded;
    }
}
