package com.milfrost.frek.modul.registerPage;

/**
 * Created by ASUS on 27/11/2017.
 */

public interface RegisterInterface {
    interface View{
        void showErrorMessage(int mode,String message);
        void dismissErrorMessage();
    }
}
