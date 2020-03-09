package com.example.workoutapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.workoutapp.data.model.Workout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FillDetailsActivity extends AppCompatActivity {
    private EditText numSets, numReps, pounds;
    private TextView title;
    private Button submit;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseWorkouts;
    private String musclePart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fill_details);
        Intent intent = getIntent();
        musclePart = intent.getStringExtra(MasteryActivity.EXTRA_TEXT);
        setupUIViews();

        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();
        String owner = user.getEmail().replaceAll("[^0-9a-zA-Z]","");
        databaseWorkouts = FirebaseDatabase.getInstance().getReference(owner + "/workouts");

        title.setText("Add Mastery to your " + musclePart + "!");

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addWorkoutMastery(musclePart);
                finish();
            }
        });
    }

    private void setupUIViews(){
        numSets = (EditText) findViewById(R.id.etSets);
        numReps = (EditText) findViewById(R.id.etReps);
        pounds = (EditText) findViewById(R.id.etWeight);
        title = (TextView) findViewById(R.id.tvUpdateMastery);
        submit = (Button) findViewById(R.id.btnSubmitUserDetails);
     }

     private void addWorkoutMastery(String musclePart){
        String sets, reps, weight;
        sets = numSets.getText().toString().replaceAll("[^0-9.]", "");
        reps = numReps.getText().toString().replaceAll("[^0-9.]", "");
        weight = pounds.getText().toString().replaceAll("[^0-9.]", "");
        if (sets.equals("") || reps.equals("") || weight.equals("")){
            Toast.makeText(FillDetailsActivity.this, "Please input numbers", Toast.LENGTH_SHORT).show();
        } else {
            String id = databaseWorkouts.push().getKey();

            Workout workout = new Workout(Double.valueOf(sets),
                    Double.valueOf(reps), Double.valueOf(weight), musclePart);

            databaseWorkouts.child(id).setValue(workout);

            Double points = Double.valueOf(sets) * Double.valueOf(reps) * Double.valueOf(weight);

            Toast.makeText(this, "" + points + " have been awarded for your " + musclePart, Toast.LENGTH_SHORT).show();
        }

     }
}
