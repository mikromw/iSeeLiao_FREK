package com.milfrost.frek.utils;

import android.content.ContentResolver;
import android.location.Location;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.milfrost.frek.MyApplication;
import com.milfrost.frek.models.Chat;
import com.milfrost.frek.models.Circle;
import com.milfrost.frek.models.CircleLoadingStatus;
import com.milfrost.frek.models.Comment;
import com.milfrost.frek.models.FirstAidTutorial;
import com.milfrost.frek.models.NewsFeedLoadingStatus;
import com.milfrost.frek.models.Newsfeed;
import com.milfrost.frek.models.TutorialStep;
import com.milfrost.frek.models.User;
import com.milfrost.frek.models.UserMedia;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import static com.google.android.gms.plus.PlusOneDummyView.TAG;

/**
 * Created by ASUS on 27/11/2017.
 */

public class ApiRequest {

    private static ApiRequest apiRequest;
    FirebaseAuth mAuth;
    FirebaseStorage storage;
    StorageReference storageReference;
    DatabaseReference databaseReference;

    public interface ServerCallback{
        void onSuccess(Object object);
        void onError(Object object);
    }

    public interface RealTimeServerCallback{
        void onNewData(Object object);
        void onDataChanged(Object object);
        void onDataRemoved(Object object);
        void onCancelled(Object object);
    }

    public ApiRequest(){
        mAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
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

    public void getNewsList(boolean isContinuous, final RealTimeServerCallback serverCallback){

        databaseReference.child("newsfeed").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                final NewsFeedLoadingStatus loadingStatus = new NewsFeedLoadingStatus();
                final DataSnapshot newsfeedData = dataSnapshot;
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
                                serverCallback.onNewData(newsfeed);
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
                        serverCallback.onNewData(newsfeed);
                    }
                }


                //Load media from data above
                /*if(!TextUtils.isEmpty(newsfeedData.child("media").getValue().toString())) {

                    ApiRequest.getInstance().getMediaFromUrl(new ApiRequest.ServerCallback() {
                        @Override
                        public void onSuccess(Object object) {
                            UserMedia userMedia = (UserMedia) object;
                            newsfeed.setMedia(userMedia);
                            System.out.println("Newskey = "+newsfeedData.getKey()+";Media = "+newsfeedData.child("media").getValue().toString());
                            loadingStatus.isMediaLoaded = true;
                            if(loadingStatus.isAllLoaded()){
                                serverCallback.onSuccess(newsfeed);
                            }
                        }

                        @Override
                        public void onError(Object object) {

                        }
                    }, newsfeedData.child("media").getValue().toString());
                }else{
                    loadingStatus.isMediaLoaded = true;
                    if(loadingStatus.isAllLoaded()){
                        serverCallback.onSuccess(newsfeed);
                    }
                }*/

                //Load comments from data above
                if(newsfeedData.child("comments").getValue().toString().equals("")){
                    loadingStatus.isCommentLaoded = true;
                    if(loadingStatus.isAllLoaded()){
                        serverCallback.onNewData(newsfeed);
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
                                    newsfeed.setComments(commentList.toArray(new Comment[0]));
                                }
                                if(loadingStatus.isAllLoaded()){
                                    serverCallback.onNewData(newsfeed);
                                }
                            }

