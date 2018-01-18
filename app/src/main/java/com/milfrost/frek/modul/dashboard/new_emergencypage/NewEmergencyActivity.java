package com.milfrost.frek.modul.dashboard.new_emergencypage;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.location.Location;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.milfrost.frek.R;
import com.milfrost.frek.models.Category;
import com.milfrost.frek.utils.ApiRequest;
import com.milfrost.frek.utils.Constant;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.security.Permission;
import java.util.ArrayList;
import java.util.List;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class NewEmergencyActivity extends AppCompatActivity implements NewEmergencyActivityInterface.View,NewEmergencyActivityInterface.ImageAdapterCommunication {

    static final int REQUEST_IMAGE_CAPTURE = 0;

    File pickedImage;
    Toolbar toolbar;

    RecyclerView imageRecyclerView;
    RecyclerView categoryRecyclerView;

    CategoryAdapter categoryAdapter;
    ImageAdapter imageAdapter;

    List<Category> categoryList;
    List<String> imagePath;

    NewEmergencyPresenter presenter;

    TextView postNewsBtn;
    EditText postField;

    private FusedLocationProviderClient mFusedLocationClient;


    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_emergency);
        initViews();
        initObjects();
        setEvents();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void initViews() {
        postNewsBtn = (TextView) findViewById(R.id.post_news);
        postField = (EditText) findViewById(R.id.post_field);
        imageRecyclerView = (RecyclerView) findViewById(R.id.image_rv);
        categoryRecyclerView = (RecyclerView) findViewById(R.id.category_rv);
        toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(getString(R.string.new_emergency));
        toolbar.setTitleTextColor(Color.parseColor("#ffffff"));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    private void initObjects() {
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        presenter = new NewEmergencyPresenter(NewEmergencyActivity.this);
        presenter.viewInterface = this;

        categoryList = new ArrayList<>();
        imagePath = new ArrayList<>();

        categoryAdapter = new CategoryAdapter(NewEmergencyActivity.this, categoryList);
        categoryRecyclerView.setAdapter(categoryAdapter);
        categoryRecyclerView.setLayoutManager(new LinearLayoutManager(NewEmergencyActivity.this, LinearLayoutManager.HORIZONTAL, false));

        imageAdapter = new ImageAdapter(NewEmergencyActivity.this, imagePath);
        imageAdapter.adapterCommunication = this;
        imageRecyclerView.setAdapter(imageAdapter);
        imageRecyclerView.setLayoutManager(new LinearLayoutManager(NewEmergencyActivity.this, LinearLayoutManager.HORIZONTAL, false));

        presenter.loadData();

    }

    private String validation() {
        if (categoryAdapter.chosenCategory == -1) {
            return "Please choose one category";
        } else if (TextUtils.isEmpty(postField.getText().toString())) {
            return "You have to describe the event";
        } else if (imageAdapter.chosenImage == -1) {
            return "Including an image will help verify the credibility of this news.";
        }
        return "OK";
    }

    private Uri getImageUri() {
        if (imageAdapter.chosenImage != 0 && imageAdapter.chosenImage != imagePath.size() - 1) {
            Log.d("Debug", "getImageUri: "+Uri.fromFile(new File(imagePath.get(imageAdapter.chosenImage))).toString());
            return Uri.fromFile(new File(imagePath.get(imageAdapter.chosenImage)));
        }else{
            return Uri.fromFile(pickedImage);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode==1){
            if(grantResults.length>0&&grantResults[0]== PackageManager.PERMISSION_GRANTED&&grantResults[1]==PackageManager.PERMISSION_GRANTED){
                getLocation(new NewEmergencyActivityInterface.LocationCallback() {
                    @Override
                    public void onLocationRetrieved(Location location) {
                        postNews(getImageUri()
                                ,postField.getText().toString()
                                ,categoryList.get(categoryAdapter.chosenCategory).name
                                ,location);
                    }

                    @Override
                    public void onFail(Object obj) {

                    }
                });
            }
        }
        else if(requestCode==Constant.REQ_CAMERA_CODE){
            if(grantResults[0]==PackageManager.PERMISSION_GRANTED){
                useCamera();
            }
        }
    }

    private void getLocation(final NewEmergencyActivityInterface.LocationCallback locationCallback) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.ACCESS_FINE_LOCATION},1);
            System.out.println("getLocation: Permission not granted");
        }else{
            System.out.println("getLocation: Permission granted");
            mFusedLocationClient.getLastLocation()
                    .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            // Got last known location. In some rare situations this can be null.
                            if (location != null) {
                                locationCallback.onLocationRetrieved(location);
                                // Logic to handle location object
                            }
                        }
                    });
        }

    }

    private void showLoadingDialog(){
        ProgressDialog pd= new ProgressDialog(this);
        pd.setMessage("posting...");
        pd.show();
    }

    private void setEvents(){
        postNewsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(validation().equals("OK")){
                    showLoadingDialog();
                    getLocation(new NewEmergencyActivityInterface.LocationCallback() {
                        @Override
                        public void onLocationRetrieved(Location location) {
                            postNews(getImageUri()
                                    ,postField.getText().toString()
                                    ,categoryList.get(categoryAdapter.chosenCategory).name
                                    ,location);
                        }

                        @Override
                        public void onFail(Object obj) {
                        }
                    });
                }
                else{
                    Toast.makeText(NewEmergencyActivity.this,validation(),Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void postNews(Uri uri, String content, String category, Location location){
        System.out.println("postNews: Posting");
        ApiRequest.getInstance().postNewsfeed(uri, content, category, location, new ApiRequest.ServerCallback() {
            @Override
            public void onSuccess(Object object) {
                System.out.println("postNews: successful");
                finish();
            }

            @Override
            public void onError(Object object) {
                showErrorDialog();
            }
        });
    }

    private void showErrorDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Failed to Post.")
                .setMessage("Please try again later.")
                .setPositiveButton("Dismiss", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public boolean hasCamera(){
        return getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY);
    }

    private void useCamera(){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent,REQUEST_IMAGE_CAPTURE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==REQUEST_IMAGE_CAPTURE&&resultCode==RESULT_OK){
            System.out.println("image ok");
            Bundle bundle = data.getExtras();
            Bitmap bm = (Bitmap)bundle.get("data");
            //create a file to write bitmap data
            pickedImage = new File(this.getCacheDir(), "frekimg");
            try {
                pickedImage.createNewFile();
            }catch (Exception e){e.printStackTrace();}

//Convert bitmap to byte array
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            bm.compress(Bitmap.CompressFormat.PNG, 0 /*ignored for PNG*/, bos);
            byte[] bitmapData = bos.toByteArray();

//write the bytes in file
            try {
                FileOutputStream fos = new FileOutputStream(pickedImage);
                fos.write(bitmapData);
                fos.flush();
                fos.close();
            }catch (Exception e){}
        }
    }

    @Override
    public void setCategoryList(List<Category> categoryList) {
        this.categoryList.clear();
        this.categoryList.addAll(categoryList);
    }

    @Override
    public void setImageList(List<String> imagePath) {
        this.imagePath.clear();
        this.imagePath.addAll(imagePath);
    }

    @Override
    public void notifyCategoryAdapter() {
        categoryAdapter.notifyDataSetChanged();
    }

    @Override
    public void notifyImageAdapter() {
        imageAdapter.notifyDataSetChanged();
    }

    @Override
    public void openCamera() {
        if(hasCamera()){
            if(ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.CAMERA}, Constant.REQ_CAMERA_CODE);
            }else{
                useCamera();
            }
        }
    }

    @Override
    public void openGallery() {

    }
}
