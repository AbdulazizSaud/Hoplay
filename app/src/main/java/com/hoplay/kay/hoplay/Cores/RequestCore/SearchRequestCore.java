package com.hoplay.kay.hoplay.Cores.RequestCore;

import android.util.Log;

import com.hoplay.kay.hoplay.CoresAbstract.RequestAbstracts.SearchRequests;
import com.hoplay.kay.hoplay.Interfaces.Constants;
import com.hoplay.kay.hoplay.Interfaces.FirebasePaths;
import com.hoplay.kay.hoplay.Models.GameModel;
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

        if(region.equals("All"))
        {

            requestModelArrayList = new ArrayList<>();

            for(String reg : regionList)
            {
                if(reg.equals("All"))
                    continue;

                gameRef = app.getDatabaseRequests().child(gamePlat).child(gameId).child(reg);
                searchQuery2(currentTimeStamp);

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
            searchQuery(currentTimeStamp);
        }

    }


    private void searchQuery(long currentTimestamp) {

        long last48 = currentTimestamp - DUE_REQUEST_TIME_IN_VALUE_HOURS;

        final Query query = gameRef.orderByChild(FIREBASE_REQUEST_TIME_STAMP_ATTR).startAt(last48).endAt(currentTimestamp);
        requestModelArrayList = new ArrayList<>();

        query.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot == null)
                    return;

                Iterable<DataSnapshot> shots = dataSnapshot.getChildren();
                for (DataSnapshot shot : shots) {

                    RequestModel receivedRequestModel = shot.getValue(RequestModel.class);

                    if (playersNumber != 0)
                        if (receivedRequestModel.getPlayerNumber() != playersNumber)
                            continue;

                    if (!rank.equals("All Ranks"))
                        if (!receivedRequestModel.getRank().equals(rank))
                            continue;

                    if(!matchType.equals("All Matches"))
                    if (!matchType.equals(receivedRequestModel.getMatchType()))
                        continue;

                    receivedRequestModel.setRequestId(shot.getKey());
                    requestModelArrayList.add(receivedRequestModel);

                }
                app.setSearchRequestResult(requestModelArrayList);
                goToResultLayout();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }



    private void searchQuery2(long currentTimestamp) {

        long last48 = currentTimestamp - DUE_REQUEST_TIME_IN_VALUE_HOURS;

        final Query query = gameRef.orderByChild(FIREBASE_REQUEST_TIME_STAMP_ATTR).startAt(last48).endAt(currentTimestamp);

        query.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot == null)
                    return;


                Iterable<DataSnapshot> shots = dataSnapshot.getChildren();
                for (DataSnapshot shot : shots) {

                    RequestModel receivedRequestModel = shot.getValue(RequestModel.class);

                    if (playersNumber != 0)
                        if (receivedRequestModel.getPlayerNumber() != playersNumber)
                            continue;

                    if (!rank.equals("All Ranks"))
                        if (!receivedRequestModel.getRank().equals(rank))
                            continue;

                    if(!matchType.equals("All Matches"))
                        if (!matchType.equals(receivedRequestModel.getMatchType()))
                            continue;

                    receivedRequestModel.setRequestId(shot.getKey());
                    requestModelArrayList.add(receivedRequestModel);

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }



}
