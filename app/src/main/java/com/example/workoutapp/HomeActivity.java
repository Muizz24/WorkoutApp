package com.example.workoutapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class HomeActivity extends AppCompatActivity {

    private EditText email, password;
    private Button login;
    private TextView userRegistration;
    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);

        FirebaseUser user = firebaseAuth.getCurrentUser();

        if(user != null){
            finish();
            Toast.makeText(HomeActivity.this, "Found a user!", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(HomeActivity.this, Hub2Activity.class));
        }

        email = (EditText)findViewById(R.id.etEmail);
        password = (EditText)findViewById(R.id.etPassword);
        login = (Button)findViewById(R.id.btnLogin);
        userRegistration = (TextView) findViewById(R.id.tvRegister);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validate(email.getText().toString().trim(), password.getText().toString());
            }
        });

        userRegistration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                startActivity(new Intent(HomeActivity.this, RegActivity.class));
            }
        });

    }

    private void validate(String email, String userPassword){

        progressDialog.setMessage("Logging in");
        progressDialog.show();

        if (email.equals("") || userPassword.equals("")) {
            Toast.makeText(HomeActivity.this, "Login Failed", Toast.LENGTH_SHORT).show();
        } else {
            firebaseAuth.signInWithEmailAndPassword(email, userPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        progressDialog.dismiss();
                        Toast.makeText(HomeActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                        finish();
                        startActivity(new Intent(HomeActivity.this, Hub2Activity.class));
                        //checkEmailVerification();
                    } else {
                        Toast.makeText(HomeActivity.this, "Login Failed", Toast.LENGTH_SHORT).show();

                        progressDialog.dismiss();
                    }
                }
            });
        }
    }
}
