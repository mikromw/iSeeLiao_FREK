package com.milfrost.frek.modul.dashboard.new_emergencypage;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

//import com.bumptech.glide.Glide;
import com.bumptech.glide.Glide;
import com.milfrost.frek.R;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.List;

/**
 * Created by vincent on 06/12/17.
 */

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ViewHolder>{

    int chosenImage;
    Context context;
    List<String> imagePath;

    public ImageAdapter(Context context,List<String> imagePath){
        this.context = context;
        this.imagePath = imagePath;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.image_model,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        String path = imagePath.get(position);
        if(position==chosenImage){
            holder.chosenImage.setVisibility(View.VISIBLE);
        }else {
            holder.chosenImage.setVisibility(View.GONE);
        }
        Glide.with(context)
                .load(path)
                .into(holder.imageView);
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(position==0){
                    //open camera
                }else if(position==imagePath.size()-1){
                    //open gallery
                }else{
                    chosenImage = position;
                    notifyDataSetChanged();
                }
                holder.chosenImage.setVisibility(View.VISIBLE);
            }
        });
        holder.chosenImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.chosenImage.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public int getItemCount() {
        return imagePath.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        ImageView imageView;
        ImageView chosenImage;
        public ViewHolder(View view){
            super(view);
            imageView = (ImageView)view.findViewById(R.id.image_view);
            chosenImage = (ImageView)view.findViewById(R.id.chosen_mask);
        }
    }
}
