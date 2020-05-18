package com.example.photography;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.Toast;

import com.example.photography.database.Event;
import com.example.photography.database.Hall;
import com.example.photography.utils.Globals;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class OwnerActivity1 extends AppCompatActivity {

    private EditText etName;
    private EditText etEventType;
    private EditText etHallName;
    private EditText etStreet;
    private EditText etNumber;
    private EditText etCity;

    // Write a message to the database
    private FirebaseDatabase database;
    private DatabaseReference myRef;
    private Event event;
    private Hall hall;

    private long maxId=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //change the theme from launch to background app
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_owner1);

        etName = findViewById(R.id.editTextName_owner1);
        etEventType = findViewById(R.id.editTextEventType_owner1);
        etHallName = findViewById(R.id.editTextHallName_owner1);
        etStreet = findViewById(R.id.editTextStreet_owner1);
        etNumber = findViewById(R.id.editTextStreetNumber_owner1);
        etCity = findViewById(R.id.editTextCityName_owner1);
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference().child("Event");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    maxId = dataSnapshot.getChildrenCount();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        findViewById(R.id.buttonCreateEvent_owner1).setOnClickListener(v -> {
            if(TextUtils.isEmpty(etName.getText()) || TextUtils.isEmpty(etEventType.getText()) || TextUtils.isEmpty(etHallName.getText())
                    || TextUtils.isEmpty(etStreet.getText()) || TextUtils.isEmpty(etNumber.getText()) || TextUtils.isEmpty(etCity.getText())){
                Toast.makeText(this, "אחד או יותר מהשדות ריקים", Toast.LENGTH_SHORT).show();
                return;
            }else {
                hall = new Hall(etHallName.getText().toString(), etStreet.getText().toString(), Integer.parseInt(etNumber.getText().toString()), etCity.getText().toString());
                event = new Event(etName.getText().toString(), etEventType.getText().toString(), hall);
                myRef.child(String.valueOf(maxId+1)).setValue(event);
                Globals.listItemsGlobal.add(etEventType.getText() + " של " + etName.getText());
                Intent intent = new Intent(OwnerActivity1.this, OwnerActivity2.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
