package com.example.mentalhealf;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class ActivityProfessionalHelp extends AppCompatActivity {
    private Button btnMind,btnSamartian, btnNHS, btnBack;
    private String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_professional_help);
        getWindow().setNavigationBarColor(ContextCompat.getColor(this, android.R.color.black));

        btnMind = findViewById(R.id.btnMind);
        btnSamartian = findViewById(R.id.btnSamtan);
        btnNHS = findViewById(R.id.btnNHS);
        btnBack = findViewById(R.id.btnBack);

        btnMind.setOnClickListener(v -> {
            openWebsite("https://www.mind.org.uk");
        });
        btnSamartian.setOnClickListener(v -> {
            openWebsite("https://www.samaritans.org");
        });
        btnNHS.setOnClickListener(v -> {
            openWebsite("https://www.nhs.uk/mental-health");
        });

        btnBack.setOnClickListener(v -> finish());


    }
    private void openWebsite(String url) {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(browserIntent);
    }
}