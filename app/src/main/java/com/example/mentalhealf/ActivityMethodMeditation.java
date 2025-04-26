package com.example.mentalhealf;

import android.os.Build;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class ActivityMethodMeditation extends AppCompatActivity {
    private TextView txtMethodType;
    private String meditationType;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_method_meditation);
        getWindow().setNavigationBarColor(ContextCompat.getColor(this, android.R.color.black));
        //initialise layout variables
        txtMethodType = findViewById(R.id.txtMethodType);
        Button btnExit = findViewById(R.id.btnExit);
        //Button btnFinish = findViewById(R.id.btnFinish);

        //get intent data for framgment manager
        meditationType = getIntent().getStringExtra("MEDITATION_TYPE");
        if (meditationType != null) {
            txtMethodType.setText(meditationType);
            loadFragment(meditationType);
        }
        btnExit.setOnClickListener(v -> {
            Toast.makeText(this,"You are returning to the Mindfulness page.", Toast.LENGTH_SHORT);
            finish();
        });
        //btnFinish.setOnClickListener(v -> finish());
    }
    //fragment manageer changes fragment depending on the intent value
    private void loadFragment(String meditation) {
        Fragment fragment = null;
        if (meditation.equals("Exercise")) {
            fragment = new MeditationExerciseFragment();
        } else if (meditation.equals("Metta Meditation")) {
            fragment = new MeditationMettaFragment();
        } else if (meditation.equals("Mantra Meditation")) {
            fragment = new MeditationMantraFragment();
        }else if (meditation.equals("Box Breathing")) {
            fragment = new MeditationBoxBreathFragment();
        }else if (meditation.equals("Attention Meditation")) {
            fragment = new MeditationAttentionFragment();
        }
        //if fragment is valid, change to the selected one.
        if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.replace(R.id.fragmentContainerView5, fragment);
            transaction.commit();
        }
    }
}