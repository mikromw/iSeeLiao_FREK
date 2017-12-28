package com.milfrost.frek.modul.dashboard;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.os.Bundle;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.milfrost.frek.R;
import com.milfrost.frek.modul.dashboard.circlepage.CirclePage;
import com.milfrost.frek.modul.dashboard.emergency_number.EmergencyNumberFragment;
import com.milfrost.frek.modul.dashboard.first_aid_page.FirstAidFragment;
import com.milfrost.frek.modul.dashboard.homepage.HomeFragment;
import com.milfrost.frek.modul.dashboard.new_emergencypage.NewEmergencyActivity;
import com.milfrost.frek.modul.dashboard.profilepage.ProfileFragment;

import java.util.ArrayList;
import java.util.List;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class Dashboard extends AppCompatActivity {

    private final int pageCount= 5;

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    private TabLayout tabLayout;
    private Toolbar toolbar;
    private TextView toolbarTitle;

    private ImageView btnNewPost;

    private int[] inactiveTabIcons;
    private int[] activeTabIcons;
    private String[] pageTitle;

    private List<Fragment> fragmentList;



    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        initObjects();
        initViews();
        setEvents();
    }

    private void initViews(){
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbarTitle = (TextView)toolbar.findViewById(R.id.toolbar_title);
        toolbarTitle.setText(pageTitle[0]);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);


        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        //Set initial active tab
        tabLayout.getTabAt(0).setIcon(activeTabIcons[0]);
        for(int i=1;i<pageCount;i++){
            tabLayout.getTabAt(i).setIcon(inactiveTabIcons[i]);
        }
        mViewPager.setOffscreenPageLimit(4);
        btnNewPost = (ImageView)findViewById(R.id.btn_new_post);
    }

    private void initObjects(){
        inactiveTabIcons = new int[]{R.drawable.ic_phone_inactive,R.drawable.ic_circle_inactive,R.drawable.ic_home_inactive,R.drawable.ic_first_aid_inactive,R.drawable.ic_profile_inactive};
        activeTabIcons = new int[]{R.drawable.ic_phone_active,R.drawable.ic_circle_active,R.drawable.ic_home_active,R.drawable.ic_first_aid_active,R.drawable.ic_profile_active};
        pageTitle = new String[]{getString(R.string.emergency_dial),getString(R.string.circle),getString(R.string.news),getString(R.string.first_aid),getString(R.string.profile)};

        fragmentList = new ArrayList<>();

        fragmentList.add(new EmergencyNumberFragment());
        fragmentList.add(new CirclePage());
        fragmentList.add(new HomeFragment());
        fragmentList.add(new FirstAidFragment());
        fragmentList.add(new ProfileFragment());

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(),fragmentList);
    }

    private void setEvents(){
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                toolbarTitle.setText(pageTitle[tab.getPosition()]);
                tab.setIcon(activeTabIcons[tab.getPosition()]);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                tab.setIcon(inactiveTabIcons[tab.getPosition()]);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        btnNewPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(Dashboard.this, NewEmergencyActivity.class);
                startActivity(intent);
            }
        });
    }


    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_dashboard, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }*/
}
