package com.example.mentalhealf;

import android.util.Log;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Map;

public class feedbackHelper {
    private final FirebaseFirestore db;

    // Init firebase
    public feedbackHelper() {
        db = FirebaseFirestore.getInstance();
    }
    // submits feedback to feedback collection in firebase
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
    //interface for result of function
    public interface FeedbackCallback {
        void onSuccess();
        void onFailure(Exception e);
    }

}
