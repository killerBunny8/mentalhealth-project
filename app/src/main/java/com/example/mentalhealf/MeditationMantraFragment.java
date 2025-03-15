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
 * Use the {@link MeditationMantraFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MeditationMantraFragment extends Fragment {
    private int step = 0;
    private String txtStep;
    private TextView txtSteps;
    private Button btnMantraNext;
    private Timestamp startTime, endTime;
    private CountDownTimer countDownTimer;
    private boolean isTimerRunning = false;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public MeditationMantraFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MeditationMantraFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MeditationMantraFragment newInstance(String param1, String param2) {
        MeditationMantraFragment fragment = new MeditationMantraFragment();
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
        View view = inflater.inflate(R.layout.fragment_meditation_mantra, container, false);
        txtSteps = view.findViewById(R.id.txtSteps);
        btnMantraNext = view.findViewById(R.id.btnMantraNext);

        btnMantraNext.setOnClickListener(v -> {
            step += 1;
            mantraSteps(step);
            startTimer();
        });

        // Inflate the layout for this fragment
        return view;
    }

    private void mantraSteps(int step) {
        if (step == 1){
            txtStep = "Sit comfortably with your back straight. Close your eyes and take a few deep breaths.";
            startTime = Timestamp.now();
            txtSteps.setText(txtStep);
        } else if (step == 2) {
            txtStep = "Choose a mantra that resonates with you.\n a. I am calm.\nb. I am present.\nc. I’m not anxious.\n d. I’m not angry.";
            txtSteps.setText(txtStep);
        } else if (step == 3) {
            txtStep = "Silently repeat your mantra in your mind. Focus on the sound and vibration.";
            txtSteps.setText(txtStep);
        } else if (step == 4) {
            txtStep = "If your mind wanders, gently bring your focus back to the mantra.";
            txtSteps.setText(txtStep);
        } else if (step == 5) {
            txtStep = "Continue repeating the mantra for a few minutes. Let it fill your mind and body.";
            txtSteps.setText(txtStep);
        } else if (step == 6) {
            txtStep = "When you're ready, slowly open your eyes and return to the present moment.";
            txtSteps.setText(txtStep);
        } else if (step == 7) {
            txtStep = "Spend a few moments visualizing this person in front of you and repeat the statements:\n a. May we be filled with love and kindness.\n b. “May we be safe”\n c. “May we be well”\n d. “May we be happy and at ease”";
            txtSteps.setText(txtStep);
            btnMantraNext.setText("Finish");
        }else if (step == 8) {
            step = 0;
            Intent intent = new Intent(requireContext(), ActivityPostMeditation.class);
            Timestamp endTime = Timestamp.now();
            long durationInMillis = endTime.toDate().getTime() - startTime.toDate().getTime();
            int durationInSeconds = (int) (durationInMillis / 1000);
            int minutes = durationInSeconds / 60;
            int seconds = durationInSeconds % 60;

            String durationFormatted = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
            intent.putExtra("MEDITATION_TYPE", "Mantra Meditation");
            intent.putExtra("DURATION", durationFormatted);
            intent.putExtra("START_TIME", startTime);

            startActivity(intent);
            requireActivity().finish();
        }
    }
    private void startTimer() {
        btnMantraNext.setEnabled(false);
        isTimerRunning = true;

        countDownTimer = new CountDownTimer(5000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                btnMantraNext.setText("Next (" + millisUntilFinished / 1000 + "s)");
            }
            @Override
            public void onFinish() {
                btnMantraNext.setEnabled(true);
                btnMantraNext.setText("Next");
                isTimerRunning = false;
            }
        }.start();
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }
}