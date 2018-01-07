package com.milfrost.frek.modul.dashboard.new_emergencypage;

import android.location.Location;

import com.milfrost.frek.models.Category;

import java.util.List;

/**
 * Created by vincent on 06/12/17.
 */

public interface NewEmergencyActivityInterface {
    interface View{
        void setCategoryList(List<Category> categoryList);
        void setImageList(List<String> imagePath);
        void notifyCategoryAdapter();
        void notifyImageAdapter();
    }
    interface LocationCallback{
        void onLocationRetrieved(Location location);
        void onFail(Object obj);
    }
    interface ImageAdapterCommunication{
        void openCamera();
        void openGallery();
    }
}
