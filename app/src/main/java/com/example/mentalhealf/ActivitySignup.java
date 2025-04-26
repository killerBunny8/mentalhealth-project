package com.example.mentalhealf;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.firebase.auth.FirebaseUser;

public class ActivitySignup extends AppCompatActivity {

    private EditText emailInput, passwordInput, confirmPasswordInput, usernameInput, firstNameInput, lastNameInput;
    private Button signupButton;
    private TextView loginRedirect, privacyPolicy;
    private loginHelper firebaselogin;
    private Spinner genderInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        getWindow().setNavigationBarColor(ContextCompat.getColor(this, android.R.color.black));
        // Initialise helper
        firebaselogin = new loginHelper();

        // Link ui elements
        emailInput = findViewById(R.id.emailInput);
        passwordInput = findViewById(R.id.passwordInput);
        confirmPasswordInput = findViewById(R.id.confirmPasswordInput);
        signupButton = findViewById(R.id.signupButton);
        loginRedirect = findViewById(R.id.loginRedirect);
        usernameInput = findViewById(R.id.usernameInput);
        firstNameInput = findViewById(R.id.firstNameInput);
        lastNameInput = findViewById(R.id.lastNameInput);
        genderInput = findViewById(R.id.genderInput);
        privacyPolicy =  findViewById(R.id.privacyPolicyLink1);


        // login btn click

        signupButton.setOnClickListener(v ->{
            signUp();
        });

        loginRedirect.setOnClickListener(v -> {
            Toast.makeText(ActivitySignup.this, "Redirecting to login page", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(ActivitySignup.this, ActivityMain.class));
        });
        privacyPolicy.setOnClickListener(v -> {
            Toast.makeText(ActivitySignup.this, "Redirecting to login page", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(ActivitySignup.this, ActivityPrivacy.class));
        });
    }

    private void signUp() {
        String email = emailInput.getText().toString().trim();
        String password = passwordInput.getText().toString().trim();
        String confirmPassword = confirmPasswordInput.getText().toString().trim();
        String username = usernameInput.getText().toString().trim();
        String firstName = firstNameInput.getText().toString().trim();
        String lastName = lastNameInput.getText().toString().trim();
        String gender = genderInput.getSelectedItem().toString();

        // Validate inputs
        if (email.isEmpty() || password.isEmpty() || username.isEmpty() || firstName.isEmpty() || lastName.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!password.equals(confirmPassword)) {
            Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show();
            return;
        }
        User user = new User(null, username, firstName, lastName, email, gender);


        // check if username is available
        dupeCheck(user, password);
    }

    public void dupeCheck(User user, String password){
        String username = user.getUsername();
        firebaselogin.dupeUsername(username, new loginHelper.UsernameCheckCallback() {
            @Override
            public void onResult(boolean isTaken) {
                if (isTaken) {
                    Toast.makeText(ActivitySignup.this, "Username already taken!", Toast.LENGTH_SHORT).show();
                    return; // Stops if username exists
                }

                // Only runs if username is available
                registerUser(user, password);
            }

            @Override
            public void onFailure(Exception e) {
                Toast.makeText(ActivitySignup.this, "Error checking username: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void registerUser(User user, String password) {
        firebaselogin.registerUser(user.getEmail(), password, user, ActivitySignup.this, new loginHelper.AuthCallback() {
            @Override
            public void onSuccess(FirebaseUser user) { // Success
                Toast.makeText(ActivitySignup.this, "Sign-up successful!", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(ActivitySignup.this, ActivityMain.class));
            }

            @Override
            public void onFailure(Exception e) { // Failure
                Toast.makeText(ActivitySignup.this, "Sign-up failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }




}
