package com.example.mentalhealf;

import android.util.Log;
import android.widget.Toast;

import com.github.mikephil.charting.data.Entry;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class oldfumction {


//    private void getMoodData(Long date, String timeRange) {
//        //date = getStartTimeForRange(date, timeRange);
//        List<Entry> moodEntries = new ArrayList<>();
//        moodLogHelper.getAllMoodLogs(selectedDate, new moodLogHelper.MoodLogListCallback() {
//            @Override
//            public void onSuccess(List<Moodlog> moodLogs) {
//                if (moodLogs.isEmpty()) {
//                    Toast.makeText(TrendsActivity.this, "You have no logs.", Toast.LENGTH_SHORT).show();
//                    return;
//                }
//
//                moodLogsList.clear();
//                moodLogsList.addAll(moodLogs);
//
//                long startTimeMillis = getStartTimeForRange(date, timeRange);
//                long endOfDayMillis = date + (23 * 60 * 60 * 1000) + (59 * 60 * 1000) + (59 * 1000) + 999;
//
//                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
//                String selectedDateStr = dateFormat.format(new Date(date));
//
//                List<Moodlog> filteredLogs = new ArrayList<>();
//                Log.d("getmood3", "Total logs retrieved from Firestore: " + moodLogs.size());
//
//
//                for (Moodlog moodlog : moodLogs) {
//                    long logTimeMillis = moodlog.getTime().toDate().getTime();
////                    Log.d("getmood4", "Checking log: Mood=" + moodlog.getFeeling() +
////                            ", Time=" + logTimeMillis + " (" + new Date(logTimeMillis) + ")");
////                    Log.d("getmood9", "times, logtimemillis  " + logTimeMillis +
////                            ", start time= " + startTimeMillis + "  date " + endOfDayMillis + ")");
//
//                    String logDateStr = dateFormat.format(new Date(logTimeMillis)); // Convert log timestamp to String format
//
////                    if (logTimeMillis >= startTimeMillis && logTimeMillis <= endOfDayMillis) {
////                        filteredLogs.add(moodlog);
////                    }
//                    if (logDateStr.equals(selectedDateStr)) {
//                        filteredLogs.add(moodlog);
//                    }
//                }
//                Log.d("getmood6", "Filtered logs count: " + filteredLogs.size());
//
//                if (filteredLogs.isEmpty()) {
//                    Toast.makeText(TrendsActivity.this, "No logs in selected range.", Toast.LENGTH_SHORT).show();
//                    return;
//                }
//                long firstTimestamp = filteredLogs.get(0).getTime().toDate().getTime();
//
//
//                for (Moodlog moodlog : filteredLogs) {
//                    long timeInMillis = moodlog.getTime().toDate().getTime();
//                    float timeInHours = (timeInMillis - firstTimestamp) / (24f * 60 * 60 * 1000f);
//                    float moodLevel = moodlog.getFeeling();
//                    moodEntries.add(new Entry(timeInHours, moodLevel));
//                }
//
//                if (!moodEntries.isEmpty()) {
//                    trendsChart.setMoodData(moodEntries);
//                } else {
//                }
//            }
//
//            @Override
//            public void onFailure(String error) {
//                Log.d("getmood5", "Error fetching mood logs: " + error);
//            }
//        });
//    }


}
