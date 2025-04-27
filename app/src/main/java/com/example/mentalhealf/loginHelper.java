///////////////////////////////////////////////////////////////////////////
//                      LOGIN HELPER CLASS                               //
//                                                                       //
// This class manages firebase intertactions to authenticate user account//
// And also to register user with firebase                               //
///////////////////////////////////////////////////////////////////////////

package com.example.mentalhealf;
//      com.example.mentalhealf

import android.app.Activity;
import android.util.Log;
import android.widget.Toast;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class loginHelper {

    private FirebaseAuth mAuth;
    private final FirebaseFirestore db;

    // Contrstuctor
    public loginHelper() {
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

    }
    //function which takes the email and password and returns success or failure
    public void loginUser(String email, String password, Activity activity, AuthCallback callback) {
        // checks if its empty and sends a toast if it is
        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(activity, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }
        // attemps to login with email and password
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(activity, task -> {
            if (task.isSuccessful()) { //if login is successful
                FirebaseUser user = mAuth.getCurrentUser(); // retreive current user.
                callback.onSuccess(user);
            } else { //if login failed
                callback.onFailure(task.getException());
            }
        });
    }
    // Registers new user with email and password
    public void registerUser(String email, String password,User user, Activity activity, AuthCallback callback) {
        // checks if its empty and sends a toast if it is
        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(activity, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }
        // creates yser with email and password
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(activity, task -> {
            if (task.isSuccessful()) {
                FirebaseUser user1 = mAuth.getCurrentUser();

                if (user != null) {
                    // Set user ID and save user details to Firestore
                    user.setId(user.getid());
                    db.collection("users").document(user1.getUid())
                            .set(user)
                            .addOnSuccessListener(aVoid -> callback.onSuccess(user1))
                            .addOnFailureListener(callback::onFailure);
                }
            } else {
                callback.onFailure(task.getException());
            }
        });
    }



    // handles function for success and fail for login and register
    public interface AuthCallback {
        void onSuccess(FirebaseUser user);
        void onFailure(Exception e);
    }
    //function which gets email from username, used during login
    public void getUserEmail(String username, EmailCallback callback) {
        db.collection("users").whereEqualTo("username", username).get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null && !task.getResult().isEmpty()) {
                        DocumentSnapshot document = task.getResult().getDocuments().get(0);
                        User user = document.toObject(User.class);
                        if (user != null) {
                            callback.onSuccess(user);
                            return;
                        }
                    }
                    db.collection("users").whereEqualTo("email", username).get()
                            .addOnCompleteListener(task1 -> {
                                if (task1.isSuccessful() && task1.getResult() != null && !task1.getResult().isEmpty()) {
                                    DocumentSnapshot document1 = task1.getResult().getDocuments().get(0);
                                    User user = document1.toObject(User.class);
                                    if (user != null) {
                                        callback.onSuccess(user);
                                        return;
                                    }
                                }
                                callback.onFailure(new Exception("User not found."));
                            })
                            .addOnFailureListener(callback::onFailure);
                })
                .addOnFailureListener(callback::onFailure);
    }
    public interface EmailCallback {
        void onSuccess(User user);
        void onFailure(Exception e);
    }

    // Check if username exists
    public void dupeUsername(String username, UsernameCheckCallback callback) {
        db.collection("users").whereEqualTo("username", username).get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null) {
                        boolean isTaken = !task.getResult().isEmpty(); // If result is empty, username is available
                        callback.onResult(isTaken);
                    } else {
                        callback.onFailure(task.getException()); // Handle Firestore errors
                    }
                }).addOnFailureListener(callback::onFailure);
    }


    // Username callback
    public interface UsernameCheckCallback {
        void onResult(boolean isTaken);
        void onFailure(Exception e);

    }


}
