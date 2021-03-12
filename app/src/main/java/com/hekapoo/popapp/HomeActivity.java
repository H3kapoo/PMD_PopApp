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

            homeCurrentSocialTextView.setText("No social connected!\nGo to settings to log in");
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
                Log.d("HOME", "LoginHandler.getInstance().getLoginType() currently not logged");
                break;
        }

        //Set refresh listener
        refresher.setOnRefreshListener(() -> {
            //call here fetchAndPlotFacebookData()

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

        List<DataEntry> column_data = new ArrayList<>();

        //column
        column_data.add(new ValueDataEntry("Reactions", 14000));
        column_data.add(new ValueDataEntry("Comments", 4000));

        Bundle column_extras = new Bundle();
        column_extras.putString("TITLE", "Column Data Chart");

        ChartModel column_chart = new ChartModel("COLUMN", column_data, column_extras);
        homeChart.setChart(column_chart.populate());

    }

    public void fetchAndPlotFacebookData() {

        //TODO: CONTINUE PARSING
        Log.d("HOME", "fb has token OK");

        Bundle bundle = new Bundle();
        bundle.putString("fields", "reactions.summary(true),comments.summary(true)");

        FacebookAPIHandler.getInstance().sendGraphRequest("/me/posts", bundle, res -> {
            JSONObject mainObject = res.getJSONObject();
            try {
                JSONArray arr = mainObject.getJSONArray("data");
                JSONObject reactionsObj = arr.getJSONObject(0);
                Log.d("HOME", reactionsObj.toString());
            } catch (JSONException e) {

            }
        });
    }

    public void fetchAndPlotTwitterData() {

    }
}
