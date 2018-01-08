package com.milfrost.frek.modul.dashboard.first_aid_page;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.milfrost.frek.R;
import com.milfrost.frek.models.FirstAidTutorial;
import com.milfrost.frek.models.TutorialStep;

import java.util.ArrayList;
import java.util.List;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class TutorialDetailsActivity extends AppCompatActivity {

    FirstAidTutorial firstAidTutorial;
    ImageView cover;
    TextView title;
    RecyclerView stepListing;
    List<TutorialStep> tutorialSteps;
    TutorialDetailsAdapter tutorialDetailsAdapter;
    Toolbar toolbar;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutorial_details);
        firstAidTutorial = (FirstAidTutorial)getIntent().getExtras().getSerializable("tutorial");
        initViews();
        initObjects();
        setViews();
    }

    private void initViews(){
        cover = (ImageView)findViewById(R.id.place_image);
        stepListing = (RecyclerView)findViewById(R.id.step_listing);

        toolbar = (Toolbar)findViewById(R.id.tool_bar);
        toolbar.setTitle(firstAidTutorial.title);
        toolbar.setTitleTextColor(Color.parseColor("#ffffff"));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }
    private void initObjects(){
        tutorialSteps = new ArrayList<>();
        tutorialSteps.addAll(firstAidTutorial.tutorialSteps);

        tutorialDetailsAdapter = new TutorialDetailsAdapter(TutorialDetailsActivity.this,tutorialSteps);
        stepListing.setAdapter(tutorialDetailsAdapter);
        stepListing.setHasFixedSize(true);
        stepListing.setLayoutManager(new LinearLayoutManager(TutorialDetailsActivity.this));
    }

    private void setViews(){
        Glide.with(this)
                .load(firstAidTutorial.coverUrl)
                .into(cover);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
