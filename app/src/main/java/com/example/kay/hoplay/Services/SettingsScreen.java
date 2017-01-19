package com.example.kay.hoplay.Services;

import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.widget.Toast;

import com.example.kay.hoplay.R;

/**
 * Created by Kay on 12/1/2016.
 */

public class SettingsScreen extends PreferenceFragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.settings_screen);


        // Log out on click preference
        Preference preferences = (Preference) findPreference("settings_logout");
        preferences.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            public boolean onPreferenceClick(Preference preference) {
                //Do whatever you want here
                Toast.makeText(getActivity().getApplicationContext(),"YOYOYOOYOYO",Toast.LENGTH_LONG).show();

                return true;
            }
        });
    }
}