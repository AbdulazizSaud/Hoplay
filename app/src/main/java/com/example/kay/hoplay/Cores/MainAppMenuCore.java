package com.example.kay.hoplay.Cores;

import android.support.annotation.NonNull;
import android.util.Log;

import com.example.kay.hoplay.CoresAbstract.MainAppMenu;
import com.example.kay.hoplay.App.App;
import com.example.kay.hoplay.Interfaces.FirebasePaths;
import com.example.kay.hoplay.Models.GameModel;
import com.example.kay.hoplay.Models.Rank;
import com.example.kay.hoplay.Models.Ranks;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

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

                    // set current user information
                    app.getUserInformation().setUsername(username);
                    app.getUserInformation().setNickName(nickname);
                    app.getUserInformation().setPictureURL(picUrl);

                    // load games
                    loadFavorGamesList();

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



    private void loadFavorGamesList() {
        DatabaseReference currentUserFavorGame = app.getDatabaseUsersInfo().child(app.getUserInformation().getUID() + "/" + FIREBASE_FAVOR_GAMES_PATH);

        currentUserFavorGame.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                setGamesLoaderEvent(dataSnapshot);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                setGamesLoaderEvent(dataSnapshot);
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }


    private void setGamesLoaderEvent(DataSnapshot dataSnapshot) {
        final String gameKey = dataSnapshot.getKey();
        final String gameType = dataSnapshot.getValue().toString().trim();

        app.getDatabaseGames().child(gameType + "/" + gameKey).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot gameShot) {

                String gameName=  gameShot.child("name").getValue(String.class);
                String gamePhoto= gameShot.child("photo").getValue(String.class);
                String platforms= gameShot.child("platforms").getValue(String.class);
                String gameProvider = gameShot.child("pc_game_provider").getValue(String.class);
                int maxPlayers=  gameShot.child("max_player").getValue(Integer.class);

                ArrayList<Rank> ranks = new ArrayList<>();

                for(DataSnapshot rank  : gameShot.child("ranks").getChildren())
                {
                    ranks.add(new Rank(rank.getKey(),rank.getValue(String.class)));
                }
                GameModel gameModel = new GameModel(gameKey,gameName,gamePhoto,platforms,gameType,maxPlayers,ranks,gameProvider);

                app.getGameManager().addGame(gameModel);

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
