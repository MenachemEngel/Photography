package com.example.photography.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
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
import com.example.photography.utils.Menus;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;


public class OwnerActivity extends AppCompatActivity {

    private final String TAG = OwnerActivity.class.getName();

    private Toolbar toolbar;

    private FirebaseUser user;
    private FirebaseAuth mAuth;

    private DatabaseReference myRef;
    private Event event;
    private EditText det;

    private LinearLayout.LayoutParams params;

    private List<Event> eventList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //change the theme from launch to background app
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_owner);

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();//getIntent().getParcelableExtra("user");

        toolbar = findViewById(R.id.ao_toolbar);
        toolbar.setLogo(R.drawable.logo_toolbar);

        params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(0,5,0,15);

        myRef = FirebaseDatabase.getInstance().getReference().child("Users");
        myRef.child(user.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                toolbar.setTitle(dataSnapshot.child("firstName").getValue() + " " + dataSnapshot.child("lastNAme").getValue());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        setSupportActionBar(toolbar);

        eventList = new ArrayList<>();
        myRef = FirebaseDatabase.getInstance().getReference().child("Event");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Event e = snapshot.getValue(Event.class);
                    if(e.getOwner().equals(user.getUid())){
                        Log.v(TAG, e.getOwner());
                        Log.v(TAG, e.getName());
                        Log.v(TAG, user.getUid());
                        eventList.add(e);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        //FirebaseUser user = getIntent().getParcelableExtra("user");

        //move to GuestActivity1 by click the button of "Enter Event"
        findViewById(R.id.int_event1).setOnClickListener(v -> {

            //build the alert dialog
            AlertDialog.Builder builder = new AlertDialog.Builder(OwnerActivity.this, android.R.style.ThemeOverlay_Material_Dialog_Alert);

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
                                        Toast.makeText(OwnerActivity.this, "מזהה אירוע לא יכול להיות ריק.", Toast.LENGTH_SHORT).show();
                                        return;
                                    }
                                    if(!dataSnapshot.child(det.getText().toString()).exists()){
                                        det.setError("לא נמצא אירוע.");
                                        Toast.makeText(OwnerActivity.this, "לא נמצא אירוע.", Toast.LENGTH_SHORT).show();
                                        return;
                                    }else {
                                        event  = dataSnapshot.child(det.getText().toString()).getValue(Event.class);
                                        Log.v(TAG, event.getName());
                                                /*new Event(dataSnapshot.child(det.getText().toString()).child("name").getValue(),
                                                dataSnapshot.child(det.getText().toString()).child("eventType").getValue(),
                                                new Hall(dataSnapshot.child(det.getText().toString()).child("hall").child("name").getValue(),
                                                        dataSnapshot.child(det.getText().toString()).child("hall").child("")));*/

                                        Intent intent;
                                        if(event.getOwner().equals(user.getUid())) {
                                            intent = new Intent(OwnerActivity.this, OwnerActivity2.class);
                                        }else{
                                            intent = new Intent(OwnerActivity.this, GuestActivity2.class);
                                        }
                                        Gson gson = new Gson();
                                        String eventJSON = gson.toJson(event);
                                        intent.putExtra("event", eventJSON);
                                        intent.putExtra("user", user);
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

        findViewById(R.id.cre_event1).setOnClickListener(v -> {
            Intent intent = new Intent(OwnerActivity.this , OwnerActivity1.class);
            Log.v(TAG, user.getEmail());
            intent.putExtra("user", user);
            startActivity(intent);
        });

        findViewById(R.id.my_events).setOnClickListener(v -> {
            Log.v(TAG, "Events length "+eventList.size());
            String [] eventArray = new String[eventList.size()];
            for(int i = 0; i < eventList.size(); i++){
                eventArray[i] = eventList.get(i).getName();
            }
            AlertDialog.Builder builder = new AlertDialog.Builder(OwnerActivity.this, android.R.style.ThemeOverlay_Material_Dialog_Alert);
            if(eventArray.length != 0) {
                builder.setTitle(R.string.my_events)
                        .setItems(eventArray, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Intent intent = new Intent(OwnerActivity.this, OwnerActivity2.class);
                                Gson gson = new Gson();
                                String eventJSON = gson.toJson(eventList.get(i));
                                intent.putExtra("event", eventJSON);
                                intent.putExtra("user", user);
                                startActivity(intent);
                            }
                        });
            }else{
                builder.setTitle(R.string.my_events)
                        .setMessage("עדיין לא יצרת אירועים.")
                        .setPositiveButton("הבנתי", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                            }
                        });
            }
            AlertDialog alertDialog = builder.create();
            alertDialog.show();

            alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setBackgroundResource(R.drawable.button_selector);
            alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.black));
            //alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setLayoutParams(params);
            LinearLayout.LayoutParams lpp = (LinearLayout.LayoutParams) alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).getLayoutParams();
            lpp.gravity = Gravity.CENTER;

        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.toolbar_regester_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Menus.registerMenu(this,item, user.getEmail());
        return true;
    }
}
