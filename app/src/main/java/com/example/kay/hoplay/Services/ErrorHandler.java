package com.example.kay.hoplay.Services;

import android.app.Activity;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.kay.hoplay.R;

import java.net.HttpURLConnection;

/**
 * Created by Kay on 10/27/2016.
 */

public class ErrorHandler{

    public static final String ERROR_CONNECTION = "ERROR_CONNECTION";
    public static final String ERROR_PATH = "ERROR_PATH";
    public static final String ERROR_DATA_MISS_MATCH = "ERROR_DATA_MISS_MATCH";
    public static final String ERROR_IO_EXP = "IO_EXP";
    public static final String CLEAR = "CLEAR";

    public static boolean isError(String e){
        switch (e){
            case ERROR_CONNECTION:
            case ERROR_PATH:
            case ERROR_DATA_MISS_MATCH:
            case  ERROR_IO_EXP:
                return true;
        }

        if(e.equals(null))
            return true;

        return false;
    }

    public static void showConnectionERROR(Context c){

        LayoutInflater inflater =  LayoutInflater.from(c);
        View layout = inflater.inflate(R.layout.no_connection
                ,null);

        Toast toast = new Toast(c);
        toast.setGravity(Gravity.FILL, 0, 0);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(layout);
        toast.show();
    }

}
