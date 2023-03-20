package com.example.photography.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;
//import android.support.annotation.NonNull;
//import android.support.v7.app.AppCompatActivity;
//import android.support.v7.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.photography.R;
import com.example.photography.utils.Menus;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = LoginActivity.class.getName();
    private EditText email;
    private EditText password;

    private FirebaseAuth mAuth;

    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        toolbar = findViewById(R.id.ali_toolbar);
        toolbar.setTitle("PHOTOGRAPHY");
        setSupportActionBar(toolbar);

        mAuth = FirebaseAuth.getInstance();

        email = findViewById(R.id.et_enter_account1);
        password = findViewById(R.id.et_enter_account2);

        findViewById(R.id.link_enter_account).setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        });

        findViewById(R.id.btn_enter_account).setOnClickListener(v -> {
            String eml = email.getText().toString();
            String pswd = password.getText().toString();

            if(eml.isEmpty()){
                email.setError("שדה 'דואר אלקטרוני' לא יכול להיות ריק.");
                return;
            }

            if(pswd.isEmpty()){
                password.setError("שדה 'סיסמה' לא יכול להיות ריק.");
                return;
            }

            if(!Patterns.EMAIL_ADDRESS.matcher(eml).matches()){
                email.setError("כתובת 'דואר אלקטרוני' לא חוקית.");
                return;
            }

            mAuth.signInWithEmailAndPassword(eml, pswd)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d(TAG, "signInWithEmail:success");
                                FirebaseUser user = mAuth.getCurrentUser();
                                updateUI(user);
                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w(TAG, "signInWithEmail:failure", task.getException());
                                Toast.makeText(LoginActivity.this, "שם משתמש ו/או סיסמה שגויים.",
                                        Toast.LENGTH_SHORT).show();
                                updateUI(null);
                            }

                            // ...
                        }
                    });

        });

    }

    private void updateUI(FirebaseUser currentUser) {
        if(currentUser != null) {
            Intent intent = new Intent(LoginActivity.this, OwnerActivity.class);
            intent.putExtra("user", currentUser);
            startActivity(intent);
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
