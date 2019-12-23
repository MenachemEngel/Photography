package com.example.photography;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    //private Button goToEvent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //goToEvent = findViewById(R.id.int_event);
        findViewById(R.id.int_event).setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this ,GuestActivity1.class);
            startActivity(intent);
        });

    }
    //butter knife
}
