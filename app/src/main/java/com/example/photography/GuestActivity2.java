package com.example.photography;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.GridLayout;
import android.widget.GridView;

import com.example.photography.adapters.ImageAdapter;
import com.example.photography.utils.Utilities;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;

//Guest activity2 class (activity)
public class GuestActivity2 extends AppCompatActivity {
    // request image capture
    static final int REQUEST_IMAGE_CAPTURE = 1;
    private ArrayList<Bitmap> images= new ArrayList<>();
    private GridView gridView;
    private ImageAdapter adapter;
    File currentFile = null;

    //onCreate function that create the activity
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //call parent constructor
        super.onCreate(savedInstanceState);
        //load the xml file
        if(Build.VERSION.SDK_INT>=24){
            try{
                Method m = StrictMode.class.getMethod("disableDeathOnFileUriExposure");
                m.invoke(null);
            }catch(Exception e){
                e.printStackTrace();
            }
        }
        setContentView(R.layout.activity_guest2);
        //move to camera by click on camera button
        findViewById(R.id.cumeraButton).setOnClickListener(v -> {
            dispatchTakePictureIntent();
        });
        gridView = findViewById(R.id.grid_view);
        // load pictures already exist
    }
    //Take a photo with a camera app
    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go


            // Continue only if the File was successfully created

                try {
                    currentFile =  Utilities.createImageFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(currentFile));
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
                Bitmap bitmap = Utilities.getBitmapFromFile(currentFile.getPath());
                images.add(bitmap);
                adapter = new ImageAdapter(GuestActivity2.this,images);
                gridView.setAdapter(adapter);


        }
    }

}
