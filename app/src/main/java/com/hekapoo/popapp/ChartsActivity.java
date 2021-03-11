package com.hekapoo.popapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.anychart.chart.common.dataentry.CategoryValueDataEntry;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.hekapoo.popapp.Charts.ChartModel;
import com.hekapoo.popapp.Charts.ChartModelAdapter;
import com.hekapoo.popapp.R;

import java.util.ArrayList;
import java.util.List;

public class ChartsActivity extends AppCompatActivity {

    private RecyclerView recyclerViewCharts;
    private RecyclerView.Adapter<ChartModelAdapter.ViewHolder> chartAdapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ArrayList<ChartModel> charts;
    private Button homeButton,settingsBtn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.charts_layout);

        swipeRefreshLayout = findViewById(R.id.refresher);
        homeButton = findViewById(R.id.home_btn);
        settingsBtn = findViewById(R.id.settings_btn);


        homeButton.setOnClickListener(e->{
            Intent intent = new Intent(ChartsActivity.this, HomeActivity.class);
            startActivity(intent);
        });

        settingsBtn.setOnClickListener(e->{
            Intent intent = new Intent(ChartsActivity.this, SettingsActivity.class);
            startActivity(intent);
        });


        //TODO: Automate this on refres and on activity load
        charts = new ArrayList<>();

        List<DataEntry> column_data = new ArrayList<>();

        //column
        column_data.add(new ValueDataEntry("Reactions", 223));
        column_data.add(new ValueDataEntry("Comments", 43));

        Bundle column_extras = new Bundle();
        column_extras.putString("TITLE", "ceva titlu");
        column_extras.putString("XTITLE", "pe x aicea");
        column_extras.putString("YTITLE", "pe y aicea");

        ChartModel column_chart = new ChartModel("COLUMN", column_data, column_extras);


        //pie chart
        List<DataEntry> pie_data = new ArrayList<>();
        pie_data.add(new ValueDataEntry("Reactions", 130));
        pie_data.add(new ValueDataEntry("Comments", 66));


        Bundle pie_extras = new Bundle();
        pie_extras.putString("TITLE", "ceva titlu");
        pie_extras.putString("SUB_TITLE", "subtitlu");

        ChartModel pie_chart = new ChartModel("PIE", pie_data, pie_extras);


        //tag_cloud
        List<DataEntry> tag_data = new ArrayList<>();
        tag_data.add(new CategoryValueDataEntry("Wow", "31 mar", 1));
        tag_data.add(new CategoryValueDataEntry("HaHa", "12 mar", 3));
        tag_data.add(new CategoryValueDataEntry("GGWP", "3 aug", 4));
        tag_data.add(new CategoryValueDataEntry("Nic3e", "5 apr", 5));
        tag_data.add(new CategoryValueDataEntry("Nic3e", "7 apr", 5));
        tag_data.add(new CategoryValueDataEntry("Nic43e", "8 apr", 5));
        tag_data.add(new CategoryValueDataEntry("Ni43ce", "9 apr", 5));


        Bundle tag_extras = new Bundle();
        tag_extras.putString("TITLE", "ceva titlu");

        ChartModel tag_chart = new ChartModel("TAG_CLOUD", tag_data, tag_extras);

        charts.add(column_chart);
        charts.add(pie_chart);
        charts.add(tag_chart);


        swipeRefreshLayout.setOnRefreshListener(() -> {
                //TODO: Get data for all charts on refresh from APIs
            chartAdapter = new ChartModelAdapter(charts);
            this.recyclerViewCharts.setAdapter(chartAdapter);
            swipeRefreshLayout.setRefreshing(false);
        });

        recyclerViewCharts = findViewById(R.id.recycler);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        this.recyclerViewCharts.setLayoutManager(layoutManager);
        chartAdapter = new ChartModelAdapter(charts);
        this.recyclerViewCharts.setAdapter(chartAdapter);

    }
}
