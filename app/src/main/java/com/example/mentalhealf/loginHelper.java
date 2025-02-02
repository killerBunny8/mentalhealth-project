///////////////////////////////////////////////////////////////////////////
//                      LOGIN HELPER CLASS                               //
//                                                                       //
// This class manages firebase intertactions to authenticate user account//
// And also to register user with firebase                               //
///////////////////////////////////////////////////////////////////////////

package com.example.mentalhealf;

import android.app.Activity;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
public class loginHelper {

    private FirebaseAuth mAuth;
    // Contrstuctor
    public loginHelper() {
        mAuth = FirebaseAuth.getInstance();
    }
    //function which takes the email and password and returns success or failure
    public void loginUser(String email, String password, Activity activity, AuthCallback callback) {
        // checks if its empty and sends a toast if it is
        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(activity, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }
        // attemps to login with email and password
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(activity, task -> {
                    if (task.isSuccessful()) { //if login is successful
                        FirebaseUser user = mAuth.getCurrentUser(); // retreive current user.
                        callback.onSuccess(user);
                    } else { //if login failed
                        callback.onFailure(task.getException());
                    }
                });
    }
    // Registers new user with email and password
    public void registerUser(String email, String password, Activity activity, AuthCallback callback) {
        // checks if its empty and sends a toast if it is
        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(activity, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }
        // creates yser with email and password
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(activity, task -> {
                    if (task.isSuccessful()) { //if its successful
                        FirebaseUser user = mAuth.getCurrentUser();
                        callback.onSuccess(user);
                    } else { // if it fails
                        callback.onFailure(task.getException());
                    }
                });
    }
    // handles function for success and fail for login and register
    public interface AuthCallback {
        void onSuccess(FirebaseUser user);
        void onFailure(Exception e);
    }

}
