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

public class ImageAdapter extends RecyclerView.Adapter{

    final static int VIEW_CAMERA = 0;
    final static int VIEW_IMAGE = 1;
    int chosenImage = -1;
    Context context;
    List<String> imagePath;
    NewEmergencyActivityInterface.ImageAdapterCommunication adapterCommunication;

    public ImageAdapter(Context context,List<String> imagePath){
        this.context = context;
        this.imagePath = imagePath;
    }

    @Override
    public int getItemViewType(int position) {
        if (position==0)
            return VIEW_CAMERA;
        else
            return VIEW_IMAGE;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType==VIEW_CAMERA) {
            View view = LayoutInflater.from(context).inflate(R.layout.image_model, parent, false);
            return new ImageViewHolder(view);
        }else{
            View view = LayoutInflater.from(context).inflate(R.layout.image_model, parent, false);
            return new ViewHolder(view);
        }
    }



    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        if(position!=0) {
            final ViewHolder vHolder = (ViewHolder)holder;
            String path = imagePath.get(position);
            if (position == chosenImage) {
                vHolder.chosenImage.setVisibility(View.VISIBLE);
            } else {
                vHolder.chosenImage.setVisibility(View.GONE);
            }
            Glide.with(context)
                    .load(path)
                    .into(vHolder.imageView);
            vHolder.imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    chosenImage = position;
                    if (position == 0) {
                        adapterCommunication.openCamera();
                        //open camera
                    } else if (position == imagePath.size() - 1) {
                        adapterCommunication.openGallery();
                        //open gallery
                    } else {
                        vHolder.chosenImage.setVisibility(View.VISIBLE);
                        notifyDataSetChanged();
                    }

                }
            });
            vHolder.chosenImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    vHolder.chosenImage.setVisibility(View.GONE);
                }
            });
        }else{
            ImageViewHolder imgViewHolder = (ImageViewHolder)holder;
            imgViewHolder.chosenImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    chosenImage = 0;
                    adapterCommunication.openCamera();
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return imagePath.size();
    }

    class ImageViewHolder extends RecyclerView.ViewHolder{
        ImageView imageView;
        ImageView chosenImage;
        public ImageViewHolder(View view){
            super(view);
            imageView = (ImageView)view.findViewById(R.id.image_view);
            chosenImage = (ImageView)view.findViewById(R.id.chosen_mask);
        }
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
