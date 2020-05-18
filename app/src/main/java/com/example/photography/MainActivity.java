package com.example.photography;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.photography.database.Event;
import com.example.photography.database.Hall;
import com.example.photography.utils.Globals;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

//Main activity class (activity)
public class MainActivity extends AppCompatActivity {

    //database variables
    private FirebaseDatabase database;
    private DatabaseReference myRef;
    private Event event;
    private Hall hall;

    //onCreate function that create the activity
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //for launch screen
        try {
            Thread.sleep(3000);
        }catch (InterruptedException ie){
            System.out.println(ie.toString());
        }
        //change the theme from launch to background app
        setTheme(R.style.AppTheme);
        //call parent constructor
        super.onCreate(savedInstanceState);

        //load the xml file
        setContentView(R.layout.activity_main);
        //request for permission camera
        ActivityCompat.requestPermissions(MainActivity.this,
                new String[]{Manifest.permission.CAMERA,Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE},
                1);

        if(Globals.listItemsGlobal.isEmpty()) {
            loadEventList();
        }

        //move to GuestActivity1 by click the button of "Enter Event"
        findViewById(R.id.int_event).setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this ,GuestActivity1.class);
            startActivity(intent);
        });

        findViewById(R.id.to_register).setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this ,OwnerActivity.class);
            startActivity(intent);
        });
    }

    private void loadEventList(){
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference().child("Event");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds : dataSnapshot.getChildren()){
                    String eventType = ds.child("eventType").getValue().toString();
                    String name = ds.child("name").getValue().toString();
                    hall = ds.child("event").getValue(Hall.class);
                    Globals.listItemsGlobal.add(eventType + " של " + name);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
