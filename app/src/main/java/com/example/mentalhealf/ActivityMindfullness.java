package com.example.mentalhealf;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class ActivityMindfullness extends AppCompatActivity {
    private Button btnExercise, btnMetta,btnMantra,btnBoxBreathing,btnAttentionMed,btnViewHistory,btnExtHelp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mindfullness);
        getWindow().setNavigationBarColor(ContextCompat.getColor(this, android.R.color.black));


        //initialise UI stuffs
        btnExercise = findViewById(R.id.btnExercise);
        btnMetta = findViewById(R.id.btnMetta);
        btnMantra = findViewById(R.id.button8);
        btnBoxBreathing = findViewById(R.id.btnBoxBreathing);
        btnAttentionMed = findViewById(R.id.btnGuidedMed);
        btnViewHistory = findViewById(R.id.btnViewHistory);
        btnExtHelp = findViewById(R.id.btnExtHelp);

        // Set click listeners
        btnExercise.setOnClickListener(v -> openMethodMeditation("Exercise"));
        btnMetta.setOnClickListener(v -> openMethodMeditation("Metta Meditation"));
        btnMantra.setOnClickListener(v -> openMethodMeditation("Mantra Meditation"));
        btnBoxBreathing.setOnClickListener(v -> openMethodMeditation("Box Breathing"));
        btnAttentionMed.setOnClickListener(v -> openMethodMeditation("Attention Meditation"));
        btnViewHistory.setOnClickListener(v -> {
            Intent intent = new Intent(this, ActivityMeditationHistory.class);
            Toast.makeText(this,"You are going to the History page.", Toast.LENGTH_SHORT).show();
            startActivity(intent);
        });
        btnExtHelp.setOnClickListener( v -> {
            Intent intent = new Intent(this, ActivityProfessionalHelp.class);
            Toast.makeText(this,"You are going to the Additinal Help page.", Toast.LENGTH_SHORT).show();
            startActivity(intent);
        });

    }
    // function that opens next page with correct fragment
    private void openMethodMeditation(String meditationType) {
        Intent intent = new Intent(this, ActivityMethodMeditation.class);
        intent.putExtra("MEDITATION_TYPE", meditationType);
        Toast.makeText(this,"You are going to the "+ meditationType+ " page.", Toast.LENGTH_SHORT).show();
        startActivity(intent);
    }



}