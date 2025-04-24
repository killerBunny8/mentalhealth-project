package com.example.mentalhealf;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ActivityMain extends AppCompatActivity {
    private FirebaseAuth mAuth;


    private EditText emailInput, passwordInput;
    private Button loginButton;
    private TextView registerRedirect, resetPassRedirect;
    private loginHelper firebaselogin;
    private SharedPreferences prefs;
    private CheckBox saveLogin;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().setNavigationBarColor(ContextCompat.getColor(this, android.R.color.black));
        prefs = getSharedPreferences("MyPrefs", MODE_PRIVATE);

       // login check
        if (prefs.getBoolean("isLoggedIn", false)) {
            startActivity(new Intent(ActivityMain.this, ActivityHome.class));
            finish();
        }


        /// initialise firebase login
        firebaselogin = new loginHelper();

        // variables that link to ui elements
        emailInput = findViewById(R.id.emailInput);
        passwordInput = findViewById(R.id.passwordInput);
        loginButton = findViewById(R.id.loginButton);
        registerRedirect = findViewById(R.id.registerRedirect);
        resetPassRedirect = findViewById(R.id.resetpasswordredirect);
        saveLogin= findViewById(R.id.checkBoxSaveLogin);

        mAuth = FirebaseAuth.getInstance();

        // Get the current user
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser != null) {
            String userId = currentUser.getUid();
            Log.d("FirebaseUser", "User ID: " + userId);
        } else {
            Log.d("no cyrrentuser", "onCreate: " + "no user logged in");
        }


        // button click listener
        loginButton.setOnClickListener(v -> {
            String email = emailInput.getText().toString().trim();
            String password = passwordInput.getText().toString().trim();

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(ActivityMain.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            firebaselogin.getUserEmail(email, new loginHelper.EmailCallback() {
                @Override
                public void onSuccess(User user) {
                    String email = user.getEmail();
                    firebaselogin.loginUser(email, password, ActivityMain.this, new loginHelper.AuthCallback() {
                        @Override
                        public void onSuccess(FirebaseUser user) {
                            Toast.makeText(ActivityMain.this, "Login successful!", Toast.LENGTH_SHORT).show();
                            if (saveLogin.isChecked()){
                                SharedPreferences.Editor editor = prefs.edit();
                                editor.putBoolean("isLoggedIn", true);
                                editor.putString("userEmail", email);
                                editor.apply();
                            }


                            Intent intent = new Intent(ActivityMain.this, ActivityHome.class);
                            intent.putExtra("user", email);
                            startActivity(intent);
                        }

                        @Override
                        public void onFailure(Exception e) {
                            Toast.makeText(ActivityMain.this, "Login failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }

                @Override
                public void onFailure(Exception e) {
                    Toast.makeText(ActivityMain.this, "Username not found: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        });

        // navigate to sigup page or reset password page
        registerRedirect.setOnClickListener(v -> {
            Toast.makeText(ActivityMain.this, "Redirecting to registration page", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(ActivityMain.this, ActivitySignup.class));
        });
        resetPassRedirect.setOnClickListener(v -> {
            Toast.makeText(ActivityMain.this, "Redirecting to registration page", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(ActivityMain.this, ActivityResetPassword.class));
        });

    }
}