package com.example.mentalhealf;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;

import java.text.SimpleDateFormat;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.math3.stat.regression.SimpleRegression;

public class ActivityTrends extends AppCompatActivity {

    private List<Moodlog> moodLogsList;
    private moodLogHelper moodLogHelper;
    private trendsChart trendsChart;
    private String selectedDate, timeRange;
    private TextView datePick, txtTrendInfo, txtTrendTime, txtBestAct, txtMostCommon;
    private Button updateBtn;
    private Spinner timeRangeSpinner;
    private float averageMood;
    private Switch avgData, avgLine;
    private int movingLineLimit, windowSize;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trends);
        getWindow().setNavigationBarColor(ContextCompat.getColor(this, android.R.color.black));


        // Initialise moodlogHelper

        moodLogHelper = new moodLogHelper();
        moodLogsList = new ArrayList<>();
        movingLineLimit = 4;
        windowSize = 3;

        timeRangeSpinner = findViewById(R.id.timeLinespinner);

        txtTrendInfo = findViewById(R.id.txtTrendInfo);
        txtTrendTime = findViewById(R.id.txtTrendTime);
        txtMostCommon = findViewById(R.id.txtMostCommon);
        txtBestAct = findViewById(R.id.txtBestAct);
        timeRange = "Day";

        txtMostCommon.setVisibility(TextView.GONE);
        txtBestAct.setVisibility(TextView.GONE);

        datePick = findViewById(R.id.datePicktxt);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        selectedDate = simpleDateFormat.format(Calendar.getInstance().getTime());
        datePick.setText(selectedDate);

        datePick.setOnClickListener(v -> showDatePicker());

        avgData = findViewById(R.id.switchShowAverage);
        avgLine = findViewById(R.id.switchAverageLine);
        updateBtn = findViewById(R.id.btnUpdateChart);

        LineChart lineChart = findViewById(R.id.chart);
        trendsChart = new trendsChart(lineChart);

        getMoodData(selectedDate, "Day");
        updateBtn.setOnClickListener(v -> {
            timeRange = timeRangeSpinner.getSelectedItem().toString();
            getMoodData(selectedDate, timeRange);
        });

        avgData.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                if (moodLogsList.size() < windowSize || moodLogsList.size() == movingLineLimit) {
                    avgData.setChecked(false);
                    Toast.makeText(ActivityTrends.this, "Not enough data to display moving average.", Toast.LENGTH_SHORT).show();
                } else {
                    linearRegression(moodLogsList); // Calculate and display moving average
                }
            } else {
                trendsChart.clearMovingAverageData(); // Hide moving average
            }
        });

        avgLine.setOnCheckedChangeListener(((buttonView, isChecked) -> {
            if (isChecked) {
                trendsChart.addAverageLine(averageMood);
            } else {
                // If the switch is unchecked, hide the moving average
                trendsChart.clearAverageLine();
            }
        }));
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

    private void getMoodData(String selectedDate, String timeRange) {
        List<Entry> moodEntries = new ArrayList<>();
        Log.d("getmood_start", "Querying logs for date: " + selectedDate + ", Range: " + timeRange);
        //  Query Firestore for logs in the selected time range
        moodLogHelper.getMoodLogsByRange(selectedDate, timeRange, new moodLogHelper.MoodLogListCallback() {
            @Override
            public void onSuccess(List<Moodlog> moodLogs) {
                if (moodLogs.isEmpty()) {
                    Toast.makeText(ActivityTrends.this, "No logs in selected range.", Toast.LENGTH_SHORT).show();
                    return;
                }
                moodLogsList.clear();
                moodLogsList.addAll(moodLogs);
                Log.d("getmood6", "Filtered logs count: " + moodLogs.size());
                long firstTimestamp = moodLogs.get(0).getTime().toDate().getTime();
                for (Moodlog moodlog : moodLogs) {
                    long timeInMillis = moodlog.getTime().toDate().getTime();
                    float timeInDays = (timeInMillis - firstTimestamp) / (24f * 60 * 60 * 1000f);
                    float moodLevel = moodlog.getFeeling();
                    moodEntries.add(new Entry(timeInDays, moodLevel));
                }
                if (!moodEntries.isEmpty()) {
                    averageMood = getAvgMood(moodLogs);
                    linearRegression(moodLogs);
                    trendsChart.setMoodData(moodEntries);

                    //trendsChart.addAverageLine(averageMood);
                    Log.d("AVGMODO", "onSuccess: "+ averageMood);
                }
                if (avgData.isChecked() && moodLogsList.size() != movingLineLimit) {
                    linearRegression(moodLogs);
                }
                if (avgLine.isChecked()) {
                    trendsChart.addAverageLine(averageMood);
                }
                //updateUI(selectedDate, timeRange, averageMood);
            }

            @Override
            public void onFailure(String error) {
                Log.d("getmood5", "Error fetching mood logs: " + error);
            }
        });
    }

    private float getAvgMood(List<Moodlog> moodLogsList) {
        if (moodLogsList.isEmpty()) return 0;

        float sum = 0;
        for (Moodlog moodlog : moodLogsList) {
            sum += moodlog.getFeeling();
        }
        return sum / moodLogsList.size();
    }

    private void linearRegression(List<Moodlog> moodLogsList) {
        if (moodLogsList.isEmpty()) return;
        averageMood = getAvgMood(moodLogsList);
        SimpleRegression regression = new SimpleRegression();
        long firstTimestamp = moodLogsList.get(0).getTime().toDate().getTime();
        for (Moodlog moodlog : moodLogsList) {
            long timeInMillis = moodlog.getTime().toDate().getTime();
            float timeInDays = (timeInMillis - firstTimestamp) / (24f * 60 * 60 * 1000f);
            float moodLevel = moodlog.getFeeling();
            regression.addData(timeInDays, moodLevel);
        }
        double slope = regression.getSlope();
        double intercept = regression.getIntercept();
        List<Float> movingAverages = new ArrayList<>();
        for (int i = 0; i < moodLogsList.size(); i++) {
            if (i < windowSize - 1) {
                movingAverages.add(null); // Not enough data points
            } else {
                float sum = 0;
                for (int j = 0; j < windowSize; j++) {
                    sum += moodLogsList.get(i - j).getFeeling();
                }
                movingAverages.add(sum / windowSize);
            }
        }
        // Convert moving averages to Entry objects for the chart
        List<Entry> movingAverageEntries = new ArrayList<>();
        for (int i = 0; i < movingAverages.size(); i++) {
            if (movingAverages.get(i) != null) {
                long timeInMillis = moodLogsList.get(i).getTime().toDate().getTime();
                float timeInDays = (timeInMillis - firstTimestamp) / (24f * 60 * 60 * 1000f);
                movingAverageEntries.add(new Entry(timeInDays, movingAverages.get(i)));
            }
        }
        // Add moving averages to the chart
        if (avgData.isChecked() && moodLogsList.size() != movingLineLimit) {
            trendsChart.addMovingAverageData(movingAverageEntries, moodLogsList.size(), windowSize);
        }
        // Update UI with regression and moving average results
        updateUI(selectedDate, timeRange, averageMood, slope, intercept, movingAverages);
    }

    private void updateUI(String selectedDate, String timeRange, Float averageMood, Double slope, Double intercept, List<Float> movingAverages) {

        String trendInfoText;
        String trendTimeText = "Trends for " + selectedDate + " (" + timeRange + ")";
        String mostCommon= "Most common activities:";
        String bestActivity = "You felt the best while: ";

        if (moodLogsList.isEmpty()) {
            trendInfoText = "There are no logs available for the selected range. Please choose another day, increase the range, or add some logs from the Journal activity.";
        } else if (moodLogsList.size() < 3) { // Adjust the threshold as needed
            trendInfoText = "You have very few logs. Try logging your mood more often to see better trends. Please choose another day, increase the range, or add some logs from the Journal activity.";
        } else {
            if (averageMood < 2.5) {
                trendInfoText = "Your average mood is low. Consider trying some self-meditation or relaxation techniques.";
                new androidx.appcompat.app.AlertDialog.Builder(this)
                        .setTitle("Need Support?")
                        .setMessage("Your average mood has been quite low. Would you like to check out some professional mental health support resources?")
                        .setPositiveButton("Yes", (dialog, which) -> {
                            startActivity(new Intent(ActivityTrends.this, ActivityProfessionalHelp.class));
                        })
                        .setNegativeButton("No", null)
                        .show();
            } else if (averageMood < 4) {
                trendInfoText = "Your average mood is moderate. Keep up the good work!";
            } else {
                trendInfoText = "Your average mood is high. Great job!";
            }

            // Add regression and moving average info
            if (slope > 0) {
                trendInfoText += "\n\nYour mood is improving over time. Keep it up!";
            } else if (slope < 0) {
                trendInfoText += "\n\nYour mood is declining. Consider trying activities that improve your mood.";
            } else {
                trendInfoText += "\n\nYour mood is stable.";
            }

            if (!timeRange.equals("Day")) {
                txtMostCommon.setVisibility(TextView.VISIBLE);
                txtBestAct.setVisibility(TextView.VISIBLE);
                List<Map.Entry<String, Integer>> topActivities = getTopActivities(moodLogsList, 3);

                List<Map.Entry<String, Double>> bestActivities = getBestActivity(moodLogsList, 3);
                if (!topActivities.isEmpty()) {

                    for (int i = 0; i < topActivities.size(); i++) {
                        Map.Entry<String, Integer> entry = topActivities.get(i);
                        mostCommon += "\n" + (i + 1) + ". " + entry.getKey() + " (" + entry.getValue() + " times)";
                    }
                    Log.d("TopActivities", "Number of activities: " + topActivities.size());
                } else {
                    mostCommon += "\n\nNo activities logged in this range.";
                }

                if (!bestActivities.isEmpty()) {
                    for (int i = 0; i < bestActivities.size(); i++) {
                        Map.Entry<String, Double> bestActivityEntry = bestActivities.get(i);
                        bestActivity += "\n" + (i + 1) + ". " + bestActivityEntry.getKey() + " (Avg Mood: " + String.format("%.2f", bestActivityEntry.getValue()) + "/5)";
                    }
                } else {
                    bestActivity += "\n\nNo activities logged in this range.";
                }
            } else {
                txtMostCommon.setVisibility(TextView.GONE);
                txtBestAct.setVisibility(TextView.GONE);
            }
        }



        txtTrendTime.setText(trendTimeText);
        txtTrendInfo.setText(trendInfoText);
        txtMostCommon.setText((mostCommon));
        txtBestAct.setText(bestActivity);
    }

    private List<Map.Entry<String, Integer>> getTopActivities(List<Moodlog> logs, int top) {
        Map<String, Integer> activityAmount = new HashMap<>();

        for (Moodlog log : logs) {
            String activity = log.getActivity();
            if (activity != null && !activity.isEmpty()) {
                activityAmount.put(activity, activityAmount.getOrDefault(activity, 0) + 1);
            }
        }

        List<Map.Entry<String, Integer>> sortedActivities = new ArrayList<>(activityAmount.entrySet());
        sortedActivities.sort((a, b) -> b.getValue().compareTo(a.getValue()));
        
        return sortedActivities.subList(0, Math.min(top, sortedActivities.size()));
    }
    private List<Map.Entry<String, Double>> getBestActivity(List<Moodlog> moodLogs, int top) {
        Map<String, List<Integer>> activityMoodMap = new HashMap<>();
        for (Moodlog moodlog : moodLogs) {
            String activity = moodlog.getActivity();
            int feeling = moodlog.getFeeling();

            if (!activityMoodMap.containsKey(activity)) {
                activityMoodMap.put(activity, new ArrayList<>());
            }
            activityMoodMap.get(activity).add(feeling);
        }
        List<Map.Entry<String, Double>> activityAverages = new ArrayList<>();
        for (Map.Entry<String, List<Integer>> entry : activityMoodMap.entrySet()) {
            String activity = entry.getKey();
            List<Integer> moods = entry.getValue();

            double average = moods.stream().mapToInt(Integer::intValue).average().orElse(0.0);
            activityAverages.add(new AbstractMap.SimpleEntry<>(activity, average));
        }
        activityAverages.sort((a, b) -> Double.compare(b.getValue(), a.getValue()));
        return activityAverages.subList(0, Math.min(top, activityAverages.size()));
    }



}