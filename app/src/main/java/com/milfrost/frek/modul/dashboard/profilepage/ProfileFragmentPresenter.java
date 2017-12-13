package com.milfrost.frek.modul.dashboard.profilepage;

import android.content.Context;

import com.milfrost.frek.MyApplication;
import com.milfrost.frek.models.User;

/**
 * Created by vincent on 06/12/17.
 */

public class ProfileFragmentPresenter {
    Context context;
    ProfileFragmentInterface.View viewInterface;

    public ProfileFragmentPresenter(Context context){
        this.context = context;
    }

    public void loadData(){
        User user = MyApplication.getInstance().loggedUser;
        if(viewInterface!=null){
            viewInterface.updateName(user.getName());
            if(user.getProfile()!=null){
                viewInterface.updateUserProfile(user.getProfile().downloadUrl);
            }
        }
    }

}
