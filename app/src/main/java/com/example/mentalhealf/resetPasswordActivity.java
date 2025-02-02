package com.example.mentalhealf;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;


public class resetPasswordActivity extends AppCompatActivity {

    private EditText emailInput;
    private Button resetPasswordButton;
    private FirebaseAuth mAuth;
    private passwordHelper pwordHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_reset_password);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        // init firebase
        mAuth = FirebaseAuth.getInstance();
        //get fields from UI
        emailInput = findViewById(R.id.emailInput);
        resetPasswordButton = findViewById(R.id.resetPasswordButton);
        // init reset password class
        pwordHelper = new passwordHelper();

        // button onclick listener
        resetPasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { //function
                // gets text from ui
                String email = emailInput.getText().toString().trim();

                // Use the helper class to reset the password
                pwordHelper.resetPassword(email, resetPasswordActivity.this, new passwordHelper.ResetCallback() {
                    @Override
                    public void onSuccess() { //success
                        Toast.makeText(resetPasswordActivity.this, "Password reset email sent!", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(Exception e) { //failure
                        Toast.makeText(resetPasswordActivity.this, "Failed to send reset email: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            };
        });
    }
}