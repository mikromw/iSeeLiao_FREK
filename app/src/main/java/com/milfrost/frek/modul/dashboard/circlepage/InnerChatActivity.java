package com.milfrost.frek.modul.dashboard.circlepage;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toolbar;

import com.milfrost.frek.R;
import com.milfrost.frek.models.Chat;
import com.milfrost.frek.models.Circle;
import com.milfrost.frek.utils.ApiRequest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class InnerChatActivity extends AppCompatActivity implements CirclePageInterface.InnerChatView {

    Circle circle;
    InnerChatActivityPresenter chatActivityPresenter;
    RecyclerView recyclerView;
    ChatAdapter chatAdapter;
    List<Chat> chats;
    int count;

    @Override
    protected void onStop() {
        super.onStop();
        count = 0;
    }

    ImageView sendBtn;
    EditText textBox;

    android.support.v7.widget.Toolbar toolbar;

    boolean first;


    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inner_chat);
        Bundle bundle = getIntent().getBundleExtra("bundle");
        circle = (Circle) bundle.getSerializable("item");
        count = 0;
        for(int i=0;i<circle.chats.length;i++){
            System.out.println("chats ="+circle.chats[i].content);
        }
        initViews();
        initObjects();
        setEvents();
    }

    private void initViews(){
        toolbar = (android.support.v7.widget.Toolbar)findViewById(R.id.toolbar);
        recyclerView = (RecyclerView)findViewById(R.id.chat_rv);
        sendBtn = (ImageView)findViewById(R.id.send_btn);
        textBox = (EditText)findViewById(R.id.chat_message_textbox);

        toolbar.setTitle(circle.name);
        toolbar.setTitleTextColor(Color.parseColor("#ffffff"));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    private void showAddFriendDialog(){

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_dashboard, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.add_member) {
            return true;
        }else if(id==R.id.left_circle){
            return true;
        }
        else if(id==R.id.view_member){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setEvents(){
        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!TextUtils.isEmpty(textBox.getText().toString())){
                    chatActivityPresenter.sendChat(circle.key,textBox.getText().toString());
                }
            }
        });
    }

    private void initObjects(){
        chatActivityPresenter = new InnerChatActivityPresenter(InnerChatActivity.this,circle);
        chatActivityPresenter.viewInterface = this;

        chats = new ArrayList<>();

        chatAdapter = new ChatAdapter(InnerChatActivity.this,chats);

        recyclerView.setAdapter(chatAdapter);
        recyclerView.setLayoutManager( new LinearLayoutManager(InnerChatActivity.this));
        recyclerView.setHasFixedSize(true);

        chatActivityPresenter.loadData();
        chatActivityPresenter.listenToChatData();

    }

    @Override
    public void setList(List<Chat> chatList) {
        chats.clear();
        chats.addAll(chatList);
        for(Circle circle:CirclePage.circles){
            if(circle.key.equals(this.circle.key)) {
                circle.chats = chats.toArray(new Chat[0]);
            }
        }
    }

    @Override
    public void notifyAdapter() {
        chatAdapter.notifyDataSetChanged();
    }

    @Override
    public void resetInputBox() {
        textBox.setText("");
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
        //hide keyboard
    }
}
