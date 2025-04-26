package com.example.mentalhealf;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.Timestamp;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ActivityPostMeditation extends AppCompatActivity {
    private TextView txtSumamryH, txtSummaryD,imgSad, imgAbitSad, imgOkay, imgGood, imgGreat;
    private Button btnFinish;
    private EditText descriptionm;
    private int selectMood = 0;
    private String formattedTime;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_meditation);
        getWindow().setNavigationBarColor(ContextCompat.getColor(this, android.R.color.black));
        //intent
        Intent intent = getIntent();
        String meditationType = intent.getStringExtra("MEDITATION_TYPE");
        String duration = intent.getStringExtra("DURATION");
        Timestamp startTime = intent.getParcelableExtra("START_TIME");
        // init layout components to variable
        txtSumamryH = findViewById(R.id.textView17);
        txtSummaryD = findViewById(R.id.txtSummaryInfo);
        descriptionm = findViewById(R.id.inputMoodExplanation2);
        getSummary(meditationType, duration, startTime);
        imgSad = findViewById(R.id.imgSad);
        imgAbitSad = findViewById(R.id.imgAbitSad);
        imgOkay = findViewById(R.id.imgOkay);
        imgGood = findViewById(R.id.imgGood);
        imgGreat = findViewById(R.id.imgGreat);
        btnFinish = findViewById(R.id.btnFinishWalk);
        //set onclick listener for mood, same as homepage
        imgSad.setOnClickListener(v -> selectEmoji(imgSad, 1));
        imgAbitSad.setOnClickListener(v -> selectEmoji(imgAbitSad, 2));
        imgOkay.setOnClickListener(v -> selectEmoji(imgOkay, 3));
        imgGood.setOnClickListener(v -> selectEmoji(imgGood, 4));
        imgGreat.setOnClickListener(v -> selectEmoji(imgGreat, 5));

        //log mood witha  check to ensure a mood is atleast selected
        btnFinish.setOnClickListener(v -> {
            if (selectMood == 0) {
                Toast.makeText(ActivityPostMeditation.this, "Please select a mood before finishing.", Toast.LENGTH_SHORT).show();
            } else {
                logMoodAndMeditation(selectMood, meditationType, duration, formattedTime);
            }
        });
    }
    //function to display breif summary
    private void getSummary(String meditationType,String duration,Timestamp startTime){
        Date startDate = startTime.toDate();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());

        formattedTime = simpleDateFormat.format(startDate);
        String summaryText = "While "+ meditationType + "\n"
                + "Duration: " + duration + "\n"
                + "Start Time: " + formattedTime;


        txtSummaryD.setText(summaryText);
        txtSummaryD.setVisibility(View.VISIBLE);
        txtSumamryH.setVisibility(View.VISIBLE);
    }
    //select emoji similar to homepage
    public void selectEmoji(TextView emoji, int moodVal){
        resetEmoji();
        emoji.setTextSize(64);
        selectMood = moodVal;

    }
    //reset emoji similar to homepage

    private void resetEmoji() {
        imgSad.setTextSize(48);
        imgAbitSad.setTextSize(48);
        imgOkay.setTextSize(48);
        imgGood.setTextSize(48);
        imgGreat.setTextSize(48);
        selectMood = 0;
    }
    //fucnction to logmood into firestore
    private void logMoodAndMeditation(int selectedMood, String meditationType, String duration, String startTime) {
        moodLogHelper moodHelper = new moodLogHelper();


        String description = meditationType + "\n"
                + "Duration: " + duration + "\n"
                + "Start Time: " + startTime;

        moodHelper.logMood(selectedMood, description, "Meditation", new moodLogHelper.MoodLogCallback() {
            @Override
            public void onSuccess(String message) {
                Toast.makeText(ActivityPostMeditation.this, message, Toast.LENGTH_SHORT).show();
                descriptionm.setText("");
                resetEmoji();
                finishAct();
            }

            @Override
            public void onFailure(String error) {
                Toast.makeText(ActivityPostMeditation.this, error, Toast.LENGTH_SHORT).show();
            }
        });
    }
    //navigates to different page
    private void finishAct(){
        Intent intent = new Intent(this, ActivityHome.class);
        Toast.makeText(this,"You are going to the home page.", Toast.LENGTH_SHORT).show();
        startActivity(intent);
        finish();
    }
}