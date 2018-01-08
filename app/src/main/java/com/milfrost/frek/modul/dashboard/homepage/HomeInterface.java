package com.milfrost.frek.modul.dashboard.homepage;

import com.milfrost.frek.models.Comment;
import com.milfrost.frek.models.Newsfeed;

import java.util.List;

/**
 * Created by ASUS on 01/12/2017.
 */

public interface HomeInterface {

    interface View{
        void notifyAdapter();
        void addDataToList(Newsfeed newsfeed);
        void setDataList(List<Newsfeed> newsfeedList);
        void updateNewsfeedData(Newsfeed newsfeed);
        void showCommentDetails(List<Comment> commentList);
    }
    interface Adapter{

    }

}
