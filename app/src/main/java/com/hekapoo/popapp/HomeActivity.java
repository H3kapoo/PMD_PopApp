package com.hekapoo.popapp;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.hekapoo.popapp.Charts.ChartModel;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class HomeActivity extends AppCompatActivity {

    SwipeRefreshLayout refresher;
    Button chartsBtn, settingsBtn;
    TextView homeStatusTextView;
    ImageView homeStatusImageView;
    AnyChartView homeChart;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_layout);

        chartsBtn = findViewById(R.id.charts_btn);
        settingsBtn = findViewById(R.id.settings_btn);
        homeChart = findViewById(R.id.home_chart_view);
        homeStatusTextView = findViewById(R.id.homeStatusTextView);
        homeStatusImageView = findViewById(R.id.homeStatusImageView);
        refresher = findViewById(R.id.refresher);

        //TODO: At start,populate chart & status with data

        refresher.setOnRefreshListener(() -> {
            //TODO: data fetching on refresh
            refresher.setRefreshing(false);
        });

        chartsBtn.setOnClickListener(e -> {
            Intent intent = new Intent(HomeActivity.this, ChartsActivity.class);
            startActivity(intent);
        });

        settingsBtn.setOnClickListener(e -> {
            Intent intent = new Intent(HomeActivity.this, SettingsActivity.class);
            startActivity(intent);
        });

        List<DataEntry> column_data = new ArrayList<>();

        //column
        column_data.add(new ValueDataEntry("Reactions", 14000));
        column_data.add(new ValueDataEntry("Comments", 4000));

        Bundle column_extras = new Bundle();
        column_extras.putString("TITLE", "Column Data Chart");

        ChartModel column_chart = new ChartModel("COLUMN", column_data, column_extras);
        homeChart.setChart(column_chart.populate());

    }
}
