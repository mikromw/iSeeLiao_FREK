package com.milfrost.frek;

import android.app.Application;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.auth.FirebaseAuth;
import com.milfrost.frek.models.User;
import com.milfrost.frek.models.UserMedia;

import java.util.ArrayList;
import java.util.List;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

/**
 * Created by ASUS on 21/11/2017.
 */

public class MyApplication extends Application {
    private static MyApplication mInstance;

    public List<UserMedia> mediaList;
    public List<User> userList;

    public User loggedUser;

    @Override
    public void onCreate() {
        super.onCreate();
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/OpenSans-Regular.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );

        initObjects();
    }

    private void initObjects(){
        mInstance = this;
        //loggedUser = new User("Vincent Theonardo",new LatLng(3.588180, 98.699029));
        mediaList = new ArrayList<>();
        userList = new ArrayList<>();
    }

    public static synchronized MyApplication getInstance(){
        return mInstance;
    }

    public int isUserExistInList(String email){
        for(int i=0;i<userList.size();i++){
            if(userList.get(i).getEmailAddress().equals(email)){
                return i;
            }
        }
        return -1;
    }

    public void addUserToList(User user){
        userList.add(user);
    }
}
