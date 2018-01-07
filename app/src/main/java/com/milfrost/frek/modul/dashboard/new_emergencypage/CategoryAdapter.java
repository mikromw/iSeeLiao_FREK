package com.milfrost.frek.modul.dashboard.new_emergencypage;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

//import com.bumptech.glide.Glide;
import com.milfrost.frek.R;
import com.milfrost.frek.models.Category;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by vincent on 06/12/17.
 */

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {

    int chosenCategory=-1;
    Context context;
    List<Category> categoryList;

    public CategoryAdapter(Context context,List<Category> categoryList){
        this.categoryList = categoryList;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.category_model,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final Category category = categoryList.get(position);

        //holder.chosenImage.setVisibility(View.GONE);

        if(category.iconUrl!=null){
            Picasso.with(context)
                    .load(category.iconUrl)
                    .into(holder.categoryImg);
        }
        GradientDrawable txtBg = (GradientDrawable) holder.categoryImg.getBackground();

        try {
            txtBg.setColor(Color.parseColor(category.backgroundColor));
        }catch (Exception e){}
        holder.categoryCaption.setText(category.name);
        if(chosenCategory==position){
            holder.chosenImage.setVisibility(View.VISIBLE);
        }else{
            holder.chosenImage.setVisibility(View.GONE);
        }
        holder.categoryImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chosenCategory = position;
                notifyDataSetChanged();
                //holder.chosenImage.setVisibility(View.VISIBLE);
            }
        });
        holder.chosenImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chosenCategory = -1;
                notifyDataSetChanged();
                //holder.chosenImage.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        ImageView categoryImg;
        TextView categoryCaption;
        ImageView chosenImage;


        public ViewHolder(View view){
            super(view);
            categoryImg = (ImageView)view.findViewById(R.id.category_img);
            categoryCaption = (TextView)view.findViewById(R.id.category_caption);
            chosenImage = (ImageView)view.findViewById(R.id.chosen_mask);
        }
    }
}
