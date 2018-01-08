package com.milfrost.frek.modul.dashboard.homepage;


import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.milfrost.frek.R;
import com.milfrost.frek.models.Comment;
import com.milfrost.frek.models.Newsfeed;
import com.milfrost.frek.utils.ApiRequest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment implements HomeInterface.View{

    boolean isCommentShowing = false;
    View view;

    RecyclerView rvNews;
    NewsAdapter newsAdapter;
    List<Newsfeed> newsList;
    List<Comment> comments;

    HomePresenter homePresenter;

    boolean isDataLoaded;


    //Comment layout view
    LinearLayout commentDetailsCont;
    TextView commentNumber;
    EditText commentField;
    ImageView sendBtn;
    RecyclerView commentList;
    TextView emptyListMessage;
    ImageView closePopUpBtn;

    CommentDetailsAdapter commentDetailsAdapter;

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_home, container, false);
        initViews();
        initObjects();
        setEvents();
        return view;
    }

    private void initViews(){
        rvNews = (RecyclerView)view.findViewById(R.id.rv_news);
        commentDetailsCont = (LinearLayout)view.findViewById(R.id.comment_details_cont);
        commentNumber = (TextView)view.findViewById(R.id.comment_number);
        commentField = (EditText)view.findViewById(R.id.comment_field);
        sendBtn = (ImageView)view.findViewById(R.id.send_btn);
        commentList = (RecyclerView)view.findViewById(R.id.comment_list_rv);
        emptyListMessage = (TextView)view.findViewById(R.id.empty_comment_msg);
        closePopUpBtn = (ImageView)view.findViewById(R.id.close_pop_up_btn);
        commentDetailsCont.setVisibility(View.GONE);
    }
    private void initObjects() {
        if(newsList ==null)
            newsList = new ArrayList<>();

        if(homePresenter==null)
            homePresenter = new HomePresenter(getContext(),newsList);

        newsAdapter = new NewsAdapter(getContext(), newsList);
        newsAdapter.viewInterface = this;
        rvNews.setAdapter(newsAdapter);
        rvNews.setLayoutManager(new LinearLayoutManager(getContext()));
        rvNews.setHasFixedSize(true);

        comments = new ArrayList<>();
        commentDetailsAdapter = new CommentDetailsAdapter(getContext(), comments);
        this.commentList.setAdapter(commentDetailsAdapter);
        this.commentList.setHasFixedSize(true);
        this.commentList.setLayoutManager(new LinearLayoutManager(getContext()));

        homePresenter.viewInterface = this;
        if(!isDataLoaded) {
            homePresenter.loadData();
        }

        System.out.println("newsList size = "+newsList.size());
        System.out.println("isDataLoaded = "+isDataLoaded);
    }

    private void setEvents(){
        closePopUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideCommentDetailsView();
            }
        });
        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!commentField.getText().toString().trim().isEmpty()){
                    ApiRequest.getInstance().sendComment(newsAdapter.selectedNewsfeedId, commentField.getText().toString(), new ApiRequest.ServerCallback() {
                        @Override
                        public void onSuccess(Object object) {
                            commentList.getAdapter().notifyDataSetChanged();
                        }

                        @Override
                        public void onError(Object object) {

                        }
                    });
                }
            }
        });
    }

    private void updateCommentDetailsView(List<Comment> commentList){
        if(commentList!=null) {
            emptyListMessage.setVisibility(View.GONE);
            comments.clear();
            comments.addAll(commentList);
            commentDetailsAdapter.notifyDataSetChanged();
            if(commentList.size()>1){
                commentNumber.setText(String.format(getString(R.string.amount_comments),commentList.size()));
            }
            else{
                commentNumber.setText(String.format(getString(R.string.amount_comment),commentList.size()));
            }

        }else{
            this.commentList.setVisibility(View.GONE);
            emptyListMessage.setVisibility(View.VISIBLE);
            commentNumber.setText(String.format(getString(R.string.amount_comment),0));
        }
    }

    private void showCommentDetailsView(List<Comment> commentList){
        isCommentShowing = true;
        updateCommentDetailsView(commentList);

        //plural of singular use of noun


        commentDetailsCont.setVisibility(View.VISIBLE);
        commentDetailsCont.setAlpha(0.0f);

        commentDetailsCont.animate()
                .translationY(0)
                .alpha(1.0f)
                .setListener(null);

    }

    private void hideCommentDetailsView(){
        isCommentShowing = false;
        commentDetailsCont.animate()
                .translationY(50)
                .alpha(0.0f)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        commentDetailsCont.setVisibility(View.GONE);
                    }
                });
    }

    @Override
    public void notifyAdapter() {
        newsAdapter.notifyDataSetChanged();
    }

    @Override
    public void addDataToList(Newsfeed newsfeed) {
        newsList.add(0,newsfeed);
    }

    @Override
    public void setDataList(List<Newsfeed> newsfeedList) {
        this.newsList.clear();
        this.newsList.addAll(newsfeedList);
        isDataLoaded = true;
    }

    @Override
    public void updateNewsfeedData(Newsfeed newsfeed) {
        for(int i=0;i<newsList.size();i++){
            if(newsList.get(i).key.equals(newsfeed.key)){
                newsList.remove(i);
                newsList.add(i,newsfeed);
                notifyAdapter();
                if(isCommentShowing&&i==newsAdapter.selectedPosition){
                    List<Comment> commentList = new ArrayList<>();
                    commentList.addAll(Arrays.asList(newsfeed.comments));
                    updateCommentDetailsView(commentList);
                }
            }
        }
    }

    @Override
    public void showCommentDetails(List<Comment> commentList) {
        showCommentDetailsView(commentList);
    }
}
