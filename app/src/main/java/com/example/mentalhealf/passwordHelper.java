///////////////////////////////////////////////////////////////////////////
//                       RESET PASSWORD CLASS                            //
//                                                                       //
// This class manages firebase password reset                            //
//                                                                       //
///////////////////////////////////////////////////////////////////////////

package com.example.mentalhealf;

import android.app.Activity;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class passwordHelper {

    private final FirebaseAuth mAuth;

    public passwordHelper() {
        mAuth = FirebaseAuth.getInstance();
    }

    // method to reset password
    public void resetPassword(String email, Activity activity, ResetCallback callback) {
        // checks if its empty and sends a toast if it is
        if (email.isEmpty()) {
            Toast.makeText(activity, "Please enter your email address", Toast.LENGTH_SHORT).show();
            return;
        }
        // sends an email using firebase
        mAuth.sendPasswordResetEmail(email)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) { //if email sent
                        callback.onSuccess();
                    } else {
                        callback.onFailure(task.getException()); //if email failed to send
                    }
                });
    }

    // Callback interface to handle success and failure
    public interface ResetCallback {
        void onSuccess();
        void onFailure(Exception e);
    }
}
