package com.milfrost.frek.modul.dashboard.first_aid_page;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.milfrost.frek.R;
import com.milfrost.frek.models.FirstAidTutorial;

import java.util.ArrayList;
import java.util.List;

public class FirstAidFragment extends Fragment implements FirstAidInterface.View {

    View view;


    List<String> categoryList;
    ArrayAdapter<String> categoryAdapter;
    Spinner categorySpinner;



    RecyclerView tutorialList;
    List<FirstAidTutorial> firstAidTutorials;
    FirstAidAdapter firstAidAdapter;

    FirstAidPresenter firstAidPresenter;

    public FirstAidFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_first_aid, container, false);
        initViews();
        initObjects();
        return view;
    }

    private void initViews(){
        categorySpinner = (Spinner)view.findViewById(R.id.category_spinner);
        tutorialList = (RecyclerView)view.findViewById(R.id.first_aid_list);

    }

    private void initObjects(){
        firstAidPresenter = new FirstAidPresenter(getContext());
        firstAidPresenter.viewInterface = this;
        firstAidTutorials = new ArrayList<>();
        firstAidAdapter = new FirstAidAdapter(getContext(),firstAidTutorials);
        tutorialList.setAdapter(firstAidAdapter);
        tutorialList.setLayoutManager(new LinearLayoutManager(getContext()));
        tutorialList.setHasFixedSize(true);

        categoryList = new ArrayList<>();
        categoryAdapter = new ArrayAdapter<String>(getContext(),R.layout.white_spinner_item,categoryList);
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);


        categorySpinner.setAdapter(categoryAdapter);

        firstAidPresenter.loadData();
    }


    @Override
    public void notifyAdapter() {
        firstAidAdapter.notifyDataSetChanged();
    }

    @Override
    public void setList(List<FirstAidTutorial> firstAidTutorials) {
        this.firstAidTutorials.clear();
        this.firstAidTutorials.addAll(firstAidTutorials);
    }

    @Override
    public void setCategoryList(List<String> categoryList) {
        this.categoryList.clear();
        this.categoryList.addAll(categoryList);
    }

    @Override
    public void notifyCategoryAdapter() {
        categoryAdapter.notifyDataSetChanged();
    }
}
