package com.hoplay.kay.hoplay.Services;

import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.iid.FirebaseInstanceId;
import com.hoplay.kay.hoplay.App.App;

import java.io.IOException;

/**
 * Created by khaledAlhindi on 10/4/2017 AD.
 */

public class FirebaseInstanceIdService  extends com.google.firebase.iid.FirebaseInstanceIdService{

public static final String REG_TOKEN = "REG_TOKEN";


   public static String recentRoken = "";

    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();



       recentRoken = FirebaseInstanceId.getInstance().getToken();
        Log.i(REG_TOKEN,recentRoken);


    }
}
