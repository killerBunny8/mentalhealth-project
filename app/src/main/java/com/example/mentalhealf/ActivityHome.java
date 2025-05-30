package com.example.mentalhealf;

import android.content.Intent;
import android.content.SharedPreferences;
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

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.firebase.auth.FirebaseAuth;

import java.util.Random;

public class ActivityHome extends AppCompatActivity {
    private loginHelper firebaselogin;
    private FirebaseAuth mAuth;


    private moodLogHelper moodLogHelper;
    private User user;
    private Button logmood,btnNewAffirmation;

    private TextView greetingText,imgSad, imgAbitSad, imgOkay, imgGood, imgGreat,affirmationText;
    private static int selectMood = 0;
    private EditText explanation;
    private Spinner activitySpinner;
    private ImageView iconMindfullness, iconMessages;
    private String email;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        getWindow().setNavigationBarColor(ContextCompat.getColor(this, android.R.color.black));
        //get user to display name on layout
        email = getIntent().getStringExtra("user");
        if (email == null) {
            SharedPreferences prefs = getSharedPreferences("MyPrefs", MODE_PRIVATE);
            email = prefs.getString("userEmail", null);
        }
        getUser(email);
        // define layout componants
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
        affirmationText = findViewById(R.id.affirmationText);
        btnNewAffirmation = findViewById(R.id.btnNewAffirmation);

