package com.example.mentalhealf;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;


public class ActivityResetPassword extends AppCompatActivity {

    private EditText emailInput;
    private Button resetPasswordButton;
    private FirebaseAuth mAuth;
    private passwordHelper pwordHelper;
    private TextView loginRedirect;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        getWindow().setNavigationBarColor(ContextCompat.getColor(this, android.R.color.black));


        // init firebase
        mAuth = FirebaseAuth.getInstance();
        //get fields from UI
        emailInput = findViewById(R.id.emailInput);
        resetPasswordButton = findViewById(R.id.resetPasswordButton);
        loginRedirect = findViewById(R.id.loginRedirect);
        // init reset password class
        pwordHelper = new passwordHelper();

        // button onclick listener
        resetPasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { //function
                // gets text from ui
                String email = emailInput.getText().toString().trim();

                // Use the helper class to reset the password
                pwordHelper.resetPassword(email, ActivityResetPassword.this, new passwordHelper.ResetCallback() {
                    @Override
                    public void onSuccess() { //success
                        Toast.makeText(ActivityResetPassword.this, "Password reset email sent!", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(Exception e) { //failure
                        Toast.makeText(ActivityResetPassword.this, "Failed to send reset email: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            };
        });

        // login btn click
        loginRedirect.setOnClickListener(v -> {
            Toast.makeText(ActivityResetPassword.this, "Redirecting to login page", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(ActivityResetPassword.this, ActivityMain.class));
        });

    }
}