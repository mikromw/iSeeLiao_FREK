package com.milfrost.frek.modul.dashboard.new_emergencypage;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.milfrost.frek.R;
import com.milfrost.frek.models.Category;

import java.util.ArrayList;
import java.util.List;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class NewEmergencyActivity extends AppCompatActivity implements NewEmergencyActivityInterface.View {

    Toolbar toolbar;

    RecyclerView imageRecyclerView;
    RecyclerView categoryRecyclerView;

    CategoryAdapter categoryAdapter;
    ImageAdapter imageAdapter;

    List<Category> categoryList;
    List<String> imagePath;

    NewEmergencyPresenter presenter;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_emergency);
        initViews();
        initObjects();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void initViews(){
        imageRecyclerView = (RecyclerView)findViewById(R.id.image_rv);
        categoryRecyclerView = (RecyclerView)findViewById(R.id.category_rv);
        toolbar = (android.support.v7.widget.Toolbar)findViewById(R.id.toolbar);
        toolbar.setTitle(getString(R.string.new_emergency));
        toolbar.setTitleTextColor(Color.parseColor("#ffffff"));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    private void initObjects(){
        presenter = new NewEmergencyPresenter(NewEmergencyActivity.this);
        presenter.viewInterface = this;


        categoryList = new ArrayList<>();
        imagePath = new ArrayList<>();

        categoryAdapter = new CategoryAdapter(NewEmergencyActivity.this,categoryList);
        categoryRecyclerView.setAdapter(categoryAdapter);
        categoryRecyclerView.setLayoutManager(new LinearLayoutManager(NewEmergencyActivity.this,LinearLayoutManager.HORIZONTAL,false));

        imageAdapter = new ImageAdapter(NewEmergencyActivity.this,imagePath);
        imageRecyclerView.setAdapter(imageAdapter);
        imageRecyclerView.setLayoutManager(new LinearLayoutManager(NewEmergencyActivity.this,LinearLayoutManager.HORIZONTAL,false));

        presenter.loadData();

    }


    @Override
    public void setCategoryList(List<Category> categoryList) {
        this.categoryList.clear();
        this.categoryList.addAll(categoryList);
    }

    @Override
    public void setImageList(List<String> imagePath) {
        this.imagePath.clear();
        this.imagePath.addAll(imagePath);
    }

    @Override
    public void notifyCategoryAdapter() {
        categoryAdapter.notifyDataSetChanged();
    }

    @Override
    public void notifyImageAdapter() {
        imageAdapter.notifyDataSetChanged();
    }
}