        //Set onclick listeners for mood
        imgSad.setOnClickListener(v -> selectEmoji(imgSad, 1));
        imgAbitSad.setOnClickListener(v -> selectEmoji(imgAbitSad, 2));
        imgOkay.setOnClickListener(v -> selectEmoji(imgOkay, 3));
        imgGood.setOnClickListener(v -> selectEmoji(imgGood, 4));
        imgGreat.setOnClickListener(v -> selectEmoji(imgGreat, 5));
        //set onclick lsitener with basic checks
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
        //onclick listener which takes you to mindfulness page
        iconMindfullness.setOnClickListener(v -> {
            Intent mindfullness = new Intent(this, ActivityMindfullness.class);
            Toast.makeText(this,"Navigating to the Mindfulness.", Toast.LENGTH_SHORT).show();
            startActivity(mindfullness);
        });
        //onclick listener which takes you to feedback page
        iconMessages.setOnClickListener(v -> {
            Intent feedback = new Intent(this, ActivityFeedback.class);
            Toast.makeText(this,"Navigating to the Feedback.", Toast.LENGTH_SHORT).show();
            startActivity(feedback);
        });
        setAffirmations();
    }
    //function which sets
    private void setAffirmations() {
        String[] affirmations = {
                "Asking for help is a sign of self-respect and self-awareness.",
                "Changing my mind is a strength, not a weakness.",
                "Every decision I make is supported by my whole and inarguable experience.",
                "I affirm and encourage others, as I do myself.",
                "I alone hold the truth of who I am.",
                "I am allowed to ask for what I want and what I need.",
                "I am allowed to feel good.",
                "I am capable of balancing ease and effort in my life.",
                "I am complete as I am, others simply support me.",
                "I am content and free from pain.",
                "I am doing the work that works for me.",
                "I am good and getting better.",
                "I am growing and I am going at my own pace.",
                "I am held and supported by those who love me.",
                "I am in charge of how I feel and I choose to feel happy.",
                "I am listening and open to the messages the universe has to offer today.",
                "I am loved and worthy.",
                "I am more than my circumstances dictate.",
                "I am open to healing.",
                "I am optimistic because today is a new day.",
                "I am peaceful and whole.",
                "I am proof enough of who I am and what I deserve.",
                "I am responsible for myself, and I start there.",
                "I am safe and surrounded by love and support.",
                "I am still learning so it’s okay to make mistakes.",
                "I am understood and my perspective is important.",
                "I am valued and helpful.",
                "I am well-rested and excited for the day.",
                "I am worthy of investing in myself.",
                "I belong here, and I deserve to take up space.",
                "I breathe in healing, I exhale the painful things that burden my heart.",
                "I breathe in trust, I exhale doubt.",
                "I can be soft in my heart and firm in my boundaries.",
                "I can control how I respond to things that are confronting.",
                "I can hold two opposing feelings at once, it means I am processing.",
                "I celebrate the good qualities in others and myself.",
                "I deserve an affirming touch on my own terms.",
                "I deserve information and I deserve moments of silence, too.",
                "I deserve self-respect and a clean space.",
                "I do all things in love.",
                "I do not have to linger in dark places; there is help for me here.",
                "I do not pretend to be anyone or anything other than who I am.",
                "I do not rise and fall for another.",
                "I do not rush through my life, I temper speed with stillness.",
                "I embrace change seamlessly and rise to the new opportunity it presents.",
                "I embrace the questions in my heart and welcome the answers in their own time.",
                "I grow towards my interests, like a plant reaching for the sun.",
                "I have come farther than I would have ever thought possible, and I’m learning along the way.",
                "I have everything I need to succeed.",
                "I hold community for others, and am held in community by others.",
                "I hold wisdom beyond knowledge.",
                "I invite abundance and a generous heart.",
                "I invite art and music into my life.",
                "I leave room in my life for spontaneity.",
                "I let go of the things that sit achingly out of reach.",
                "I look forward to tomorrow and the opportunities that await me.",
                "I love that I love what I love.",
                "I make decisions based on a good gut, I make changes based on a growing heart.",
                "I make time to experience grief and sadness when necessary.",
                "I nourish myself with kind words and joyful foods.",
                "I practice gratitude for all that I have, and all that is yet to come.",
                "I release the fears that do not serve me.",
                "I respect the cycle of the seasons.",
                "I seek out mystery in the ordinary.",
                "I strive for joy, not for perfection.",
                "I tell the truth about who I am and what I need from others.",
                "I uplift my joy and the joy of others.",
                "I welcome the wisdom that comes with growing older.",
                "I welcome what is, I welcome what comes.",
                "I will allow myself to evolve.",
                "Letting go creates space for opportunities to come.",
                "My body is beautiful in this moment and at its current size.",
                "My body is worthy of being cared for and adorned in beautiful garments.",
                "My feelings deserve names, deserve recognition, deserve to be felt.",
                "My heart is open to helpfulness from myself and from others.",
                "My heart knows its own way.",
                "My life is not a race or competition.",
                "My perspective is unique and important.",
                "My pleasure does not require someone else’s pain.",
                "My sensitivity is beautiful, and my feelings and emotions are valid.",
                "My weirdness is wonderful.",
                "Saying “no” is an act of self-affirmation, too.",
                "Sometimes the work is resting.",
                "There is growth in stillness.",
                "There is peace in changing your mind when it is done in love.",
                "There is poetry in everything, if I look for it.",
                "There is room for me at the table.",
                "There is something in this world that only I can do. That is why I am here.",
                "There is strength in quiet, there is vulnerability in being loud.",
                "Today I celebrate that I am younger than I’m ever going to be.",
                "Today is an opportunity to grow and learn.",
                "When I feel fear, I feed trust.",
                "When I focus on my reason for being, I am infinitely brave.",
                "When I forgive myself, I free myself.",
                "When I release shame, I move into myself more beautifully.",
                "When I root into the earth, the earth rises to support me.",
                "When I speak my needs, I receive them abundantly.",
                "When I talk to myself as I would a friend, I see all my best qualities and I allow myself to shine.",
                "Words may shape me, but they do not make me. I am here already."
        };
        Random random = new Random();
        affirmationText.setText(affirmations[random.nextInt(affirmations.length)]);

        btnNewAffirmation.setOnClickListener(v -> {
            affirmationText.setText(affirmations[random.nextInt(affirmations.length)]);
        });
    }
    //function that helps with greeting message
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
    //enlarges selected emoji after resetting them all to old font size.
    public void selectEmoji(TextView emoji, int moodVal){
        resetEmoji();
        emoji.setTextSize(64);
        selectMood = moodVal;

    }
    //sets fontsize of all emojis the same, while also resetting the int value
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
    //function that logs mood, has neccesary paramters
    private void logMood(int selectMood, String description, String activity) {
        //new instance of moodloghelper
        moodLogHelper = new moodLogHelper();
        //function from moodloghelper
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


