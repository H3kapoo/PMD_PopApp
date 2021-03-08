package com.hekapoo.popapp.Charts;

import android.os.Bundle;

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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_social);
        swipeRefreshLayout = findViewById(R.id.refresher);

        ArrayList<ChartModel> charts = new ArrayList<>();

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

        charts.add(column_chart);

        //pie chart
        List<DataEntry> pie_data = new ArrayList<>();
        pie_data.add(new ValueDataEntry("Apples", 6371664));
        pie_data.add(new ValueDataEntry("Pears", 789622));
        pie_data.add(new ValueDataEntry("Bananas", 7216301));
        pie_data.add(new ValueDataEntry("Grapes", 1486621));
        pie_data.add(new ValueDataEntry("Oranges", 1200000));

        Bundle pie_extras = new Bundle();
        pie_extras.putString("TITLE", "ceva titlu");
        pie_extras.putString("SUB_TITLE", "subtitlu");

        ChartModel pie_chart = new ChartModel("PIE", pie_data, pie_extras);

        charts.add(pie_chart);

        //tag_cloud
        List<DataEntry> tag_data = new ArrayList<>();
        tag_data.add(new CategoryValueDataEntry("China", "asia", 1383220000));
        tag_data.add(new CategoryValueDataEntry("India", "asia", 1316000000));
        tag_data.add(new CategoryValueDataEntry("United States", "america", 324982000));
        tag_data.add(new CategoryValueDataEntry("Indonesia", "asia", 263510000));
        tag_data.add(new CategoryValueDataEntry("Brazil", "america", 207505000));
        tag_data.add(new CategoryValueDataEntry("Pakistan", "asia", 196459000));
        tag_data.add(new CategoryValueDataEntry("Nigeria", "africa", 191836000));

        Bundle tag_extras = new Bundle();
        tag_extras.putString("TITLE", "ceva titlu");

        ChartModel tag_chart = new ChartModel("TAG_CLOUD", tag_data, tag_extras);

        charts.add(tag_chart);

        //funnel
        List<DataEntry> funnel_data = new ArrayList<>();
        funnel_data.add(new ValueDataEntry("Website Visits", 528756));
        funnel_data.add(new ValueDataEntry("Downloads", 164052));
        funnel_data.add(new ValueDataEntry("Valid Contacts", 112167));
        funnel_data.add(new ValueDataEntry("Interested to Buy", 79128));
        funnel_data.add(new ValueDataEntry("Purchased", 79128));

        Bundle funnel_extras = new Bundle();
        funnel_extras.putString("TITLE", "ceva titlu");

        ChartModel funnel_chart = new ChartModel("FUNNEL", funnel_data, funnel_extras);

        charts.add(funnel_chart);


        swipeRefreshLayout.setOnRefreshListener(() -> {

            charts.add(column_chart);
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
