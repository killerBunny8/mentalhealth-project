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
 * Use the {@link MeditationMettaFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MeditationMettaFragment extends Fragment {
    private int step =0;
    private String txtStep;
    private TextView txtSteps;
    private Button btnMettaNext;
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

    public MeditationMettaFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MeditationMettaFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MeditationMettaFragment newInstance(String param1, String param2) {
        MeditationMettaFragment fragment = new MeditationMettaFragment();
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
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_meditation_metta, container, false);
        txtSteps = view.findViewById(R.id.txtSteps);

        btnMettaNext=view.findViewById(R.id.btnMettaNext);
        btnMettaNext.setOnClickListener(v -> {
            step += 1 ;
            mettaSteps(step);
            startTimer();

        });
        startTimer();

        return view;
    }

    private void mettaSteps(int step){
        if (step == 1){
            txtStep = " Bring awareness to your breath for a few moments, paying attention to each inhalation and exhalation.";
            startTime = Timestamp.now();
            txtSteps.setText(txtStep);
        } else if (step == 2) {
            txtStep = "You may place one hand on top of your chest and feel the warm sensation this may bring";
            txtSteps.setText(txtStep);
        } else if (step == 3) {
            txtStep = "Bring your attention towards yourself as you say to yourself:\n a.  May I be filled with love and kindness.\n b.  May I be safe.\n c.  May I be well\n d.  May I be happy and at ease.  ";
            txtSteps.setText(txtStep);
        } else if (step == 4) {
            txtStep = "You may add any other statement that feels appropriate";
            txtSteps.setText(txtStep);
        } else if (step == 5) {
            txtStep = " Keep repeating these statements.";
            txtSteps.setText(txtStep);
        } else if (step == 6) {
            txtStep = "If your mind wanders, gently bring back your attention to your breath and repeat the statements";
            txtSteps.setText(txtStep);
        } else if (step == 7) {
            txtStep = "After spending a few moments doing this, visualize someone who you deeply care for and who has been a source of unconditional love and support to you";
            txtSteps.setText(txtStep);
        } else if (step == 8) {
            txtStep = "Spend a few moments visualizing this person in front of you and repeat the statements:\n a. May we be filled with love and kindness.\n b. “May we be safe”\n c. “May we be well”\n d. “May we be happy and at ease”";
            txtSteps.setText(txtStep);
        }else if (step == 9) {
            txtStep = "You may place one hand on top of your chest and feel the warm sensation this may bring";
            txtSteps.setText(txtStep);
        }else if (step == 10) {
            txtStep = "Keep repeating these statements.";
            txtSteps.setText(txtStep);
        }else if (step == 11) {
            txtStep = "If your mind wanders, gently bring back your attention to your breath and repeat the statements.";
            txtSteps.setText(txtStep);
            btnMettaNext.setText("Finish");
        }else if (step == 12) {
            step = 0;
            Intent intent = new Intent(requireContext(), ActivityPostMeditation.class);
            Timestamp endTime = Timestamp.now();
            long durationInMillis = endTime.toDate().getTime() - startTime.toDate().getTime();
            int durationInSeconds = (int) (durationInMillis / 1000);
            int minutes = durationInSeconds / 60;
            int seconds = durationInSeconds % 60;

            String durationFormatted = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
            intent.putExtra("MEDITATION_TYPE", "Metta Meditation");
            intent.putExtra("DURATION", durationFormatted);
            intent.putExtra("START_TIME", startTime);

            startActivity(intent);
            requireActivity().finish();
        }
    }
    private void startTimer() {
        btnMettaNext.setEnabled(false);
        isTimerRunning = true;

        countDownTimer = new CountDownTimer(100, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                btnMettaNext.setText("Next (" + millisUntilFinished / 1000 + "s)");
            }

            @Override
            public void onFinish() {
                btnMettaNext.setEnabled(true);
                btnMettaNext.setText("Next");
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