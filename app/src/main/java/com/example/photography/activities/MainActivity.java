package com.example.photography.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
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

//Main activity class (activity)
public class MainActivity extends AppCompatActivity {

    //database variables
    private FirebaseDatabase database;
    private DatabaseReference myRef;
    private Event event;
    private Hall hall;

    private EditText det;

    private FirebaseAuth mAuth;

    private Toolbar toolbar;

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

        Utilities.getOrCreateAppFolder();

        toolbar = findViewById(R.id.am_toolbar);
        toolbar.setTitle("PHOTOGRAPHY");
        setSupportActionBar(toolbar);

        mAuth = FirebaseAuth.getInstance();

        //request for permission of camera
        ActivityCompat.requestPermissions(MainActivity.this,
                new String[]{Manifest.permission.CAMERA,Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE},
                1);

        if(Globals.listItemsGlobal.isEmpty()) {
            loadEventList();
        }

        findViewById(R.id.int_event).setOnClickListener(v -> {

            //build the alert dialog
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this, android.R.style.ThemeOverlay_Material_Dialog_Alert);

            //get the layout inflater
            LayoutInflater inflater = LayoutInflater.from(builder.getContext());

            View dialogView = inflater.inflate(R.layout.event_id_dialog,null);

            det = dialogView.findViewById(R.id.dialogEditText);
            myRef = FirebaseDatabase.getInstance().getReference().child("Event");

            //connect the view xml with the builder and set the buttons
            builder.setView(dialogView)
                    //set continue button
                    .setPositiveButton(R.string.enter_event, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                            myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    if(TextUtils.isEmpty(det.getText())){
                                        det.setError("מזהה אירוע לא יכול להיות ריק.");
                                        Toast.makeText(MainActivity.this, "מזהה אירוע לא יכול להיות ריק.", Toast.LENGTH_SHORT).show();
                                        return;
                                    }
                                    if(!dataSnapshot.child(det.getText().toString()).exists()){
                                        det.setError("לא נמצא אירוע.");
                                        Toast.makeText(MainActivity.this, "לא נמצא אירוע.", Toast.LENGTH_SHORT).show();
                                        return;
                                    }else {
                                        event  = dataSnapshot.child(det.getText().toString()).getValue(Event.class);
                                        Intent intent = new Intent(MainActivity.this, GuestActivity2.class);
                                        Gson gson = new Gson();
                                        String eventJSON = gson.toJson(event);
                                        intent.putExtra("event", eventJSON);
                                        startActivity(intent);
                                        dialogInterface.dismiss();
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                            //move to GuestActivity2 by click on event
                        }
                    })
                    //set cancel button
                    .setNegativeButton(R.string.back, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    });
            /*//return the builder and create dialog
            return builder.create();
*/          AlertDialog alertDialog = builder.create();
            alertDialog.show();

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            params.setMargins(0,0,0,15);

            alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setBackgroundResource(R.drawable.button_selector);
            alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.black));
            alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setLayoutParams(params);
            LinearLayout.LayoutParams lpp = (LinearLayout.LayoutParams) alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).getLayoutParams();
            lpp.gravity = Gravity.CENTER;
            alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setBackgroundResource(R.drawable.button_selector);
            alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.black));
            LinearLayout.LayoutParams lpn = (LinearLayout.LayoutParams) alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).getLayoutParams();
            lpn.gravity = Gravity.CENTER;

        });

        findViewById(R.id.to_register).setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this , RegisterActivity.class);
            startActivity(intent);
        });
    }

    private void loadEventList(){
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference().child("Event");
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
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

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }

    private void updateUI(FirebaseUser currentUser) {
        if(currentUser != null) {
            Intent intent = new Intent(MainActivity.this, OwnerActivity.class);
            intent.putExtra("user", currentUser);
            startActivity(intent);
            finish();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.toolbar_guest_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Menus.unregisterMenu(this,item);
        return true;
    }

    @Override
    public void onBackPressed() {
        System.exit(0);
        return;
    }
}
