package com.example.photography.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.photography.R;
import com.example.photography.database.Event;
import com.example.photography.database.Hall;
import com.example.photography.utils.Globals;
import com.example.photography.utils.Menus;
import com.example.photography.utils.Utilities;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class OwnerActivity1 extends AppCompatActivity {

    private static final String TAG = "OwnerActivity1";

    private EditText etName;
    private EditText etEventType;
    private EditText etHallName;
    private EditText etStreet;
    private EditText etNumber;
    private EditText etCity;
    private TextView tvDate;

    private DatePickerDialog dpd;
    private int dy, dm, dd;

    // Write a message to the database
    private FirebaseDatabase database;
    private DatabaseReference myRefHell;
    private DatabaseReference myRefEvent;
    private Event event;
    private Hall hall;

    private long createEventButtonTime = 0;

    private FirebaseUser firebaseUser;

    private Toolbar toolbar;

    List<Hall> hallList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //change the theme from launch to background app
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_owner1);

        toolbar = findViewById(R.id.ao1_toolbar);
        toolbar.setTitle(R.string.app_name);
        setSupportActionBar(toolbar);

        firebaseUser = getIntent().getParcelableExtra("user");
        Log.v(TAG, firebaseUser.getEmail());

        hallList = new ArrayList<>();
        myRefHell = FirebaseDatabase.getInstance().getReference().child("Hall");
        myRefHell.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Hall h = snapshot.getValue(Hall.class);
                    hallList.add(h);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        etName = findViewById(R.id.editTextName_owner1);
        etEventType = findViewById(R.id.editTextEventType_owner1);
        etHallName = findViewById(R.id.editTextHallName_owner1);
        etStreet = findViewById(R.id.editTextStreet_owner1);
        etNumber = findViewById(R.id.editTextStreetNumber_owner1);
        etCity = findViewById(R.id.editTextCityName_owner1);
        tvDate = findViewById(R.id.datePicker1);
        database = FirebaseDatabase.getInstance();
        myRefEvent = database.getReference().child("Event");



        tvDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR);
                int mMonth = c.get(Calendar.MONTH);
                int mDay = c.get(Calendar.DAY_OF_MONTH);
                dpd = new DatePickerDialog(OwnerActivity1.this, R.style.DatePickerTheme, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int y, int m, int d) {
                        dy = y;
                        dm = m+1;
                        dd = d;
                        tvDate.setText(d+"/"+(m+1)+"/"+y);
                    }
                }, mYear, mMonth, mDay);
                dpd.show();
            }
        });

        findViewById(R.id.button_halls_owner1).setOnClickListener(v -> {
            String [] hallArray = new String[hallList.size()];
            for(int i = 0; i < hallList.size(); i++){
                hallArray[i] = hallList.get(i).getName() + " - " + hallList.get(i).getCity();
            }
            AlertDialog.Builder builder = new AlertDialog.Builder(OwnerActivity1.this, android.R.style.ThemeOverlay_Material_Dialog_Alert);
            if(hallArray.length != 0) {
                builder.setTitle(R.string.halls_list)
                        .setItems(hallArray, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                etHallName.setText(hallList.get(i).getName());
                                etStreet.setText(hallList.get(i).getStreet());
                                etNumber.setText(String.valueOf(hallList.get(i).getNumber()));
                                etCity.setText(hallList.get(i).getCity());
                            }
                        });
            }
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        });

        findViewById(R.id.buttonCreateEvent_owner1).setOnClickListener(v -> {
            if((SystemClock.elapsedRealtime() - createEventButtonTime) < 3000){
                return;
            }
            createEventButtonTime = SystemClock.elapsedRealtime();
            if(etName.getText().toString().isEmpty()){
                etName.setError("שדה 'שם אירוע' לא יכול להיות ריק.");
                return;
            }
            if(etEventType.getText().toString().isEmpty()){
                etEventType.setError("שדה 'סוג אירוע' לא יכול להיות ריק.");
                return;
            }
            if(etHallName.getText().toString().isEmpty()){
                etHallName.setError("שדה 'שם אולם' לא יכול להיות ריק.");
                return;
            }
            if(etStreet.getText().toString().isEmpty()){
                etStreet.setError("שדה 'רחוב' לא יכול להיות ריק.");
                return;
            }
            if(etNumber.getText().toString().isEmpty()){
                etNumber.setError("שדה 'מספר' לא יכול להיות ריק.");
                return;
            }
            if(etCity.getText().toString().isEmpty()){
                etCity.setError("שדה 'עיר' לא יכול להיות ריק.");
                return;
            }
            if(!checkTheDate(dy, dm, dd)){
                Toast.makeText(this, "תאריך לא חוקי", Toast.LENGTH_SHORT).show();
                return;
            }else{
                try {
                    addEvent();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        createEventButtonTime = SystemClock.elapsedRealtime();
    }

    private void addEvent() throws IOException {
        Date date = new Date(dy, dm, dd);
        hall = new Hall(etHallName.getText().toString(), etStreet.getText().toString(), Integer.parseInt(etNumber.getText().toString()), etCity.getText().toString());
        event = new Event(etName.getText().toString(), etEventType.getText().toString(), hall, date, firebaseUser.getUid());
        myRefEvent.child(String.valueOf(event.getId())).setValue(event);
        myRefHell.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(!dataSnapshot.hasChild(String.valueOf(hall.getId()))){
                    myRefHell.child(String.valueOf(hall.getId())).setValue(hall);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        Globals.listItemsGlobal.add(etEventType.getText() + " של " + etName.getText());
        Utilities.createPoster(this, Utilities.getOrCreateFolder(event.getName()+"-"+event.getId()),event.getName()+"\n\n"+"מזהה אירוע"+"\n"+event.getId());
        Intent intent = new Intent(OwnerActivity1.this, OwnerActivity2.class);
        Gson gson = new Gson();
        String eventJSON = gson.toJson(event);
        intent.putExtra("event", eventJSON);
        startActivity(intent);
        finish();
    }

    public boolean checkTheDate(int y, int m, int d) {
        int year = Calendar.getInstance().get(Calendar.YEAR);
        int month = Calendar.getInstance().get(Calendar.MONTH)+1;
        int day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
        Log.v(TAG, year + "<=>" + y + " " + month + "<=>" + m + " " + day + "<=>" + d);
        if (y < year) {
            return false;
        } else if (m < month && y == year) {
            return false;
        } else if (d < day && m == month && y == year){
            return false;
        }else {
            return true;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.toolbar_regester_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Menus.registerMenu(this,item, firebaseUser.getEmail());
        return true;
    }

}
