package com.hoplay.kay.hoplay.Activities;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.hoplay.kay.hoplay.App.App;
import com.hoplay.kay.hoplay.R;

public class NoConnection extends AppCompatActivity {

    TextView connectionMessageTextView ;
    Runnable runnable;
    final Handler handler = new Handler();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.no_connection);

        connectionMessageTextView = (TextView) findViewById(R.id.connection_error_textview);
        Typeface playbold = Typeface.createFromAsset(getAssets(), "playbold.ttf");
        connectionMessageTextView.setTypeface(playbold);



        // Check continuously if the user has network connection

        final int delay = 1000; //milliseconds
        handler.postDelayed(runnable= new Runnable(){
            public void run(){
                //do something


                if (isNetworkAvailable())
                {
                   finish();
                    handler.removeCallbacks(runnable);
                }

                handler.postDelayed(this, delay);
            }
        }, delay);





    }


    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }



    @Override
    public void onStart() {
        super.onStart();
        App.noConnectionActivityIsActive = true;
    }

    @Override
    public void onStop() {
        super.onStop();
        App.noConnectionActivityIsActive = false;
    }





    @Override
    public void onBackPressed() {
        // do nothing.
    }



    //    @Override
//    protected void onResume() {
//        super.onResume();
//
//        handler.post(runnable);
//        App.noConnectionHandler.post(App.noConnectionRunnable);
//    }
}
