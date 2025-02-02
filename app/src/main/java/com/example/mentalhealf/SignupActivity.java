package com.example.mentalhealf;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseUser;

public class SignupActivity extends AppCompatActivity {

    private EditText emailInput, passwordInput, confirmPasswordInput;
    private Button signupButton;
    private TextView loginRedirect;
    private loginHelper firebaselogin;

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

        // button click handler
        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //get text from ui
                String email = emailInput.getText().toString().trim();
                String password = passwordInput.getText().toString().trim();
                String confirmPassword = confirmPasswordInput.getText().toString().trim();
                //send toast if passwords dont match
                if (!password.equals(confirmPassword)) {
                    Toast.makeText(SignupActivity.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
                    return;
                }

                // register user function
                firebaselogin.registerUser(email, password, SignupActivity.this, new loginHelper.AuthCallback() {
                    @Override
                    public void onSuccess(FirebaseUser user) { //success
                        Toast.makeText(SignupActivity.this, "Sign up successful!", Toast.LENGTH_SHORT).show();
                        // navigate to login
                    }

                    @Override
                    public void onFailure(Exception e) { //failed and send toast to explaiun why
                        Toast.makeText(SignupActivity.this, "Sign up failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        // login btn click
        loginRedirect.setOnClickListener(v -> {
            Toast.makeText(SignupActivity.this, "Redirecting to login page", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(SignupActivity.this, MainActivity.class));
        });
    }
}
