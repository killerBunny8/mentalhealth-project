package com.example.mentalhealf;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseUser;

public class SignupActivity extends AppCompatActivity {

    private EditText emailInput, passwordInput, confirmPasswordInput, usernameInput, firstNameInput, lastNameInput;
    private Button signupButton;
    private TextView loginRedirect;
    private loginHelper firebaselogin;
    private Spinner genderInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_signup);

        // Apply window insets
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize helper
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


        // login btn click

        signupButton.setOnClickListener(v ->{
            signUp();
        });

        loginRedirect.setOnClickListener(v -> {
            Toast.makeText(SignupActivity.this, "Redirecting to login page", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(SignupActivity.this, MainActivity.class));
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


        // Proceed to check if username is available
        dupeCheck(user, password);
    }

    public void dupeCheck(User user, String password){
        String username = user.getUsername();
        firebaselogin.dupeUsername(username, new loginHelper.UsernameCheckCallback() {
            @Override
            public void onResult(boolean isTaken) {
                if (isTaken) {
                    Toast.makeText(SignupActivity.this, "Username already taken!", Toast.LENGTH_SHORT).show();
                    return; // ✅ Stops execution if username exists
                }

                // ✅ Only runs if username is available
                registerUser(user, password);
            }

            @Override
            public void onFailure(Exception e) {
                Toast.makeText(SignupActivity.this, "Error checking username: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void registerUser(User user, String password) {
        firebaselogin.registerUser(user.getEmail(), password, user, SignupActivity.this, new loginHelper.AuthCallback() {
            @Override
            public void onSuccess(FirebaseUser user) { // Success
                Toast.makeText(SignupActivity.this, "Sign-up successful!", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(SignupActivity.this, MainActivity.class));
            }

            @Override
            public void onFailure(Exception e) { // Failure
                Toast.makeText(SignupActivity.this, "Sign-up failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }




}
