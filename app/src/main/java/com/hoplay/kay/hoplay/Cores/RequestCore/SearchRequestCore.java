package com.hoplay.kay.hoplay.Cores.RequestCore;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.hoplay.kay.hoplay.CoresAbstract.RequestAbstracts.SearchRequests;
import com.hoplay.kay.hoplay.Interfaces.Constants;
import com.hoplay.kay.hoplay.Interfaces.FirebasePaths;
import com.hoplay.kay.hoplay.Models.GameModel;
import com.hoplay.kay.hoplay.Models.Rank;
import com.hoplay.kay.hoplay.Models.Ranks;
import com.hoplay.kay.hoplay.Models.RequestModel;
import com.hoplay.kay.hoplay.Services.CallbackHandlerCondition;
import com.hoplay.kay.hoplay.Services.HandlerCondition;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class SearchRequestCore extends SearchRequests implements FirebasePaths, Constants {

    private ArrayList<RequestModel> requestModelArrayList = new ArrayList<>();

    private long currentTimeStamp;


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
                        getGameInfo(shot.getKey(), gameType, shot);
                        isDone = true;
                        whichDialog = true;

                    }



                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        };

        return listener;
    }


    private void getGameInfo(String key, String gametype, DataSnapshot dataSnapshot) {


        String gameId = key;
        String gameName = dataSnapshot.child("name").getValue().toString().trim();

        // Capitlizing the first letter of a game
        String gameNameWithCapitalLetter = gameName.substring(0, 1).toUpperCase() + gameName.substring(1);


        String gamPic = dataSnapshot.child("photo").getValue(String.class);
        int maxPlayer = dataSnapshot.child("max_player").getValue(Integer.class);
        String supportedPlatformes = dataSnapshot.child(FIREBASE_GAME_PLATFORMS_ATTR).getValue(String.class);
        String gameProvider= dataSnapshot.child(FIREBASE_GAME_PC_GAME_PROVIDER).getValue(String.class);

        GameModel gameModel = new GameModel(gameId, gameNameWithCapitalLetter, gamPic, supportedPlatformes,gametype,maxPlayer,gameProvider);

        notAddedGame = gameModel;


    }



    @Override
    protected void OnStartActivity() {
        // Load regions
        DatabaseReference regionsRef = app.getDatabaseRegions();

        regionsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                if (dataSnapshot != null) {
                    for (DataSnapshot region : dataSnapshot.getChildren()) {
                        String reg = region.getValue(String.class);

                        regionAdapter.add(reg);
                        regionList.add(reg);
                    }
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        app.getTimeStamp().setTimestampLong();

        CallbackHandlerCondition callback = new CallbackHandlerCondition() {
            @Override
            public boolean callBack() {
                if (app.getTimeStamp().getTimestampLong() != -1)
                    currentTimeStamp =  app.getTimeStamp().getTimestampLong();

                return app.getTimeStamp().getTimestampLong() != -1;
            }
        };

        new HandlerCondition(callback, 0);


    }


    @Override
    protected void searchForRequest(RequestModel requestModel) {

        Intent i = new Intent(getActivity().getApplicationContext(),SearchResultsCore.class);

        Bundle bundle = new Bundle();
        bundle.putParcelable("requestModel",requestModel);

        app.setRegionList(regionList);
        i.putExtras(bundle);
        i.putExtra("currentTimestamp",currentTimeStamp);
        startActivity(i);

    }

    @Override
    protected void addGame(GameModel gameModel) {
        // Create game ref for users
        DatabaseReference userFavorGameRef = app.getDatabaseUsersInfo().child(app.getUserInformation().getUID()).child(FIREBASE_FAVOR_GAMES_PATH);
        userFavorGameRef.child(gameModel.getGameID()).setValue(gameModel.getGameType());

        // add the game to the game manager
        app.getGameManager().addGame(gameModel);
    }

    @Override
    protected void loadRanksForNotAddedGame(final GameModel gameModel) {
        app.getDatabaseGames().child(gameModel.getGameType() + "/" + gameModel.getGameID()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<Rank> ranks = new ArrayList<>();

                for(DataSnapshot rank  : dataSnapshot.child("ranks").getChildren())
                {
                    ranks.add(new Rank(rank.getValue().toString(),rank.getValue(String.class)));
                }

                Ranks ranksAsClass = new Ranks();
                ranksAsClass.setRanksList(ranks);
                app.getGameManager().getGameByName(gameModel.getGameName()).setGameRanks(ranksAsClass);
                isDoneForLoadingRanks = true;

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }








}
