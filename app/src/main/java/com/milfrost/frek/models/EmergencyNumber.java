package com.milfrost.frek.models;

import android.content.Context;
import android.graphics.drawable.Drawable;

import com.milfrost.frek.MyApplication;
import com.milfrost.frek.R;

/**
 * Created by ASUS on 05/12/2017.
 */

public class EmergencyNumber {
    public static final int DOCTOR = 0;
    public static final int FIREFIGHTER = 1;
    public static final int SAVE_AND_RESCUE = 2;
    public static final int POLICE = 3;


    public String name;
    public String number;
    public int type;

    public EmergencyNumber(String name,String number,int type){
        this.name = name;
        this.number = number;
        this.type = type;
    }

    public Drawable getDrawableIcon(){
        Context context = MyApplication.getInstance().getApplicationContext();
        if(type==DOCTOR)
            return context.getResources().getDrawable(R.drawable.ic_doctor);
        else if(type==POLICE)
            return context.getResources().getDrawable(R.drawable.ic_police);
        else if(type==SAVE_AND_RESCUE)
            return context.getResources().getDrawable(R.drawable.ic_alarm);
        else if(type==FIREFIGHTER)
            return context.getResources().getDrawable(R.drawable.ic_firefighter);
        else
            return null;
    }
}
