package com.milfrost.frek.modul.dashboard.emergency_number;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.milfrost.frek.R;
import com.milfrost.frek.models.EmergencyNumber;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class EmergencyNumberFragment extends Fragment implements EmergencyNumberInterface.View {

    View view;
    EmergencyNumberPresenter emergencyNumberPresenter;
    EmergencyNumberAdapter numberAdapter;
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

        return view;
    }

    private void initViews(){
        numberRv = (RecyclerView)view.findViewById(R.id.emergency_num_rv);
    }

    private void initObjects(){
        emergencyNumberPresenter = new EmergencyNumberPresenter(getContext());
        emergencyNumberPresenter.viewInterface = this;

        emergencyNumberList = new ArrayList<>();
        numberAdapter = new EmergencyNumberAdapter(getContext(),emergencyNumberList);
        numberRv.setAdapter(numberAdapter);
        numberRv.setLayoutManager(new LinearLayoutManager(getContext()));
        numberRv.setHasFixedSize(true);

        emergencyNumberPresenter.loadData();
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
