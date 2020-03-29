package com.example.photography;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.Toast;

import com.example.photography.utils.Globals;

public class OwnerActivity extends AppCompatActivity {

    public EditText etName;
    public EditText etEvent;
    public EditText etLocation;
    public EditText etStreet;
    public EditText etNumber;
    public EditText etCity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //change the theme from launch to background app
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_owner);

        etName = findViewById(R.id.editTextName_owner1);
        etEvent=findViewById(R.id.editTextEventType_owner1);
        etLocation=findViewById(R.id.editTextHallName_owner1);
        etStreet=findViewById(R.id.editTextStreet_owner1);
        etNumber=findViewById(R.id.editTextStreetNumber_owner1);
        etCity=findViewById(R.id.editTextCityName_owner1);

        findViewById(R.id.buttonCreateEvent_owner1).setOnClickListener(v -> {
            if(TextUtils.isEmpty(etName.getText()) || TextUtils.isEmpty(etEvent.getText()) || TextUtils.isEmpty(etLocation.getText())
                    || TextUtils.isEmpty(etStreet.getText()) || TextUtils.isEmpty(etNumber.getText()) || TextUtils.isEmpty(etCity.getText())){
                Toast.makeText(this, "אחד או יותר מהשדות ריקים", Toast.LENGTH_SHORT).show();
                return;
            }else {
                Globals.listItemsGlobal.add(etEvent.getText() + " של " + etName.getText());
                Intent intent = new Intent(OwnerActivity.this, OwnerActivity2.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
