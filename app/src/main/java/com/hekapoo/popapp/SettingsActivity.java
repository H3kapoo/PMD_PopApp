package com.hekapoo.popapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.hekapoo.popapp.APIHandler.FacebookAPIHandler;

import java.util.Arrays;
import java.util.List;

public class SettingsActivity extends AppCompatActivity {

    Button homeBtn,chartsBtn;
    Button logFbBtn,logTwBtn;
    Button columnBtn,pieBtn,tagcloudBtn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_layout);

        chartsBtn = findViewById(R.id.charts_btn);
        homeBtn = findViewById(R.id.home_btn);
        logFbBtn = findViewById(R.id.loginto_fb_btn);
        logTwBtn = findViewById(R.id.loginto_tw_btn);
        columnBtn = findViewById(R.id.column_chart_select_btn);
        pieBtn = findViewById(R.id.pie_chart_selection_option);
        tagcloudBtn = findViewById(R.id.tagcloud_selection_option);

        List<String> perms = Arrays.asList("email", "public_profile", "pages_manage_posts", "user_posts");

        FacebookAPIHandler.getInstance().login(logFbBtn, this, perms, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d("settings", "Logged into facebook OK");
            }

            @Override
            public void onCancel() {
                Log.d("settings", "Logged into facebook ABORT");

            }

            @Override
            public void onError(FacebookException error) {
                Log.d("settings", "Logged into facebook ERROR");

            }
        });

        chartsBtn.setOnClickListener(e -> {
            Intent intent = new Intent(SettingsActivity.this, ChartsActivity.class);
            startActivity(intent);
        });

        homeBtn.setOnClickListener(e -> {
            Intent intent = new Intent(SettingsActivity.this, HomeActivity.class);
            startActivity(intent);
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        FacebookAPIHandler.getInstance().getCallbackManager().onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }
}
