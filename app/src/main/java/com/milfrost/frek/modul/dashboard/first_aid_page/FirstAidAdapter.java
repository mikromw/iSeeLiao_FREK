package com.milfrost.frek.modul.dashboard.first_aid_page;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

//import com.bumptech.glide.Glide;
import com.milfrost.frek.R;
import com.milfrost.frek.models.FirstAidTutorial;
import com.milfrost.frek.models.TutorialStep;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by ASUS on 04/12/2017.
 */

public class FirstAidAdapter extends RecyclerView.Adapter<FirstAidAdapter.ViewHolder> {

    Context context;
    List<FirstAidTutorial> firstAidTutorials;

    public FirstAidAdapter(Context context,List<FirstAidTutorial> firstAidTutorials){
        this.context = context;
        this.firstAidTutorials = firstAidTutorials;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.first_aid_tutorial_model,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final FirstAidTutorial firstAidTutorial = firstAidTutorials.get(position);
        holder.title.setText(firstAidTutorial.title);
        holder.spoiler.setText(firstAidTutorial.content);
        if(firstAidTutorial.coverUrl!=null) {
            Picasso.with(context)
                    .load(firstAidTutorial.coverUrl)
                    .into(holder.tutorialCover);
        }
        holder.container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //open fragmentDetails
            }
        });
    }

    @Override
    public int getItemCount() {
        return firstAidTutorials.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        ImageView tutorialCover;
        TextView title;
        TextView spoiler;
        LinearLayout container;
        public ViewHolder(View view){
            super(view);
            tutorialCover = (ImageView)view.findViewById(R.id.cover_img);
            title = (TextView)view.findViewById(R.id.tutorial_title);
            spoiler = (TextView)view.findViewById(R.id.tutorial_spoiler);
            container = (LinearLayout)view.findViewById(R.id.container);
        }
    }
}
