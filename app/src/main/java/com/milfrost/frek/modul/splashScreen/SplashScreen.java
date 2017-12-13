package com.milfrost.frek.modul.splashScreen;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.milfrost.frek.MyApplication;
import com.milfrost.frek.R;
import com.milfrost.frek.models.User;
import com.milfrost.frek.modul.dashboard.Dashboard;
import com.milfrost.frek.modul.loginPage.LoginPage;
import com.milfrost.frek.utils.ApiRequest;

import java.util.ArrayList;
import java.util.List;

public class SplashScreen extends AppCompatActivity {

    final static int REQUEST_PERMISSION = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        checkPermission();
    }
    private void openLoginPage(){
        Intent intent = new Intent(SplashScreen.this,LoginPage.class);
        startActivity(intent);
        finish();
    }
    private void checkPermission(){
        List<String> permissionList = new ArrayList<String>();
        permissionList.add( android.Manifest.permission.READ_EXTERNAL_STORAGE);
        permissionList.add( android.Manifest.permission.ACCESS_COARSE_LOCATION);
        permissionList.add( android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        permissionList.add( android.Manifest.permission.READ_CONTACTS);
        /*ActivityCompat.requestPermissions(this,new String[]{
        android.Manifest.permission.READ_EXTERNAL_STORAGE, android.Manifest.permission.ACCESS_COARSE_LOCATION},REQUEST_PERMISSION);*/
        askForPermission(permissionList,REQUEST_PERMISSION);
    }

    private void askForPermission(List<String> permission, Integer requestCode) {
        List<String> newPermission = new ArrayList<>();
        for(int i=0;i<permission.size();i++){
            if (ContextCompat.checkSelfPermission(SplashScreen.this, permission.get(i)) == PackageManager.PERMISSION_GRANTED) {

            }
            else{
                newPermission.add(permission.get(i));
            }
        }
        if(newPermission.size()>0) {
            String[] permissionArr = newPermission.toArray(new String[newPermission.size()]);
            ActivityCompat.requestPermissions(SplashScreen.this, permissionArr, requestCode);
        }
        else{
            openLoginPage();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(FirebaseAuth.getInstance().getCurrentUser()!=null){
            System.out.print(FirebaseAuth.getInstance().getCurrentUser().getEmail());
            openDashboard();

        }else {
            openLoginPage();
        }
    }

    private void openDashboard(){
        ApiRequest.getInstance().getUserInformation(new ApiRequest.ServerCallback() {
            @Override
            public void onSuccess(Object object) {
                User user = (User)object;
                MyApplication.getInstance().loggedUser = user;
                Intent dasboardIntent = new Intent(SplashScreen.this, Dashboard.class);
                startActivity(dasboardIntent);
                finish();
            }

            @Override
            public void onError(Object object) {

            }
        },FirebaseAuth.getInstance().getCurrentUser().getEmail());

    }
}
