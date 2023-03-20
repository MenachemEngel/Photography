package com.example.photography.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.example.photography.R;
import com.example.photography.database.User;
import com.example.photography.utils.Menus;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class RegisterActivity extends AppCompatActivity {

    private static final String TAG = RegisterActivity.class.getName();
    private EditText firstName;
    private EditText lastName;
    private EditText email;
    private EditText password;

    private FirebaseAuth mAuth;

    private DatabaseReference ref;

    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //change the theme from launch to background app
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        toolbar = findViewById(R.id.reg_toolbar);
        toolbar.setTitle("PHOTOGRAPHY");
        setSupportActionBar(toolbar);

        firstName = findViewById(R.id.reg_fn);
        lastName = findViewById(R.id.reg_ln);
        email = findViewById(R.id.reg_email);
        password = findViewById(R.id.reg_pwrd);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        ref = FirebaseDatabase.getInstance().getReference().child("Users");
        /*ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });*/

        findViewById(R.id.reg_link).setOnClickListener(v -> {
            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
            startActivity(intent);
        });

        findViewById(R.id.reg_btn).setOnClickListener(v -> {
            String fName = firstName.getText().toString();
            String lName = lastName.getText().toString();
            String eml = email.getText().toString();
            String pswd = password.getText().toString();

            if(fName.isEmpty()){
                firstName.setError("שדה 'שם פרטי' לא יכול להיות ריק.");
                return;
            }

            if(lName.isEmpty()){
                lastName.setError("שדה 'שם משפחה' לא יכול להיות ריק.");
                return;
            }

            if(eml.isEmpty()){
                email.setError("שדה 'דואר אלקטרוני' לא יכול להיות ריק.");
                return;
            }

            if(pswd.isEmpty()){
                password.setError("שדה 'סיסמה' לא יכול להיות ריק.");
                return;
            }

            if(pswd.length() < 6){
                password.setError("הסיסמה חייבית להכיל לפחות 6 תווים.");
                return;
            }

            if(!Patterns.EMAIL_ADDRESS.matcher(eml).matches()){
                email.setError("כתובת 'דואר אלקטרוני' לא חוקית.");
                return;
            }

            mAuth.createUserWithEmailAndPassword(eml, pswd)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d(TAG, "createUserWithEmail:success");
                                FirebaseUser user = mAuth.getCurrentUser();
                                User userData = new User(fName, lName, eml);
                                ref.child(user.getUid()).setValue(userData);
                                updateUI(user);
                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                Toast.makeText(RegisterActivity.this, "הרשמה נכשלה\nודא שיש לך חיבור אינטרנט פעיל.",
                                        Toast.LENGTH_SHORT).show();
                                updateUI(null);
                            }
                        }
                    });
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.v(TAG, "ON_RESUME");
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.v(TAG, "ON_RESTART");
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }

    private void updateUI(FirebaseUser currentUser) {
        if(currentUser != null) {
            Intent intent = new Intent(RegisterActivity.this, OwnerActivity.class);
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

}
