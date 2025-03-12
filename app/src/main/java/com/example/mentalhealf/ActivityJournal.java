package com.example.mentalhealf;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class ActivityJournal extends AppCompatActivity implements MoodLogAdapter.onItemClickListener{

    private moodLogHelper moodLogHelper;
    private MoodLogAdapter adapter;
    private List<Moodlog> moodLogsList;

    private TextView datePick;
    private TextView txtViewNoLogs;
    private String selectedDate;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_journal);
        View rootView = findViewById(android.R.id.content);
        rootView.setForceDarkAllowed(false);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialise moodlogHelper
        moodLogHelper = new moodLogHelper();

        // Initialise RecyclerView
        RecyclerView recyclerView = findViewById(R.id.journalView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Initialise list and adapter
        moodLogsList = new ArrayList<>();
        adapter = new MoodLogAdapter(moodLogsList, this);
        recyclerView.setAdapter(adapter);

        // Initialise date elemends
        datePick = findViewById(R.id.txtDatePickeer);
        TextView updateJournal = findViewById(R.id.updateLogs);

        Button addmass = findViewById(R.id.txtAddfakeLogs);
        txtViewNoLogs = findViewById(R.id.txtViewNoLogs);

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        selectedDate = simpleDateFormat.format(Calendar.getInstance().getTime());
        datePick.setText(selectedDate);

        datePick.setOnClickListener(v -> showDatePicker());
        updateJournal.setOnClickListener(v -> {
            Log.d("Date", "onCreate: "+ selectedDate);
            Toast.makeText(ActivityJournal.this, "Showing logs for day: " + selectedDate, Toast.LENGTH_SHORT).show();
            loadMoodLogs(selectedDate);
        });

        addmass.setOnClickListener(v-> moodLogHelper.addMassMoodLogs(30, new moodLogHelper.MoodLogCallback() {
            @Override
            public void onSuccess(String message) {
                Toast.makeText(ActivityJournal.this, message, Toast.LENGTH_SHORT).show();
                loadMoodLogs(selectedDate); // Refresh RecyclerView
            }

            @Override
            public void onFailure(String error) {
                Toast.makeText(ActivityJournal.this, error, Toast.LENGTH_SHORT).show();
            }
        }));

        // mood logs from Firestore
        loadMoodLogs(selectedDate);
    }

    @Override
    public void onMoodUpdate(Moodlog moodlog, String updatedDescription, int position) {
        moodLogHelper.updateMoodLog(moodlog.getId(), updatedDescription, position, new moodLogHelper.MoodLogUpdateCallback() {
            @Override
            public void onSuccess(String message, int position) {
                Toast.makeText(ActivityJournal.this, message, Toast.LENGTH_SHORT).show();
                moodLogsList.get(position).setDescription(updatedDescription);
                adapter.notifyItemChanged(position);
            }

            @Override
            public void onFailure(String error) {
                Toast.makeText(ActivityJournal.this, error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showDatePicker() {

        final Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR); // current year
        int mMonth = c.get(Calendar.MONTH); // current month
        int mDay = c.get(Calendar.DAY_OF_MONTH); // current day
        // date picker dialog
        // set day of month , month and year value in the edit text
        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (view, year, monthOfYear, dayOfMonth) -> {
                    // set day of month , month and year value in the edit text
                    selectedDate = String.format("%02d/%02d/%d", dayOfMonth, monthOfYear + 1, year);
                    datePick.setText(selectedDate);


                }, mYear, mMonth, mDay);
        datePickerDialog.show();
    }

    private void loadMoodLogs(String selectedDate) {
        moodLogHelper.getAllMoodLogs(selectedDate,new moodLogHelper.MoodLogListCallback() {
            @Override
            public void onSuccess(List<Moodlog> moodLogs) {
                moodLogsList.clear();
                moodLogsList.addAll(moodLogs);
                adapter.notifyDataSetChanged();

                if (moodLogsList.isEmpty()) {
                    txtViewNoLogs.setVisibility(View.VISIBLE);
                } else {
                    txtViewNoLogs.setVisibility(View.GONE);
                }

            }

            @Override
            public void onFailure(String error) {
                Toast.makeText(ActivityJournal.this, error, Toast.LENGTH_SHORT).show();
            }
        });
    }



}
