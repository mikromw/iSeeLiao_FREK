package com.milfrost.frek.modul.dashboard.circlepage;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.milfrost.frek.MyApplication;
import com.milfrost.frek.R;
import com.milfrost.frek.models.Circle;
import com.milfrost.frek.models.User;

import java.util.ArrayList;
import java.util.List;

public class MapLocationActivity extends AppCompatActivity implements OnMapReadyCallback {

    GoogleMap googleMap;
    MapView mapView;

    Circle circle;
    List<User> users;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = this.getIntent();
        circle = (Circle)intent.getExtras().getSerializable("circle");

        setContentView(R.layout.activity_map_location);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        initViews();
        initObjects();
    }

    private void initViews(){
        mapView = (MapView)findViewById(R.id.map_view);
        mapView.onCreate(null);
        mapView.onResume();
        mapView.getMapAsync(this);
    }
    private void initObjects(){
        users = new ArrayList<>();
        if(circle==null){
            System.out.println("circle is null");
        }else {
            for (int i = 0; i < circle.users.length; i++) {
                System.out.println("inside map"+circle.users[i].getName()+";latitude="+circle.users[i].getLatLng().latitude);
                users.add(circle.users[i]);
            }
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        if(users!=null){
            for(User user:users){
                googleMap.addMarker(new MarkerOptions().position(user.getLatLng()).title(user.getName()));
            }
        }
        CameraPosition position = CameraPosition.builder().target(new LatLng(3.587609, 98.690657)).zoom(16).bearing(0).tilt(45).build();
        googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(position));
    }

}
