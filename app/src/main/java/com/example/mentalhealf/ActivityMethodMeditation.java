package com.example.mentalhealf;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
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
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        txtMethodType = findViewById(R.id.txtMethodType);
        Button btnExit = findViewById(R.id.btnExit);
        Button btnFinish = findViewById(R.id.btnFinish);


        meditationType = getIntent().getStringExtra("MEDITATION_TYPE");
        if (meditationType != null) {
            txtMethodType.setText(meditationType);
            loadFragment(meditationType);
        }
        btnExit.setOnClickListener(v -> {
            Toast.makeText(this,"You are returning to the Mindfulness page.", Toast.LENGTH_SHORT);
            finish();
        });
        btnFinish.setOnClickListener(v -> finish());
    }

    private void loadFragment(String meditation) {
        Fragment fragment = null;

        if (meditationType.equals("Exercise")) {
            fragment = new MeditationExerciseFragment();
        } else if (meditationType.equals("Metta Meditation")) {
            fragment = new MeditationMettaFragment();
        } else if (meditationType.equals("Mantra Meditation")) {
            fragment = new MeditationMantraFragment();
        }else if (meditationType.equals("Yoga")) {
            fragment = new MeditationMantraFragment();
        }else if (meditationType.equals("Box Breathing")) {
            fragment = new MeditationBoxBreathFragment();
        }else if (meditationType.equals("Attention Meditation")) {
            fragment = new MeditationMantraFragment();
        }else if (meditationType.equals("Guided Meditation")) {
            fragment = new MeditationMantraFragment();
        }else if (meditationType.equals("Gratitude Meditation")) {
            fragment = new MeditationMantraFragment();
        }
        if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.replace(R.id.fragmentContainerView5, fragment);
            transaction.commit();
        }
    }
}