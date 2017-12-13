package com.milfrost.frek.modul.dashboard.profilepage;

import android.graphics.Bitmap;

/**
 * Created by vincent on 06/12/17.
 */

public interface ProfileFragmentInterface {

    interface View{
        void updateName(String name);
        void updateUserProfile(String url);
        void updateCover(String url);
    }

}
