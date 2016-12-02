package com.example.kay.hoplay.Activities;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.kay.hoplay.Fragments.SettingsScreen;
import com.example.kay.hoplay.R;

/**
 * Created by Kay on 12/1/2016.
 */

public class SettingsActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);


        Fragment fragment = new SettingsScreen();
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        if(savedInstanceState == null)
        {
            fragmentTransaction.add(R.id.activity_settings_relativelayout,fragment,"settings_fragment");
            fragmentTransaction.commit();

        }
        else {
            fragment = getFragmentManager().findFragmentByTag("settings_fragment");
        }
    }
}
