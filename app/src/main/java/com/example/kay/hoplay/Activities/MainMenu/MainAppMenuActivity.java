package com.example.kay.hoplay.Activities.MainMenu;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.kay.hoplay.App.App;
import com.example.kay.hoplay.Authentication.LoginActivity;
import com.example.kay.hoplay.Interfaces.FirebasePaths;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by Kay on 2/10/2017.
 */

public class MainAppMenuActivity extends MainAppMenu implements FirebasePaths{

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
                    toLogin();
                } else {
                    app.setmAuthStateListener(authStateListener);
                    setupUserInformation(user);
                }
            }
        };

    }

    private void setupUserInformation(FirebaseUser user) {
        app.getUserInformation().setUID(user.getUid());
        app.getDatabaseUsers().child(app.getUserInformation().getUID()).child(FIREBASE_DETAILS_ATTR).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                if(isInfoValid(dataSnapshot))
                {

                    app.getUserInformation().setUsername(dataSnapshot.child("_username_").getValue().toString());
                    app.getUserInformation().setNickName(dataSnapshot.child("_nickname_").getValue().toString());
                    app.getUserInformation().setPictureURL(dataSnapshot.child("_picUrl_").getValue().toString());
                }
                else
                {
                    app.signOut();
                    toLogin();
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private boolean isInfoValid(DataSnapshot dataSnapshot) {
        return dataSnapshot.hasChild("_username_") && dataSnapshot.hasChild("_nickname_") && dataSnapshot.hasChild("_picUrl_");
    }
}
