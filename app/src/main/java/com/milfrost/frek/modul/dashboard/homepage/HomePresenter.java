package com.milfrost.frek.modul.dashboard.homepage;

import android.content.Context;

import com.google.firebase.database.DataSnapshot;
import com.milfrost.frek.MyApplication;
import com.milfrost.frek.models.Comment;
import com.milfrost.frek.models.Newsfeed;
import com.milfrost.frek.models.User;
import com.milfrost.frek.models.UserMedia;
import com.milfrost.frek.utils.ApiRequest;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by ASUS on 01/12/2017.
 */

public class HomePresenter {
    Context context;
    List<Newsfeed> newsfeedList;
    HomeInterface.View viewInterface;

    public HomePresenter(Context context,List<Newsfeed> newsfeedList){
        this.context = context;
        this.newsfeedList =newsfeedList;
    }

    public void loadData(){
        /*ApiRequest.getInstance().getUserInformation(new ApiRequest.ServerCallback() {
            @Override
            public void onSuccess(Object object) {

            }

            @Override
            public void onError(Object object) {

            }
        },"vincenttheonardo@gmail.com");*/
        ApiRequest.getInstance().getNewsList(true,new ApiRequest.ServerCallback() {
            @Override
            public void onSuccess(Object object) {
                if(viewInterface!=null){
                    Newsfeed newsfeed = (Newsfeed)object;

                    //check if newsfeed is already added or not
                    if(!isExistingInList(newsfeed.key)){
                        viewInterface.addDataToList((Newsfeed)object);
                        viewInterface.notifyAdapter();
                    }
                }
            }

            @Override
            public void onError(Object object) {

            }
        });
    }

    private boolean isExistingInList(String key){
        for(Newsfeed newsfeed:newsfeedList){
            if(newsfeed.key.equals(key))
                return true;
        }
        return false;
    }
}
