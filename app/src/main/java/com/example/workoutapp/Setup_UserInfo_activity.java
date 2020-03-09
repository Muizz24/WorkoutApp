package com.example.workoutapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.workoutapp.data.model.Level;
import com.example.workoutapp.data.model.UserInfo;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Text;

public class Setup_UserInfo_activity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private Spinner spinnerHeight, spinnerWeight, spinnerGender;
    private EditText weight, height, inches, age;
    private Button submitBtn;
    private TextView welcomeMsg;
    private UserInfo userData = new UserInfo();
    private String weightTxt, heightTxt, inchesTxt, ageTxt, heightType = "feet", username;
    private FirebaseAuth firebaseAuth;
    final FirebaseDatabase database = FirebaseDatabase.getInstance();
    public DatabaseReference dbrefInfo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup__user_info_activity);
        Intent intent = getIntent();
        username = intent.getStringExtra(MasteryActivity.EXTRA_TEXT).toUpperCase();
        setupUIViews();

        welcomeMsg.setText("Welcome " + username + "\n fill in some extra details");
        userData.setWeightType("kg");
        userData.setHeightType("feet");

        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();
        String owner = user.getEmail().replaceAll("[^0-9a-zA-Z]","");
        dbrefInfo = database.getReference(owner + "/userInfo");

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validate()) {
                    saveUserInfo();
                }
            }
        });
    }

    private void setupUIViews(){
        spinnerHeight = (Spinner) findViewById(R.id.spinnerHeightOptions);
        spinnerGender = (Spinner) findViewById(R.id.spinnerGenderOptions);
        spinnerWeight = (Spinner) findViewById(R.id.spinnerWeightOptions);
        weight = (EditText)findViewById(R.id.etWeight);
        height = (EditText)findViewById(R.id.etHeight);
        inches = (EditText)findViewById(R.id.etHeight2);
        age = (EditText)findViewById(R.id.etAge);
        submitBtn = (Button)findViewById(R.id.btnSubmitUserDetails);
        welcomeMsg = (TextView) findViewById(R.id.tvWelcomeUser);

        setupSpinners();
    }

    private void setupSpinners() {
        ArrayAdapter<CharSequence> adapterW = ArrayAdapter.createFromResource(this, R.array.weightOptions, android.R.layout.simple_spinner_item);
        ArrayAdapter<CharSequence> adapterH = ArrayAdapter.createFromResource(this, R.array.heightOptions, android.R.layout.simple_spinner_item);
        ArrayAdapter<CharSequence> adapterG = ArrayAdapter.createFromResource(this, R.array.genderOptions, android.R.layout.simple_spinner_item);

        adapterW.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerWeight.setAdapter(adapterW);

        adapterH.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerHeight.setAdapter(adapterH);

        adapterG.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerGender.setAdapter(adapterG);

        spinnerHeight.setOnItemSelectedListener(this);
        spinnerGender.setOnItemSelectedListener(this);
        spinnerWeight.setOnItemSelectedListener(this);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String text = parent.getItemAtPosition(position).toString();
        if (parent == spinnerHeight) {
            if (text.equals("feet")){
                inches.setVisibility(View.VISIBLE);
                height.setHint("feet");
            } else if (text.equals("cm")){
                inches.setVisibility(View.INVISIBLE);
                height.setHint("cm");
            } else {
                inches.setVisibility(View.INVISIBLE);
            }
            userData.setHeightType(text);
        } else if (parent == spinnerWeight) {
            weight.setHint("weight in " + text);
            userData.setWeightType(text);
        } else if (parent == spinnerGender) {
            userData.setGender(text);
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void saveUserInfo() {
        userData.setWeight(Integer.parseInt(weightTxt));
        userData.setAge(Integer.parseInt(ageTxt));

        int val;
        if (heightType == "feet") {
            val = (int) ((30.48 * Integer.parseInt(heightTxt)) + (Integer.parseInt(inchesTxt) * 2.54));
        } else {
            val = Integer.parseInt(heightTxt);
        }
        userData.setHeight(val);

        dbrefInfo.setValue(userData);
        finish();
        startActivity(new Intent(Setup_UserInfo_activity.this, HomeActivity.class));
    }

    public Boolean validate() {
        Boolean result = false;

        ageTxt = age.getText().toString().replaceAll("[^0-9]","");
        weightTxt = weight.getText().toString().replaceAll("[^0-9]","");
        heightTxt = height.getText().toString().replaceAll("[^0-9]","");
        inchesTxt = inches.getText().toString().replaceAll("[^0-9]","");

        if (ageTxt.equals("") || (heightType.equals("feet") && inchesTxt.equals("")) || weightTxt.equals("") || heightTxt.equals("")){
            Toast.makeText(this, "Please enter all the details", Toast.LENGTH_SHORT).show();
        } else {
            result = true;
        }

        return result;
    }
}
