package com.example.photography;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.GridView;

import com.example.photography.adapters.ImageAdapter;
import com.example.photography.adapters.RecyclerAdapter;
import com.example.photography.utils.FontAwesome;
import com.example.photography.utils.Utilities;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.util.ArrayList;

//Guest activity2 class (activity)
public class GuestActivity2 extends AppCompatActivity {
    //request image capture code
    static final int REQUEST_IMAGE_CAPTURE = 1;
    //array list for bitmap
    private ArrayList<Bitmap> images= new ArrayList<>();
    //file for (jpg) image
    File currentFile = null;

    //------------------------
    private ArrayList<String> imageUrls = new ArrayList<>();
    private RecyclerView recyclerView;
    private RecyclerAdapter adapter;
    private ArrayList<String> imgToDelete = new ArrayList<>();
    //------------------------

    //onCreate function that create the activity
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //change the theme from launch to background app
        setTheme(R.style.AppTheme);
        //call parent constructor
        super.onCreate(savedInstanceState);
        //load the xml file
        setContentView(R.layout.activity_guest2);

        //for font awesome
        Typeface iconFont = FontAwesome.getTypeface(getApplicationContext(), FontAwesome.FONTAWESOME);

        Button cb = findViewById(R.id.cameraButton);
        cb.setTypeface(iconFont);
        Button delB = findViewById(R.id.deleteButton);
        delB.setTypeface(iconFont);

        recyclerView = findViewById(R.id.recycler_view_guest);
        adapter = new RecyclerAdapter(this, imageUrls);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));

        //check the version to avoid from the common bug
        if(Build.VERSION.SDK_INT>=24){
            try{
                Method m = StrictMode.class.getMethod("disableDeathOnFileUriExposure");
                m.invoke(null);
            }catch(Exception e){
                e.printStackTrace();
            }
        }

        recyclerView.setOnClickListener(view -> {
            String str = recyclerView.getAdapter().toString();
            File file = new File(str);
            file.delete();
        });

        //move to camera by click on camera button
        findViewById(R.id.cameraButton).setOnClickListener(v -> {
            dispatchTakePictureIntent();
        });

        initImageBitmap();
    }

    private void initImageBitmap(){
        File photographyAppDir = new File(Utilities.getOrCreateAppFolder());
        //String[] photographyAppFiles = photographyAppDir.list();
        File[] imgFiles = photographyAppDir.listFiles();
        for(int i = 0; i < imgFiles.length; i++){
            imageUrls.add(imgFiles[i].getPath());
        }
        adapter.notifyDataSetChanged();
    }



    //Take a photo with a camera app
    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Continue only if the File was successfully created
            try {
                currentFile =  Utilities.createImageFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            //put the file on the intent
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(currentFile));
            //go to camera
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }

    }

    //get the image from the camera and 1)show it on the grid view
    //                                  2)and save it on the list
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
                Bitmap bitmap = Utilities.getBitmapFromFile(currentFile.getPath());
                images.add(bitmap);
                imageUrls.add(currentFile.getPath());
                adapter.notifyDataSetChanged();
        }else{
            currentFile.delete();
        }
    }

}
