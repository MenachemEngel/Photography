package com.example.photography.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;

import com.example.photography.R;
import com.example.photography.utils.EmailSender;
import com.google.firebase.auth.FirebaseUser;

public class EmailActivity extends AppCompatActivity {

    Toolbar toolbar;

    EditText subject,body;

    String email;

    String back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email);

        toolbar = findViewById(R.id.aem_toolbar);
        toolbar.setLogo(R.drawable.logo_toolbar);
        toolbar.setTitle(" צור קשר");
        setSupportActionBar(toolbar);

        email = getIntent().getStringExtra("email");

        back = "\nאימייל לתשובה: " + email;

        subject = findViewById(R.id.email_subject_et);
        body = findViewById(R.id.email_body_et);

        findViewById(R.id.email_send_btn).setOnClickListener(v -> {
            if(TextUtils.isEmpty(subject.getText())){
                subject.setError("כותרת ההודעה לא יכולה להיות ריקה");
                return;
            }
            if(TextUtils.isEmpty(body.getText())){
                subject.setError("גוף ההודעה לא יכול להיות ריק");
                return;
            }
            EmailSender.sendMail(this, subject.getText().toString(), body.getText().toString() + back);
        });

    }
}
