package com.milfrost.frek.modul.dashboard.circlepage;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.milfrost.frek.R;
import com.milfrost.frek.models.Chat;
import com.milfrost.frek.utils.DateParser;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by ASUS on 06/12/2017.
 */

public class ChatAdapter extends RecyclerView.Adapter {
    Context context;
    List<Chat>chats;

    public ChatAdapter(Context context,List<Chat> chats){
        this.context = context;
        this.chats = chats;
    }

    @Override
    public int getItemViewType(int position) {
        Chat chat = chats.get(position);
        if(chat.sender.getName().equals("Vincent Theonardo")){
            return Chat.SENT_CHAT;
        }
        else {
            return Chat.RECEIVED_CHAT;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType==Chat.RECEIVED_CHAT){
            View view = LayoutInflater.from(context).inflate(R.layout.received_chat_model,parent,false);
            return new ReceivedViewHolder(view);
        }
        else if(viewType == Chat.SENT_CHAT){
            View view = LayoutInflater.from(context).inflate(R.layout.sent_chat_model,parent,false);
            return new SentViewHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Chat chat = chats.get(position);
        if(holder.getItemViewType()==Chat.RECEIVED_CHAT){
            ((ReceivedViewHolder)holder).onBind(chat);
        }else{
            ((SentViewHolder)holder).onBind(chat);
        }
    }

    @Override
    public int getItemCount() {
        return chats.size();
    }

    class ReceivedViewHolder extends RecyclerView.ViewHolder{
        de.hdodenhof.circleimageview.CircleImageView imageView;
        TextView name;
        TextView content;
        TextView timestamp;
        public ReceivedViewHolder(View itemView) {
            super(itemView);
            imageView = (CircleImageView)itemView.findViewById(R.id.user_profile);
            name = (TextView)itemView.findViewById(R.id.sender);
            content = (TextView)itemView.findViewById(R.id.message_bubble);
            timestamp = (TextView)itemView.findViewById(R.id.time_label);
        }

        public void onBind(Chat chat){
            name.setText(chat.sender.getName());
            content.setText(chat.content);
            timestamp.setText(DateParser.getParsedFormat("HH:mm",chat.timestamp));
        }
    }

    class SentViewHolder extends RecyclerView.ViewHolder{
        TextView content;
        TextView timestamp;
        public SentViewHolder(View itemView) {
            super(itemView);
            content = (TextView)itemView.findViewById(R.id.message_bubble);
            timestamp = (TextView)itemView.findViewById(R.id.time_label);
        }

        public void onBind(Chat chat){
            content.setText(chat.content);
            timestamp.setText(DateParser.getParsedFormat("HH:mm",chat.timestamp));
        }
    }
}
