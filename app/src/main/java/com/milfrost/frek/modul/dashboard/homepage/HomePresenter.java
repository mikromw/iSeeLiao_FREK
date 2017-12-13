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
    HomeInterface.View viewInterface;

    public HomePresenter(Context context){
        this.context = context;
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
        ApiRequest.getInstance().getNewsList(new ApiRequest.ServerCallback() {
            @Override
            public void onSuccess(Object object) {
                if(viewInterface!=null){
                    viewInterface.setDataList((List<Newsfeed>)object);
                    viewInterface.notifyAdapter();
                }
            }

            @Override
            public void onError(Object object) {

            }
        });
    }
}
