package com.milfrost.frek.modul.dashboard.circlepage;

import android.content.Context;

import com.milfrost.frek.MyApplication;
import com.milfrost.frek.models.Chat;
import com.milfrost.frek.models.Circle;
import com.milfrost.frek.utils.ApiRequest;
import com.milfrost.frek.utils.DateParser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by ASUS on 06/12/2017.
 */

public class InnerChatActivityPresenter  {
    int count =0;
    Context context;
    Circle circle;
    List<Chat> chatList;
    CirclePageInterface.InnerChatView viewInterface;


    public InnerChatActivityPresenter(Context context,Circle circle){
        this.context = context;
        this.circle = circle;
        chatList = new ArrayList<>();
        chatList.addAll(Arrays.asList(circle.chats));
    }

    public void loadData(){
        if(viewInterface!=null){
            viewInterface.setList(chatList);
            viewInterface.notifyAdapter();
        }
    }

    public void sendChat(String circleId, String content){
        String datetime = DateParser.getCurrentTimeInString();
        ApiRequest.getInstance().sendChat(circleId,content,datetime);
        List<Chat> chats = new ArrayList<>();
        chats.addAll(Arrays.asList(circle.chats));
        chats.add(new Chat(MyApplication.getInstance().loggedUser,content,datetime));
        viewInterface.setList(chats);
        viewInterface.notifyAdapter();
        viewInterface.resetInputBox();
    }

    public void listenToChatData(){
        count = 0;
        ApiRequest.getInstance().listenToChatData(circle.key, new ApiRequest.ServerCallback() {
            @Override
            public void onSuccess(Object object) {
                count+=1;
                if(count>chatList.size()) {
                    Chat chat = (Chat) object;
                    chatList.add(chat);
                    viewInterface.setList(chatList);
                    viewInterface.notifyAdapter();
                    System.out.println(chat.content);
                }


            }

            @Override
            public void onError(Object object) {

            }
        });
    }

}
