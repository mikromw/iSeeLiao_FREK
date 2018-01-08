package com.milfrost.frek.modul.dashboard.homepage;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.milfrost.frek.MyApplication;
import com.milfrost.frek.R;
import com.milfrost.frek.models.Comment;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by ASUS on 07/01/2018.
 */

public class CommentDetailsAdapter extends RecyclerView.Adapter<CommentDetailsAdapter.ViewHolder>{

    Context context;
    List<Comment> commentList;

    public CommentDetailsAdapter(Context context,List<Comment> commentList){
        this.context = context;
        this.commentList = commentList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.comment_layout,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Comment comment = commentList.get(position);
        holder.userName.setText(MyApplication.getInstance().getUserFromList(comment.author.getEmailAddress()).getName());
        holder.commentContent.setText(comment.content);
        holder.commentTimeAttr.setText(comment.getPostingTime());
    }

    @Override
    public int getItemCount() {
        return commentList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        CircleImageView userProfile;
        TextView userName;
        TextView commentContent;
        TextView commentTimeAttr;
        public ViewHolder(View view){
            super(view);
            userProfile = (CircleImageView)view.findViewById(R.id.user_profile);
            userName = (TextView)view.findViewById(R.id.user_name);
            commentContent = (TextView)view.findViewById(R.id.comment_content);
            commentTimeAttr = (TextView)view.findViewById(R.id.comment_time_attr);
        }
    }
}
