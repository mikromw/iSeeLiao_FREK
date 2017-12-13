package com.milfrost.frek.modul.dashboard.circlepage;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.milfrost.frek.R;
import com.milfrost.frek.models.Circle;
import com.milfrost.frek.models.User;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class LocationFragment extends Fragment implements OnMapReadyCallback{

    GoogleMap googleMap;
    MapView mapView;
    View view;

    List<Circle> circles;
    List<User> users;

    public LocationFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view =  inflater.inflate(R.layout.fragment_location, container, false);
        initViews();
        initObject();
        return view;
    }

    private void initObject(){
        circles = new ArrayList<>();
    }
    private void initViews(){
        mapView = (MapView)view.findViewById(R.id.map_view);
        mapView.onCreate(null);
        mapView.onResume();
        mapView.getMapAsync(this);
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

    public void setCircles(List<Circle> circles){
        this.circles.clear();
        this.circles.addAll(circles);
        users = Arrays.asList(circles.get(0).users);
    }
}
