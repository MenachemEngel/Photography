package com.example.photography;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

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
        //call parent constructor
        super.onCreate(savedInstanceState);
        //load the xml file
        setContentView(R.layout.activity_guest1);

        //find the list view from xml by id to the variable guestListView
        guestListView = findViewById(R.id.guest_list);

        //events names example
        str = "Hello";
        str1 = "World";
        str2 = "Menachem David Engel";
        str3 = "elchanan madmon";

        //init the adapter
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listItems);
        //set adapter to the list view
        guestListView.setAdapter(adapter);

        //add names event to listItems
        listItems.add(str);
        listItems.add(str1);
        listItems.add(str2);
        listItems.add(str3);

        //load the changes
        adapter.notifyDataSetChanged();

        //listener to list view for password
        guestListView.setOnItemClickListener(new  AdapterView.OnItemClickListener() {
             //open the dialog for event
            @Override
            public void  onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //declaration  EventIdDialog and show it
                EventIdDialog  dialog = new EventIdDialog();
                dialog.show(getSupportFragmentManager() ,"tag");
            }
        });
    }

            //Trying commit
}
