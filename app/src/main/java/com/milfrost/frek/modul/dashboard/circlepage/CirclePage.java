package com.milfrost.frek.modul.dashboard.circlepage;


import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;
import com.milfrost.frek.R;
import com.milfrost.frek.models.Chat;
import com.milfrost.frek.models.Circle;
import com.milfrost.frek.models.User;
import com.milfrost.frek.modul.dashboard.Dashboard;
import com.milfrost.frek.modul.dashboard.SectionsPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class CirclePage extends Fragment implements CirclePageInterface.ChatFragmentView {

    View view;
    TabLayout tabLayout;
    View leftTab;
    View rightTab;
    TextView leftTabText;
    TextView rightTabText;

    ViewPager viewPager;
    SectionsPagerAdapter pagerAdapter;

    List<Fragment> fragmentList;
    List<Circle> circles;

    CirclePagePresenter circlePagePresenter;

    public CirclePage() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_circle_page, container, false);
        initObjects();
        initViews();
        setupEvents();
        pagerAdapter.notifyDataSetChanged();
        return view;
    }

    private void initViews(){
        tabLayout = (TabLayout)view.findViewById(R.id.circle_tab_layout);
        viewPager = (ViewPager)view.findViewById(R.id.circle_view_pager);
        viewPager.setAdapter(pagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
        leftTabText = (TextView) ((ViewGroup)tabLayout.getChildAt(0)).findViewById(android.R.id.title);
        rightTabText = (TextView) ((ViewGroup)tabLayout.getChildAt(0)).findViewById(android.R.id.title);
        leftTab = ((ViewGroup)tabLayout.getChildAt(0)).getChildAt(0);
        leftTab.setBackground(getContext().getResources().getDrawable(R.drawable.bg_tab_left_selected));
        rightTab = ((ViewGroup)tabLayout.getChildAt(0)).getChildAt(1);
        rightTab.setBackground(getContext().getResources().getDrawable(R.drawable.bg_tab_right_unselected));
    }
    private void initObjects(){
        circlePagePresenter = new CirclePagePresenter(getContext());
        circlePagePresenter.viewInterface = this;

        if(circles==null){
            circles = new ArrayList<>();
        }
        if(fragmentList==null) {
            fragmentList = new ArrayList<>();
            ChatFragment chatFragment = new ChatFragment();
            LocationFragment locationFragment = new LocationFragment();
            fragmentList.add(chatFragment);
            fragmentList.add(locationFragment);
        }
        else{
            System.out.println("Size of chat list = "+fragmentList.size());
        }
        if(pagerAdapter==null) {
            pagerAdapter = new SectionsPagerAdapter(((Dashboard) getContext()).getSupportFragmentManager(), fragmentList
                    , new String[]{getString(R.string.chat), getString(R.string.location)});
        }

        circlePagePresenter.loadData();


    }
    private void setupEvents(){
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if(tab.getPosition()==0){
                    leftTab.setBackground(getContext().getResources().getDrawable(R.drawable.bg_tab_left_selected));
                }
                else {
                    rightTab.setBackground(getContext().getResources().getDrawable(R.drawable.bg_tab_right_selected));
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                if(tab.getPosition()==0){
                    leftTab.setBackground(getContext().getResources().getDrawable(R.drawable.bg_tab_left_unselected));
                }
                else {
                    rightTab.setBackground(getContext().getResources().getDrawable(R.drawable.bg_tab_right_unselected));
                }
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    @Override
    public void addItemToList(Circle circle) {
        this.circles.add(circle);
    }



    @Override
    public void setList(List<Circle> Circles) {
        this.circles.clear();
        this.circles.addAll(circles);
    }

    @Override
    public void notifyAdapter() {
        ((ChatFragment)fragmentList.get(0)).setCircles(circles);
        ((LocationFragment)fragmentList.get(1)).setCircles(circles);
    }
}
