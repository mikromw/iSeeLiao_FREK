package com.milfrost.frek.modul.dashboard.homepage;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.milfrost.frek.R;
import com.milfrost.frek.models.Newsfeed;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment implements HomeInterface.View{

    View view;

    RecyclerView rvNews;
    NewsAdapter newsAdapter;
    List<Newsfeed> newsList;

    HomePresenter homePresenter;

    boolean isDataLoaded;


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
        return view;
    }

    private void initViews(){
        rvNews = (RecyclerView)view.findViewById(R.id.rv_news);
    }
    private void initObjects() {
        if(newsList ==null)
            newsList = new ArrayList<>();

        if(homePresenter==null)
            homePresenter = new HomePresenter(getContext(),newsList);

        newsAdapter = new NewsAdapter(getContext(), newsList);
        rvNews.setAdapter(newsAdapter);
        rvNews.setLayoutManager(new LinearLayoutManager(getContext()));
        rvNews.setHasFixedSize(true);


        homePresenter.viewInterface = this;
        if(!isDataLoaded) {
            homePresenter.loadData();
        }

        System.out.println("newsList size = "+newsList.size());
        System.out.println("isDataLoaded = "+isDataLoaded);
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
}
