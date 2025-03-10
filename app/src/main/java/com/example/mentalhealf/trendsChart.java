package com.example.mentalhealf;

import android.graphics.Color;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.github.mikephil.charting.charts.LineChart;
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

    public trendsChart(LineChart chart) {
        this.lineChart = chart;
        setupChart();
    }

    private void setupChart() {
        // Configure X-Axis
        XAxis xAxis = lineChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularity(1f);
        xAxis.setTextSize(12f);

        xAxis.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                int totalHours = (int) (value * 24 * 60);
                int days = totalHours / (24 * 60);
                int hours = (totalHours % (24* 60)) /60 ;
                int minutes = totalHours % 60 ;

                return String.format(Locale.getDefault(), "%02d:%02d:%02d", days, hours, minutes);
            }
        });


        YAxis yAxis = lineChart.getAxisLeft();
        yAxis.setGranularity(1f);
        yAxis.setTextSize(12f);
        yAxis.setAxisMinimum(0f);
        yAxis.setAxisMaximum(6f);

        lineChart.getAxisRight().setEnabled(false);
    }

    public void setMoodData(List<Entry> moodEntries) {
        LineDataSet dataSet = new LineDataSet(moodEntries, "Time");
        dataSet.setLineWidth(2f);
        dataSet.setCircleRadius(5f);
        dataSet.setValueTextSize(12f);

        LineData lineData = new LineData(dataSet);
        lineChart.setData(lineData);
        lineChart.invalidate();
    }

}
