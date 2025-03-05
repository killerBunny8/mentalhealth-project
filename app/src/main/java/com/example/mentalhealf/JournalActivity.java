package com.example.mentalhealf;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class JournalActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private moodLogHelper moodLogHelper;
    private RecyclerView recyclerView;
    private MoodLogAdapter adapter;
    private List<Moodlog> moodLogsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_journal);
        View rootView = findViewById(android.R.id.content);
        rootView.setForceDarkAllowed(false);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialise Firebase
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        moodLogHelper = new moodLogHelper();

        // Initialise RecyclerView
        recyclerView = findViewById(R.id.journalView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Initialise list and adapter
        moodLogsList = new ArrayList<>();
        adapter = new MoodLogAdapter(moodLogsList);
        recyclerView.setAdapter(adapter);

        // mood logs from Firestore
        loadMoodLogs();
    }

    private void loadMoodLogs() {
        moodLogHelper.getAllMoodLogs(new moodLogHelper.MoodLogListCallback() {
            @Override
            public void onSuccess(List<Moodlog> moodLogs) {
                moodLogsList.clear();
                moodLogsList.addAll(moodLogs);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(String error) {
                Toast.makeText(JournalActivity.this, error, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
