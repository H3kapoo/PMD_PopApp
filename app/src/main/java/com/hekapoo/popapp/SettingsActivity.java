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
import com.hekapoo.popapp.Login.LoginHandler;

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

        //Handlers Init
        chartsBtn = findViewById(R.id.charts_btn);
        homeBtn = findViewById(R.id.home_btn);
        logFbBtn = findViewById(R.id.loginto_fb_btn);
        logTwBtn = findViewById(R.id.loginto_tw_btn);
        columnBtn = findViewById(R.id.column_chart_select_btn);
        pieBtn = findViewById(R.id.pie_chart_selection_option);
        tagcloudBtn = findViewById(R.id.tagcloud_selection_option);

        //Set on create buttons text
        setViewTexts();

        //Set facebook login handler
        List<String> perms = Arrays.asList("email", "public_profile", "pages_manage_posts", "user_posts");

        //TODO: Radio button like (only one can be chosen)
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

    private void setViewTexts(){
        switch (LoginHandler.getInstance().getLoginType()) {
            case 1:
                logFbBtn.setText("LOG OUT FACEBOOK");
                logTwBtn.setText("LOG INTO TWITTER");
                break;
            case 2:
                logTwBtn.setText("LOG OUT TWITTER");
                logFbBtn.setText("LOG INTO FACEBOOK");
                break;
            case 0:
                logTwBtn.setText("LOG INTO TWITTER");
                logFbBtn.setText("LOG INTO FACEBOOK");
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
