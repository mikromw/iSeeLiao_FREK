package com.milfrost.frek.modul.dashboard.homepage;

import android.content.Context;
import android.content.Intent;
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
import com.milfrost.frek.models.Comment;
import com.milfrost.frek.models.Newsfeed;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by ASUS on 01/12/2017.
 */

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ViewHolder> {

    Context context;
    List<Newsfeed> newsList;
    HomeInterface.View viewInterface;
    String selectedNewsfeedId ="";
    int selectedPosition = -1;

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
    public void onBindViewHolder(final ViewHolder holder, final int position) {
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
        holder.shareHolder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                String shareBody ="\""+ newsfeed.content + " \"published via FREK (Fast Response Emergency Kit)";
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Subject Here");
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                context.startActivity(Intent.createChooser(sharingIntent, "Share via"));
            }
        });
        holder.comment.setText(newsfeed.getCommentAmount());
        holder.comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(viewInterface!=null) {
                    selectedNewsfeedId = newsfeed.key;
                    selectedPosition = position;
                    if(newsfeed.comments!=null) {
                        List<Comment> commentList = new ArrayList<Comment>();
                        commentList.addAll(Arrays.asList(newsfeed.comments));
                        viewInterface.showCommentDetails(commentList);
                    }
                    else {
                        viewInterface.showCommentDetails(null);
                    }
                }
            }
        });
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
