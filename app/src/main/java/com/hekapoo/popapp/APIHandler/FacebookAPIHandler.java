package com.hekapoo.popapp.APIHandler;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageButton;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;

import java.util.List;

public class FacebookAPIHandler {

    private static CallbackManager callbackManager;
    private static FacebookAPIHandler instance = null;

    public interface FacebookAPIHandlerCB {
        void result(GraphResponse obj);

    }

    private FacebookAPIHandler() {
        callbackManager = CallbackManager.Factory.create();
    }

    public static FacebookAPIHandler getInstance() {
        if (instance == null)
            instance = new FacebookAPIHandler();
        return instance;
    }

    public void login(Button btn, Activity act, List<String> perms, FacebookCallback<LoginResult> re) {

        btn.setOnClickListener(e -> {
            LoginManager.getInstance().logInWithReadPermissions(act, perms);
        });

        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                re.onSuccess(loginResult);
            }

            @Override
            public void onCancel() {
                re.onCancel();
                Log.d("app", "onCancel: ");
            }

            @Override
            public void onError(FacebookException exception) {
                Log.d("app", "onError: ");
                re.onError(exception);

            }
        });
    }

    public void logout() {
        LoginManager.getInstance().logOut();
    }

    //Function to send api calls and get the response
    public void setBtnGraphRequest(Button btn, String endPoint, Bundle bundle, FacebookAPIHandlerCB cb) {
        if (AccessToken.getCurrentAccessToken() != null)
            btn.setOnClickListener(e -> {
                GraphRequest request = GraphRequest.newGraphPathRequest(
                        AccessToken.getCurrentAccessToken(),
                        endPoint,
                        cb::result);
                request.setParameters(bundle);
                request.executeAsync();
            });

    }

    public boolean hasToken() {
        return AccessToken.getCurrentAccessToken() != null;
    }

    public CallbackManager getCallbackManager() {
        return callbackManager;
    }

}
