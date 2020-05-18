package com.example.photography;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.Toast;

import com.example.photography.utils.Globals;


public class OwnerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //change the theme from launch to background app
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_owner);

        //move to GuestActivity1 by click the button of "Enter Event"
        findViewById(R.id.int_event1).setOnClickListener(v -> {
            Intent intent = new Intent(OwnerActivity.this ,GuestActivity1.class);
            startActivity(intent);
        });

        findViewById(R.id.cre_event1).setOnClickListener(v -> {
            Intent intent = new Intent(OwnerActivity.this ,OwnerActivity1.class);
            startActivity(intent);
        });
    }
}
