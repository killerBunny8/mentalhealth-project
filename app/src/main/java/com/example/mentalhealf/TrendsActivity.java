package com.example.mentalhealf;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class TrendsActivity extends AppCompatActivity {

    private List<Moodlog> moodLogsList;
    private moodLogHelper moodLogHelper;
    private trendsChart trendsChart;
    private String selectedDate, timeRnage;
    private TextView datePick;
    private Button updateBtn;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_trends);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        // Initialise moodlogHelper

        moodLogHelper = new moodLogHelper();
        moodLogsList = new ArrayList<>();

        datePick = findViewById(R.id.datePicktxt);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        selectedDate = simpleDateFormat.format(Calendar.getInstance().getTime());
        datePick.setText(selectedDate);

        datePick.setOnClickListener(v -> showDatePicker());

        updateBtn = findViewById(R.id.btnUpdateChart);

        LineChart lineChart = findViewById(R.id.chart);
        trendsChart = new trendsChart(lineChart);

        getMoodData(selectedDate);
        updateBtn.setOnClickListener(v -> {
            getMoodData(selectedDate);
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

    private long convertDateToMillis(String dateStr) {
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            Date date = simpleDateFormat.parse(dateStr);
            return date != null ? date.getTime() : System.currentTimeMillis();
        } catch (Exception e) {
            Log.e("getmood7", "Error parsing date: " + e.getMessage());
            return System.currentTimeMillis();
        }
    }

    private long getStartTimeForRange(long selectedDateMillis, String timeRange) {
        long startTimeMillis = selectedDateMillis;

        if (timeRange.equals("Day")) {
            startTimeMillis -= (24 * 60 * 60 * 1000);
        } else if (timeRange.equals("Week")) {
            startTimeMillis -= (7 * 24 * 60 * 60 * 1000);
        } else if (timeRange.equals("Month")) {
            startTimeMillis -= (30L * 24 * 60 * 60 * 1000);
        } else if (timeRange.equals("Three Months")) {
            startTimeMillis -= (3L * 30 * 24 * 60 * 60 * 1000);
        }

        return startTimeMillis;
    }


    private void getMoodData(String date, String timeRange) {
        date = selectedDate;
        List<Entry> moodEntries = new ArrayList<>();
        // Test data
//        moodEntries.add(new Entry(1, 2));
//        moodEntries.add(new Entry(2, 4));
//        moodEntries.add(new Entry(3, 1));
//        moodEntries.add(new Entry(4, 2));
//        moodEntries.add(new Entry(5, 3));
        moodLogHelper.getAllMoodLogs(selectedDate, new moodLogHelper.MoodLogListCallback() {
            @Override
            public void onSuccess(List<Moodlog> moodLogs) {
                if (moodLogs.isEmpty()) {
                    Toast.makeText(TrendsActivity.this, "You have no logs.", Toast.LENGTH_SHORT).show();
                    return;
                }

                moodLogsList.clear();
                moodLogsList.addAll(moodLogs);

                long selectedDateMillis = convertDateToMillis(selectedDate);
                long firstTimestamp = moodLogs.get(0).getTime().toDate().getTime();

                for (Moodlog moodlog : moodLogs) {
                    long timeInMillis = moodlog.getTime().toDate().getTime();
                    float timeInHours = (timeInMillis - firstTimestamp) / (24f * 60 * 60 * 1000f);

                    float moodLevel = moodlog.getFeeling();
                    moodEntries.add(new Entry(timeInHours, moodLevel));
                }

                if (!moodEntries.isEmpty()) {
                    trendsChart.setMoodData(moodEntries);
                } else {
                }
            }

            @Override
            public void onFailure(String error) {
                Log.d("getmood5", "Error fetching mood logs: " + error);
            }
        });
    }


    }