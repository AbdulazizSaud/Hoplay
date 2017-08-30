package com.hoplay.kay.hoplay.Cores.UserProfileCores;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.hoplay.kay.hoplay.Interfaces.FirebasePaths;
import com.hoplay.kay.hoplay.Models.GameModel;
import com.hoplay.kay.hoplay.CoresAbstract.ProfileAbstracts.AddGame;
import com.hoplay.kay.hoplay.R;
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

//        searchBar.setVisibility(View.GONE);
//        searchBar= null;


        loadFavorGamesList();
    }




    @Override
    protected void deleteGame(String gameKey, String gameName) {

        // users_info_ -> user id  -> _games_ -> _favor_games_
        DatabaseReference userGameRef = app.getDatabaseUsersInfo().child(app.getUserInformation().getUID()).child(FIREBASE_GAMES_REFERENCES).child(FIREBASE_FAVOR_GAME_ATTR);

        // access _favor_games_ -> game_key and delete the game from the database
        userGameRef.child(gameKey).removeValue();

        // now delete the game from the GameManager
        app.getGameManager().deleteGame(gameKey,gameName);
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


        String gamPic = dataSnapshot.child("photo").getValue(String.class);
        int maxPlayer = dataSnapshot.child("max_player").getValue(Integer.class);
        String supportedPlatformes = dataSnapshot.child(FIREBASE_GAME_PLATFORMS_ATTR).getValue(String.class);
        String gameProvider= dataSnapshot.child(FIREBASE_GAME_PC_GAME_PROVIDER).getValue(String.class);

        GameModel gameModel = new GameModel(gameId, gameNameWithCapitalLetter, gamPic, supportedPlatformes,gametype,maxPlayer,gameProvider);
        addGame(gameModel);

    }


}
