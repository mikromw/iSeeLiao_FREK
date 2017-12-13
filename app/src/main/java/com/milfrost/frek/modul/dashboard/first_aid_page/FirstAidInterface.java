package com.milfrost.frek.modul.dashboard.first_aid_page;

import com.milfrost.frek.models.FirstAidTutorial;

import java.util.List;

/**
 * Created by ASUS on 04/12/2017.
 */

public interface FirstAidInterface {
    interface View{
        void notifyAdapter();
        void setList(List<FirstAidTutorial> firstAidTutorials);
        void setCategoryList(List<String> categoryList);
        void notifyCategoryAdapter();
    }
}
