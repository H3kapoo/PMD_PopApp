package com.hekapoo.popapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import com.hekapoo.popapp.APIHandler.FacebookAPIHandler;

import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    FacebookAPIHandler facebookAPIHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button fbLoginBtn = findViewById(R.id.logBtn);

        List<String> perms = Arrays.asList("email", "public_profile", "pages_manage_posts", "user_posts");

        facebookAPIHandler = new FacebookAPIHandler(this, fbLoginBtn, perms);

        Button btn1 = findViewById(R.id.dataExtract);

        Bundle parameters = new Bundle();
        parameters.putString("fields", "reactions.summary(true),comments.summary(true)");

        facebookAPIHandler.setBtnGraphRequest(btn1, "/me/posts", parameters, (response) -> {
            Log.d("resp", "Response: " + response.toString());
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        facebookAPIHandler.getCallbackManager().onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }


}