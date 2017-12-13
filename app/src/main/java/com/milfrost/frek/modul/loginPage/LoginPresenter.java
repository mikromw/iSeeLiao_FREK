package com.milfrost.frek.modul.loginPage;


import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.milfrost.frek.R;
import com.milfrost.frek.utils.ApiRequest;
import com.milfrost.frek.modul.dashboard.Dashboard;
import com.milfrost.frek.modul.registerPage.RegisterPage;

/**
 * Created by ASUS on 26/11/2017.
 */

public class LoginPresenter{

    Context context;
    LoginPage loginPage;
    LoginInterface.View viewInterface;

    public LoginPresenter(Context context){
        this.context = context;
        loginPage = (LoginPage)context;
    }

    public void setViewInterface (LoginInterface.View viewInterface){
        this.viewInterface = viewInterface;
    }

    public void openRegisterPage(){
        Intent intent = new Intent(context, RegisterPage.class);
        loginPage.startActivity(intent);
    }

    public void openForgotPasswordPage(){

    }

    private void openDashboardPage(){
        Intent intent = new Intent(context, Dashboard.class);
        context.startActivity(intent);
    }

    public void login(String emailAddress,String password){
        if(validation(emailAddress,password)){
            viewInterface.showProgressDialog();
            ApiRequest.getInstance().login(emailAddress, password, new ApiRequest.ServerCallback() {
                @Override
                public void onSuccess(Object object) {
                    openDashboardPage();
                    viewInterface.dismissProgressDialog();
                    viewInterface.finishActivity();
                }

                @Override
                public void onError(Object object) {
                    viewInterface.dismissProgressDialog();
                    viewInterface.showError(LoginPage.ERROR_EMAIL_PASSWORD,context.getString(R.string.wrong_username_pwd));
                }
            });
        }
    }

    private boolean validation(String emailAddress,String password) {
        if (TextUtils.isEmpty(emailAddress) && TextUtils.isEmpty(password)) {
            if(viewInterface!=null){
                viewInterface.showError(LoginPage.ERROR_EMAIL_PASSWORD,context.getString(R.string.empty_field_warning));
            }
            return false;
        } else if (TextUtils.isEmpty(emailAddress)) {
            if(viewInterface!=null){
                viewInterface.showError(LoginPage.ERROR_EMAIL,context.getString(R.string.empty_email_warning));
            }
            return false;
        } else if (TextUtils.isEmpty(password)) {
            if(viewInterface!=null){
                viewInterface.showError(LoginPage.ERROR_PASSWORD,context.getString(R.string.empty_password_warning));
            }
            return false;
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(emailAddress).matches()){
            if(viewInterface!=null){
                viewInterface.showError(LoginPage.ERROR_EMAIL,context.getString(R.string.invalid_email_format_warning));
            }
            return false;
        } else {
            return true;
        }
    }
}
