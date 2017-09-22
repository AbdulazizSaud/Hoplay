package com.hoplay.kay.hoplay.Cores.RequestCore;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.Query;
import com.hoplay.kay.hoplay.Cores.ChatCore.ChatCore;
import com.hoplay.kay.hoplay.CoresAbstract.RequestAbstracts.SearchResults;
import com.hoplay.kay.hoplay.Interfaces.Constants;
import com.hoplay.kay.hoplay.Interfaces.FirebasePaths;
import com.hoplay.kay.hoplay.Models.GameModel;
import com.hoplay.kay.hoplay.Models.RequestModel;
import com.hoplay.kay.hoplay.R;
import com.hoplay.kay.hoplay.Services.CallbackHandlerCondition;
import com.hoplay.kay.hoplay.Services.HandlerCondition;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import static com.hoplay.kay.hoplay.Interfaces.FirebasePaths.FIREBASE_USER_PC_GAME_PROVIDER;
import static com.hoplay.kay.hoplay.Interfaces.FirebasePaths.FIREBASE_USER_PS_GAME_PROVIDER;
import static com.hoplay.kay.hoplay.Interfaces.FirebasePaths.FIREBASE_USER_XBOX_GAME_PROVIDER;

public class SearchResultsCore extends SearchResults implements FirebasePaths, Constants {

    private String gameName;
    private String region;
    private String gamePlat;
    private String rank;
    private long playersNumber;
    private String matchType;

    private RequestModel requestModel = new RequestModel();

    private DatabaseReference gameRef;

    @Override
    protected void OnStartActivity() {


        Intent intent = getIntent();


        Bundle bundle = intent.getExtras();
        long currentTimeStamp = intent.getLongExtra("currentTimestamp", 0);

        if (bundle != null) {
            requestModel = bundle.getParcelable("requestModel");
        }


        gameName = requestModel.getRequestTitle();
        region = requestModel.getRegion();
        gamePlat = requestModel.getPlatform();
        rank = requestModel.getRank();
        matchType = requestModel.getMatchType();
        playersNumber = requestModel.getPlayerNumber();


        GameModel model = app.getGameManager().getGameByName(gameName.toLowerCase());

        if (model == null)
            return;

        String gameId = model.getGameID();

        if (region.equals("All")) {

            ArrayList<String> requestRegionList = app.getRegionList();

            for (String reg : requestRegionList) {
                if (reg.equals("All"))
                    continue;

                gameRef = app.getDatabaseRequests().child(gamePlat).child(gameId).child(reg);

            }

        } else {

            gameRef = app.getDatabaseRequests().child(gamePlat).child(gameId).child(region);
            searchQuery(currentTimeStamp);
            //

        }


    }

    @Override
    protected void OnClickHolders(RequestModel model, View v) {
        Intent i = new Intent(v.getContext(), RequestLobbyCore.class);


        // here we will send the data;
        Bundle bundle = new Bundle();
        bundle.putParcelable("requestModel", model);
        i.putExtras(bundle);

        v.getContext().startActivity(i);

    }

    private ValueEventListener getGameInfo(final RequestModel request) {

        return new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String picture = dataSnapshot.child("photo").getValue(String.class);
                request.setRequestPicture(picture);
                addResult(request);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
    }


    @Override
    protected void saveGameProviderAccount(String gameProvider, String userGameProviderAcc, String platform) {

        // users_info_ -> user id
        DatabaseReference userRef = app.getDatabaseUsersInfo().child(app.getUserInformation().getUID());
        if (platform.equalsIgnoreCase("PS"))
            // users_info_ -> user id - > PSN_account
            userRef.child(FIREBASE_USER_PS_GAME_PROVIDER).setValue(userGameProviderAcc);
        else if (platform.equalsIgnoreCase("XBOX"))
            // users_info_ -> user id - > XBOX_life_account
            userRef.child(FIREBASE_USER_XBOX_GAME_PROVIDER).setValue(userGameProviderAcc);
        else if (platform.equalsIgnoreCase("PC")) {
            // users_info_ -> user id - > PC_game_provider_accopunt STWAM , BATTLENET .. etc
            userRef.child(FIREBASE_USER_PC_GAME_PROVIDER).child(gameProvider).setValue(userGameProviderAcc);
        }

    }


    private void searchQuery(long currentTimestamp) {

        long last48 = currentTimestamp - DUE_REQUEST_TIME_IN_VALUE_HOURS;

        final Query query = gameRef.orderByChild(FIREBASE_REQUEST_TIME_STAMP_ATTR).startAt(last48).endAt(currentTimestamp);


        query.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Log.i("-->",dataSnapshot.toString());

                queryAddingProces(dataSnapshot);


            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                Log.i("-->",dataSnapshot.toString());

                queryAddingProces(dataSnapshot);

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

    private void queryAddingProces(final DataSnapshot requestDataSnapshot) {
        if (requestDataSnapshot.getValue() == null || requestDataSnapshot.child("players").getValue() == null)
            return;


        final RequestModel receivedRequestModel = requestDataSnapshot.getValue(RequestModel.class);



        receivedRequestModel.setRequestId(requestDataSnapshot.getKey());

        if (playersNumber != 0)
            if (receivedRequestModel.getPlayerNumber() != playersNumber)
                return;

        if (!rank.equals("All Ranks"))
            if (!receivedRequestModel.getRank().equals(rank))
                return;

        if (!matchType.equals("All Matches"))
            if (!matchType.equals(receivedRequestModel.getMatchType()))
                return;


        GameModel gameModel = app.getGameManager().getGameById(receivedRequestModel.getGameId());


        if(gameModel == null)
        {

            // here we have issue
            String path = receivedRequestModel.getMatchType()+"/"+receivedRequestModel.getGameId();
            app.getDatabaseGames().child(path)
                    .addListenerForSingleValueEvent(getGameInfo(receivedRequestModel));
        } else {

            receivedRequestModel.setRequestPicture(gameModel.getGamePhotoUrl());
            addResult(receivedRequestModel);
        }
    }

    private void searchQueryForAll(long currentTimestamp) {

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

                    if (!matchType.equals("All Matches"))
                        if (!matchType.equals(receivedRequestModel.getMatchType()))
                            continue;

                    receivedRequestModel.setRequestId(shot.getKey());

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }


}
