package com.example.kay.hoplay.Cores;

import android.support.annotation.NonNull;

import com.example.kay.hoplay.CoresAbstract.MainAppMenu;
import com.example.kay.hoplay.App.App;
import com.example.kay.hoplay.Interfaces.FirebasePaths;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by Kay on 2/10/2017.
 */

public class MainAppMenuCore extends MainAppMenu implements FirebasePaths{

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener authStateListener;



    @Override
    protected void onStop() {
        // remove authstate.listenr to firebase auth
        mAuth.removeAuthStateListener(authStateListener);
        super.onStop();

    }

    @Override
    protected void onStart(){
        // add authstate.listenr to firebase auth
        mAuth.addAuthStateListener(authStateListener);
        super.onStart();

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
        app.getDatabaseUsersInfo().child(app.getUserInformation().getUID()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                if(isInfoValid(dataSnapshot))
                {

                    String username = dataSnapshot.child(FIREBASE_USERNAME_PATH).getValue().toString();
                    String nickname = dataSnapshot.child(FIREBASE_NICKNAME_PATH).getValue().toString();
                    String picUrl = dataSnapshot.child(FIREBASE_PICTURE_URL_PATH).getValue().toString();

                    welcomeMessage(username);

                    app.getUserInformation().setUsername(username);
                    app.getUserInformation().setNickName(nickname);
                    app.getUserInformation().setPictureURL(picUrl);
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
        return dataSnapshot.hasChild(FIREBASE_USERNAME_PATH) && dataSnapshot.hasChild(FIREBASE_NICKNAME_PATH) && dataSnapshot.hasChild(FIREBASE_PICTURE_URL_PATH);
    }


}
