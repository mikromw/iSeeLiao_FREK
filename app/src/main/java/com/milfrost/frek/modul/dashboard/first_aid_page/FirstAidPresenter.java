package com.milfrost.frek.modul.dashboard.first_aid_page;

import android.content.Context;

import com.milfrost.frek.models.FirstAidTutorial;
import com.milfrost.frek.models.TutorialStep;
import com.milfrost.frek.utils.ApiRequest;

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

        ApiRequest.getInstance().getTutorialList(new ApiRequest.RealTimeServerCallback() {
            @Override
            public void onNewData(Object object) {
                FirstAidTutorial tutorial = (FirstAidTutorial) object;
                if(viewInterface!=null) {
                    viewInterface.addTutorial(tutorial);
                    viewInterface.notifyAdapter();
                }
            }

            @Override
            public void onDataChanged(Object object) {

            }

            @Override
            public void onDataRemoved(Object object) {

            }

            @Override
            public void onCancelled(Object object) {

            }
        });

        List<String> categoryList = new ArrayList<>();
        categoryList.add("Choose a Category");
        categoryList.add("Volcanic Eruption");
        categoryList.add("Earthquake");
        categoryList.add("Avalanche");
        categoryList.add("Flood");
        categoryList.add("Fire Disaster");

        viewInterface.setCategoryList(categoryList);
        viewInterface.notifyCategoryAdapter();



    }

}
