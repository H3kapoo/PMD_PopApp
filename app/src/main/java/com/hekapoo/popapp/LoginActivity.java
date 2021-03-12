package com.hekapoo.popapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.hekapoo.popapp.APIHandler.FacebookAPIHandler;

import java.util.Arrays;
import java.util.List;

//PURPOSE: Log in/out of fb/twitter
public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);

        Button fbLoginBtn = findViewById(R.id.fb_log_btn);

        List<String> perms = Arrays.asList("email", "public_profile", "pages_manage_posts", "user_posts");

        if(FacebookAPIHandler.getInstance().hasToken()){
            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
            startActivity(intent);
        }

        FacebookAPIHandler.getInstance().login(fbLoginBtn, this, perms, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Intent intent = new Intent(LoginActivity.this, ChartsActivity.class);
                startActivity(intent);
            }

            @Override
            public void onCancel() {
                Log.d("fb_log", "onCancel: user canceled");

            }

            @Override
            public void onError(FacebookException error) {
                Log.d("fb_log", "onError: something happened during the login");
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        FacebookAPIHandler.getInstance().getCallbackManager().onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }


}