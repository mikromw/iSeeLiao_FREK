package com.milfrost.frek.modul.dashboard.emergency_number;


import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.milfrost.frek.R;
import com.milfrost.frek.models.EmergencyNumber;
import com.milfrost.frek.utils.Constant;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class EmergencyNumberFragment extends Fragment implements EmergencyNumberInterface.View {

    View view;
    TextView locationLabel;
    EmergencyNumberPresenter emergencyNumberPresenter;
    EmergencyNumberAdapter numberAdapter;
    EditText searchBar;
    List<EmergencyNumber> emergencyNumberList;

    RecyclerView numberRv;

    public EmergencyNumberFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_emergency_number, container, false);

        initViews();
        initObjects();
        setEvents();

        return view;
    }

    private void setEvents(){
        searchBar.addTextChangedListener(onSearchTyped);
    }


    private void initViews(){
        numberRv = (RecyclerView)view.findViewById(R.id.emergency_num_rv);
        searchBar = (EditText)view.findViewById(R.id.search_bar);
        //locationLabel = (TextView)view.findViewById(R.id.location_label);
    }

    private void initObjects(){
        emergencyNumberPresenter = new EmergencyNumberPresenter(getContext());
        emergencyNumberPresenter.viewInterface = this;

        emergencyNumberList = new ArrayList<>();
        numberAdapter = new EmergencyNumberAdapter(getContext(),emergencyNumberList);
        numberAdapter.numberInterface = this;

        numberRv.setAdapter(numberAdapter);
        numberRv.setLayoutManager(new LinearLayoutManager(getContext()));
        numberRv.setHasFixedSize(true);

        emergencyNumberPresenter.loadData();
    }



    private TextWatcher onSearchTyped = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            emergencyNumberPresenter.filterSearch(editable.toString());
        }
    };
    @Override
    public void makePhoneCall(String number){
        if(ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.CALL_PHONE)!= PackageManager.PERMISSION_GRANTED){
            requestPermissions(new String[]{Manifest.permission.CALL_PHONE}, Constant.REQ_PHONE_CODE);
        }else{
            Intent phoneIntent = new Intent(Intent.ACTION_CALL);
            phoneIntent.setData(Uri.parse("tel:"+number));
            startActivity(phoneIntent);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        System.out.println("Phone permission granted");
        if(requestCode==Constant.REQ_PHONE_CODE){
            if(grantResults[0]==PackageManager.PERMISSION_GRANTED){

                makePhoneCall(numberAdapter.chosenNumber);
            }
        }
    }

    @Override
    public void setList(List<EmergencyNumber> emergencyNumberList) {
        this.emergencyNumberList.clear();
        this.emergencyNumberList.addAll(emergencyNumberList);

    }

    @Override
    public void notifyAdapter() {
        numberAdapter.notifyDataSetChanged();
    }
}
