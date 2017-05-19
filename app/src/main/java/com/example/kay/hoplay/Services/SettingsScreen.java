package com.example.kay.hoplay.Services;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.example.kay.hoplay.Activities.SettingsActivity;
import com.example.kay.hoplay.Cores.AuthenticationCore.LoginCore;
import com.example.kay.hoplay.App.App;
import com.example.kay.hoplay.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

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
                startActivity(i);


                Toast.makeText(getActivity().getApplicationContext(),R.string.settings_screen_logout_message,Toast.LENGTH_LONG).show();

                return true;
            }
        });

        // Change password preference
        Preference changePasswordPref = (Preference) findPreference("settings_change_password");
       changePasswordPref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
           @Override
           public boolean onPreferenceClick(Preference preference) {

               if (mAuth != null) {

                   final String email = app.getUserInformation().getUserEmail();

                   if(!email.equals(""))
                       mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                           @Override
                           public void onComplete(@NonNull Task<Void> task) {
                               if (task.isSuccessful()) {

                                   String Msg = String.format(getResources().getString( R.string.settings_change_password_message), email);
                                   Toast.makeText(context, Msg, Toast.LENGTH_LONG).show();
                                   App.getInstance().signOut();
                                   getActivity().finish();
                                   Intent i = new Intent(context, LoginCore.class);
                                   startActivity(i);

                               }
                           }
                       });

               } else {
                   Log.e("------>", "mauth is null");
               }




               return true;
           }
       });


        // Deactive user account
        Preference deactiveUserPref = (Preference) findPreference("settings_deactivate_account");
        deactiveUserPref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {


                return true;
            }
        });

    }



}