                            @Override
                            public void onError(Object object) {

                            }
                        });
                    }

                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                final NewsFeedLoadingStatus loadingStatus = new NewsFeedLoadingStatus();
                final DataSnapshot newsfeedData = dataSnapshot;
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
                                serverCallback.onDataChanged(newsfeed);
                            }
                        }

                        @Override
                        public void onError(Object object) {

                        }
                    },newsfeedData.child("author").getValue().toString());
                }else {
                    newsfeed.author = MyApplication.getInstance().userList.get(storedAuthorPos);
                    loadingStatus.isAuthorLoaded = true;
                    if (loadingStatus.isAllLoaded()) {
                        serverCallback.onDataChanged(newsfeed);
                    }
                }

                //Load comments from data above
                if(newsfeedData.child("comments").getValue().toString().equals("")){
                    loadingStatus.isCommentLaoded = true;
                    if(loadingStatus.isAllLoaded()){
                        serverCallback.onDataChanged(newsfeed);
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
                                    newsfeed.setComments(commentList.toArray(new Comment[0]));
                                }
                                if(loadingStatus.isAllLoaded()){
                                    serverCallback.onDataChanged(newsfeed);
                                }
                            }

                            @Override
                            public void onError(Object object) {

                            }
                        });
                    }

                }
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                serverCallback.onDataRemoved(dataSnapshot.getKey());
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

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
                        Log.d(TAG, "onSuccess: "+chat.content);
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

    public void uploadImage(String imagePath, Uri uri, final ServerCallback callback){
        StorageReference imageRef = storageReference.child(imagePath+"/"+uri.getLastPathSegment());
        UploadTask uploadTask = imageRef.putFile(uri);

        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                callback.onError(null);
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Uri downloadUrl = taskSnapshot.getDownloadUrl();
                callback.onSuccess(downloadUrl);
            }
        });
    }
    public void postNewsfeed(Uri uri, String content, String category, Location location, final ServerCallback callback){
        final DatabaseReference newsRef = databaseReference.child("newsfeed");
        final String newsKey = newsRef.push().getKey();
        final HashMap<String,Object> newsMap = new HashMap<>();
        HashMap<String,Object> locationMap = new HashMap<>();
        locationMap.put("latitude",location.getLatitude());
        locationMap.put("longitude",location.getLongitude());
        newsMap.put("author",MyApplication.getInstance().loggedUser.getEmailAddress());
        newsMap.put("comments","");
        newsMap.put("category",category);
        newsMap.put("content",content);
        newsMap.put("datetime",DateParser.getCurrentTimeInString());
        newsMap.put("verified",0);
        newsMap.put("location",locationMap);

        if(uri!=null) {
            /*final DatabaseReference mediaRef = databaseReference.child("media");
            final String mediaKey = mediaRef.push().getKey();

            final HashMap<String,Object> mediaMap = new HashMap<>();
            ContentResolver contentResolver = MyApplication.getInstance().getContentResolver();
            mediaMap.put("name",uri.getLastPathSegment());
            mediaMap.put("type",contentResolver.getType(uri));*/

            //upload the image to cloud storage
            uploadImage("newsfeed", uri, new ServerCallback() {
                @Override
                public void onSuccess(Object object) {
                    /*mediaMap.put("url",((Uri)object).toString());
                    mediaRef.child(mediaKey).setValue(mediaMap);*/
                    newsMap.put("media",((Uri)object).toString());
                    newsRef.child(newsKey).setValue(newsMap);
                    callback.onSuccess(null);
                }

                @Override
                public void onError(Object object) {
                    callback.onError(null);
                }
            });
        }
        else{
            newsMap.put("media","");
            newsRef.child(newsKey).setValue(newsMap);
            callback.onSuccess("OK");
        }



    }

    public void signOut(){
        FirebaseAuth.getInstance().signOut();
    }

    public void createNewCircle(String circleName){
        DatabaseReference circleRef = databaseReference.child("circle");
        String circleKey = circleRef.push().getKey();

        HashMap<String,Object> membersMap = new HashMap<>();
        membersMap.put(MyApplication.getInstance().loggedUser.getEmailAddress(),"1");

        HashMap<String,Object> circleMap = new HashMap<>();
        circleMap.put("name",circleName);
        circleMap.put("date_created",DateParser.getCurrentTimeInString());
        circleMap.put("chats","");
        circleMap.put("members",membersMap);

        DatabaseReference userRef = databaseReference.child("users").child(MyApplication.getInstance().loggedUser.getEmailAddress().replace(".",","));
        userRef.child("circle").child(circleKey).setValue(1);

    }
    public void sendComment(String circleId,String commentContent, ServerCallback callback){
        DatabaseReference newsCommentRef = databaseReference.child("newsfeed").child(circleId).child("comments");
        DatabaseReference commentRef = databaseReference.child("comments");

        String commentKey = commentRef.push().getKey();
        HashMap<String,Object> commentMap =  new HashMap<>();
        commentMap.put("author",MyApplication.getInstance().loggedUser.getEmailAddress());
        commentMap.put("content",commentContent);
        commentMap.put("datetime",DateParser.getCurrentTimeInString());
        commentMap.put("show",1);

        commentRef.child(commentKey).setValue(commentMap);
        newsCommentRef.child(commentKey).setValue(1);

        callback.onSuccess(null);
    }

    public void getTutorialList(final RealTimeServerCallback realTimeServerCallback){
        databaseReference.child("tutorials").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(final DataSnapshot dataSnapshot, String s) {
                final FirstAidTutorial tutorial = new FirstAidTutorial(dataSnapshot);
                final List<TutorialStep> tutorialSteps = new ArrayList<TutorialStep>();
                Iterator i = dataSnapshot.child("steps").getChildren().iterator();
                while (i.hasNext()){
                    DataSnapshot stepSnapshot = (DataSnapshot) i.next();
                    getTutorialStepDetails(stepSnapshot.getKey(), new ServerCallback() {
                        @Override
                        public void onSuccess(Object object) {
                            tutorialSteps.add((TutorialStep)object);
                            if(tutorialSteps.size()==(int) dataSnapshot.child("steps").getChildrenCount()){
                                tutorial.tutorialSteps = tutorialSteps;
                                realTimeServerCallback.onNewData(tutorial);
                            }
                        }

                        @Override
                        public void onError(Object object) {

                        }
                    });
                }
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

    public void getTutorialStepDetails(String stepId, final ServerCallback serverCallback){
        databaseReference.child("steps").child(stepId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                TutorialStep step = new TutorialStep(dataSnapshot);
                serverCallback.onSuccess(step);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                serverCallback.onError(databaseError);
            }
        });
    }

}
