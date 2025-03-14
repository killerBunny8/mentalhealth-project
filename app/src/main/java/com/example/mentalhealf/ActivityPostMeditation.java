package com.example.mentalhealf;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_post_meditation);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Intent intent = getIntent();
        String meditationType = intent.getStringExtra("MEDITATION_TYPE");
        String duration = intent.getStringExtra("DURATION");
        Timestamp startTime = intent.getParcelableExtra("START_TIME");

        txtSumamryH = findViewById(R.id.textView17);
        txtSummaryD = findViewById(R.id.txtSummaryInfo);
        getSummary(meditationType,duration,startTime);
        imgSad = findViewById(R.id.imgSad);
        imgAbitSad = findViewById(R.id.imgAbitSad);
        imgOkay = findViewById(R.id.imgOkay);
        imgGood = findViewById(R.id.imgGood);
        imgGreat = findViewById(R.id.imgGreat);

        imgSad.setOnClickListener(v -> selectEmoji(imgSad, 1));
        imgAbitSad.setOnClickListener(v -> selectEmoji(imgAbitSad, 2));
        imgOkay.setOnClickListener(v -> selectEmoji(imgOkay, 3));
        imgGood.setOnClickListener(v -> selectEmoji(imgGood, 4));
        imgGreat.setOnClickListener(v -> selectEmoji(imgGreat, 5));
    }
    private void getSummary(String meditationType,String duration,Timestamp startTime){
        Date startDate = startTime.toDate();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());

        String formattedTime = simpleDateFormat.format(startDate);
        String summaryText = "Meditation Type: " + meditationType + "\n"
                + "Duration: " + duration + "\n"
                + "Start Time: " + formattedTime;


        txtSummaryD.setText(summaryText);
        txtSummaryD.setVisibility(View.VISIBLE);
        txtSumamryH.setVisibility(View.VISIBLE);
    }
    public void selectEmoji(TextView emoji, int moodVal){
        resetEmoji();
        emoji.setTextSize(64);
        selectMood = moodVal;

    }
    private void resetEmoji() {
        imgSad.setBackgroundColor(Color.TRANSPARENT);
        imgAbitSad.setBackgroundColor(Color.TRANSPARENT);
        imgOkay.setBackgroundColor(Color.TRANSPARENT);
        imgGood.setBackgroundColor(Color.TRANSPARENT);
        imgGreat.setBackgroundColor(Color.TRANSPARENT);

        imgSad.setTextSize(48);
        imgAbitSad.setTextSize(48);
        imgOkay.setTextSize(48);
        imgGood.setTextSize(48);
        imgGreat.setTextSize(48);
        selectMood = 0;
    }
    private void logMoodAndMeditation(int selectedMood, String meditationType, String duration, String startTime) {
        moodLogHelper moodHelper = new moodLogHelper();

        String description = "Meditation Type: " + meditationType + "\n"
                + "Duration: " + duration + "\n"
                + "Start Time: " + startTime;

        moodHelper.logMood(selectedMood, description, "Meditation", new moodLogHelper.MoodLogCallback() {
            @Override
            public void onSuccess(String message) {
                Toast.makeText(ActivityPostMeditation.this, message, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(String error) {
                Toast.makeText(ActivityPostMeditation.this, error, Toast.LENGTH_SHORT).show();
            }
        });
    }
}