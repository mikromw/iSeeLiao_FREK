package com.milfrost.frek.modul.dashboard.emergency_number;

import com.milfrost.frek.models.EmergencyNumber;

import java.util.List;

/**
 * Created by ASUS on 05/12/2017.
 */

public interface EmergencyNumberInterface {

    interface View{
        void setList(List<EmergencyNumber> emergencyNumberList);
        void makePhoneCall(String phoneNumber);
        void notifyAdapter();
    }
}
