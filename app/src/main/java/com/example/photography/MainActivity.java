package com.example.photography;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
//Main activity class (activity)
public class MainActivity extends AppCompatActivity {

    //onCreate function that create the activity
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //call parent constructor
        super.onCreate(savedInstanceState);
        //load the xml file
        setContentView(R.layout.activity_main);
        //request for permission camera
        ActivityCompat.requestPermissions(MainActivity.this,
                new String[]{Manifest.permission.CAMERA},
                1);

        //move to GuestActivity1 by click the button of "Enter Event"
        findViewById(R.id.int_event).setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this ,GuestActivity1.class);
            startActivity(intent);
        });

    }
}
