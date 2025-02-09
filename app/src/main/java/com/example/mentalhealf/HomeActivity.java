package com.example.mentalhealf;

import android.os.Bundle;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class HomeActivity extends AppCompatActivity {
    private loginHelper firebaselogin;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        String email = getIntent().getStringExtra("user");
        getUser(email);
    }

    public void getUser(String email){
        firebaselogin = new loginHelper();
        firebaselogin.getUserEmail(email, new loginHelper.EmailCallback() {
            @Override
            public void onSuccess(User user) {
                Log.d("USER DETAILS", "onSuccess: "+ user.getFirstName());
                //updateWelcome(user);
            }

            @Override
            public void onFailure(Exception e) {

            }
        });
    }

    public void updateWelcome(User user) {

    }
}