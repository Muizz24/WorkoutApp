package com.example.workoutapp;

import android.animation.ValueAnimator;
import android.content.Intent;
import android.os.Bundle;

import com.example.workoutapp.data.model.Level;
import com.example.workoutapp.data.model.Workout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.renderscript.Sampler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.text.NumberFormat;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class MasteryActivity extends AppCompatActivity {
    private TextView biceps,chest,shoulders,back,glutes,abs,legs,triceps, lvl, lvlProg;
    private TextView[] listItems;
    public static final String EXTRA_TEXT = "com.example.workoutapp.EXTRA_TEXT";
    final FirebaseDatabase database = FirebaseDatabase.getInstance();
    public Double bicepsPts, chestPts, shouldersPts, backPts, absPts, glutePts, legsPts, tricepsPts;
    private FirebaseAuth firebaseAuth;
    public DatabaseReference dbrefWorkouts; public DatabaseReference dbrefLevel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mastery);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setupUIViews();

        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();
        String owner = user.getEmail().replaceAll("[^0-9a-zA-Z]","");
        dbrefWorkouts = database.getReference(owner + "/workouts");
        dbrefLevel = database.getReference(owner + "/level");


        biceps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFillInDetails("Biceps");
            }
        });

        chest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFillInDetails("Chest");
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFillInDetails("Back");
            }
        });

        shoulders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFillInDetails("Shoulders");
            }
        });

        glutes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFillInDetails("Glutes");
            }
        });

        abs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFillInDetails("Abs");
            }
        });

        legs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFillInDetails("Legs");
            }
        });

        triceps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFillInDetails("Triceps");
            }
        });
    }

    @Override
    protected void onStart(){
        super.onStart();
        bicepsPts = 0.0; chestPts = 0.0; backPts = 0.0; shouldersPts = 0.0; legsPts = 0.0;
        absPts = 0.0; glutePts = 0.0; tricepsPts = 0.0;

        dbrefWorkouts.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot workoutSnapshot: dataSnapshot.getChildren()) {
                    Workout workout = workoutSnapshot.getValue(Workout.class);
                    System.out.println(workout.getPoints());
                    Double workoutPts = workout.getPoints();
                    String muscleType = workout.getMuscleType();
                    if (muscleType.equals("Biceps")) {
                        bicepsPts += workoutPts;
                    } else if (muscleType.equals("Chest")) {
                        chestPts += workoutPts;
                    } else if (muscleType.equals("Back")) {
                        backPts += workoutPts;
                    } else if (muscleType.equals("Shoulders")) {
                        shouldersPts += workoutPts;
                    }
                    else if (muscleType.equals("Legs")) {
                        legsPts += workoutPts;
                    }
                    else if (muscleType.equals("Glutes")) {
                        glutePts += workoutPts;
                    }
                    else if (muscleType.equals("Abs")) {
                        absPts += workoutPts;
                    }
                    else if (muscleType.equals("Triceps")) {
                        tricepsPts += workoutPts;
                    }
                }
                setupMasteryPoints();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void setupUIViews(){
        biceps = (TextView) findViewById(R.id.tvBicepMastery);
        chest = (TextView) findViewById(R.id.tvChestMastery);
        back = (TextView) findViewById(R.id.tvBackMastery);
        shoulders = (TextView) findViewById(R.id.tvShoulderMastery);
        abs = (TextView) findViewById(R.id.tvAbsMastery);
        glutes = (TextView) findViewById(R.id.tvGlutesMastery);
        legs = (TextView) findViewById(R.id.tvLegsMastery);
        triceps = (TextView) findViewById(R.id.tvTricepMastery);
        lvl = (TextView) findViewById(R.id.tvMasteryAvgLvl);
        lvlProg = (TextView) findViewById(R.id.tvLvlProgress);

        listItems = new TextView[]{triceps, biceps, chest, back, shoulders, glutes, legs, abs};
    }

    public void openFillInDetails(String muscleType){
        Intent intent = new Intent(MasteryActivity.this, DetailedMastery.class);
        intent.putExtra(EXTRA_TEXT, muscleType);
        startActivity(intent);
    }

    private void setupMasteryPoints() {
        int i = 0; int avgMastery = 0; final NumberFormat numberFormat = NumberFormat.getNumberInstance(Locale.CANADA);
        int[] listPts = {(int) Math.round(tricepsPts), (int) Math.round(bicepsPts), (int) Math.round(chestPts), (int) Math.round(backPts),
                (int) Math.round(shouldersPts), (int) Math.round(glutePts), (int) Math.round(legsPts)
        , (int) Math.round(absPts)};
        String[] listNames = {"Triceps: ","Biceps: ", "Chest: ", "Back: ", "Shoulders: ", "Glutes: ", "Legs: ", "Abs: "};
        for (TextView tv: listItems){
            Animation fromLeft = AnimationUtils.loadAnimation(this,R.anim.fromleft);
            fromLeft.setStartOffset((i) * 100);
            tv.startAnimation(fromLeft);
            final TextView txtView = tv; final String muscle = listNames[i];
            avgMastery += listPts[i];

            ValueAnimator animator = new ValueAnimator();
            animator.setObjectValues(0, listPts[i]);
            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                public void onAnimationUpdate(ValueAnimator animation) {
                    txtView.setText(muscle + numberFormat.format(animation.getAnimatedValue()) + "xp");
                }
            });
            animator.setDuration(1000);
            animator.setStartDelay(i*150);
            animator.start();

            i++;
        }
        // get avg mastery lvl
        avgMastery = Math.round(avgMastery/listNames.length);

        // set the new user lvl based on the avgMastery and show it on the screen
        setUserLvl(avgMastery);

    }

    private void setUserLvl(int xp) {
        Level level = new Level(xp);

        dbrefLevel.setValue(level);

        // set the animations for the level
        Animation fromFade = AnimationUtils.loadAnimation(this,R.anim.fadein);

        lvl.setText("" + level.getLvl());
        NumberFormat numberFormat = NumberFormat.getNumberInstance(Locale.CANADA);
        String str_curr_xp = numberFormat.format(level.getCurr_xp());
        String str_max_xp = numberFormat.format(level.getMax_xp());
        lvlProg.setText(str_curr_xp + "xp / " + str_max_xp + "xp" );
        lvl.startAnimation(fromFade); lvlProg.startAnimation(fromFade);

    }

}
