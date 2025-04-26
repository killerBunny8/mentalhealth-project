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
 * Use the {@link MeditationAttentionFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MeditationAttentionFragment extends Fragment {
    private int step = 0;
    private TextView txtSteps;
    private Button btnAttentionNext;
    private Timestamp startTime;
    private CountDownTimer countDownTimer;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public MeditationAttentionFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MeditationAttentionFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MeditationAttentionFragment newInstance(String param1, String param2) {
        MeditationAttentionFragment fragment = new MeditationAttentionFragment();
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
        View view = inflater.inflate(R.layout.fragment_meditation_attention, container, false);
        txtSteps = view.findViewById(R.id.txtSteps);
        btnAttentionNext = view.findViewById(R.id.btnAttentionNext);

        btnAttentionNext.setOnClickListener(v -> {
            step++;
            attentionSteps(step);
            startTimer();
        });
        return view;
    }
    //fucntiom responsible for updating page when button is clicked 
    private void attentionSteps(int step) {
        if (step == 1) {
            txtSteps.setText("Sit in an upright, dignified posture. Let your hands rest on your thighs or knees.");
            startTime = Timestamp.now();
            btnAttentionNext.setText("Next");
        } else if (step == 2) {
            txtSteps.setText("Lower your gaze or gently close your eyes. Bring awareness to your breath.");
        } else if (step == 3) {
            txtSteps.setText("Feel the sensations of breathing: air entering and leaving the nose, chest and belly rising and falling.");
        } else if (step == 4) {
            txtSteps.setText("Your mind may wander — it’s okay. Gently return attention to the breath.");
        } else if (step == 5) {
            txtSteps.setText("Each time you notice distraction, return to your anchor — the breath.");
        } else if (step == 6) {
            txtSteps.setText("Continue for a few moments in silence. Feel your body resting in stillness.");
        } else if (step == 7) {
            txtSteps.setText("When you're ready, slowly open your eyes or lift your gaze. Bring awareness to the room.");
        } else if (step == 8) {
            txtSteps.setText("You’ve completed your practice. Acknowledge this moment of stillness and clarity.");
            btnAttentionNext.setText("Finish");
        } else if (step == 9) {
            finishMeditation();
        }
    }

    // Finish meditation function
    private void finishMeditation() {
        // Gets imformation to pass through intent
        Timestamp endTime = Timestamp.now();
        long durationMillis = endTime.toDate().getTime() - startTime.toDate().getTime();
        int seconds = (int) (durationMillis / 1000);
        String formatted = String.format(Locale.getDefault(), "%02d:%02d", seconds / 60, seconds % 60);

        Intent intent = new Intent(requireContext(), ActivityPostMeditation.class);
        intent.putExtra("MEDITATION_TYPE", "Attention Meditation");
        intent.putExtra("DURATION", formatted);
        intent.putExtra("START_TIME", startTime);

        startActivity(intent);
        requireActivity().finish();//remove from pop stack
    }
    // Ends timer 
    private void startTimer() {
        super.onDestroy();
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }
}