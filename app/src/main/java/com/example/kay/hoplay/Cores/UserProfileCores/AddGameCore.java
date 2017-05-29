package com.example.kay.hoplay.Cores.UserProfileCores;

import android.util.Log;
import android.view.View;

import com.example.kay.hoplay.Interfaces.FirebasePaths;
import com.example.kay.hoplay.Models.GameModel;
import com.example.kay.hoplay.CoresAbstract.ProfileAbstracts.AddGame;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.LinkedList;


public class AddGameCore extends AddGame implements FirebasePaths {


    private LinkedList<ValueEventListener> searchForGameListeners = new LinkedList<>();


    protected void OnStartActivity() {
        loadFavorGamesList();
    }


    @Override
    protected void OnClickHolders(GameModel gameModel, View v) {



        // Create game ref for users
        DatabaseReference userFavorGameRef = app.getDatabaseUsersInfo().child(app.getUserInformation().getUID()).child(FIREBASE_FAVOR_GAMES_PATH);
        userFavorGameRef.child(gameModel.getGameID()).setValue(gameModel.getGameType());
        addedGameMessage(gameModel.getGameName());
    }




    private void loadFavorGamesList() {


        ArrayList<GameModel> favorGames = app.getGameManager().getAllGamesArrayList();
        String name,gameNameWithCapitalLetter;
        for(GameModel gameModel : favorGames) {

             name = gameModel.getGameName();
             gameNameWithCapitalLetter = name.substring(0, 1).toUpperCase() + name.substring(1);

            gameModel.setGameName(gameNameWithCapitalLetter);

            addGame(gameModel);
        }
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
        String gameProvider= dataSnapshot.child(FIREBASE_GAME_PC_GAME_PROVIDER).getValue().toString().trim();
        int maxPlayer = Integer.parseInt(maxPlayerAsString);

        GameModel gameModel = new GameModel(gameId, gameNameWithCapitalLetter, gamPic, supportedPlatformes,gametype,maxPlayer,gameProvider);
        addGame(gameModel);

    }


}
