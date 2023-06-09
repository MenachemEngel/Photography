package com.example.photography.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.photography.dialogs.EventIdDialog;
import com.example.photography.R;
import com.example.photography.utils.Globals;

import java.util.ArrayList;

//Guest activity1 class (activity)
public class GuestActivity1 extends AppCompatActivity {

    //variables section
    //list view that hold the events
    private ListView guestListView;
    //events names example
    private String str,str1,str2,str3;
    //list of events names
    ArrayList<String> listItems = new ArrayList<>();
    //adapter array list to list view
    ArrayAdapter<String> adapter;

    //onCreate function that create the activity
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //change the theme from launch to background app
        setTheme(R.style.AppTheme);
        //call parent constructor
        super.onCreate(savedInstanceState);
        //load the xml file
        setContentView(R.layout.activity_guest1);



        //find the list view from xml by id and connect it to the variable guestListView
        guestListView = findViewById(R.id.guest_list);

        //init the adapter
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, Globals.listItemsGlobal);
        //set adapter to the list view
        guestListView.setAdapter(adapter);

        //load the changes
        adapter.notifyDataSetChanged();


        //listener to list view for password
        guestListView.setOnItemClickListener(new  AdapterView.OnItemClickListener() {
             //open the dialog for event
            @Override
            public void  onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //declaration  EventIdDialog and show it
                EventIdDialog dialog = new EventIdDialog();
                dialog.show(getSupportFragmentManager() ,"tag");
            }
        });
    }

}
