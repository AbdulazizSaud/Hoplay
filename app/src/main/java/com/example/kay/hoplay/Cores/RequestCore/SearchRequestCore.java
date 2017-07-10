package com.example.kay.hoplay.Cores.RequestCore;

import android.util.Log;

import com.example.kay.hoplay.CoresAbstract.RequestAbstracts.SearchRequests;
import com.example.kay.hoplay.Interfaces.Constants;
import com.example.kay.hoplay.Interfaces.FirebasePaths;
import com.example.kay.hoplay.Models.GameModel;
import com.example.kay.hoplay.Models.RequestModel;
import com.example.kay.hoplay.Services.CallbackHandlerCondition;
import com.example.kay.hoplay.Services.HandlerCondition;
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

    @Override
    protected void OnStartActivity() {
        // Load regions
        DatabaseReference regionsRef = app.getDatabaseRegions();

        regionsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                if (dataSnapshot != null) {
                    for (DataSnapshot region : dataSnapshot.getChildren()) {
                        regionAdapter.add(region.getValue(String.class).trim());
                    }
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

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
        gameRef = app.getDatabaseRequests().child(gamePlat).child(gameId).child(region);


        //------------------------
        app.getTimeStamp().setTimestampLong();

        CallbackHandlerCondition callback = new CallbackHandlerCondition() {
            @Override
            public boolean callBack() {
                if (app.getTimeStamp().getTimestampLong() != -1)
                    searchQuery(app.getTimeStamp().getTimestampLong());

                return app.getTimeStamp().getTimestampLong() != -1;
            }
        };

        new HandlerCondition(callback, 0);
        //------------------------


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


}
