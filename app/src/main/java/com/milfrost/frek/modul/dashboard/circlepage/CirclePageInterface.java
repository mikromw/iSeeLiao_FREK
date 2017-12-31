package com.milfrost.frek.modul.dashboard.circlepage;

import com.milfrost.frek.models.Chat;
import com.milfrost.frek.models.Circle;

import java.util.List;

/**
 * Created by ASUS on 06/12/2017.
 */

public interface CirclePageInterface {
    interface ChatFragmentView{
        void setList(List<Circle> Circles);
        void addItemToList(Circle circle);
        void notifyAdapter();
    }
    interface LocationFragmentView{

    }
    interface InnerChatView{
        void setList(List<Chat> chatList);
        void notifyAdapter();
        void resetInputBox();
    }

    interface CircleAdapterWithInnerChat{
        void updateCircleChat(Chat[] chats,String circleId);
    }
}
