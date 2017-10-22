package com.hoplay.kay.hoplay.Cores.RequestCore;

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

    private String gameName;
    private String region;
    private String gamePlat;
    private String matchType;
    private int playersNumber;
    private String rank;
    DatabaseReference gameRef;


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
        String gameProvider = dataSnapshot.child(FIREBASE_GAME_PC_GAME_PROVIDER).getValue(String.class);

        GameModel gameModel = new GameModel(gameId, gameNameWithCapitalLetter, gamPic, supportedPlatformes, gametype, maxPlayer, gameProvider);

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
                    currentTimeStamp = app.getTimeStamp().getTimestampLong();

                return app.getTimeStamp().getTimestampLong() != -1;
            }
        };

        new HandlerCondition(callback, 0);


    }


    @Override
    protected void searchForRequest(RequestModel requestModel) {


        gameName = requestModel.getRequestTitle();
        region = requestModel.getRegion();
        gamePlat = requestModel.getPlatform();
        matchType = requestModel.getMatchType();
        playersNumber = requestModel.getPlayerNumber();
        rank = requestModel.getRank();


        GameModel model = app.getGameManager().getGameByName(gameName.toLowerCase());

        if (model == null)
            return;

        String gameId = model.getGameID();

        if (region.equals("All")) {

            requestModelArrayList = new ArrayList<>();

            for (String reg : regionList) {
                if (reg.equals("All"))
                    continue;

                gameRef = app.getDatabaseRequests().child(gamePlat).child(gameId).child(reg);
                excuteQuery(currentTimeStamp,true);

            }


            CallbackHandlerCondition callback = new CallbackHandlerCondition() {
                @Override
                public boolean callBack() {
                    app.setSearchRequestResult(requestModelArrayList);
                    goToResultLayout();

                    return true;
                }
            };

            new HandlerCondition(callback, 1000);


        } else {

            gameRef = app.getDatabaseRequests().child(gamePlat).child(gameId).child(region);
            excuteQuery(currentTimeStamp,false);
        }

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

                for (DataSnapshot rank : dataSnapshot.child("ranks").getChildren()) {
                    ranks.add(new Rank(rank.getValue().toString(), rank.getValue(String.class)));
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



    private void requestValidator(DataSnapshot shot) {
        RequestModel receivedRequestModel = shot.getValue(RequestModel.class);

        if (playersNumber != 0)
            if (receivedRequestModel.getPlayerNumber() != playersNumber)
                return;

        if (!rank.equals("All Ranks"))
            if (!receivedRequestModel.getRank().equals(rank))
                return;

        if (!matchType.equals("All Matches"))
            if (!matchType.equals(receivedRequestModel.getMatchType()))
                return;

        receivedRequestModel.setRequestId(shot.getKey());

        if (receivedRequestModel.getPlayers() != null)
            requestModelArrayList.add(receivedRequestModel);
    }



    private void excuteQuery(long currentTimestamp, final boolean loopQuery)
    {
        long last48 = currentTimestamp - DUE_REQUEST_TIME_IN_VALUE_HOURS;

        final Query query = gameRef.orderByChild(FIREBASE_REQUEST_TIME_STAMP_ATTR).startAt(last48).endAt(currentTimestamp);
        requestModelArrayList = new ArrayList<>();
        gameRef.keepSynced(true);
        query.keepSynced(true);

        query.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> shots = dataSnapshot.getChildren();
                for (DataSnapshot shot : shots) {

                    requestValidator(shot);

                }

                if(!loopQuery) {
                    query.removeEventListener(this);
                    app.setSearchRequestResult(requestModelArrayList);
                    goToResultLayout();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

    }


}
