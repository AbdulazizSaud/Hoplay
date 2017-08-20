package com.example.kay.hoplay.Cores.AuthenticationCore;

//hello


import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;

import android.util.Log;
import android.widget.Toast;


import com.example.kay.hoplay.App.App;

import com.example.kay.hoplay.CoresAbstract.AuthenticationAbstracts.Login;
import com.example.kay.hoplay.R;


import com.example.kay.hoplay.Services.SchemaHelper;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


/*
 */

public class LoginCore extends Login {

    // Get firebase authentication from App
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseUser mFirebaseUser;

    @Override
    protected void onStart() {
        mAuth.addAuthStateListener(authStateListener);
        super.onStart();

    }

    @Override
    protected void onStop() {
        mAuth.removeAuthStateListener(authStateListener);
        super.onStop();
    }

    // this method will do some init to check if user is authenticated , if yes .. it will switch to MainAppMenu
    @Override
    public void OnStartActivity() {
        mAuth = app.getAuth();
        mFirebaseUser = mAuth.getCurrentUser();


        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null && user.isEmailVerified()) {
                    app.getUserInformation().setUID(user.getUid());
                    toMainMenu();
                }
            }
        };

    }


    public static void restartApp(Context context)
    {
        Intent mStartActivity = new Intent(context, LoginCore.class); //Replace StartActivity with the name of the first activity in your app
        int mPendingIntentId = 123456;
        PendingIntent mPendingIntent = PendingIntent.getActivity(context, mPendingIntentId,    mStartActivity, PendingIntent.FLAG_CANCEL_CURRENT);
        AlarmManager mgr = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 100, mPendingIntent);
        System.exit(0);
    }


    // this method will do auth to check if the user is existing
    // receive:  username , password

    // if success, it will switch to a mainApp and give a success message
    // if fail , it will give the user a failed message

    @Override
    protected void login(final String username, String password) {

        if (!username.equals("") || !password.equals("")) {

            loadingDialog(true);


            mAuth.signInWithEmailAndPassword(username, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {

                        SchemaHelper schemaHelper = new SchemaHelper(LoginCore.this);
                        firstTime = schemaHelper.isStamped();

                        schemaHelper.removeStamp();

                        FirebaseUser firebaseUser = mAuth.getCurrentUser();

                        if(!mAuth.getCurrentUser().isEmailVerified())
                        {
                            showVerificationEmailDialog(firebaseUser,username);
                            loadingDialog(false);

                        } else {
                            toMainMenu();
                        }

                    } else {
                        // results if it's failed
                        loadingDialog(false);
                        Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            });


        }
    }


}