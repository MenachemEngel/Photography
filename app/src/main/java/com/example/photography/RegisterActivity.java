package com.example.photography;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.example.photography.database.User;

import org.mindrot.jbcrypt.BCrypt;

public class RegisterActivity extends AppCompatActivity {

    private EditText firstName;
    private EditText lastName;
    private EditText email;
    private EditText password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //change the theme from launch to background app
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        firstName = findViewById(R.id.reg_fn);
        lastName = findViewById(R.id.reg_ln);
        email = findViewById(R.id.reg_email);
        password = findViewById(R.id.reg_pwrd);

        findViewById(R.id.reg_btn).setOnClickListener(v -> {
            User user = new User(firstName.getText().toString(),
                    lastName.getText().toString(),
                    email.getText().toString(),
                    password.getText().toString());
        });

    }
//    // Hash a password
//    String hashed = BCrypt.hashpw(password, BCrypt.gensalt());
//
//    // Check that an unencrypted password matches or not
//    if (BCrypt.checkpw(candidate, hashed))
//            System.out.println("It matches");
//    else
//        System.out.println("It does not match");
}
