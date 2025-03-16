package com.example.mentalhealf;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.Timestamp;

import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MeditationBoxBreathFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MeditationBoxBreathFragment extends Fragment {
    private int step = 0;
    private String txtStep,txtStep1;
    private TextView txtSteps,txtClockSecond, txtTimer;
    private Button btnNext;
    private Timestamp startTime, endTime;
    private CountDownTimer countDownTimer;
    private boolean isTimerRunning = false;
    private long totalTimeElapsed = 0;
    private int breathingPhase = 0;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public MeditationBoxBreathFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MeditationBoxBreathFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MeditationBoxBreathFragment newInstance(String param1, String param2) {
        MeditationBoxBreathFragment fragment = new MeditationBoxBreathFragment();
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
        View view = inflater.inflate(R.layout.fragment_meditation_box_breath, container, false);
        txtSteps = view.findViewById(R.id.txtSteps);
        txtClockSecond = view.findViewById(R.id.txtClockSecond);
        txtTimer = view.findViewById(R.id.txtTimer);

        btnNext = view.findViewById(R.id.btnNext);
        btnNext.setText("Ready?");
        txtSteps.setText("To get started, click the button below.");
        btnNext.setOnClickListener(v -> {
            step += 1;
            breathSteps(step);
            startTimer();
        });
        // Inflate the layout for this fragment
        return view;
    }
    private void breathSteps(int step) {
        if (step == 1){
            txtStep = "Sit comfortably with your back straight. Relax and prepare yourself to begin.";
            startTime = Timestamp.now();
            txtSteps.setText(txtStep);
            UiAnimations.fadeInAnimation(txtSteps);
            txtStep1 = ("Start Box Breathing");
        } else if (step == 2) {
            txtStep1 = ("Finish Box Breathing");
            //txtTimer.setVisibility(View.VISIBLE);
            txtTimer.setText(" ");
            txtClockSecond.setVisibility(View.VISIBLE);
            txtSteps.setTextSize(20);
            startBoxTimer();
        }else if (step == 3) {
            step = 0;
            Intent intent = new Intent(requireContext(), ActivityPostMeditation.class);
            Timestamp endTime = Timestamp.now();
            long durationInMillis = endTime.toDate().getTime() - startTime.toDate().getTime();
            int durationInSeconds = (int) (durationInMillis / 1000);
            int minutes = durationInSeconds / 60;
            int seconds = durationInSeconds % 60;

            String durationFormatted = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
            intent.putExtra("MEDITATION_TYPE", "Meditation - " + "Box Breathing");
            intent.putExtra("DURATION", durationFormatted);
            intent.putExtra("START_TIME", startTime);

            startActivity(intent);
            requireActivity().finish();
        }
    }
    private void startTimer() {
        btnNext.setEnabled(false);
        isTimerRunning = true;
        txtTimer.setVisibility(View.VISIBLE);
        countDownTimer = new CountDownTimer(5000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                btnNext.setText("Inactive for " + millisUntilFinished / 1000 + "s.");
            }
            @Override
            public void onFinish() {
                btnNext.setEnabled(true);
                btnNext.setText(txtStep1);
                isTimerRunning = false;
            }
        }.start();
    }
    private void startBoxTimer(){
        countDownTimer = new CountDownTimer(4000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                updateTotalTime();
                totalTimeElapsed += 1000;
                int secondsRemaining = (int)Math.round((millisUntilFinished / 1000.0));
                txtClockSecond.setText(String.valueOf(  secondsRemaining + " seconds left"));
                switch (breathingPhase) {
                    case 0:
                        txtSteps.setText("Breathe In");
                        UiAnimations.increaseTextSize(txtSteps,1000);

                        break;
                    case 1:
                        txtSteps.setText("Hold");
                        break;
                    case 2:
                        txtSteps.setText("Breathe Out");
                        UiAnimations.decreaseTextSize(txtSteps,1000);
                        break;
                    case 3:
                        txtSteps.setText("Hold");
                        break;
                }
            }

            @Override
            public void onFinish() {

                //updateTotalTime();
                breathingPhase = (breathingPhase + 1) % 4;

                startBoxTimer();
            }
        }.start();
    }
    private void updateTotalTime() {
        int totalSeconds = (int) (totalTimeElapsed / 1000);
        int minutes = totalSeconds / 60;
        int seconds = totalSeconds % 60;
        txtTimer.setText(String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds));
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }
}