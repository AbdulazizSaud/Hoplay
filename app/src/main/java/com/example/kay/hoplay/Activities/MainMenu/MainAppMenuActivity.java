package com.example.kay.hoplay.Activities.MainMenu;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.kay.hoplay.App.App;
import com.example.kay.hoplay.Authentication.LoginActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * Created by Kay on 2/10/2017.
 */

public class MainAppMenuActivity extends MainAppMenu {

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener authStateListener;



    @Override
    protected void onStop() {
        super.onStop();
        // remove authstate.listenr to firebase auth
        mAuth.removeAuthStateListener(authStateListener);
    }

    @Override
    protected void onStart(){

        super.onStart();
        // add authstate.listenr to firebase auth
        mAuth.addAuthStateListener(authStateListener);
    }

    // init method work only on start
    @Override
    public void OnStartActivity() {
        // init a firebase auth states to check if the user is already signued
        // if no , move the user to loginActivity to sign in again.


        mAuth = App.getInstance().getAuth();
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user == null) {
                    // User is sign out
                    Log.i("---------->"," Go baaaaack");
                    Intent i = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivity(i);
                } else {
                    Log.i("---------->",firebaseAuth.getCurrentUser().getUid());

                }
            }
        };

    }
}
