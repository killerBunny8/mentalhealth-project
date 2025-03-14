package com.example.mentalhealf;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NavigationBar#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NavigationBar extends Fragment {

    private ImageView navHome, navLog, navStats, navProfile;
    private String userEmail;
    private FirebaseAuth mAuth;
    private final FirebaseFirestore db;
    private loginHelper firebaselogin;



    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public NavigationBar(){
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        firebaselogin = new loginHelper();
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment navigationBar.
     */
    // TODO: Rename and change types and number of parameters
    public static NavigationBar newInstance(String param1, String param2) {
        NavigationBar fragment = new NavigationBar();
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
        return inflater.inflate(R.layout.fragment_navigation_bar, container, false);

    }


//    navHome = view.navHome.findViewById(R.id.nav_home);
//    navLog = view.findViewById(R.id.nav_log);
//    navStats = view.findViewById(R.id.nav_stats);
//    navProfile = view.findViewById(R.id.nav_profile);
//
//        navHome.setOnClickListener(v -> { });

    public void onViewCreated(@NonNull View view, @NonNull Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize each ImageView and set the OnClickListener

        Class currentActivity = getActivity().getClass();

        getUser(new UserCallback() {
            @Override
            public void onSuccess(User user) {
                userEmail = user.getEmail();
            }

            @Override
            public void onFailure(Exception e) {

            }
        });

        ImageView imgHome = view.findViewById(R.id.nav_home);
        imgHome.setOnClickListener(v -> {
            if (!currentActivity.equals(ActivityHome.class)) {
                Intent homeIntent = new Intent(getActivity(), ActivityHome.class);
                homeIntent.putExtra("user", userEmail);
                startActivity(homeIntent);
                // getActivity().finish();

            } else {
                Toast.makeText(getActivity(), "You are on already this page", Toast.LENGTH_SHORT).show();
            }

        });

        ImageView imgLog = view.findViewById(R.id.nav_log);
        imgLog.setOnClickListener(v -> {
            if (!currentActivity.equals(ActivityJournal.class)) {
                Intent journalIntent = new Intent(getActivity(), ActivityJournal.class);
                journalIntent.putExtra("user", userEmail);
                startActivity(journalIntent);
                getActivity().finish();

            } else {
                Toast.makeText(getActivity(), "You are on already this page", Toast.LENGTH_SHORT).show();
            }
        });

        ImageView imgStats = view.findViewById(R.id.nav_stats);
        imgStats.setOnClickListener(v -> {
            if (!currentActivity.equals(ActivityTrends.class)) {
                Intent trendsIntent = new Intent(getActivity(), ActivityTrends.class);
                trendsIntent.putExtra("user", userEmail);
                startActivity(trendsIntent);
                getActivity().finish();

            } else {
                Toast.makeText(getActivity(), "You are already on this page", Toast.LENGTH_SHORT).show();
            }
        });

        ImageView imgProfile = view.findViewById(R.id.nav_profile);
        imgProfile.setOnClickListener(v -> {
            if (!currentActivity.equals(ActivitySettings.class)) {
                Intent settingsIntent = new Intent(getActivity(), ActivitySettings.class);
                settingsIntent.putExtra("user", userEmail);
                startActivity(settingsIntent);
                getActivity().finish();

            } else {
                Toast.makeText(getActivity(), "You are already on this page", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getUser(UserCallback callback) {

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            String userEmail = currentUser.getEmail();
            firebaselogin.getUserEmail(userEmail, new loginHelper.EmailCallback() {
                @Override
                public void onSuccess(User user) {
                    callback.onSuccess(user);
                }
                @Override
                public void onFailure(Exception e) {
                    callback.onFailure(e);
                }
            });
        } else {
            callback.onFailure(new Exception("No user is logged in"));
        }
    }
    public interface UserCallback {
        void onSuccess(User user);
        void onFailure(Exception e);
    }


}