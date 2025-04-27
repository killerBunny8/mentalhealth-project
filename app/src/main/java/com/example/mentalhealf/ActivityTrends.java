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
        //variables for linear regression
        movingLineLimit = 4;
        windowSize = 3;
        // init layout componants into variables
        timeRangeSpinner = findViewById(R.id.timeLinespinner);
        txtTrendInfo = findViewById(R.id.txtTrendInfo);
        txtTrendTime = findViewById(R.id.txtTrendTime);
        txtMostCommon = findViewById(R.id.txtMostCommon);
        txtBestAct = findViewById(R.id.txtBestAct);
        timeRange = "Day";

        txtMostCommon.setVisibility(TextView.GONE);
        txtBestAct.setVisibility(TextView.GONE);
        avgData = findViewById(R.id.switchShowAverage);
        avgLine = findViewById(R.id.switchAverageLine);
        updateBtn = findViewById(R.id.btnUpdateChart);
        LineChart lineChart = findViewById(R.id.chart);

        datePick = findViewById(R.id.datePicktxt);
        //show current date
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        selectedDate = simpleDateFormat.format(Calendar.getInstance().getTime());
        datePick.setText(selectedDate);

        //onclick listeneer to show calander dialogue
        datePick.setOnClickListener(v -> showDatePicker());
        // init new chart
        trendsChart = new trendsChart(lineChart);
        //get mood data from todays date
        getMoodData(selectedDate, "Day");
        //function which runs when you change the date or range
        updateBtn.setOnClickListener(v -> {
            timeRange = timeRangeSpinner.getSelectedItem().toString();
            getMoodData(selectedDate, timeRange);
        });
        //onclick listener to activate or deactivate moving average mood line
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
        //onclick listener to activate or deactivate average mood
        avgLine.setOnCheckedChangeListener(((buttonView, isChecked) -> {
            if (isChecked) {
                trendsChart.addAverageLine(averageMood);
            } else {
                // If the switch is unchecked, hide the moving average
                trendsChart.clearAverageLine();
            }
        }));
    }
    // Function to show the calander
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
    // Gets the data for the day or the time range
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
                //converts logs intop chart entries
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
                //checks if switches are clicked
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
    //calculates the mean of the mood entries
    private float getAvgMood(List<Moodlog> moodLogsList) {
        if (moodLogsList.isEmpty()) return 0;

        float sum = 0;
        for (Moodlog moodlog : moodLogsList) {
            sum += moodlog.getFeeling();
        }
        return sum / moodLogsList.size();
    }
    // simple linear regression and calculates the moving average
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
        // Convert moving averages to entries for the chart
        List<Entry> movingAverageEntries = new ArrayList<>();
        for (int i = 0; i < movingAverages.size(); i++) {
            if (movingAverages.get(i) != null) {
                long timeInMillis = moodLogsList.get(i).getTime().toDate().getTime();
                float timeInDays = (timeInMillis - firstTimestamp) / (24f * 60 * 60 * 1000f);
                movingAverageEntries.add(new Entry(timeInDays, movingAverages.get(i)));
            }
        }
        // Add to the chart
        if (avgData.isChecked() && moodLogsList.size() != movingLineLimit) {
            trendsChart.addMovingAverageData(movingAverageEntries, moodLogsList.size(), windowSize);
        }
        // Update UI
        updateUI(selectedDate, timeRange, averageMood, slope, intercept, movingAverages);
    }
    //updates the layout textviews with the analysis
    private void updateUI(String selectedDate, String timeRange, Float averageMood, Double slope, Double intercept, List<Float> movingAverages) {
        String trendInfoText;
        String trendTimeText = "Trends for " + selectedDate + " (" + timeRange + ")";
        String mostCommon= "Most common activities:";
        String bestActivity = "You felt the best while: ";

        if (moodLogsList.isEmpty()) {
            trendInfoText = "There are no logs available for the selected range. Please choose another day, increase the range, or add some logs from the Journal activity.";
        } else if (moodLogsList.size() < 3) {
            trendInfoText = "You have very few logs. Try logging your mood more often to see better trends. Please choose another day, increase the range, or add some logs from the Journal activity.";
        } else {
            if (averageMood < 2.6) {
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
            // Add regression and moving average
            if (slope > 0) {
                trendInfoText += "\n\nYour mood is improving over time. Keep it up!";
            } else if (slope < 0) {
                trendInfoText += "\n\nYour mood is declining. Consider trying activities that improve your mood.";
            } else {
                trendInfoText += "\n\nYour mood is stable.";
            }
            //gets most common activities
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
                //gets activities which have the best average mood
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
    //function for the most common activities
    private List<Map.Entry<String, Integer>> getTopActivities(List<Moodlog> logs, int top) {
        Map<String, Integer> activityAmount = new HashMap<>();
        //cycles through the moodlog list and appends it to a hashmap, increasing the value each instance
        for (Moodlog log : logs) {
            String activity = log.getActivity();
            if (activity != null && !activity.isEmpty()) {
                activityAmount.put(activity, activityAmount.getOrDefault(activity, 0) + 1);
            }
        }
        //sorts it similar to in journalactivity
        List<Map.Entry<String, Integer>> sortedActivities = new ArrayList<>(activityAmount.entrySet());
        sortedActivities.sort((a, b) -> b.getValue().compareTo(a.getValue()));

        return sortedActivities.subList(0, Math.min(top, sortedActivities.size()));
    }
    //similar to previous function, instead gets the feeling value
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
            //calculates average and appends to list
            String activity = entry.getKey();
            List<Integer> moods = entry.getValue();

            double average = moods.stream().mapToInt(Integer::intValue).average().orElse(0.0);
            activityAverages.add(new AbstractMap.SimpleEntry<>(activity, average));
        }
        //sorts out largest to smallest
        activityAverages.sort((a, b) -> Double.compare(b.getValue(), a.getValue()));
        return activityAverages.subList(0, Math.min(top, activityAverages.size()));
    }



}