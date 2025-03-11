package com.example.mentalhealf;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.WriteBatch;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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
                        //Log.d("date", "getAllMoodLogs: "+ moodlog.getTime().toDate());

                        String currentDate = selectedDate1.format(moodlog.getTime().toDate());
                        if (currentDate.equals(selectedDate) || selectedDate.isEmpty()){
                            moodLogs.add(moodlog);
                        }
                        // Log.d("date", "getAllMoodLogs: "+ moodLogs.size());
                        //moodLogs.add(moodlog);
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


    public void getMoodLogsByRange(String selectedDate, String timeRange, @NonNull MoodLogListCallback callback) {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            callback.onFailure("User not authenticated.");
            return;
        }

        String userId = currentUser.getUid();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

        try {
            // Convert selectedDate to Date object
            Date selectedDateObj = dateFormat.parse(selectedDate);
            if (selectedDateObj == null) {
                callback.onFailure("Invalid selected date.");
                return;
            }

            // Calculate the start date based on the time range
            Calendar startCalendar = Calendar.getInstance();
            startCalendar.setTime(selectedDateObj);

            switch (timeRange) {
                case "Day":
                    // No change needed, start date is the same as selected date
                    break;
                case "Week":
                    startCalendar.add(Calendar.DAY_OF_YEAR, -6); // 7 days including the selected date
                    break;
                case "Month":
                    startCalendar.add(Calendar.MONTH, -1);
                    break;
                case "Three Months":
                    startCalendar.add(Calendar.MONTH, -3);
                    break;
                default:
                    callback.onFailure("Invalid time range.");
                    return;
            }

            // Set start time to 00:00:00
            startCalendar.set(Calendar.HOUR_OF_DAY, 0);
            startCalendar.set(Calendar.MINUTE, 0);
            startCalendar.set(Calendar.SECOND, 0);
            startCalendar.set(Calendar.MILLISECOND, 0);

            // Set end time to 23:59:59 on the selected date
            Calendar endCalendar = Calendar.getInstance();
            endCalendar.setTime(selectedDateObj);
            endCalendar.set(Calendar.HOUR_OF_DAY, 23);
            endCalendar.set(Calendar.MINUTE, 59);
            endCalendar.set(Calendar.SECOND, 59);
            endCalendar.set(Calendar.MILLISECOND, 999);

            // Convert to Timestamps for Firestore query
            Timestamp startTimestamp = new Timestamp(startCalendar.getTime());
            Timestamp endTimestamp = new Timestamp(endCalendar.getTime());

            Log.d("FirestoreQuery", "Fetching logs from " + dateFormat.format(startCalendar.getTime()) + " to " + selectedDate);

            // Query Firestore for logs within the date range
            db.collection("users").document(userId).collection("moodLogs")
                    .whereGreaterThanOrEqualTo("time", startTimestamp)
                    .whereLessThanOrEqualTo("time", endTimestamp)
                    .orderBy("time")
                    .get()
                    .addOnSuccessListener(queryDocumentSnapshots -> {
                        List<Moodlog> moodLogs = new ArrayList<>();
                        for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                            Moodlog moodlog = document.toObject(Moodlog.class);
                            moodLogs.add(moodlog);
                        }
                        Log.d("FirestoreQuery", "Filtered mood logs count: " + moodLogs.size());
                        callback.onSuccess(moodLogs);
                    })
                    .addOnFailureListener(e -> callback.onFailure("Error retrieving mood logs: " + e.getMessage()));
        } catch (Exception e) {
            callback.onFailure("Error processing selected date: " + e.getMessage());
        }
    }



    public void addMassMoodLogs(int count, MoodLogCallback callback) {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser == null) {
            callback.onFailure("User not authenticated.");
            return;
        }

        String userId = currentUser.getUid();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        WriteBatch batch = db.batch();

        long currentTimeMillis = System.currentTimeMillis();

        for (int i = 0; i < count; i++) {
            String logId = db.collection("users").document(userId).collection("moodLogs").document().getId();
            int randomMood = (int) (Math.random() * 5) + 1;
            String description = "Demo Log " + (i + 1);
            // String activity = i % 2 == 0 ? "working" : "exercising";
            String activity = "Jumping";

            long offsetMillis = i * 12 * 60 * 60 * 1000; // Spread logs 3 hours apart
            Timestamp moodTimestamp = new Timestamp(new Date(currentTimeMillis - offsetMillis));

            Moodlog newMoodLog = new Moodlog(logId, randomMood, description, activity, moodTimestamp);
            DocumentReference docRef = db.collection("users").document(userId)
                    .collection("moodLogs").document(logId);
            batch.set(docRef, newMoodLog);
        }

        // Commit batch operation
        batch.commit()
                .addOnSuccessListener(aVoid -> callback.onSuccess(count + " demo logs added successfully!"))
                .addOnFailureListener(e -> callback.onFailure("Error adding logs: " + e.getMessage()));
    }





}
