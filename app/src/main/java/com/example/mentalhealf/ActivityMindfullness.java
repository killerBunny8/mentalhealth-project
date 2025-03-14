package com.example.mentalhealf;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class ActivityMindfullness extends AppCompatActivity {
    private Button btnExercise,btnYoga, btnMetta,btnMantra,btnBoxBreathing,btnAttentionMeditation,btnGuidedMed,btnGratitude;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_mindfullness);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        //initialise UI stuffs
        btnExercise = findViewById(R.id.btnExercise);
        btnYoga = findViewById(R.id.btnYoga);
        btnMetta = findViewById(R.id.btnMetta);
        btnMantra = findViewById(R.id.button8);
        btnBoxBreathing = findViewById(R.id.btnBoxBreathing);
        btnAttentionMeditation = findViewById(R.id.btnAttentionMeditation);
        btnGuidedMed = findViewById(R.id.btnGuidedMed);
        btnGratitude = findViewById(R.id.btnGratitude);

        // Set click listeners
        btnExercise.setOnClickListener(v -> openMethodMeditation("Exercise"));
        btnYoga.setOnClickListener(v -> openMethodMeditation("Yoga"));
        btnMetta.setOnClickListener(v -> openMethodMeditation("Metta Meditation"));
        btnMantra.setOnClickListener(v -> openMethodMeditation("Mantra Meditation"));
        btnBoxBreathing.setOnClickListener(v -> openMethodMeditation("Box Breathing"));
        btnAttentionMeditation.setOnClickListener(v -> openMethodMeditation("Attention Meditation"));
        btnGuidedMed.setOnClickListener(v -> openMethodMeditation("Guided Meditation"));
        btnGratitude.setOnClickListener(v -> openMethodMeditation("Gratitude Meditation"));


    }

    private void openMethodMeditation(String meditationType) {
        Intent intent = new Intent(this, ActivityMethodMeditation.class);
        intent.putExtra("MEDITATION_TYPE", meditationType);
        Toast.makeText(this,"You are going to the "+ meditationType+ " page.", Toast.LENGTH_SHORT).show();
        startActivity(intent);
    }



}