package com.hekapoo.popapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class SettingsActivity extends AppCompatActivity {

    Button homeBtn,chartsBtn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_layout);

        chartsBtn = findViewById(R.id.charts_btn);
        homeBtn = findViewById(R.id.home_btn);

        chartsBtn.setOnClickListener(e -> {
            Intent intent = new Intent(SettingsActivity.this, ChartsActivity.class);
            startActivity(intent);
        });

        homeBtn.setOnClickListener(e -> {
            Intent intent = new Intent(SettingsActivity.this, HomeActivity.class);
            startActivity(intent);
        });
    }
}
