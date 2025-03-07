package com.example.mentalhealf;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class moodLogHelper {

    private FirebaseAuth mAuth;
    private final FirebaseFirestore db;
    // auth
    public moodLogHelper() {
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

    }

    public void logMood(int feeling, String description, String activity, @NonNull MoodLogCallback callback){
        FirebaseUser currentUser = mAuth.getCurrentUser();

        String userId = currentUser.getUid();

        // creates a log associated with each user instead of a giant db with everyone thing
        String logId = db.collection("users").document(userId).collection("moodLogs").document().getId();
        Moodlog moodLogging = new Moodlog(logId, feeling, description, activity, Timestamp.now());

        db.collection("users").document(userId)
                .collection("moodLogs")
                .document(logId)
                .set(moodLogging)
                .addOnSuccessListener(aVoid -> callback.onSuccess("Mood logged successfully!"))
                .addOnFailureListener(e -> callback.onFailure("Error logging mood: " + e.getMessage()));

    }


    public interface MoodLogCallback {
        void onSuccess(String message);
        void onFailure(String error);
    }

    public void getAllMoodLogs(String selectedDate, @NonNull MoodLogListCallback callback) {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        String userId = currentUser.getUid();



        db.collection("users").document(userId).collection("moodLogs")
                .orderBy("time").get().addOnSuccessListener(queryDocumentSnapshots -> {
                    List<Moodlog> moodLogs = new ArrayList<>();
                    SimpleDateFormat selectedDate1 = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());


                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        Moodlog moodlog = document.toObject(Moodlog.class);
                        String currentDate = selectedDate1.format(moodlog.getTime().toDate());
                        if (currentDate.equals(selectedDate)){
                            moodLogs.add(moodlog);
                        }
                    }
                    callback.onSuccess(moodLogs);
                })
                .addOnFailureListener(e -> callback.onFailure("Error retrieving mood logs: " + e.getMessage()));
    }
    public interface MoodLogListCallback {
        void onSuccess(List<Moodlog> moodLogs);
        void onFailure(String error);
    }

    public void updateMoodLog(String logId, String newDescription, int position, MoodLogUpdateCallback callback) {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        String userId = currentUser.getUid();

        db.collection("users").document(userId).collection("moodLogs").document(logId)
                .update("description", newDescription)
                .addOnSuccessListener(aVoid -> callback.onSuccess("Mood updated successfully!", position))
                .addOnFailureListener(e -> callback.onFailure("Update failed: " + e.getMessage()));
    }

    public interface MoodLogUpdateCallback {
        void onSuccess(String message, int position);
        void onFailure(String error);
    }


}
