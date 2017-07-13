package com.example.kay.hoplay.Services;

import android.app.Activity;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.kay.hoplay.CoresAbstract.MainAppMenu;
import com.example.kay.hoplay.R;

import java.net.HttpURLConnection;


public class ErrorHandler implements java.lang.Thread.UncaughtExceptionHandler {
    private  Activity context;


    private final Activity myContext;
    private final String LINE_SEPARATOR = "\n";

    public ErrorHandler(Activity context) {
        myContext = context;
    }

    public void uncaughtException(Thread thread, Throwable exception) {
        System.exit(1); // kill off the crashed app


    }


}
