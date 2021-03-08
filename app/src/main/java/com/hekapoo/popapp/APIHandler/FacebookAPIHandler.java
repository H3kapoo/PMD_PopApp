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

    private final CallbackManager callbackManager;
    private boolean loggedIn = false;

    public interface FacebookAPIHandlerCB {
        void result(GraphResponse obj);
    }


    //NEED LATER REFACTOR 6MAR
    @SuppressLint("SetTextI18n")
    public FacebookAPIHandler() {
        callbackManager = CallbackManager.Factory.create();
    }

    public void login(Button btn,Activity act,List<String> perms,FacebookCallback<LoginResult> re) {

        btn.setOnClickListener(e -> {
            LoginManager.getInstance().logInWithReadPermissions(act, perms);
        });

        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                loggedIn = true;
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
        loggedIn = false;
    }

    //Function to send api calls and get the response
    public void setBtnGraphRequest(Button btn, String endPoint, Bundle bundle, FacebookAPIHandlerCB cb) {
        if (AccessToken.getCurrentAccessToken() != null && loggedIn)
            btn.setOnClickListener(e -> {
                GraphRequest request = GraphRequest.newGraphPathRequest(
                        AccessToken.getCurrentAccessToken(),
                        endPoint,
                        cb::result);
                request.setParameters(bundle);
                request.executeAsync();
            });

    }

    public CallbackManager getCallbackManager() {
        return callbackManager;
    }

    public boolean isLoggedIn() {
        return loggedIn;
    }
}
