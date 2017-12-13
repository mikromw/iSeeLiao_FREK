package com.milfrost.frek.modul.dashboard.circlepage;

import android.content.Context;

import com.milfrost.frek.models.Circle;
import com.milfrost.frek.utils.ApiRequest;

import java.util.Arrays;

/**
 * Created by ASUS on 06/12/2017.
 */

public class InnerChatActivityPresenter {
    Context context;
    CirclePageInterface.InnerChatView viewInterface;


    public InnerChatActivityPresenter(Context context){
        this.context = context;
    }

    public void loadData(Circle circle){
        if(viewInterface!=null){
            viewInterface.setList(Arrays.asList(circle.chats));
            viewInterface.notifyAdapter();
        }
    }

    public void sendChat(String circleId, String content){
        ApiRequest.getInstance().sendChat(circleId,content);
    }

}
