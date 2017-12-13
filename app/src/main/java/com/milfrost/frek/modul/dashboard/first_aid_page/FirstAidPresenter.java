package com.milfrost.frek.modul.dashboard.first_aid_page;

import android.content.Context;

import com.milfrost.frek.models.FirstAidTutorial;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ASUS on 04/12/2017.
 */

public class FirstAidPresenter {
    Context context;
    FirstAidInterface.View viewInterface;

    public FirstAidPresenter(Context context){
        this.context = context;
    }

    public void loadData(){
        //dummy data
        List<FirstAidTutorial> tutorialList = new ArrayList<>();
        tutorialList.add( new FirstAidTutorial("How to do CPR?","CPR is one of the most important action that everyone should have learnt."));
        tutorialList.add( new FirstAidTutorial("Things to do when earthquake Strike","When an earthquake strike, these are things that should be done"));

        List<String> categoryList = new ArrayList<>();
        categoryList.add("Pilih kategori");
        categoryList.add("Gunung Meletus");
        categoryList.add("Gempa Bumi");
        categoryList.add("Tanah Longsor");
        categoryList.add("Banjir");
        categoryList.add("Kebakaran");


        if(viewInterface!=null) {
            viewInterface.setList(tutorialList);
            viewInterface.notifyAdapter();
            viewInterface.setCategoryList(categoryList);
            viewInterface.notifyCategoryAdapter();
        }

    }

}
