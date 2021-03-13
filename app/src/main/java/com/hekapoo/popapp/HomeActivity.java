package com.hekapoo.popapp;

import android.app.DatePickerDialog;
import android.content.Intent;
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
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.facebook.AccessToken;
import com.hekapoo.popapp.APIHandler.FacebookAPIHandler;
import com.hekapoo.popapp.Charts.ChartModel;
import com.hekapoo.popapp.Login.LoginHandler;

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

        //Check if user is logged into a social
        if (!LoginHandler.getInstance().isLoggedIntoSocial()) {
            Log.d("home", "onCreate: not logged ");

        } else {
            Log.d("home", "onCreate: logged ");

        }


        //Check which social media the user is currently logged into
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

        //Set refresh listener
        refresher.setOnRefreshListener(() -> {
            //call here fetchAndPlotFacebookData()
            loadNotPopulatedChart();
            //TODO: data fetching on refresh
//            if (FacebookAPIHandler.getInstance().hasToken()) {
//                Log.d("response", "has token");
//
//                Bundle bundle = new Bundle();
//                bundle.putString("fields", "reactions.summary(true),comments.summary(true)");
//
//                FacebookAPIHandler.getInstance().sendGraphRequest("/me/posts", bundle, res -> {
//                    Log.d("response", res.toString());
//                    refresher.setRefreshing(false);
//                });
//            }
            refresher.setRefreshing(false);
        });

        chartsBtn.setOnClickListener(e -> {
            FacebookAPIHandler.getInstance().fastLogout();
//            Intent intent = new Intent(HomeActivity.this, ChartsActivity.class);
//            startActivity(intent);
        });

        settingsBtn.setOnClickListener(e -> {
            Intent intent = new Intent(HomeActivity.this, SettingsActivity.class);
            startActivity(intent);
        });
    }

    public void fetchAndPlotFacebookData() {

        Log.d("HOME", "fb has token OK");

        Bundle bundle = new Bundle();
        bundle.putString("fields", "reactions.summary(true),comments.summary(true)");
        bundle.putString("limit", "20");

        FacebookAPIHandler.getInstance().sendGraphRequest("/me/posts", bundle, result -> {
            JSONObject resultObj = result.getJSONObject();
            try {

                JSONArray dataArray = resultObj.getJSONArray("data");
                Log.d("HOME", String.valueOf(dataArray.length()));

                List<DataEntry> columnData = new ArrayList<>();
                int totalPostLikes = 0, totalPostComments = 0;

                for (int i = 0; i < dataArray.length(); i++) {

                    //also apply time filtering
                    String postLikes = dataArray.getJSONObject(i).getJSONObject("reactions").getJSONObject("summary").getString("total_count");
                    String postComments = dataArray.getJSONObject(i).getJSONObject("comments").getJSONObject("summary").getString("total_count");

                    totalPostLikes += Integer.parseInt(postLikes);
                    totalPostComments += Integer.parseInt(postComments);

                    Log.d("HOME", "POST" + i + ": LIKES " + postLikes + " COMMENTS " + postComments);
                }

                columnData.add(new ValueDataEntry("Reactions", totalPostLikes));
                columnData.add(new ValueDataEntry("Comments", totalPostComments));

                Bundle columnExtras = new Bundle();

                //replace column dynamically
                ChartModel homeChartModel = new ChartModel("COLUMN", columnData, columnExtras);
                homeChart.setChart(homeChartModel.populate());

            } catch (JSONException e) {
                Log.d("HOME", "JSON EXCEPTION THROWN " + e.toString());

                loadNotPopulatedChart();
            }
        });
    }

    public void fetchAndPlotTwitterData() {

    }

    public void loadNotPopulatedChart() {
        homeStatusTextView.setText("No status available");
        homeCurrentSocialTextView.setText("No social connected!\nGo to settings to log in");
        List<DataEntry> columnData = new ArrayList<>();
        columnData.add(new ValueDataEntry("Reactions", 0));
        columnData.add(new ValueDataEntry("Comments", 0));

        Bundle columnExtras = new Bundle();

        //replace column dynamically
        ChartModel homeChartModel = new ChartModel("COLUMN", columnData, columnExtras);
        homeChart.setChart(homeChartModel.populate());


    }
}
