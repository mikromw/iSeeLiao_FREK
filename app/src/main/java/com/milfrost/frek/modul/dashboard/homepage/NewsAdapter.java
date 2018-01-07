package com.milfrost.frek.modul.dashboard.homepage;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.milfrost.frek.R;
import com.milfrost.frek.models.Newsfeed;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by ASUS on 01/12/2017.
 */

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ViewHolder> {

    Context context;
    List<Newsfeed> newsList;

    public NewsAdapter(Context context, List<Newsfeed> newsList){
        this.context = context;
        this.newsList = newsList;
    }

    @Override
    public int getItemCount() {
        return newsList.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context)
                .inflate(R.layout.newsfeed_model, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final Newsfeed newsfeed = newsList.get(position);

        /*if(newsfeed.media==null){
            System.out.println("Media null for newsfeed "+newsfeed.key);
        }else{
            System.out.println("Media = "+newsfeed.media+" for newsfeed "+newsfeed.key);
        }*/

        //populate data
        holder.name.setText(newsfeed.author.getName());
        holder.postingTime.setText(newsfeed.getPostingTime());
        holder.content.setText(newsfeed.content);

        ViewTreeObserver vto = holder.content.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Layout layout = holder.content.getLayout();
                if(layout.getText().toString().equalsIgnoreCase(newsfeed.content)){
                    holder.readMoreBtnHolder.setVisibility(View.GONE);
                }
                else {
                    holder.readMoreBtnHolder.setVisibility(View.VISIBLE);
                }
            }
        });
        holder.comment.setText(newsfeed.getCommentAmount());
        if(newsfeed.mediaUrl!=null){
            Picasso.with(context)
                    .load(newsfeed.mediaUrl)
                    .into(holder.media);
        }else{
            holder.media.setVisibility(View.GONE);
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        CardView viewContainer;
        CircleImageView profilePicture;
        ImageView media;

        TextView name;
        TextView postingTime;
        TextView comment;
        TextView content;
        TextView readMoreBtn;

        LinearLayout commentHolder;
        LinearLayout readMoreBtnHolder;
        LinearLayout shareHolder;

        public ViewHolder(View view){
            super(view);
            profilePicture = (CircleImageView)view.findViewById(R.id.user_profile);
            media = (ImageView)view.findViewById(R.id.media);
            name = (TextView)view.findViewById(R.id.name);
            postingTime = (TextView)view.findViewById(R.id.time_posted);
            comment = (TextView)view.findViewById(R.id.comment);
            content = (TextView)view.findViewById(R.id.content_text);
            readMoreBtn = (TextView)view.findViewById(R.id.btn_read_more);
            commentHolder = (LinearLayout)view.findViewById(R.id.comment_holder);
            shareHolder = (LinearLayout)view.findViewById(R.id.share_holder);
            readMoreBtnHolder = (LinearLayout)view.findViewById(R.id.btn_read_more_container);
        }
    }
}
