package com.milfrost.frek.modul.dashboard.new_emergencypage;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import com.milfrost.frek.models.Category;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by vincent on 06/12/17.
 */

public class NewEmergencyPresenter {

    Context context;
    NewEmergencyActivityInterface.View viewInterface;

    public NewEmergencyPresenter(Context context){
        this.context = context;
    }

    public void loadData(){
        List<Category> categoryList = new ArrayList<>();
        categoryList.add(new Category("Kebakaran","#f1c40f","https://firebasestorage.googleapis.com/v0/b/fast-response-emergency-kit.appspot.com/o/ic_fire.png?alt=media&token=93e6d43b-3cbc-4ef1-9aaa-bcae65e6045b"));
        categoryList.add(new Category("Longsor","#3498db","https://firebasestorage.googleapis.com/v0/b/fast-response-emergency-kit.appspot.com/o/ic_avalanche.png?alt=media&token=ae19536f-766c-4b98-a737-a98dd0a71c2f"));
        categoryList.add(new Category("Banjir","#2ecc71","https://firebasestorage.googleapis.com/v0/b/fast-response-emergency-kit.appspot.com/o/ic_flood.png?alt=media&token=5234974c-46c5-442f-9b8f-8374ce437b24"));
        categoryList.add(new Category("Vulkanik","#e74c3c","https://firebasestorage.googleapis.com/v0/b/fast-response-emergency-kit.appspot.com/o/ic_volcano.png?alt=media&token=1d88ff6d-2153-4aea-bf8a-096e7b7b7508"));

        if(viewInterface!=null){
            viewInterface.setCategoryList(categoryList);
            viewInterface.notifyCategoryAdapter();
            viewInterface.setImageList(getAllShownImagesPath());
            viewInterface.notifyImageAdapter();
        }


    }

    private ArrayList<String> getAllShownImagesPath() {
        Uri uri;
        Cursor cursor;
        int column_index_data, column_index_folder_name;
        ArrayList<String> listOfAllImages = new ArrayList<String>();
        String absolutePathOfImage = null;
        uri = android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI;

        String[] projection = { MediaStore.MediaColumns.DATA,
                MediaStore.Images.Media.BUCKET_DISPLAY_NAME };

        cursor = context.getContentResolver().query(uri, projection, null,
                null, null);

        column_index_data = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
        column_index_folder_name = cursor
                .getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME);
        while (cursor.moveToNext()) {
            absolutePathOfImage = cursor.getString(column_index_data);

            listOfAllImages.add(absolutePathOfImage);
        }
        return listOfAllImages;
    }
}
