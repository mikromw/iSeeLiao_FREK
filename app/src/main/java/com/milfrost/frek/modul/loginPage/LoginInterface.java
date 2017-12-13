package com.milfrost.frek.modul.loginPage;

/**
 * Created by ASUS on 26/11/2017.
 */

public interface LoginInterface {

    interface View{
        void showError(int code,String message);
        void showProgressDialog();
        void dismissProgressDialog();
        void showErrorMessageDialog(String message);
        void finishActivity();
    }
}
