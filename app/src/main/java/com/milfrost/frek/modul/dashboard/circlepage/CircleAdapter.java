package com.milfrost.frek.modul.dashboard.circlepage;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.milfrost.frek.MyApplication;
import com.milfrost.frek.R;
import com.milfrost.frek.models.Chat;
import com.milfrost.frek.models.Circle;
import com.milfrost.frek.utils.DateParser;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by ASUS on 06/12/2017.
 */

public class CircleAdapter extends RecyclerView.Adapter<CircleAdapter.ViewHolder>  {
    Context context;
    List<Circle> circles;

    public CircleAdapter(Context context, List<Circle> circles){
        this.context = context;
        this.circles = circles;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.circle_model,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final Circle circle = circles.get(position);
        holder.circleName.setText(circle.name);
        if(circle.chats!=null) {
            holder.cirlceLastActive.setText(DateParser.getParsedFormat("HH:mm", circle.chats[circle.chats.length - 1].timestamp));
            String lastMessage="";

            if(circle.chats[circle.chats.length-1].sender.getName().equals(MyApplication.getInstance().loggedUser.getName())) {
                lastMessage += "You : ";
            }
            else {
                String name ="";
                if(circle.chats[circle.chats.length - 1].sender.getName().contains(" ")){
                    name = circle.chats[circle.chats.length - 1].sender.getName().split(" ")[0];
                }else{
                    name = circle.chats[circle.chats.length - 1].sender.getName();
                }
                lastMessage += name + " : ";
            }

            lastMessage += circle.chats[circle.chats.length - 1].content;
            holder.circleLastMessage.setText(lastMessage);
        }
        holder.circleHolder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("item",circle);
                Intent intent = new Intent(context,InnerChatActivity.class);
                intent.putExtra("bundle",bundle);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return circles.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        CardView circleHolder;
        CircleImageView cirlceImage;
        TextView circleName;
        TextView circleLastMessage;
        TextView cirlceLastActive;

        public ViewHolder(View view){
            super(view);
            circleHolder = (CardView)view.findViewById(R.id.circle_holder);
            cirlceImage = (CircleImageView)view.findViewById(R.id.circle_icon);
            circleName = (TextView)view.findViewById(R.id.circle_name);
            circleLastMessage = (TextView)view.findViewById(R.id.circle_last_message);
            cirlceLastActive = (TextView)view.findViewById(R.id.time_active);
        }

    }
}
