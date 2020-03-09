package com.example.workoutapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.animation.ValueAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.workoutapp.data.model.Level;
import com.example.workoutapp.data.model.Workout;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.OnChartGestureListener;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.WeekFields;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

public class DetailedMastery extends AppCompatActivity {
    public String[] days; public int[] ptsperDay;
    public  ArrayList<Entry> yValues;
    public double pts;
    public String musclePart;
    public static final String EXTRA_TEXT = "com.example.workoutapp.EXTRA_TEXT";
    final DayOfWeek firstDayOfWeek = WeekFields.of(Locale.CANADA).getFirstDayOfWeek();
    final DayOfWeek lastDayOfWeek = DayOfWeek.of(((firstDayOfWeek.getValue() + 5) % DayOfWeek.values().length) + 1);
    private static final String TAG = "DetailedMastery";
    private LineChart mChart;
    private TextView mainTitle, muscleLvl;
    private Button btnAddMastery;
    private FirebaseAuth firebaseAuth;
    public DatabaseReference dbrefWorkouts;
    final FirebaseDatabase database = FirebaseDatabase.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_mastery);
        // setup UI views
        setupUIViews();
        // find out which muscle type this is
        Intent intent = getIntent();
        musclePart = intent.getStringExtra(MasteryActivity.EXTRA_TEXT);

        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();
        String owner = user.getEmail().replaceAll("[^0-9a-zA-Z]","");
        dbrefWorkouts = database.getReference(owner + "/workouts");

        btnAddMastery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFillInDetails(musclePart);
            }
        });

    }

    @Override
    protected void onStart(){
        super.onStart();
        yValues = new ArrayList<>();
        pts = 0.0; ptsperDay = new int[]{0,0,0,0,0,0,0}; setWeek();

        dbrefWorkouts.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                float i = 1;
                for (DataSnapshot workoutSnapshot: dataSnapshot.getChildren()) {
                    Workout workout = workoutSnapshot.getValue(Workout.class);
                    String muscleType = workout.getMuscleType();
                    if (muscleType.equals(musclePart)) {
                        Double workoutPts = workout.getPoints();
                        pts += workoutPts; String currDate = formatDate(workout.getDate());
                        for (int j = 0; i < ptsperDay.length; j++){
                            if (days[j].equals(currDate)){
                                ptsperDay[j] += workoutPts; break;
                            }
                        }
                    }
                }
                while (i <= 7) {
                    yValues.add(new Entry(i, ptsperDay[(int) (i-1)]));
                    i += 1;
                }
                setupGraph();
                setupMasteryPts();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void setupMasteryPts(){
        int intPts = (int) Math.round(pts);
        final Level lvl = new Level(intPts);
        final NumberFormat numberFormat = NumberFormat.getNumberInstance(Locale.CANADA);
        final String str_max_xp = numberFormat.format(lvl.getMax_xp());
        ValueAnimator animator = new ValueAnimator();
        animator.setObjectValues(0, lvl.getCurr_xp());
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator animation) {
                muscleLvl.setText("Level: " + lvl.getLvl() + "\n" + numberFormat.format(animation.getAnimatedValue()) + "xp / " + str_max_xp + "xp");
            }
        });
        animator.setDuration(1000);
        animator.setStartDelay(150);
        animator.start();

        mainTitle.setText(musclePart + " Mastery");
    }

    private void setupGraph(){
        LineDataSet set1 = new LineDataSet(yValues, "xp per day");

        set1.setFillAlpha(110);
        set1.setLineWidth(3f);
        set1.setCircleRadius(5f);

        mChart.setBorderWidth(5);

        LineData data = new LineData(set1);

        ValueFormatter xAxisFormatter = new DayAxisValueFormatter(mChart);
        XAxis xAxis = mChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setGranularity(1f); // only intervals of 1 day
        xAxis.setLabelCount(7);
        xAxis.setValueFormatter(xAxisFormatter);

        mChart.setData(data);
        mChart.invalidate();
    }

    public class DayAxisValueFormatter extends ValueFormatter {
        private final LineChart chart;
        final String[] xAxisLabel = new String[]{"Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"};

        public DayAxisValueFormatter(LineChart chart) {
            this.chart = chart;
        }
        @Override
        public String getFormattedValue(float value) {
            String day = xAxisLabel[Math.max(0, ((int)value - 1))];
            return ("" + day);
        }
    }

    public void openFillInDetails(String muscleType){
        Intent intent = new Intent(DetailedMastery.this, FillDetailsActivity.class);
        intent.putExtra(EXTRA_TEXT, muscleType);
        startActivity(intent);
    }

    private void setupUIViews() {
        mChart = (LineChart) findViewById(R.id.linechart);
        mainTitle = (TextView) findViewById(R.id.tvPageTitle);
        btnAddMastery = (Button) findViewById(R.id.btnAddMastery);
        muscleLvl = (TextView) findViewById(R.id.tvLevel);
    }

    private void setWeek(){
        Calendar now = Calendar.getInstance();

        SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");

        days = new String[7];
        int delta = -now.get(GregorianCalendar.DAY_OF_WEEK) + 2; //add 2 if your week start on monday
        now.add(Calendar.DAY_OF_MONTH, delta );
        for (int i = 0; i < 7; i++)
        {
            days[i] = format.format(now.getTime());
            now.add(Calendar.DAY_OF_MONTH, 1);
        }
    }

    private String formatDate(String date) {
        DateTimeFormatter fIn = DateTimeFormatter.ofPattern( "yyyy/MM/dd" , Locale.CANADA );  // As a habit, specify the desired/expected locale, though in this case the locale is irrelevant.
        LocalDate ld = LocalDate.parse( date.substring(0,10) , fIn );

        DateTimeFormatter fOut = DateTimeFormatter.ofPattern( "MM/dd/yyyy" , Locale.CANADA );
        String output = ld.format( fOut );
        return output;
    }

    private void get_week() {
    }
}
