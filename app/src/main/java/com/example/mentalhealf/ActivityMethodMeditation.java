package com.example.mentalhealf;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class ActivityMethodMeditation extends AppCompatActivity {
    private TextView txtMethodType;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_method_meditation);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        txtMethodType = findViewById(R.id.txtMethodType);
        Button btnExit = findViewById(R.id.btnExit);
        Button btnFinish = findViewById(R.id.btnFinish);

        String meditationType = getIntent().getStringExtra("MEDITATION_TYPE");
        if (meditationType != null) {
            txtMethodType.setText(meditationType);
        }
        btnExit.setOnClickListener(v -> {
            Toast.makeText(this,"You are returning to the Mindfulness page.", Toast.LENGTH_SHORT);
            finish();
        });
        btnFinish.setOnClickListener(v -> finish());
    }
}