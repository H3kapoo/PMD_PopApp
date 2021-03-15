package com.hekapoo.popapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.anychart.charts.Pie;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.hekapoo.popapp.APIHandler.FacebookAPIHandler;
import com.hekapoo.popapp.Login.LoginHandler;

import java.util.Arrays;
import java.util.List;

public class SettingsActivity extends AppCompatActivity {

    Button homeBtn, chartsBtn;
    Button logFbBtn, logTwBtn;
    Button columnBtn, pieBtn, tagcloudBtn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_layout);

        //Handlers Init
        chartsBtn = findViewById(R.id.charts_btn);
        homeBtn = findViewById(R.id.home_btn);
        logFbBtn = findViewById(R.id.loginto_fb_btn);
        logTwBtn = findViewById(R.id.loginto_tw_btn);
        columnBtn = findViewById(R.id.column_chart_selection_btn);
        pieBtn = findViewById(R.id.pie_chart_selection_option);
        tagcloudBtn = findViewById(R.id.tagcloud_selection_option);

        //Set on create buttons text & color
        setSelectedChart();
        setLoginButtonsColor();

        //Set facebook login handler
        List<String> perms = Arrays.asList("email", "public_profile", "pages_manage_posts", "user_posts");

        //Facebook login callback
        FacebookAPIHandler.getInstance().login(logFbBtn, this, perms, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d("settings", "Logged into facebook OK");
                setLoginButtonsColor();
            }

            //Not needed
            @Override
            public void onCancel() {
                Log.d("settings", "Logged into facebook ABORT");

            }

            @Override
            public void onError(FacebookException error) {
                Log.d("settings", "Logged into facebook ERROR");

            }
        });

        //Twitter login callback
        //TODO: WAIT FOR TWITTER APPROVAL :|

        //Set charts button handler
        columnBtn.setOnClickListener(e -> {
            //Get chart type from preferences
            SharedPreferences loadedSettings = getSharedPreferences("ChartInfo", 0);
            String chartType = loadedSettings.getString("CHART_TYPE", "kappa");

            if (!chartType.equals("COLUMN")) {
                pieBtn.setBackgroundColor(getResources().getColor(R.color.app_light_green));
                tagcloudBtn.setBackgroundColor(getResources().getColor(R.color.app_light_green));
                columnBtn.setBackgroundColor(getResources().getColor(R.color.app_btn_gray));

                //Save preference
                SharedPreferences settings = getSharedPreferences("ChartInfo", 0);
                SharedPreferences.Editor editor = settings.edit();
                editor.putString("CHART_TYPE", "COLUMN");
                editor.commit();

            }
        });

        pieBtn.setOnClickListener(e -> {
            //Get chart type from preferences
            SharedPreferences loadedSettings = getSharedPreferences("ChartInfo", 0);
            String chartType = loadedSettings.getString("CHART_TYPE", "kappa");

            if (!chartType.equals("PIE")) {
                columnBtn.setBackgroundColor(getResources().getColor(R.color.app_light_green));
                tagcloudBtn.setBackgroundColor(getResources().getColor(R.color.app_light_green));
                pieBtn.setBackgroundColor(getResources().getColor(R.color.app_btn_gray));

                //Save preference
                SharedPreferences settings = getSharedPreferences("ChartInfo", 0);
                SharedPreferences.Editor editor = settings.edit();
                editor.putString("CHART_TYPE", "PIE");
                editor.commit();
            }
        });

        tagcloudBtn.setOnClickListener(e -> {
            //Get chart type from preferences
            SharedPreferences loadedSettings = getSharedPreferences("ChartInfo", 0);
            String chartType = loadedSettings.getString("CHART_TYPE", "kappa");

            if (!chartType.equals("TAG_CLOUD")) {
                columnBtn.setBackgroundColor(getResources().getColor(R.color.app_light_green));
                pieBtn.setBackgroundColor(getResources().getColor(R.color.app_light_green));
                tagcloudBtn.setBackgroundColor(getResources().getColor(R.color.app_btn_gray));

                //Save preference
                SharedPreferences settings = getSharedPreferences("ChartInfo", 0);
                SharedPreferences.Editor editor = settings.edit();
                editor.putString("CHART_TYPE", "TAG_CLOUD");
                editor.commit();
            }
        });

        //Set nav buttons handlers
        chartsBtn.setOnClickListener(e -> {
            Intent intent = new Intent(SettingsActivity.this, ChartsActivity.class);
            startActivity(intent);
        });

        homeBtn.setOnClickListener(e -> {
            Intent intent = new Intent(SettingsActivity.this, HomeActivity.class);
            startActivity(intent);
        });
    }
    private void setSelectedChart(){
        //Get chart type from preferences
        SharedPreferences settings = getSharedPreferences("ChartInfo", 0);
        String chartType = settings.getString("CHART_TYPE", "kappa");

        switch (chartType){
            case "COLUMN":
                columnBtn.setBackgroundColor(getResources().getColor(R.color.app_btn_gray));
                pieBtn.setBackgroundColor(getResources().getColor(R.color.app_light_green));
                tagcloudBtn.setBackgroundColor(getResources().getColor(R.color.app_light_green));
                break;
            case "PIE":
                pieBtn.setBackgroundColor(getResources().getColor(R.color.app_btn_gray));
                tagcloudBtn.setBackgroundColor(getResources().getColor(R.color.app_light_green));
                columnBtn.setBackgroundColor(getResources().getColor(R.color.app_light_green));
                break;
            case "TAG_CLOUD":
                tagcloudBtn.setBackgroundColor(getResources().getColor(R.color.app_btn_gray));
                pieBtn.setBackgroundColor(getResources().getColor(R.color.app_light_green));
                columnBtn.setBackgroundColor(getResources().getColor(R.color.app_light_green));
                break;
        }
    }

    private void setLoginButtonsColor() {

        //Login stuff
        switch (LoginHandler.getInstance().getLoginType()) {
            case 1:
                logFbBtn.setText("LOG OUT FACEBOOK");
                logTwBtn.setText("LOG INTO TWITTER");
                logFbBtn.setBackgroundColor(getResources().getColor(R.color.app_btn_gray));
                logTwBtn.setBackgroundColor(getResources().getColor(R.color.app_light_green));
                break;
            case 2:
                logTwBtn.setText("LOG OUT TWITTER");
                logFbBtn.setText("LOG INTO FACEBOOK");
                logTwBtn.setBackgroundColor(getResources().getColor(R.color.app_btn_gray));
                logFbBtn.setBackgroundColor(getResources().getColor(R.color.app_light_green));
                break;
            case 0:
                logTwBtn.setText("LOG INTO TWITTER");
                logFbBtn.setText("LOG INTO FACEBOOK");
                logFbBtn.setBackgroundColor(getResources().getColor(R.color.app_light_green));
                logTwBtn.setBackgroundColor(getResources().getColor(R.color.app_light_green));
                Log.d("SETTINGS", "LoginHandler.getInstance().getLoginType() currently not logged");
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        FacebookAPIHandler.getInstance().getCallbackManager().onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }
}
