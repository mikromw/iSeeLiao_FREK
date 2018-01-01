package com.milfrost.frek.modul.dashboard.circlepage;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.milfrost.frek.MyApplication;
import com.milfrost.frek.models.Chat;
import com.milfrost.frek.models.Circle;
import com.milfrost.frek.utils.ApiRequest;

import java.util.List;

/**
 * Created by ASUS on 11/12/2017.
 */

public class CirclePagePresenter {
    Context context;
    CirclePageInterface.ChatFragmentView viewInterface;

    public CirclePagePresenter(Context context){
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

    public void openInnerChatActivity(Circle circle){
        Bundle bundle = new Bundle();
        bundle.putSerializable("item",circle);
        Intent intent = new Intent(context,InnerChatActivity.class);
        intent.putExtra("bundle",bundle);
        context.startActivity(intent);
    }

    //update CircleActivity to show latest Chat

}
