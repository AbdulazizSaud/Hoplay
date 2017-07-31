package com.example.kay.hoplay.Services;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kay.hoplay.Cores.AuthenticationCore.LoginCore;
import com.example.kay.hoplay.App.App;
import com.example.kay.hoplay.Cores.SupportCore;
import com.example.kay.hoplay.Cores.UserProfileCores.ChangePasswordCore;
import com.example.kay.hoplay.Cores.UserProfileCores.DeactiveAccountCore;
import com.example.kay.hoplay.Cores.UserProfileCores.EditProfileCore;
import com.example.kay.hoplay.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

/**
 * Created by Kay on 12/1/2016.
 */

public class SettingsScreen extends PreferenceFragment {

    private  Context context;
    private FirebaseAuth mAuth;
    private  App app;


    public SettingsScreen(){


    }

        public void setContext(Context c){
            this.context = c;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.settings_screen);

        app = App.getInstance();
        mAuth = app.getAuth();

        // Log out on click preference
        Preference logoutPref = (Preference) findPreference("settings_logout");
        logoutPref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            public boolean onPreferenceClick(Preference preference) {
                //Do whatever you want here
               App.getInstance().signOut();
                getActivity().finish();
                Intent i = new Intent(context, LoginCore.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);


                Toast.makeText(getActivity().getApplicationContext(),R.string.settings_screen_logout_message,Toast.LENGTH_LONG).show();

                return true;
            }
        });



        // Go to support activity
        Preference supportPref = (Preference) findPreference("settings_contact_us");
        supportPref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {

                Intent i = new Intent(context, SupportCore.class);
                startActivity(i);

                return true;
            }
        });


        // go to edit profile activity
        Preference editProfilePref = (Preference) findPreference("settings_edit_profile");
        editProfilePref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {

                Intent i = new Intent(context, EditProfileCore.class);
                startActivity(i);
                return true;
            }
        });




        // Change password preference
        Preference changePasswordPref = (Preference) findPreference("settings_change_password");
       changePasswordPref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
           @Override
           public boolean onPreferenceClick(Preference preference) {

               Intent i = new Intent(context, ChangePasswordCore.class);
               startActivity(i);
               return true;
           }
       });




        // Deactive user account
        Preference deactiveUserPref = (Preference) findPreference("settings_deactivate_account");
        deactiveUserPref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {

                Intent i = new Intent(context, DeactiveAccountCore.class);
                startActivity(i);

                return true;
            }
        });

    }




}