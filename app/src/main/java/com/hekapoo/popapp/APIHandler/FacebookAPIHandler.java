package com.hekapoo.popapp.APIHandler;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

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

    public interface FacebookAPIHandlerCB {
        void result(GraphResponse obj);
    }

    @SuppressLint("SetTextI18n")
    public FacebookAPIHandler(Activity act, Button btn, List<String> perms) {

        callbackManager = CallbackManager.Factory.create();

        if (AccessToken.getCurrentAccessToken() != null)
            btn.setText("Log out");

        btn.setOnClickListener(e -> {
            if (AccessToken.getCurrentAccessToken() == null) {
                LoginManager.getInstance().logInWithReadPermissions(act, perms);

            } else {
                LoginManager.getInstance().logOut();
                btn.setText("Log in");
            }
        });

        // Login callback registration
        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                btn.setText("Log out");
            }

            @Override
            public void onCancel() {
                // App code
                Log.d("app", "onCancel: ");
            }

            @Override
            public void onError(FacebookException exception) {
                // App code
                Log.d("app", "onError: ");
            }
        });

    }

    public void setBtnGraphRequest(Button btn, String endPoint, Bundle bundle, FacebookAPIHandlerCB cb) {
        btn.setOnClickListener(e->{
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
}
