package com.example.mentalhealf;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class trendsChart extends AppCompatActivity{

    private LineChart lineChart;
    private LineDataSet movingAverage;
    //constructor for chart
    public trendsChart(LineChart chart) {
        this.lineChart = chart;
        setupChart();
    }
    // define chart stylinh and axis
    private void setupChart() {
        lineChart.setExtraOffsets(20f, 20f, 20f, 20f);
        lineChart.getDescription().setEnabled(false);

        // Configure X-Axis
        XAxis xAxis = lineChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularity(1f);
        xAxis.setTextSize(12f);
        xAxis.setYOffset(10f);
        xAxis.setAxisMinimum(0f);
        xAxis.setLabelCount(7, true);
        xAxis.setGranularityEnabled(true);
        // format x axis label as day 1
        lineChart.getXAxis().setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return String.format(Locale.getDefault(), "day %d", Math.round(value));
            }
        });
        // Confifgure Y-Axis
        YAxis yAxis = lineChart.getAxisLeft();
        yAxis.setGranularity(1f);
        yAxis.setTextSize(20f);
        yAxis.setAxisMinimum(0f);
        yAxis.setAxisMaximum(6f);

        yAxis.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                // convert int to emoji using else-if
                int moodLevel = (int) value;
                if (moodLevel == 1) {
                    return "😢";
                } else if (moodLevel == 2) {
                    return "😕";
                } else if (moodLevel == 3) {
                    return "😐";
                } else if (moodLevel == 4) {
                    return "🙂";
                } else if (moodLevel == 5) {
                    return "😁";
                } else {
                    return "🤔";
                }
            }

        });
        //styling
        lineChart.getAxisRight().setEnabled(false);
        lineChart.getLegend().setEnabled(false);
        lineChart.getXAxis().setDrawGridLines(false);
        lineChart.getAxisLeft().setDrawGridLines(false);
        lineChart.setVisibleXRangeMaximum(7f);
        //gestures
        lineChart.setTouchEnabled(true);
        lineChart.setDragEnabled(true);
        lineChart.setScaleEnabled(false);
        lineChart.setPinchZoom(false);
        //animate line when laoding
        lineChart.animateX(1000);
    }
    //function which sets data onto chart
    public void setMoodData(List<Entry> moodEntries) {
        LineDataSet dataSet = new LineDataSet(moodEntries, "Time");
        dataSet.setLineWidth(2f);
        dataSet.setValueTextSize(12f);
        dataSet.setDrawValues(false);
        // Smooth the curve
        dataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        dataSet.setDrawCircles(false);

        XAxis xAxis = lineChart.getXAxis();
        if (moodEntries.size() > 0) {
            float padding = 0.01f;
            xAxis.setAxisMinimum(moodEntries.get(0).getX() - padding);
            xAxis.setAxisMaximum(moodEntries.get(moodEntries.size() - 1).getX() + padding);
        }
        LineData lineData = new LineData(dataSet);
        lineChart.setData(lineData);
        lineChart.invalidate();
        lineChart.setVisibleXRangeMaximum(7f);
    }
    //function which displays straight line to show the mean score
    public void addAverageLine(float averageMood) {
        YAxis yAxis = lineChart.getAxisLeft();
        yAxis.removeAllLimitLines();

        // Create a LimitLine for the average mood
        LimitLine avgLine = new LimitLine(averageMood, "Average Mood");
        avgLine.setLineColor(Color.RED); // Set line color
        avgLine.setLineWidth(1f); // Set line width
        avgLine.setTextColor(Color.BLACK); // Set text color
        avgLine.setTextSize(12f); // Set text size

        // Add the LimitLine to the Y-axis
        yAxis.addLimitLine(avgLine);

        // Refresh the chart
        //LineData lineData = new LineData(avgLine);
        //lineChart.setData(lineData);
        lineChart.invalidate();
    }
    //adds the moving average line to the chart
    public void addMovingAverageData(List<Entry> movingAverageEntries, int moodEntriesSize, int windowSize) {
        if (moodEntriesSize < windowSize || moodEntriesSize == 4) {
            // Do not display the moving average if there are too few entries
            Toast.makeText(lineChart.getContext(), "Not enough data to display moving average.", Toast.LENGTH_SHORT).show();
            return;
        }
        movingAverage = new LineDataSet(movingAverageEntries, "Moving Average");
        movingAverage.setColor(Color.rgb(128, 0, 128));
        movingAverage.setValueTextColor(Color.BLACK);
        movingAverage.setDrawValues(false);
        movingAverage.setDrawCircles(false);
        movingAverage.setMode(LineDataSet.Mode.CUBIC_BEZIER);

        LineData lineData = lineChart.getData();
        if (lineData != null) {
            lineData.removeDataSet(movingAverage);
            lineData.addDataSet(movingAverage);
            lineChart.setData(lineData);
            lineChart.invalidate();
        }
    }
    //clears moving average line or the average line
    public void clearMovingAverageData() {
        LineData lineData = lineChart.getData();
        if (lineData != null && movingAverage != null) {
            lineData.removeDataSet(movingAverage);
            lineChart.setData(lineData);
            lineChart.invalidate();// Refresh the chart
        }
    }
    public void clearAverageLine() {
        YAxis yAxis = lineChart.getAxisLeft();
        yAxis.removeAllLimitLines(); // Remove all limit lines
        lineChart.invalidate(); // Refresh the chart
    }
}
