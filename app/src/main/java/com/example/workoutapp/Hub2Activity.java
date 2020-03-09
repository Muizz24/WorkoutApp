package com.example.workoutapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

public class Hub2Activity extends AppCompatActivity {
    private Button signout, mastery, settings;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hub2);
        setupUIViews();

        firebaseAuth = FirebaseAuth.getInstance();

        signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firebaseAuth.signOut();
                finish();
                startActivity(new Intent(Hub2Activity.this, HomeActivity.class));
            }
        });

        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Hub2Activity.this, UserSettings.class));
            }
        });

        mastery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Hub2Activity.this, MasteryActivity.class));
            }
        });
    }

    private void setupUIViews(){
        //userName = (EditText)findViewById(R.id.etUsername);
        //userPassword = (EditText)findViewById(R.id.etPassword);
        signout = (Button)findViewById(R.id.btnSignOut);
        mastery = (Button)findViewById(R.id.btnMastery);
        settings = (Button)findViewById(R.id.btnSettings);
        //userLogin = (TextView)findViewById(R.id.tvUserLogin);
        //userAge = (EditText)findViewById(R.id.etAge);
        //userProfilePic = (ImageView)findViewById(R.id.ivProfile);
    }
}
