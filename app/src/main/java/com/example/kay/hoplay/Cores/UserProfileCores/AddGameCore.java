package com.example.kay.hoplay.Cores.UserProfileCores;

import android.view.View;

import com.example.kay.hoplay.Interfaces.FirebasePaths;
import com.example.kay.hoplay.Models.GameDetails;
import com.example.kay.hoplay.CoresAbstract.ProfileAbstracts.AddGame;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.LinkedList;


public class AddGameCore extends AddGame implements FirebasePaths {


    private LinkedList<ValueEventListener> searchForGameListeners = new LinkedList<>();


    protected void OnStartActivity() {
        loadFavorGamesList();
    }


    @Override
    protected void OnClickHolders(GameDetails gameDetails, View v) {
        DatabaseReference userFavorGameRef = app.getDatabaseUsersInfo().child(app.getUserInformation().getUID()).child(FIREBASE_FAVOR_GAMES_PATH);
        userFavorGameRef.child(gameDetails.getGameID()).setValue(gameDetails.getGameType());
        addedGameMessage(gameDetails.getGameName());
    }


    private void loadFavorGamesList() {
        DatabaseReference myAuthGameRef = app.getDatabaseUsersInfo().child(app.getUserInformation().getUID() + "/" + FIREBASE_FAVOR_GAMES_PATH);

        myAuthGameRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> myFavorSnaps = dataSnapshot.getChildren();

                for (DataSnapshot gameSnap : myFavorSnaps) {
                    final String gameType = gameSnap.getValue().toString().trim();
                    final String gameKey = gameSnap.getKey();
                    app.getDatabaseGames().child(gameType + "/" + gameKey).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot gameShot) {
                            addGame(gameKey, gameType, gameShot);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    protected void searchForGame(String value) {

        // Just to push
        DatabaseReference gamesRef = app.getDatabaseGames();

        Query query = gamesRef.child("_competitive_").orderByChild("name").startAt(value).endAt(value + "\uf8ff").limitToFirst(10);
        getData(query, "_competitive_");
        query = gamesRef.child("_coop_").orderByChild("name").startAt(value).endAt(value + "\uf8ff").limitToFirst(10);
        getData(query, "_coop_");
    }

    private void getData(Query query, String gameType) {
        query.addListenerForSingleValueEvent(createSearchForGameListener(gameType));

    }

    public ValueEventListener createSearchForGameListener(final String gameType) {


        ValueEventListener listener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot.getValue() != null) {

                    Iterable<DataSnapshot> iterable = dataSnapshot.getChildren();

                    for (DataSnapshot shot : iterable) {
                        String gameName = shot.child("name").getValue().toString().trim();

                        if (!checkIsInList(gameName))
                            addGame(shot.getKey(), gameType, shot);
                    }
                    hideLoadingAnimation();

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        };
        searchForGameListeners.add(listener);
        return listener;
    }


    private void addGame(String key, String gametype, DataSnapshot dataSnapshot) {


        String gameId = key;
        String gameName = dataSnapshot.child("name").getValue().toString().trim();

        // Capitlizing the first letter of a game
        String gameNameWithCapitalLetter = gameName.substring(0, 1).toUpperCase() + gameName.substring(1);


        String gamPic = dataSnapshot.child("photo").getValue().toString().trim();
        String maxPlayerAsString = dataSnapshot.child("max_player").getValue().toString().trim();
        String supportedPlatformes = dataSnapshot.child(FIREBASE_GAME_PLATFORMS_ATTR).getValue().toString().trim();
        int maxPlayer = Integer.parseInt(maxPlayerAsString);
        addGame(gameId, gameNameWithCapitalLetter, gametype, maxPlayer, gamPic, supportedPlatformes);

    }


}
