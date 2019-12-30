package com.example.photography;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;

//Guest activity2 class (activity)
public class GuestActivity2 extends AppCompatActivity {
    // request image capture
    static final int REQUEST_IMAGE_CAPTURE = 1;

    //onCreate function that create the activity
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //call parent constructor
        super.onCreate(savedInstanceState);
        //load the xml file
        setContentView(R.layout.activity_guest2);

        findViewById(R.id.cumeraButton).setOnClickListener(v -> {
            dispatchTakePictureIntent();
        });

    }
    //Take a photo with a camera app
    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }


}
