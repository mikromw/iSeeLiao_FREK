package com.milfrost.frek.modul.dashboard.circlepage;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.model.LatLng;
import com.milfrost.frek.MyApplication;
import com.milfrost.frek.R;
import com.milfrost.frek.models.Chat;
import com.milfrost.frek.models.Circle;
import com.milfrost.frek.models.User;
import com.milfrost.frek.utils.ApiRequest;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChatFragment extends Fragment {

    View view;
    List<Circle> circles;
    RecyclerView circleRv;
    CircleAdapter circleAdapter;
    public CirclePage circlePage;

    public ChatFragment() {
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
        view =  inflater.inflate(R.layout.fragment_chat, container, false);
        initViews();
        initObjects();
        return view;
    }

    private void initViews(){
        circleRv = (RecyclerView)view.findViewById(R.id.circle_rv);
    }

    private void initObjects(){
        /*Chat[] chats = new Chat[]{new Chat(circle.users[0], "Where are you Stalin?", "06-12-2017 10:19:00")
                , new Chat(circle.users[1], "I'm helping Donald to build the wall", "06-12-2017 10:21:00")
                , new Chat(circle.users[2], "No he is not.", "06-12-2017 10:22:00")
                , new Chat(MyApplication.getInstance().loggedUser, "What are you guys doing now?", "06-12-2017 10:27:00")
        };
        circle.setChats(chats);*/

        circles = new ArrayList<>();

        circleAdapter = new CircleAdapter(getContext(),circles);
        circleRv.setAdapter(circleAdapter);
        circleRv.setLayoutManager(new LinearLayoutManager(getContext()));
        circleRv.setHasFixedSize(true);
    }

    public void setCircles(List<Circle> circles){
        this.circles.clear();
        this.circles.addAll(circles);
        circleAdapter.notifyDataSetChanged();
    }


}
