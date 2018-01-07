package com.milfrost.frek.modul.dashboard.circlepage;

import android.content.Context;

import com.milfrost.frek.MyApplication;
import com.milfrost.frek.models.Circle;
import com.milfrost.frek.utils.ApiRequest;

/**
 * Created by ASUS on 06/12/2017.
 */

public class ChatFragmentPresenter {
    Context context;
    CirclePageInterface.ChatFragmentView viewInterface;

    public ChatFragmentPresenter(Context context){
        this.context = context;
    }

    public void loadData(){
        for(String circleId : MyApplication.getInstance().loggedUser.getCircleId()) {
            System.out.println("Circle id = "+circleId);
            ApiRequest.getInstance().getCircles(circleId,new ApiRequest.ServerCallback() {
                @Override
                public void onSuccess(Object object) {
                    Circle circle = (Circle)object;
                    circle.sortChats();
                    viewInterface.addItemToList(circle);
                    viewInterface.notifyAdapter();
                    System.out.println("chat data loaded");
                }

                @Override
                public void onError(Object object) {

                }
            });
        }
    }
}
