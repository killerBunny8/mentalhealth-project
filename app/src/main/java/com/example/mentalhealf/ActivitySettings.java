package com.example.mentalhealf;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.google.firebase.Timestamp;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class ActivitySettings extends AppCompatActivity {
    private TextView emailText, changePassword, privacyPolicy;

    private EditText editFirstName, editLastName,deletePassword;
    private Button btnUpdateName,btnOnce, btnTwice, btnThrice, btndeleteAccount, btnDownload,btnLogout;

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private FirebaseUser user;
    private reminders reminderManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        getWindow().setNavigationBarColor(ContextCompat.getColor(this, android.R.color.black));
        // init layout components to variable
        editFirstName = findViewById(R.id.editFirstName);
        editLastName = findViewById(R.id.editLastName);
        btnUpdateName = findViewById(R.id.btnUpdateName);
        emailText = findViewById(R.id.email);
        changePassword = findViewById(R.id.changePassword);
        btnOnce = findViewById(R.id.btnOnce);
        btnTwice = findViewById(R.id.btnTwice);
        btnThrice = findViewById(R.id.btnThrice);
        deletePassword = findViewById(R.id.deletePasswordInput);
        btndeleteAccount = findViewById(R.id.deleteAccountButton);
        btnDownload = findViewById(R.id.downloadDataButton);
        btnLogout = findViewById(R.id.logoutButton);
        privacyPolicy = findViewById(R.id.privacyPolicyLink);
        //init reminders
        reminderManager = new reminders(this);
        //init firestore
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        user = mAuth.getCurrentUser();
        //call function to load username into edittext
        loadUserName();
        loadSelectedReminder();

        //btnclick to update name
        btnUpdateName.setOnClickListener(v -> {
            String firstName = editFirstName.getText().toString().trim();
            String lastName = editLastName.getText().toString().trim();

            if (firstName.isEmpty() || lastName.isEmpty()) {
                Toast.makeText(this, "Please enter both first and last name", Toast.LENGTH_SHORT).show();
            } else {
                updateName();
            }
        });
        //function which sends email to change password
        changePassword.setOnClickListener(v -> {
            changePword();
        });
        //btn to set reminders
        btnOnce.setOnClickListener(v -> {
            highlightSelectedButton(btnOnce);
            saveSelectedReminder(1);
            reminderManager.setReminders(
                    1,
                    "Mental Health Check",
                    "Time to check in with yourself!"
            );
            testNotification(); //function to show notification
        });

        // onclick of two a day notif
        btnTwice.setOnClickListener(v -> {
            highlightSelectedButton(btnTwice);
            saveSelectedReminder(2);
            reminderManager.setReminders(
                    2,
                    "Mental Health Check",
                    "Time to check in with yourself!"
            );
            testNotification();
        });
        // onclick of three a day notif
        btnThrice.setOnClickListener(v -> {
            highlightSelectedButton(btnThrice);
            saveSelectedReminder(3);
            reminderManager.setReminders(
                    3,
                    "Mental Health Check",
                    "Time to check in with yourself!"
            );
            testNotification();
        });
        //delete account
        btndeleteAccount.setOnClickListener(v -> {
            String password = deletePassword.getText().toString().trim();
            if (password.isEmpty()) {
                Toast.makeText(this, "Please enter your password to confirm", Toast.LENGTH_SHORT).show();
            } else {
                //deleteAccount(password);
                //alert box to double check deleting account
                new AlertDialog.Builder(this)
                        .setTitle("Confirm Deletion")
                        .setMessage("Are you sure you want to permanently delete your account? this action can not be undone.")
                        .setPositiveButton("Delete", (dialog, which) -> {
                            deleteAccount(password);
                        })
                        .setNegativeButton("Cancel", null)
                        .show();
            }
        });
        //download moodlogs btn
        btnDownload.setOnClickListener(v -> {
            String password = deletePassword.getText().toString().trim();
            if (password.isEmpty()) {
                Toast.makeText(this, "Please enter your password to confirm", Toast.LENGTH_SHORT).show();
            } else {
                downloadUserData(password);
            }
        });
        //logout btn
        btnLogout.setOnClickListener(v -> {
            logout();
        });
        //view privcacy policy
        privacyPolicy.setOnClickListener(v -> {
            Toast.makeText(ActivitySettings.this, "Redirecting to privacy page", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(ActivitySettings.this, ActivityPrivacy.class));
        });

    }
    //function which  loads the chosen reminder oncreate()
    private void loadSelectedReminder() {
        SharedPreferences prefs = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        int selectedReminder = prefs.getInt("SelectedReminder", 0);

        if (selectedReminder == 1) {
            highlightSelectedButton(btnOnce);
        } else if (selectedReminder == 2) {
            highlightSelectedButton(btnTwice);
        } else if (selectedReminder == 3) {
            highlightSelectedButton(btnThrice);
        }
    }
    //function which saves setting of notif status
    private void saveSelectedReminder(int reminderOption) {
        SharedPreferences prefs = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt("SelectedReminder", reminderOption);
        editor.apply();
    }
    //views the chosen notif button
    private void highlightSelectedButton(Button selectedButton) {
        // Reset buttons
        btnOnce.setBackgroundColor(ContextCompat.getColor(this, android.R.color.darker_gray));
        btnTwice.setBackgroundColor(ContextCompat.getColor(this, android.R.color.darker_gray));
        btnThrice.setBackgroundColor(ContextCompat.getColor(this, android.R.color.darker_gray));

        // change colour of butotn
        selectedButton.setBackgroundColor(ContextCompat.getColor(this, R.color.purplebtn));
    }

    //logs out and removes account from shared preferences
    private void logout() {
        FirebaseAuth.getInstance().signOut();
        SharedPreferences prefs = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        prefs.edit().clear().apply();
        Toast.makeText(this, "Logged out successfully", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(this, ActivityMain.class));
        finish();
    }
    //function to update name
    private void updateName() {
        String firstName = editFirstName.getText().toString().trim();
        String lastName = editLastName.getText().toString().trim();
        String fullName = firstName + " " + lastName;

        if (user != null) {
            String uid = user.getUid();

            db.collection("users").document(uid)
                    .update("firstName", firstName, "lastName", lastName, "fullName", fullName)
                    .addOnSuccessListener(unused ->
                            Toast.makeText(this, "Name updated successfully!", Toast.LENGTH_SHORT).show())
                    .addOnFailureListener(e ->
                            Toast.makeText(this, "Error updating name: " + e.getMessage(), Toast.LENGTH_SHORT).show());
        } else {
            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show();
        }
    }
    //function to load username
    private void loadUserName() {
        if (user != null) {
            String uid = user.getUid();
            String email = user.getEmail();

            db.collection("users").document(uid).get().addOnSuccessListener(documentSnapshot -> {
                if (documentSnapshot.exists()) {
                    String firstName = documentSnapshot.getString("firstName");
                    String lastName = documentSnapshot.getString("lastName");

                    if (firstName != null) {
                        editFirstName.setText(firstName);
                    }
                    if (lastName != null) {
                        editLastName.setText(lastName);
                    }
                    if (email != null) {
                        emailText.setText("Email: " + email);
                    }
                }
            }).addOnFailureListener(e ->
                    Toast.makeText(this, "Failed to load name: " + e.getMessage(), Toast.LENGTH_SHORT).show()
            );
        }
    }
    //function which sends email to change password
    private void changePword(){
        if (user != null) {
            //String email = changePassword.getText().toString();
            String email = user.getEmail();

            if (email != null) {
                mAuth.sendPasswordResetEmail(email)
                        .addOnSuccessListener(unused ->
                                Toast.makeText(this, "Reset email sent to " + email, Toast.LENGTH_LONG).show())
                        .addOnFailureListener(e ->
                                Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show());
            }
        } else {
            Toast.makeText(this, "No user logged in", Toast.LENGTH_SHORT).show();
        }
    }
    //sends out a test notification
    private void testNotification() {
        Intent intent = new Intent(this, ReminderMessage.class);
        intent.putExtra("title", "Test Notification");
        intent.putExtra("message", "This is a test message");
        Toast.makeText(this, "Notif sent", Toast.LENGTH_SHORT).show();

        sendBroadcast(intent);
    }
    // deletes account data, authentication and also stored data after authenticating
    private void deleteAccount(String password){
        String email = user.getEmail();

        AuthCredential credential = EmailAuthProvider.getCredential(email, password);
        user.reauthenticate(credential)
                .addOnSuccessListener(unused -> {
                    // Delete data
                    db.collection("users").document(user.getUid()).delete();
                    // Delete the FirebaseAuth user
                    user.delete()
                            .addOnSuccessListener(unused1 -> {
                                Toast.makeText(this, "Account deleted", Toast.LENGTH_SHORT).show();
                                FirebaseAuth.getInstance().signOut();
                                //startActivity(new Intent(this, ActivityMain.class));
                                logout();
                                finish();
                            })
                            .addOnFailureListener(e ->
                                    Toast.makeText(this, "Error deleting account: " + e.getMessage(), Toast.LENGTH_SHORT).show());
                })
                .addOnFailureListener(e ->
                        Toast.makeText(this, "Re-authentication failed: " + e.getMessage(), Toast.LENGTH_SHORT).show());

    }
    //downlaods all moodlog data after authenticating account
    private void downloadUserData(String password) {

        String email = user.getEmail();
        AuthCredential credential = EmailAuthProvider.getCredential(email, password);

        user.reauthenticate(credential)
                .addOnSuccessListener(unused -> {
                    String uid = user.getUid();
                    db.collection("users").document(uid).collection("moodLogs")
                            .get()
                            .addOnSuccessListener(querySnapshot -> {
                                StringBuilder data = new StringBuilder();
                                SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy - HH:mm", Locale.getDefault());
                                for (var doc : querySnapshot) { //for every moodlog this happens
                                    String activity = doc.getString("activity");
                                    String description = doc.getString("description");
                                    Long feeling = doc.getLong("feeling");
                                    Timestamp timestamp = doc.getTimestamp("time");
                                    String moodEmoji;
                                    // transforms the feeling into their emojis for consistency
                                    if (feeling == null) {
                                        moodEmoji = "🤔";
                                    } else if (feeling == 1) {
                                        moodEmoji = "😢";
                                    } else if (feeling == 2) {
                                        moodEmoji = "😕";
                                    } else if (feeling == 3) {
                                        moodEmoji = "😐";
                                    } else if (feeling == 4) {
                                        moodEmoji = "🙂";
                                    } else if (feeling == 5) {
                                        moodEmoji = "😁";
                                    } else {
                                        moodEmoji = "🤔";
                                    }
                                    //appends to a document
                                    data.append("Date: ").append(sdf.format(timestamp.toDate()))
                                            .append("\nMood: ").append(moodEmoji)
                                            .append("\nActivity: ").append(activity)
                                            .append("\nDescription: ").append(description)
                                            .append("\n\n");
                                }
                                //calls function to save to file
                                saveTextToFile(data.toString());
                            })
                            .addOnFailureListener(e ->
                                    Toast.makeText(this, "Error downloading data: " + e.getMessage(), Toast.LENGTH_SHORT).show());

                })
                .addOnFailureListener(e ->
                        Toast.makeText(this, "Re-authentication failed: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }
    //defines name and location of where to esave file.
    private void saveTextToFile(String content) {
        try {
            String fileName = "my_mood_logs.txt";
            File downloads = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS); //location of save

            File file = new File(downloads, fileName);
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(content.getBytes());
            fos.close();

            Toast.makeText(this, "Saved to Downloads folder", Toast.LENGTH_LONG).show();

        } catch (Exception e) {
            Toast.makeText(this, "Error saving file: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

}
