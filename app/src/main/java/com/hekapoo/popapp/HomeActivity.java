package com.hekapoo.popapp;

import android.os.Bundle;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.hekapoo.popapp.Charts.ChartModel;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {

    Button homeBtn, chartsBtn;
    AnyChartView homeChart;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_layout);

        homeBtn = findViewById(R.id.home_btn);
        chartsBtn = findViewById(R.id.charts_btn);
        homeChart = findViewById(R.id.home_chart_view);

        List<DataEntry> column_data = new ArrayList<>();

        //column
        column_data.add(new ValueDataEntry("Rouge", 80540));
        column_data.add(new ValueDataEntry("Foundation", 94190));
        column_data.add(new ValueDataEntry("Mascara", 102610));
        column_data.add(new ValueDataEntry("Lip gloss", 110430));

        Bundle column_extras = new Bundle();
        column_extras.putString("TITLE", "ceva titlu");
        column_extras.putString("XTITLE", "pe x aicea");
        column_extras.putString("YTITLE", "pe y aicea");

        ChartModel column_chart = new ChartModel("COLUMN", column_data, column_extras);
        homeChart.setChart(column_chart.populate());

    }
}
