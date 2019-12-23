package com.example.photography;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class GuestActivity1 extends AppCompatActivity {

    private ListView guestListView;
    private TextView tv;
    private String str,str1;

    ArrayList<String> listItems = new ArrayList<>();
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guest1);

        guestListView = findViewById(R.id.guest_list);

        //str = "Hello";
        //str1 = "World";

        String str2 = "ori";
        String str3 = "gri";

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listItems);
        guestListView.setAdapter(adapter);

        //listItems.add(str);
        //listItems.add(str1);

        adapter.notifyDataSetChanged();
    }


}
