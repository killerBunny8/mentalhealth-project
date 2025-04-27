package com.example.mentalhealf;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;



//import org.apache.commons.math3.analysis.function.Log;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import android.util.Log;

import com.google.firebase.Timestamp;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MeditationExerciseFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MeditationExerciseFragment extends Fragment {
    private TextView txtTime;
    private Button btnWalkingMeditation, btnFinishWalk;
    private Spinner spinner;
    private CountDownTimer countDownTimer;
    private boolean isTimerRunning = false;
    private long timeLeftInMillis;
    private int totalTimeInSeconds, minutes, seconds;
    private long startTimeInMillis;
    private Timer freestyleTime;
    private String selectedDate, duration;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public MeditationExerciseFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MeditationExerciseFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MeditationExerciseFragment newInstance(String param1, String param2) {
        MeditationExerciseFragment fragment = new MeditationExerciseFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_meditation_exercise, container, false);
        //layout components
        txtTime = view.findViewById(R.id.txtTime);
        btnWalkingMeditation = view.findViewById(R.id.btnWalkingMeditation);
        btnFinishWalk = view.findViewById(R.id.btnFinishWalk);
        spinner = view.findViewById(R.id.spinner);
        selectedDate = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
        //onclick listner for button
        btnWalkingMeditation.setOnClickListener(v -> {
            if (!isTimerRunning){
                startTimer();
            } else {
                Toast.makeText(requireContext(), "Timer already started.", Toast.LENGTH_SHORT).show();
            }
        });
        btnFinishWalk.setOnClickListener(v -> {
            if (isTimerRunning) {
                stopTimer();
                goNextPage();
            } else {
                Toast.makeText(requireContext(), "No timer started.", Toast.LENGTH_SHORT).show();
            }

        });
        return view;
    }

    private void goNextPage() {
        String meditationType = spinner.getSelectedItem().toString();
        if (meditationType.equals("Freestyle")) {
            meditationType = "Freestyle Walk";
        } else {
            meditationType = "Walk for: " + meditationType;
        }

        long elapsedTimeInMillis = System.currentTimeMillis() - startTimeInMillis;
        int elapsedTimeInSeconds = (int) (elapsedTimeInMillis / 1000);
        int minutes = (elapsedTimeInSeconds % 3600) / 60;
        int seconds = elapsedTimeInSeconds % 60;
        duration = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);

        Timestamp startTime = Timestamp.now();

        Intent intent = new Intent(requireContext(), ActivityPostMeditation.class);
        intent.putExtra("MEDITATION_TYPE", meditationType);
        intent.putExtra("DURATION", duration);
        intent.putExtra("START_TIME", startTime);

        startActivity(intent);
        requireActivity().finish();
    }
    //countdowntimer
    private void startTimer() {
        String selectedTime = spinner.getSelectedItem().toString();

        if (selectedTime.equals("Freestyle")) {
            startFreestyleTimer();
        } else if (selectedTime.equals("Walk Duration")) {
            Toast.makeText(requireContext(), "No Duration Selected", Toast.LENGTH_SHORT).show();
            return;
        } else {
            timeLeftInMillis = convertTimeToSeconds(selectedTime) * 1000;
            totalTimeInSeconds = (int) (timeLeftInMillis / 1000);
            startTimeInMillis = System.currentTimeMillis();
            countDownTimer = new CountDownTimer(timeLeftInMillis, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    timeLeftInMillis = millisUntilFinished;
                    updateTimer();
                }
                @Override
                public void onFinish() {
                    String selectedTime = spinner.getSelectedItem().toString();
                    long remainingTimeMillis = calculateRemainingTime(selectedTime);

                    Log.d("MeditationExerciseFragment", "Remaining time: " + remainingTimeMillis + " ms");
                    Toast.makeText(requireContext(), "Walk Completed!", Toast.LENGTH_SHORT).show();
                    isTimerRunning = false;
                    goNextPage();
                }
            }.start();
        }

        isTimerRunning = true;
    }
    //countup timer
    private void startFreestyleTimer() {
        startTimeInMillis = System.currentTimeMillis();
        freestyleTime = new Timer();
        freestyleTime.schedule(new TimerTask() {
            @Override
            public void run() {
                timeLeftInMillis = System.currentTimeMillis() - startTimeInMillis;
                requireActivity().runOnUiThread(() -> {
                    updateTimer();
                });
            }
        }, 0, 100);
    }
    private void updateTimer() {
        int minutes = (int) (timeLeftInMillis / 1000) / 60;
        int seconds = (int) (timeLeftInMillis / 1000) % 60;

        selectedDate = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
        txtTime.setText(selectedDate);

        //int progress = (int) (timeLeftInMillis / 1000);
    }
    //stop timer (or automatically end when it hits 0)
    private void stopTimer() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        Log.d("MeditationExerciseFragment",
                "Selected time: " + txtTime.getText());

        long elapsedTimeInMillis = System.currentTimeMillis() - startTimeInMillis;
        int elapsedTimeInSeconds = (int) (elapsedTimeInMillis / 1000);
        int minutes = (elapsedTimeInSeconds % 3600) / 60;
        int seconds = elapsedTimeInSeconds % 60;
        duration = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);

        Toast.makeText(requireContext(), "Walk Ended", Toast.LENGTH_SHORT).show();
        isTimerRunning = false;
    }
    //convert time to seconds
    private int convertTimeToSeconds(String time) {
        return Integer.parseInt(time.replaceAll("\\D+", "")) * 60;
    }
    //convert time to milisecs
    private long convertTimeToMillis(String time) {
        return convertTimeToSeconds(time) * 1000L;
    }
    //calculate remaaining time
    private long calculateRemainingTime(String selectedTime) {
        long selectedTimeMillis = convertTimeToMillis(selectedTime);
        long elapsedTimeMillis = System.currentTimeMillis() - startTimeInMillis;
        return selectedTimeMillis - elapsedTimeMillis;
    }
    //end timer completely
    @Override
    public void onDestroy() {
        super.onDestroy();

        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        if (freestyleTime != null) {
            freestyleTime.cancel();
        }
    }

}