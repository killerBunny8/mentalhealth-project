package com.example.mentalhealf;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private EditText emailInput, passwordInput;
    private Button loginButton;
    private TextView registerRedirect, resetPassRedirect;
    private loginHelper firebaselogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        View rootView = findViewById(android.R.id.content);
        rootView.setForceDarkAllowed(false);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });



        /// initialise firebase login
        firebaselogin = new loginHelper();

        // variables that link to ui elements
        emailInput = findViewById(R.id.emailInput);
        passwordInput = findViewById(R.id.passwordInput);
        loginButton = findViewById(R.id.loginButton);
        registerRedirect = findViewById(R.id.registerRedirect);
        resetPassRedirect = findViewById(R.id.resetpasswordredirect);

        // button click listener
        loginButton.setOnClickListener(v -> {
            // gets details from the UI as a string
            String email = emailInput.getText().toString().trim();
            String password = passwordInput.getText().toString().trim();
            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(MainActivity.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                return;
            }
            firebaselogin.getUserEmail(email, new loginHelper.EmailCallback() {
                @Override
                public void onSuccess(User user) {
                    String email = user.getEmail();
                    // Proceed with login after retrieving email
                    firebaselogin.loginUser(email, password, MainActivity.this, new loginHelper.AuthCallback() {
                        @Override
                        public void onSuccess(FirebaseUser user) {
                            Toast.makeText(MainActivity.this, "Login successful!", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                            intent.putExtra("user", email);
                            startActivity(intent);
                        }

                        @Override
                        public void onFailure(Exception e) {
                            Toast.makeText(MainActivity.this, "Login failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }

                @Override
                public void onFailure(Exception e) {
                    Toast.makeText(MainActivity.this, "Username not found: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

        });

        // navigate to sigup page or reset password page
        registerRedirect.setOnClickListener(v -> {
            Toast.makeText(MainActivity.this, "Redirecting to registration page", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(MainActivity.this, SignupActivity.class));
        });
        resetPassRedirect.setOnClickListener(v -> {
            Toast.makeText(MainActivity.this, "Redirecting to registration page", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(MainActivity.this, ResetPasswordActivity.class));
        });

    }
}