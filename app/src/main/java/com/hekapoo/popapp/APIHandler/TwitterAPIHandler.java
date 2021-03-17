package com.hekapoo.popapp.APIHandler;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.hekapoo.popapp.R;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.Twitter;
import com.twitter.sdk.android.core.TwitterApiClient;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterAuthClient;
import com.twitter.sdk.android.core.models.Tweet;

import java.util.List;

import retrofit2.Call;

public class TwitterAPIHandler {
    private static TwitterAPIHandler instance = null;
    private static TwitterAuthClient client;

    private TwitterAPIHandler() {
        client = new TwitterAuthClient();
    }

    public void login(Button btn, Activity act, Callback<TwitterSession> cb) {

        //if logged in already,log out if clicked
        btn.setOnClickListener(e -> {
            if (hasToken()) {
                fastLogout();
                Log.d("tweet", "Log out ses has token " + hasToken());
                btn.setBackgroundResource(R.color.app_light_green); //check why this
                btn.setText("LOG INTO TWITTER");
            } else {
                client.authorize(act, new Callback<TwitterSession>() {
                    @Override
                    public void success(Result<TwitterSession> result) {
                        Log.d("tweet", "Login ok");
                        btn.setBackgroundResource(R.color.app_btn_gray);
                        btn.setText("LOG OUT TWITTER");
                        cb.success(result);
                    }

                    @Override
                    public void failure(TwitterException e) {
                        //feedback
                        Log.d("tweet", "Login failed");
                    }
                });
            }//else end
        });
    }

    //Function to send api calls and get the response
    public void sendGraphRequest(Callback<List<Tweet>> cb) {

        TwitterApiClient twitterApiClient = TwitterCore.getInstance().getApiClient();

        Call<List<Tweet>> tweets = twitterApiClient.getStatusesService().userTimeline(TwitterCore.getInstance().getSessionManager().getActiveSession().getUserId(), null, 10, null, null, false, false, false, false);

        tweets.enqueue(new Callback<List<Tweet>>() {
            @Override
            public void success(Result<List<Tweet>> result) {
                Log.d("tweet", "Login worked");
                cb.success(result);
            }

            @Override
            public void failure(TwitterException exception) {
                //Do something on failure
                Log.d("tweet", exception.toString());
                cb.failure(exception);
            }
        });

    }

    public static TwitterAPIHandler getInstance() {
        if (instance == null)
            instance = new TwitterAPIHandler();
        return instance;
    }

    public TwitterAuthClient getClient() {
        return client;
    }

    public void fastLogout() {
        TwitterCore.getInstance().getSessionManager().clearActiveSession();
    }

    public boolean hasToken() {
        return TwitterCore.getInstance().getSessionManager().getActiveSession() == null ? false : true;
    }
}
