package com.hekapoo.popapp.Login;

import com.hekapoo.popapp.APIHandler.FacebookAPIHandler;
import com.hekapoo.popapp.APIHandler.TwitterAPIHandler;

public class LoginHandler {
    private static LoginHandler instance = null;

    private LoginHandler() {
    }

    public static LoginHandler getInstance() {
        if (instance == null)
            instance = new LoginHandler();
        return instance;
    }

    public boolean isLoggedIntoSocial() {
        //return logged into facebook or logged into twitter
        return FacebookAPIHandler.getInstance().hasToken() ? true : false ||
                TwitterAPIHandler.getInstance().hasToken() ? true : false;
    }

    public int getLoginType() {
        if (FacebookAPIHandler.getInstance().hasToken()) return 1;
        if(TwitterAPIHandler.getInstance().hasToken()) return 2;
        //else
        return 0;
    }

}
