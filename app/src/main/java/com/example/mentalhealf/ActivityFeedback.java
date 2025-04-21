package com.example.mentalhealf;

import android.os.Build;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class ActivityFeedback extends AppCompatActivity {
    EditText feedbackText;
    Spinner feedbackTypeSpinner;
    Button submitFeedback;

    FirebaseAuth mAuth;
    FirebaseUser user;
    FirebaseFirestore db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        getWindow().setNavigationBarColor(ContextCompat.getColor(this, android.R.color.black));


        feedbackText = findViewById(R.id.feedbackText);
        feedbackTypeSpinner = findViewById(R.id.feedbackTypeSpinner);
        submitFeedback = findViewById(R.id.submitFeedback);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        user = mAuth.getCurrentUser();

        submitFeedback.setOnClickListener(v -> {
            logFeedback();
        });

    }

    private void logFeedback() {
        feedbackHelper helper = new feedbackHelper();

        String message = feedbackText.getText().toString().trim();
        String category = feedbackTypeSpinner.getSelectedItem().toString();
        String email = user.getEmail();
        long timestamp = System.currentTimeMillis();

        if (message.isEmpty()) {
            feedbackText.setError("Feedback cannot be empty.");
            feedbackText.requestFocus();
            return;
        }

        Feedback feedback = new Feedback(email, category, message, timestamp);

        helper.submitFeedback(feedback, new feedbackHelper.FeedbackCallback() {
            @Override
            public void onSuccess() {
                Toast.makeText(ActivityFeedback.this, "Thanks for your feedback!", Toast.LENGTH_SHORT).show();
                clearStuff();
            }

            @Override
            public void onFailure(Exception e) {
                Toast.makeText(ActivityFeedback.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void clearStuff(){
        feedbackText.setText("");
        feedbackTypeSpinner.setSelection(0);
        feedbackText.clearFocus();
    }
}
