package com.example.photography;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;

public class OwnerActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_owner);

        findViewById(R.id.buttonCreateEvent_owner1).setOnClickListener(v -> {
            Intent intent = new Intent(OwnerActivity.this ,OwnerActivity2.class);
            startActivity(intent);
        });
    }
}
