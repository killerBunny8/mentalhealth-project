package com.example.mentalhealf;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;

public class ActivityHome extends AppCompatActivity {
    private loginHelper firebaselogin;
    private FirebaseAuth mAuth;


    private moodLogHelper moodLogHelper;
    private User user;
    private Button logmood;

//    private static TextView imgSad;
//    private static TextView imgAbitSad;
//    private static TextView imgOkay;
//    private static TextView imgGood;
//    private static TextView imgGreat;
    private TextView greetingText,imgSad, imgAbitSad, imgOkay, imgGood, imgGreat;
    private static int selectMood = 0;
    private EditText explanation;
    private Spinner activitySpinner;
    private ImageView iconMindfullness, iconMessages;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setNavigationBarColor(ContextCompat.getColor(this, android.R.color.black));
        }


        String email = getIntent().getStringExtra("user");
        getUser(email);

        logmood = findViewById(R.id.logMoodButton);

        greetingText = findViewById(R.id.greetingText);
        imgSad = findViewById(R.id.imgSad);
        imgAbitSad = findViewById(R.id.imgAbitSad);
        imgOkay = findViewById(R.id.imgOkay);
        imgGood = findViewById(R.id.imgGood);
        imgGreat = findViewById(R.id.imgGreat);

        explanation = findViewById(R.id.inputMoodExplanation);
        activitySpinner= findViewById(R.id.activitySpinner);

        iconMessages = findViewById(R.id.iconMessages);
        iconMindfullness=findViewById(R.id.iconMindfullness);

        imgSad.setOnClickListener(v -> selectEmoji(imgSad, 1));
        imgAbitSad.setOnClickListener(v -> selectEmoji(imgAbitSad, 2));
        imgOkay.setOnClickListener(v -> selectEmoji(imgOkay, 3));
        imgGood.setOnClickListener(v -> selectEmoji(imgGood, 4));
        imgGreat.setOnClickListener(v -> selectEmoji(imgGreat, 5));

        logmood.setOnClickListener(v -> {
            if (selectMood== 0 ) {
                Toast.makeText(this, "No mood selected, please choose one to logMood", Toast.LENGTH_SHORT).show();
                return;
            }
            Log.d("The Value", "onCreate: " + selectMood);

            String description = explanation.getText().toString().trim();
            String activity = activitySpinner.getSelectedItem().toString();

            if (description.isEmpty()) {
                description = "No description given.";
            }
            if (activity.equals("Current Activity")) {
                Toast.makeText(ActivityHome.this, "Please select an activity before logging.", Toast.LENGTH_SHORT).show();
                return;
            }
            logMood(selectMood, description, activity);


        });

        iconMindfullness.setOnClickListener(v -> {
            Intent mindfullness = new Intent(this, ActivityMindfullness.class);
            Toast.makeText(this,"Navigating to the Mindfulness.", Toast.LENGTH_SHORT).show();
            startActivity(mindfullness);
        });

    }

    private void getUser(String email){
        firebaselogin = new loginHelper();
        firebaselogin.getUserEmail(email, new loginHelper.EmailCallback() {
            @Override
            public void onSuccess(User user) {
                Log.d("USER DETAILS", "onSuccess: "+ user.getFirstName());
                updateWelcome(user);
            }
            @Override
            public void onFailure(Exception e) {

            }
        });
    }

    private void updateWelcome(User user) {
        greetingText.setText("Hello "+ user.getFirstName() + ", How Are You Feeling Today?");
    }

    public void selectEmoji(TextView emoji, int moodVal){
        resetEmoji();
        emoji.setTextSize(64);
        selectMood = moodVal;

    }
    private void resetEmoji() {
        imgSad.setBackgroundColor(Color.TRANSPARENT);
        imgAbitSad.setBackgroundColor(Color.TRANSPARENT);
        imgOkay.setBackgroundColor(Color.TRANSPARENT);
        imgGood.setBackgroundColor(Color.TRANSPARENT);
        imgGreat.setBackgroundColor(Color.TRANSPARENT);

        imgSad.setTextSize(48);
        imgAbitSad.setTextSize(48);
        imgOkay.setTextSize(48);
        imgGood.setTextSize(48);
        imgGreat.setTextSize(48);
        selectMood = 0;
    }

    private void logMood(int selectMood, String description, String activity) {
        moodLogHelper = new moodLogHelper();


        moodLogHelper.logMood(selectMood, description, activity, new moodLogHelper.MoodLogCallback() {
                    @Override
                    public void onSuccess(String message) {
                        Toast.makeText(ActivityHome.this, message, Toast.LENGTH_SHORT).show();
                        //reset fields
                        explanation.setText("");
                        resetEmoji();
                        activitySpinner.setSelection(0);
                    }

                    @Override
                    public void onFailure(String error) {
                        Toast.makeText(ActivityHome.this, "Error: " + error, Toast.LENGTH_SHORT).show();
                    }

                }
        );
    }
}


