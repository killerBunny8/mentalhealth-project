package com.example.mentalhealf;

import android.util.Log;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Map;

public class feedbackHelper {
    private final FirebaseFirestore db;


    public feedbackHelper() {
        db = FirebaseFirestore.getInstance();
    }
    public void submitFeedback(Feedback feedback, FeedbackCallback callback) {
        if (feedback.getMessage() == null || feedback.getMessage().isEmpty()) {
            callback.onFailure(new Exception("Message cannot be empty."));
            return;
        }

        db.collection("feedback").add(feedback)
                .addOnSuccessListener(documentReference -> {
                    Log.d("feedbackHelper", "Feedback submitted.");
                    callback.onSuccess();
                })
                .addOnFailureListener(e -> {
                    Log.e("feedbackHelper", "Submission failed", e);
                    callback.onFailure(e);
                });
    }
    public interface FeedbackCallback {
        void onSuccess();
        void onFailure(Exception e);
    }

}
