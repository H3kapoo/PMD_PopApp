package com.hekapoo.popapp;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.CategoryValueDataEntry;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.facebook.AccessToken;
import com.hekapoo.popapp.APIHandler.FacebookAPIHandler;
import com.hekapoo.popapp.APIHandler.TwitterAPIHandler;
import com.hekapoo.popapp.Charts.ChartModel;
import com.hekapoo.popapp.Charts.TagCloudValuesGenerator;
import com.hekapoo.popapp.Login.LoginHandler;
import com.hekapoo.popapp.homeStatus.HomeStatusUpdater;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.Twitter;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.models.Tweet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class HomeActivity extends AppCompatActivity {

    SwipeRefreshLayout refresher;
    Button chartsBtn, settingsBtn;
    TextView homeStatusTextView, homeCurrentSocialTextView;
    ImageView homeStatusImageView;
    AnyChartView homeChart;
    CardView chartHolder;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Twitter.initialize(this);
        setContentView(R.layout.home_layout);

        //Handlers init
        chartHolder = findViewById(R.id.home_chart_holder);
        chartsBtn = findViewById(R.id.charts_btn);
        settingsBtn = findViewById(R.id.settings_btn);
        homeChart = findViewById(R.id.home_chart_view);
        homeStatusTextView = findViewById(R.id.homeStatusTextView);
        homeStatusImageView = findViewById(R.id.homeStatusImageView);
        homeCurrentSocialTextView = findViewById(R.id.currentSocialTextView);
        refresher = findViewById(R.id.refresher);

        //Set default home chart (only once if needed)
        SharedPreferences loadedSettings = getSharedPreferences("ChartInfo", 0);
        String chartType = loadedSettings.getString("CHART_TYPE", "kappa");

        if (chartType.equals("kappa")) {
            SharedPreferences settings = getSharedPreferences("ChartInfo", 0);
            SharedPreferences.Editor editor = settings.edit();
            editor.putString("CHART_TYPE", "COLUMN");
            editor.commit();
            Log.d("home", "was kappa indeed");
        } else
            Log.d("home", "no kappa this time");


        //Check if user is logged into a social
        if (!LoginHandler.getInstance().isLoggedIntoSocial())
            Log.d("home", "onCreate: not logged ");
        else
            Log.d("home", "onCreate: logged ");

        //Check which social media the user is currently logged into & plot data
        fetchAndPlot();

        //Set refresh listener
        refresher.setOnRefreshListener(() -> {
            if (!LoginHandler.getInstance().isLoggedIntoSocial())
                loadNotPopulatedChart();
            else
                fetchAndPlot();
        });

        //Set buttons on click listener
        chartsBtn.setOnClickListener(e -> {
            Intent intent = new Intent(HomeActivity.this, ChartsActivity.class);
            startActivity(intent);
        });

        settingsBtn.setOnClickListener(e -> {
            Intent intent = new Intent(HomeActivity.this, SettingsActivity.class);
            startActivity(intent);
        });
    }

    //Function to fetch and plot data from facebook to chart
    private void fetchAndPlotFacebookData() {

        Log.d("HOME", "fb has token OK");

        Bundle bundle = new Bundle();
        bundle.putString("fields", "reactions.summary(true),comments.summary(true)");
        bundle.putString("limit", "20");  //<- current limit facor

        FacebookAPIHandler.getInstance().sendGraphRequest("/me/posts", bundle, result -> {
            JSONObject resultObj = result.getJSONObject();
            try {

                JSONArray dataArray = resultObj.getJSONArray("data");
                Log.d("HOME", String.valueOf(dataArray.length()));

                List<DataEntry> chartData = new ArrayList<>();
                int totalPostLikes = 0, totalPostComments = 0;

                for (int i = 0; i < dataArray.length(); i++) {

                    //also apply time filtering
                    String postLikes = dataArray.getJSONObject(i).getJSONObject("reactions").getJSONObject("summary").getString("total_count");
                    String postComments = dataArray.getJSONObject(i).getJSONObject("comments").getJSONObject("summary").getString("total_count");

                    totalPostLikes += Integer.parseInt(postLikes);
                    totalPostComments += Integer.parseInt(postComments);

                    Log.d("HOME", "POST" + i + ": LIKES " + postLikes + " COMMENTS " + postComments);
                }

                //Get chart type from preferences
                SharedPreferences settings = getSharedPreferences("ChartInfo", 0);
                String chartType = settings.getString("CHART_TYPE", "COLUMN");

                if (chartType.equals("TAG_CLOUD"))
                    chartData = TagCloudValuesGenerator.getValuesArray(totalPostLikes, totalPostComments);
                else {
                    chartData.add(new ValueDataEntry("Reactions", totalPostLikes));
                    chartData.add(new ValueDataEntry("Comments", totalPostComments));
                }

                Bundle chartExtras = new Bundle();

                ChartModel homeChartModel = new ChartModel(chartType, chartData, chartExtras);
                homeChart.setChart(homeChartModel.populate());

                computeAndSetStatus(totalPostLikes,totalPostComments);

            } catch (JSONException e) {
                Log.d("HOME", "JSON EXCEPTION THROWN " + e.toString());
                loadNotPopulatedChart();
            }
            refresher.setRefreshing(false);
        });
    }

    //Function to fetch and plot data from twitter to chart
    private void fetchAndPlotTwitterData() {
        Log.d("HOME", "tw has token OK");

        //get data we need from twitter (req already setup in the function body)
        TwitterAPIHandler.getInstance().sendGraphRequest(new Callback<List<Tweet>>() {
            @Override
            public void success(Result<List<Tweet>> result) {

                List<DataEntry> chartData = new ArrayList<>();
                int totalPostLikes = 0, totalPostComments = 0;

                for (Tweet t : result.data) {
                    totalPostLikes += t.favoriteCount;
                    totalPostComments += t.retweetCount;

                    Log.d("HOME", "POST LIKES " + t.favoriteCount + " COMMENTS " + t.retweetCount);
                }

                //Get chart type from preferences
                SharedPreferences settings = getSharedPreferences("ChartInfo", 0);
                String chartType = settings.getString("CHART_TYPE", "COLUMN");

                if (chartType.equals("TAG_CLOUD"))
                    chartData = TagCloudValuesGenerator.getValuesArray(totalPostLikes, totalPostComments);
                else {
                    chartData.add(new ValueDataEntry("Reactions", totalPostLikes));
                    chartData.add(new ValueDataEntry("Comments", totalPostComments));
                }

                Bundle chartExtras = new Bundle();

                ChartModel homeChartModel = new ChartModel(chartType, chartData, chartExtras);
                homeChart.setChart(homeChartModel.populate());

                computeAndSetStatus(totalPostLikes,totalPostComments);
                refresher.setRefreshing(false);
            }

            @Override
            public void failure(TwitterException exception) {
                Log.d("HOME", "TWITTER EXCEPTION THROWN " + exception.toString());
                loadNotPopulatedChart();
            }
        });

    }

    //Function to determine the social and call fetch and plot for it
    private void fetchAndPlot() {
        switch (LoginHandler.getInstance().getLoginType()) {
            case 1:
                homeCurrentSocialTextView.setText("Facebook");
                fetchAndPlotFacebookData();
                break;
            case 2:
                homeCurrentSocialTextView.setText("Twitter");
                fetchAndPlotTwitterData();
                break;
            case 0:
                loadNotPopulatedChart();
                Log.d("HOME", "LoginHandler.getInstance().getLoginType() currently not logged");
                break;
        }
    }

    //Function to plot dummy chart if no social is connected
    private void loadNotPopulatedChart() {
        homeStatusTextView.setText("No status available");
        homeCurrentSocialTextView.setText("No social connected!\nGo to settings to log in");
        List<DataEntry> columnData = new ArrayList<>();
        columnData.add(new ValueDataEntry("Reactions", 0));
        columnData.add(new ValueDataEntry("Comments", 0));

        Bundle columnExtras = new Bundle();

        ChartModel homeChartModel = new ChartModel("COLUMN", columnData, columnExtras);
        homeChart.setChart(homeChartModel.populate());

        refresher.setRefreshing(false);
    }

    //Function to pick status text and picture
    private void computeAndSetStatus(int totalLikes,int totalComments){
        //todo today,yeah sure
       // homeStatusImageView.setImageResource(HomeStatusUpdater.getSuitableImageResource(totalLikes,totalComments));
        homeStatusTextView.setText(HomeStatusUpdater.getSuitableTextString(totalLikes,totalComments));
    }
}
