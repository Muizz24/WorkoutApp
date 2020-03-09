package com.example.workoutapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.workoutapp.data.model.Level;
import com.example.workoutapp.data.model.Workout;
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

public class RegActivity extends AppCompatActivity {
    public static final String EXTRA_TEXT = "com.example.workoutapp.EXTRA_TEXT";
    private EditText userName, userEmail, userPassword;
    private Button regButton;
    private FirebaseAuth firebaseAuth;
    final FirebaseDatabase database = FirebaseDatabase.getInstance();
    public DatabaseReference dbrefLevel;
    //private StorageReference storageReference;
    //private FirebaseStorage firebaseStorage;
    private ImageView userProfilePic;
    String email, name, age, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg);
        setupUIViews();

        firebaseAuth = FirebaseAuth.getInstance();

        regButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validate()) {
                    //Upload data to the database
                    final String user_email = userEmail.getText().toString().trim();
                    final String user_password = userPassword.getText().toString().trim();

                    firebaseAuth.createUserWithEmailAndPassword(user_email, user_password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if(task.isSuccessful()){
                                //sendEmailVerification();
                                //sendUserData();
                                //firebaseAuth.signOut();
                                Toast.makeText(RegActivity.this, "Successfully Registered, Upload complete!", Toast.LENGTH_SHORT).show();
                                FirebaseUser user = firebaseAuth.getCurrentUser();
                                String owner = user.getEmail().replaceAll("[^0-9a-zA-Z]","");
                                dbrefLevel = database.getReference(owner + "/level");
                                // Create the first lvl for the user
                                Level user_lvl = new Level(1, 0, 50);

                                dbrefLevel.setValue(user_lvl);
                                finish();
                                Intent intent = new Intent(RegActivity.this, Setup_UserInfo_activity.class);
                                intent.putExtra(EXTRA_TEXT, name);
                                startActivity(intent);
                            }else{
                                Toast.makeText(RegActivity.this, "Registration Failed, email:" + user_email + "pass:" + user_password, Toast.LENGTH_SHORT).show();
                            }

                        }
                    });
                }
            }
        });
    }

    private Boolean validate(){
        Boolean result = false;

        name = userName.getText().toString();
        password = userPassword.getText().toString();
        email = userEmail.getText().toString();
        //age = userAge.getText().toString();


        if(name.isEmpty() || password.isEmpty() || email.isEmpty()){
            Toast.makeText(this, "Please enter all the details", Toast.LENGTH_SHORT).show();
        }else{
            result = true;
        }

        return result;
    }

    private void setupUIViews(){
        userName = (EditText)findViewById(R.id.etUsername);
        userPassword = (EditText)findViewById(R.id.etPassword);
        userEmail = (EditText)findViewById(R.id.etEmail);
        regButton = (Button)findViewById(R.id.btnRegister);
        //userLogin = (TextView)findViewById(R.id.tvUserLogin);
        //userAge = (EditText)findViewById(R.id.etAge);
        //userProfilePic = (ImageView)findViewById(R.id.ivProfile);
    }

//    private void sendUserData(){
//        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
//        DatabaseReference myRef = firebaseDatabase.getReference(firebaseAuth.getUid());
//        StorageReference imageReference = storageReference.child(firebaseAuth.getUid()).child("Images").child("Profile Pic");  //User id/Images/Profile Pic.jpg
//        UploadTask uploadTask = imageReference.putFile(imagePath);
//        uploadTask.addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//                Toast.makeText(RegistrationActivity.this, "Upload failed!", Toast.LENGTH_SHORT).show();
//            }
//        }).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
//                Toast.makeText(RegistrationActivity.this, "Upload successful!", Toast.LENGTH_SHORT).show();
//            }
//        });
//        UserProfile userProfile = new UserProfile(age, email, name);
//        myRef.setValue(userProfile);
//    }
}
