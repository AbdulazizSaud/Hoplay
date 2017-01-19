package com.example.kay.hoplay.Activities;

import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.kay.hoplay.R;
import com.example.kay.hoplay.Services.SettingsScreen;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        android.app.Fragment fragment = new SettingsScreen();

        ((SettingsScreen)fragment).setContext(getApplicationContext());

        android.app.FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        if(savedInstanceState ==null)
        {
            fragmentTransaction.add(R.id.activity_settings,fragment,"settings_fragment");
            fragmentTransaction.commit();
        }
        else
        {
            fragment = getFragmentManager().findFragmentByTag("settings_fragment");
        }


    }
}
