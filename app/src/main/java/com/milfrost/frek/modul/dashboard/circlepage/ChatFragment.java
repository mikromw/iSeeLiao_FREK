package com.milfrost.frek.modul.dashboard.circlepage;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

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
public class ChatFragment extends Fragment implements CirclePageInterface.ChatFragmentView{

    View view;
    static List<Circle> circles;
    RecyclerView circleRv;
    CircleAdapter circleAdapter;
    ChatFragmentPresenter presenter;

    ImageView fabAddCircle;

    //PopupView
    LinearLayout popupContainer;
    ImageView closePopUpBtn;
    EditText popupCircleNameField;
    TextView createGroupBtn;
    ImageView popupCircleIconView;

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
        setEvents();
        initObjects();
        return view;
    }

    private void initViews(){
        circleRv = (RecyclerView)view.findViewById(R.id.circle_rv);

        popupContainer = (LinearLayout)view.findViewById(R.id.new_circle_page);
        closePopUpBtn = (ImageView)view.findViewById(R.id.close_btn);
        popupCircleNameField = (EditText)view.findViewById(R.id.circle_name_field);
        createGroupBtn = (TextView) view.findViewById(R.id.btn_create_circle);
        popupCircleIconView = (ImageView)view.findViewById(R.id.group_ic) ;

        fabAddCircle = (ImageView) view.findViewById(R.id.fab_add_circle);

        popupContainer.setVisibility(View.GONE);
    }

    private void setEvents(){
        fabAddCircle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupContainer.setVisibility(View.VISIBLE);
            }
        });
        popupContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupContainer.setVisibility(View.GONE);
            }
        });
        closePopUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupContainer.setVisibility(View.GONE);
            }
        });
        createGroupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createGroup();
            }
        });
    }

    private void createGroup(){
        if(TextUtils.isEmpty(popupCircleNameField.getText().toString())){
            Toast.makeText(getContext(),getString(R.string.empty_field_warning),Toast.LENGTH_LONG).show();
        }else{

        }
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

        presenter = new ChatFragmentPresenter(getContext());
        presenter.viewInterface = this;
        presenter.loadData();
    }

    public void setCircles(List<Circle> circles){
        this.circles.clear();
        this.circles.addAll(circles);
        circleAdapter.notifyDataSetChanged();
    }


    @Override
    public void setList(List<Circle> Circles) {
        setCircles(circles);
    }

    @Override
    public void addItemToList(Circle circle) {
        this.circles.add(circle);
    }

    @Override
    public void notifyAdapter() {
        circleAdapter.notifyDataSetChanged();
    }
}
