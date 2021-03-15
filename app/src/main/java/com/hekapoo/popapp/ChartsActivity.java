package com.hekapoo.popapp;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.anychart.chart.common.dataentry.CategoryValueDataEntry;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.hekapoo.popapp.APIHandler.FacebookAPIHandler;
import com.hekapoo.popapp.Charts.ChartModel;
import com.hekapoo.popapp.Charts.ChartModelAdapter;
import com.hekapoo.popapp.Charts.TagCloudValuesGenerator;
import com.hekapoo.popapp.Login.LoginHandler;
import com.hekapoo.popapp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ChartsActivity extends AppCompatActivity {

    private RecyclerView recyclerViewCharts;
    private RecyclerView.Adapter<ChartModelAdapter.ViewHolder> chartAdapter;
    private SwipeRefreshLayout refresher;
    private ArrayList<ChartModel> charts;
    private Button homeButton, settingsBtn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.charts_layout);

        ActivityCompat.requestPermissions(ChartsActivity.this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
        ActivityCompat.requestPermissions(ChartsActivity.this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},1);

        //Handlers Init
        recyclerViewCharts = findViewById(R.id.recycler);
        refresher = findViewById(R.id.refresher);
        homeButton = findViewById(R.id.home_btn);
        settingsBtn = findViewById(R.id.settings_btn);

        //Check if user is logged into a social
        if (!LoginHandler.getInstance().isLoggedIntoSocial())
            Log.d("charts", "onCreate: not logged ");
        else
            Log.d("charts", "onCreate: logged ");

        //Set the data for the recycler view container accordingly
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        this.recyclerViewCharts.setLayoutManager(layoutManager);

        //Check which social media the user is currently logged into & plot data
        fetchAndPlot();

        //Set on refresh layout listener
        refresher.setOnRefreshListener(() -> {
            if (!LoginHandler.getInstance().isLoggedIntoSocial())
                loadNotPopulatedChart();
            else
                fetchAndPlot();
        });

        //Home and Settings handlers
        homeButton.setOnClickListener(e -> {
            Intent intent = new Intent(ChartsActivity.this, HomeActivity.class);
            startActivity(intent);
        });

        settingsBtn.setOnClickListener(e -> {
            Intent intent = new Intent(ChartsActivity.this, SettingsActivity.class);
            startActivity(intent);
        });
    }

    private void fetchAndPlot() {
        switch (LoginHandler.getInstance().getLoginType()) {
            case 1:
                fetchAndPlotFacebookData();
                break;
            case 2:
                fetchAndPlotTwitterData();
                break;
            case 0:
                loadNotPopulatedChart();
                Log.d("charts", "LoginHandler.getInstance().getLoginType() currently not logged");
                break;
        }
    }

    //Function to fetch and plot data from facebook to all charts
    private void fetchAndPlotFacebookData() {

        Log.d("charts", "fb has token OK");

        Bundle bundle = new Bundle();
        bundle.putString("fields", "reactions.summary(true),comments.summary(true)");
        bundle.putString("limit", "20");

        FacebookAPIHandler.getInstance().sendGraphRequest("/me/posts", bundle, result -> {
            JSONObject resultObj = result.getJSONObject();
            try {

                JSONArray dataArray = resultObj.getJSONArray("data");
                Log.d("charts", String.valueOf(dataArray.length()));

                List<DataEntry> columnData = new ArrayList<>();
                List<DataEntry> pieData = new ArrayList<>();
                List<DataEntry> tagcloudData = new ArrayList<>();
                int totalPostLikes = 0, totalPostComments = 0;

                for (int i = 0; i < dataArray.length(); i++) {

                    //also apply time filtering
                    String postLikes = dataArray.getJSONObject(i).getJSONObject("reactions").getJSONObject("summary").getString("total_count");
                    String postComments = dataArray.getJSONObject(i).getJSONObject("comments").getJSONObject("summary").getString("total_count");

                    totalPostLikes += Integer.parseInt(postLikes);
                    totalPostComments += Integer.parseInt(postComments);

                    Log.d("charts", "POST" + i + ": LIKES " + postLikes + " COMMENTS " + postComments);
                }

                columnData.add(new ValueDataEntry("Reactions", totalPostLikes));
                columnData.add(new ValueDataEntry("Comments", totalPostComments));
                pieData.add(new ValueDataEntry("Reactions", totalPostLikes));
                pieData.add(new ValueDataEntry("Comments", totalPostComments));

                //tag data
                tagcloudData = TagCloudValuesGenerator.getValuesArray(totalPostLikes, totalPostComments);

                Bundle columnExtras = new Bundle();
                columnExtras.putString("TITLE", "Column chart");

                Bundle pieExtras = new Bundle();
                columnExtras.putString("TITLE", "Pie chart");

                Bundle tagcloudExtras = new Bundle();
                columnExtras.putString("TITLE", "Tag cloud chart");

                ChartModel columnChart = new ChartModel("COLUMN", columnData, columnExtras);
                ChartModel pieChart = new ChartModel("PIE", pieData, pieExtras);
                ChartModel tagcloudChart = new ChartModel("TAG_CLOUD", tagcloudData, tagcloudExtras);

                charts = new ArrayList<>();
                charts.add(columnChart);
                charts.add(pieChart);
                charts.add(tagcloudChart);

                //Update recyclerview
                chartAdapter = new ChartModelAdapter(charts);
                this.recyclerViewCharts.setAdapter(chartAdapter);
                refresher.setRefreshing(false);

            } catch (JSONException e) {
                Log.d("charts", "JSON EXCEPTION THROWN " + e.toString());
                loadNotPopulatedChart();
            }
        });
    }

    //Function to fetch and plot data from twitter to chart
    private void fetchAndPlotTwitterData() {
        //TODO
    }

    private void loadNotPopulatedChart() {
        charts = new ArrayList<>();
        List<DataEntry> columnData = new ArrayList<>();
        List<DataEntry> pieData = new ArrayList<>();
        List<DataEntry> tagcloudData = new ArrayList<>();

        columnData.add(new ValueDataEntry("Reactions", 0));
        columnData.add(new ValueDataEntry("Comments", 0));
        pieData.add(new ValueDataEntry("Reactions", 0));
        pieData.add(new ValueDataEntry("Comments", 0));
        tagcloudData.add(new CategoryValueDataEntry("Reactions", "Reactions", 0));
        tagcloudData.add(new CategoryValueDataEntry("Comments", "Comments", 0));

        Bundle columnExtras = new Bundle();
        columnExtras.putString("TITLE", "NO SOCIAL CONNECTED");

        Bundle pieExtras = new Bundle();
        pieExtras.putString("TITLE", "NO SOCIAL CONNECTED");

        Bundle tagcloudExtras = new Bundle();
        tagcloudExtras.putString("TITLE", "NO SOCIAL CONNECTED");

        //replace column dynamically
        ChartModel columnChart = new ChartModel("COLUMN", columnData, columnExtras);
        ChartModel pieChart = new ChartModel("PIE", pieData, pieExtras);
        ChartModel tagcloudChart = new ChartModel("TAG_CLOUD", tagcloudData, tagcloudExtras);

        charts.add(columnChart);
        charts.add(pieChart);
        charts.add(tagcloudChart);

        //Update recyclerview
        chartAdapter = new ChartModelAdapter(charts);
        this.recyclerViewCharts.setAdapter(chartAdapter);
        refresher.setRefreshing(false);
    }
}
