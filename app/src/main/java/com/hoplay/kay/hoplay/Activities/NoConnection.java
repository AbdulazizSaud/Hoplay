package com.hoplay.kay.hoplay.Activities;

import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.hoplay.kay.hoplay.R;

public class NoConnection extends AppCompatActivity {

    TextView connectionMessageTextView ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.no_connection);

        connectionMessageTextView = (TextView) findViewById(R.id.connection_error_textview);
        Typeface sansationbold = Typeface.createFromAsset(getAssets(), "sansationbold.ttf");
        connectionMessageTextView.setTypeface(sansationbold);


    }
}
