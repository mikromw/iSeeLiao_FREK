package com.milfrost.frek.modul.registerPage;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;

import com.milfrost.frek.R;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class RegisterPage extends AppCompatActivity implements RegisterInterface.View{


    EditText fullNameTxt;
    EditText emailAddrTxt;
    EditText passwordTxt;
    EditText confPasswordTxt;
    EditText mobileTxt;


    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_page);
        initViews();
        initObjects();
    }

    private void initViews(){

    }
    private void initObjects(){

    }



    @Override
    public void showErrorMessage(int mode, String message) {}

    @Override
    public void dismissErrorMessage() {}
}
