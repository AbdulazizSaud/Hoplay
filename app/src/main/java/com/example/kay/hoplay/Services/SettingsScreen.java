package com.example.kay.hoplay.Services;

import android.os.Bundle;
import android.preference.PreferenceFragment;

import com.example.kay.hoplay.R;

/**
 * Created by Kay on 12/1/2016.
 */

public class SettingsScreen extends PreferenceFragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.settings_screen);
    }
}