package com.example.mentalhealf;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class ActivityMeditationHistory extends AppCompatActivity {

    private RecyclerView meditationRecycler;
    private MeditationHistoryAdapter adapter;
    private List<Moodlog> meditationLogs = new ArrayList<>();
    private TextView txtViewNoLogs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meditation_history);
        getWindow().setNavigationBarColor(ContextCompat.getColor(this, android.R.color.black));


        // Initialise views
        meditationRecycler = findViewById(R.id.meditationRecycler);
        txtViewNoLogs = findViewById(R.id.txtViewNoLogs);

        // Set up Recyclerview
        meditationRecycler.setLayoutManager(new LinearLayoutManager(this));
        adapter = new MeditationHistoryAdapter(meditationLogs);
        meditationRecycler.setAdapter(adapter);

        // Load all meditation logs
        loadMeditationLogs();
    }
    // Function to load all logs
    private void loadMeditationLogs() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();//user id to get the logs from the correct user
        //firebase query to collect logs
        db.collection("users").document(userId).collection("moodLogs")
                .whereEqualTo("activity", "Meditation").get()
                .addOnSuccessListener(querySnapshot -> {
                    meditationLogs.clear(); //clear previous list
                    for (QueryDocumentSnapshot doc : querySnapshot) {
                        Moodlog log = doc.toObject(Moodlog.class);
                        meditationLogs.add(log); //add logs to list
                    }
                    meditationLogs.sort((a, b) -> {// sorts them out early to late
                        if (a.getTime() == null || b.getTime() == null) return 0;
                        return b.getTime().toDate().compareTo(a.getTime().toDate());
                    });


                    adapter.notifyDataSetChanged();

                    // Show/hide empty
                    txtViewNoLogs.setVisibility(meditationLogs.isEmpty() ? View.VISIBLE : View.GONE);
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Error loading meditation history", Toast.LENGTH_SHORT).show();
                    txtViewNoLogs.setVisibility(View.VISIBLE);
                });
    }
}