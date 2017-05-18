package com.example.kay.hoplay.Services;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.widget.Toast;

import com.example.kay.hoplay.Cores.AuthenticationCore.LoginCore;
import com.example.kay.hoplay.App.App;
import com.example.kay.hoplay.R;

/**
 * Created by Kay on 12/1/2016.
 */

public class SettingsScreen extends PreferenceFragment {

    private  Context context;

    public SettingsScreen(){


    }

        public void setContext(Context c){
            this.context = c;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.settings_screen);




        // Update user profile





        // Log out on click preference
        Preference preferences = (Preference) findPreference("settings_logout");
        preferences.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            public boolean onPreferenceClick(Preference preference) {
                //Do whatever you want here
                App.getInstance().signOut();
                Intent i = new Intent(context, LoginCore.class);
                startActivity(i);

                Toast.makeText(getActivity().getApplicationContext(),R.string.settings_screen_logout_message,Toast.LENGTH_LONG).show();

                return true;
            }
        });
    }
}