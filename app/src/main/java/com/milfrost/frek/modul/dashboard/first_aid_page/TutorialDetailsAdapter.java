package com.milfrost.frek.modul.dashboard.first_aid_page;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.milfrost.frek.R;
import com.milfrost.frek.models.TutorialStep;

import java.util.List;

/**
 * Created by ASUS on 08/01/2018.
 */

public class TutorialDetailsAdapter extends RecyclerView.Adapter<TutorialDetailsAdapter.ViewHolder> {
    Context context;
    List<TutorialStep> tutorialSteps;

    public TutorialDetailsAdapter(Context context,List<TutorialStep> tutorialSteps){
        this.context = context;
        this.tutorialSteps = tutorialSteps;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.step_layout,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        TutorialStep tutorialStep = tutorialSteps.get(position);
        Glide.with(context)
                .load(tutorialStep.media)
                .into(holder.stepImg);
        holder.stepContent.setText(tutorialStep.content);
        holder.stepTitle.setText(tutorialStep.title);
    }

    @Override
    public int getItemCount() {
        return tutorialSteps.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder{
        ImageView stepImg;
        TextView stepTitle;
        TextView stepContent;
        public ViewHolder(View view){
            super(view);
            stepImg = (ImageView)view.findViewById(R.id.step_img);
            stepTitle = (TextView)view.findViewById(R.id.step_title);
            stepContent = (TextView)view.findViewById(R.id.step_content);
        }
    }
}
