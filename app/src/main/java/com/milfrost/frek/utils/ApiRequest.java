package com.milfrost.frek.utils;

import android.support.annotation.NonNull;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.milfrost.frek.MyApplication;
import com.milfrost.frek.models.Chat;
import com.milfrost.frek.models.Circle;
import com.milfrost.frek.models.CircleLoadingStatus;
import com.milfrost.frek.models.Comment;
import com.milfrost.frek.models.NewsFeedLoadingStatus;
import com.milfrost.frek.models.Newsfeed;
import com.milfrost.frek.models.User;
import com.milfrost.frek.models.UserMedia;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 * Created by ASUS on 27/11/2017.
 */

public class ApiRequest {

    private static ApiRequest apiRequest;
    FirebaseAuth mAuth;
    DatabaseReference databaseReference;

    public interface ServerCallback{
        void onSuccess(Object object);
        void onError(Object object);
    }

    public ApiRequest(){
        mAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();
    }

    public static ApiRequest getInstance(){
        if(apiRequest==null){
            apiRequest = new ApiRequest();
        }
        return apiRequest;
    }

    public void login(String email, String password, final ServerCallback serverCallback){
        mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    serverCallback.onSuccess("OK");
                }
                else {
                    serverCallback.onError(null);
                }
            }
        });
    }

    public void getNewsList (final ServerCallback serverCallback){
        final NewsFeedLoadingStatus loadingStatus = new NewsFeedLoadingStatus();
        databaseReference.child("newsfeed").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //System.out.print("getNews = "+dataSnapshot);
                final List<Newsfeed> newsfeedList = new ArrayList<Newsfeed>();
                Iterator i = dataSnapshot.getChildren().iterator();
                while (i.hasNext()){
                    final DataSnapshot newsfeedData = (DataSnapshot)i.next();
                    final Newsfeed newsfeed = new Newsfeed(newsfeedData);

                    //Load User data
                    int storedAuthorPos = MyApplication.getInstance().isUserExistInList(newsfeedData.child("author").getValue().toString());

                    //if author is not stored, fetch data
                    if(storedAuthorPos == -1){
                        ApiRequest.getInstance().getUserInformation(new ServerCallback() {
                            @Override
                            public void onSuccess(Object object) {
                                User user = (User)object;
                                System.out.println("Name = "+user.getName());
                                newsfeed.setAuthor(user);
                                MyApplication.getInstance().addUserToList(user);
                                loadingStatus.isAuthorLoaded = true;
                                if(loadingStatus.isAllLoaded()){
                                    serverCallback.onSuccess(newsfeedList);
                                }
                            }

                            @Override
                            public void onError(Object object) {

                            }
                        },newsfeedData.child("author").getValue().toString());
                    }else{
                        newsfeed.author = MyApplication.getInstance().userList.get(storedAuthorPos);
                        loadingStatus.isAuthorLoaded = true;
                        if(loadingStatus.isAllLoaded()){
                            serverCallback.onSuccess(newsfeedList);
                        }
                    }


                    //Load media from data above
                    if(!newsfeedData.child("media").getValue().toString().equals("")) {
                        ApiRequest.getInstance().getMediaFromUrl(new ApiRequest.ServerCallback() {
                            @Override
                            public void onSuccess(Object object) {
                                UserMedia userMedia = (UserMedia) object;
                                newsfeed.setMedia(userMedia);
                                loadingStatus.isMediaLoaded = true;
                                if(loadingStatus.isAllLoaded()){
                                    serverCallback.onSuccess(newsfeedList);
                                }
                            }

                            @Override
                            public void onError(Object object) {

                            }
                        }, newsfeedData.child("media").getValue().toString());
                    }else{
                        loadingStatus.isMediaLoaded = true;
                        if(loadingStatus.isAllLoaded()){
                            serverCallback.onSuccess(newsfeedList);
                        }
                    }

                    //Load comments from data above
                    if(newsfeedData.child("comments").getValue().toString().equals("")){
                        loadingStatus.isCommentLaoded = true;
                        if(loadingStatus.isAllLoaded()){
                            serverCallback.onSuccess(newsfeedList);
                        }
                    }else {
                        Iterator commentIterator = newsfeedData.child("comments").getChildren().iterator();
                        final List<Comment> commentList = new ArrayList<Comment>();
                        while (commentIterator.hasNext()) {
                            DataSnapshot commentData = (DataSnapshot) commentIterator.next();
                            ApiRequest.getInstance().loadComment(commentData.getKey(), new ApiRequest.ServerCallback() {
                                @Override
                                public void onSuccess(Object object) {
                                    Comment comment = (Comment) object;
                                    commentList.add(comment);
                                    if (commentList.size() == newsfeedData.child("comments").getChildrenCount()) {
                                        loadingStatus.isCommentLaoded = true;
                                    }
                                    if(loadingStatus.isAllLoaded()){
                                        serverCallback.onSuccess(newsfeedList);
                                    }
                                }

                                @Override
                                public void onError(Object object) {

                                }
                            });
                        }
                        newsfeed.setComments(commentList.toArray(new Comment[0]));
                    }
                    newsfeedList.add(newsfeed);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                serverCallback.onError(databaseError);
            }
        });
    }

    public void getMediaFromUrl(final ServerCallback callback,String mediaName){
        databaseReference.child("media").child(mediaName).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //System.out.print("getMediaFromUrl = "+dataSnapshot.toString());
                UserMedia userMedia = new UserMedia(dataSnapshot);
                callback.onSuccess(userMedia);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                callback.onError(databaseError);
            }
        });
    }

    public void getUserInformation (final ServerCallback callback, String email){
        final String emailFinal = email.replace(".",",");
        databaseReference.child("users").child(emailFinal).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //System.out.print("getUserInformation = "+dataSnapshot.toString());
                //System.out.println("User loaded = "+dataSnapshot.toString());
                LogHelper.debug("getUserInformation",dataSnapshot.toString());
                User user = new User(dataSnapshot);
                callback.onSuccess(user);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("User Error = "+databaseError.toString());
                callback.onError(databaseError);
            }
        });
    }

    public void loadComment (String commentId, final ServerCallback callback){
        databaseReference.child("comments").child(commentId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                System.out.println("loadComment = "+dataSnapshot.toString());
                final Comment comment = new Comment(dataSnapshot);
                //if user is not loaded before, then fetch data
                int storedUserPosition = MyApplication.getInstance().isUserExistInList(dataSnapshot.child("author").getValue().toString());
                if(storedUserPosition==-1){
                    ApiRequest.getInstance().getUserInformation(new ApiRequest.ServerCallback() {
                        @Override
                        public void onSuccess(Object object) {
                            User user = (User)object;
                            MyApplication.getInstance().addUserToList(user);
                            comment.setAuthor(user);
                        }

                        @Override
                        public void onError(Object object) {}
                    },dataSnapshot.child("author").getValue().toString());
                }

                //If user exists in list then fetch from list
                else {
                    comment.setAuthor(MyApplication.getInstance().userList.get(storedUserPosition));
                }

                callback.onSuccess(comment);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                callback.onError(databaseError);
            }
        });
    }

    public void getCircles(String circleId,final ServerCallback callback){
        final CircleLoadingStatus circleLoadingStatus = new CircleLoadingStatus();
        databaseReference.child("circles").child(circleId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(final DataSnapshot dataSnapshot) {
                System.out.println("circle Snapshot :" + dataSnapshot.toString());
                final Circle circle = new Circle(dataSnapshot);

                final Iterator circleId = dataSnapshot.child("members").getChildren().iterator();
                final List<User> users = new ArrayList<User>();
                    while (circleId.hasNext()){
                        final DataSnapshot snapshot = (DataSnapshot)circleId.next();
                        String username = snapshot.getKey().toString();
                        int existingUser =MyApplication.getInstance().isUserExistInList(username);
                        if(existingUser==-1){
                            getUserInformation(new ServerCallback() {
                                @Override
                                public void onSuccess(Object object) {
                                    User user = (User)object;
                                    users.add(user);
                                    MyApplication.getInstance().addUserToList(user);
                                    if(users.size()==(int)dataSnapshot.child("members").getChildrenCount()){
                                        circle.setUsers(users.toArray(new User[0]));
                                        circleLoadingStatus.isUserLoaded = true;
                                        if(circleLoadingStatus.isAllLoaded()){
                                            callback.onSuccess(circle);
                                        }
                                    }
                                }

                                @Override
                                public void onError(Object object) {

                                }
                            },username);
                        }
                        else {
                            User user = MyApplication.getInstance().userList.get(existingUser);
                            users.add(user);
                            if(users.size()==(int)dataSnapshot.child("members").getChildrenCount()){
                                circle.setUsers(users.toArray(new User[0]));
                                circleLoadingStatus.isUserLoaded = true;
                                if(circleLoadingStatus.isAllLoaded()){
                                    callback.onSuccess(circle);
                                }
                            }
                        }
                    }
                    Iterator chatIterator = dataSnapshot.child("chats").getChildren().iterator();
                    final List<Chat> chats = new ArrayList<Chat>();
                    while (chatIterator.hasNext()){
                        final DataSnapshot chatData = (DataSnapshot)chatIterator.next();

                        getChatData(chatData.getKey().toString(), new ServerCallback() {
                            @Override
                            public void onSuccess(Object object) {
                                chats.add((Chat)object);
                                System.out.println("key = "+chatData.getKey());
                                if(chats.size() == dataSnapshot.child("chats").getChildrenCount()){
                                    circle.setChats(chats.toArray(new Chat[0]));
                                    circleLoadingStatus.isChatLoaded=true;
                                    if(circleLoadingStatus.isAllLoaded()){
                                        callback.onSuccess(circle);
                                    }
                                }
                            }

                            @Override
                            public void onError(Object object) {

                            }
                        });
                    }

                }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void getChatData(String chatId, final ServerCallback serverCallback){
        databaseReference.child("chats").child(chatId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                final Chat chat = new Chat(dataSnapshot);
                int existingUser = MyApplication.getInstance().isUserExistInList(dataSnapshot.child("author").getValue().toString());
                if(existingUser==-1){
                    getUserInformation(new ServerCallback() {
                        @Override
                        public void onSuccess(Object object) {
                            System.out.println("Chat user retrieved");
                            chat.setUser((User)object);
                            serverCallback.onSuccess(chat);
                        }

                        @Override
                        public void onError(Object object) {

                        }
                    },dataSnapshot.child("author").getValue().toString());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void listenToChatData(String chatroomId, final ServerCallback callback){
        Query query = databaseReference.child("circles").child(chatroomId).child("chats").orderByKey();
        query.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                getChatData(dataSnapshot.getKey(), new ServerCallback() {
                    @Override
                    public void onSuccess(Object object) {
                        Chat chat = (Chat)object;
                        callback.onSuccess(chat);
                    }

                    @Override
                    public void onError(Object object) {
                    }
                });
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void sendChat(String circleId,String content,String datetime ){
        HashMap<String,Object> map = new HashMap<>();
        map.put("author",MyApplication.getInstance().loggedUser.getEmailAddress().replace(",","."));
        map.put("content",content);
        map.put("media","");
        map.put("time_created",datetime);

        String key = databaseReference.child("chats").push().getKey();

        databaseReference.child("chats").child(key).setValue(map);
        databaseReference.child("circles").child(circleId).child("chats").child(key).setValue(1);
    }

}
