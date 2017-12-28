package com.milfrost.frek.modul.dashboard.circlepage;

import android.content.Context;

import com.milfrost.frek.MyApplication;
import com.milfrost.frek.models.Chat;
import com.milfrost.frek.models.Circle;
import com.milfrost.frek.utils.ApiRequest;
import com.milfrost.frek.utils.DateParser;

import java.util.Arrays;
import java.util.List;

/**
 * Created by ASUS on 06/12/2017.
 */

public class InnerChatActivityPresenter {
    Context context;
    Circle circle;
    CirclePageInterface.InnerChatView viewInterface;


    public InnerChatActivityPresenter(Context context,Circle circle){
        this.context = context;
        this.circle = circle;
    }

    public void loadData(){
        if(viewInterface!=null){
            viewInterface.setList(Arrays.asList(circle.chats));
            viewInterface.notifyAdapter();
        }
    }

    public void sendChat(String circleId, String content){
        String datetime = DateParser.getCurrentTimeInString();
        ApiRequest.getInstance().sendChat(circleId,content,datetime);
        List<Chat> chats = Arrays.asList(circle.chats);
        chats.add(new Chat(MyApplication.getInstance().loggedUser,content,datetime));
        viewInterface.setList(chats);
        viewInterface.notifyAdapter();
    }

}
