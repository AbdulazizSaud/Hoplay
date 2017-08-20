package com.example.kay.hoplay.Cores.RequestCore;

import android.content.Intent;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.kay.hoplay.Cores.ChatCore.ChatCore;
import com.example.kay.hoplay.CoresAbstract.RequestAbstracts.SearchResults;
import com.example.kay.hoplay.Models.GameModel;
import com.example.kay.hoplay.Models.RequestModel;
import com.example.kay.hoplay.Services.CallbackHandlerCondition;
import com.example.kay.hoplay.Services.HandlerCondition;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import static com.example.kay.hoplay.Interfaces.FirebasePaths.FIREBASE_USER_PC_GAME_PROVIDER;
import static com.example.kay.hoplay.Interfaces.FirebasePaths.FIREBASE_USER_PS_GAME_PROVIDER;
import static com.example.kay.hoplay.Interfaces.FirebasePaths.FIREBASE_USER_XBOX_GAME_PROVIDER;

public class SearchResultsCore extends SearchResults {


    @Override
    protected void OnStartActivity() {
        ArrayList<RequestModel> requestsModel = app.getSearchRequestResult();

        for(RequestModel request : requestsModel)
        {

            GameModel gameModel = app.getGameManager().getGameById(request.getGameId());

            if(gameModel == null)
            {

                // here we have issue
                String path = request.getMatchType()+"/"+request.getGameId();
                app.getDatabaseGames().child(path)
                        .addListenerForSingleValueEvent(getGameInfo(request));
            } else {

                if(request.getPlayers() == null)
                    continue;

                request.setRequestPicture(gameModel.getGamePhotoUrl());
                addResult(request);
            }


        }
    }

    @Override
    protected void OnClickHolders(RequestModel model, View v) {
        Intent i = new Intent(v.getContext(), RequestLobbyCore.class);


            // here we will send the data;
            i.putExtra("requestId",model.getRequestId());
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
    protected void saveGameProviderAccount(String gameProvider ,String userGameProviderAcc , String platform) {

        // users_info_ -> user id
        DatabaseReference userRef = app.getDatabaseUsersInfo().child(app.getUserInformation().getUID());
        if (platform.equalsIgnoreCase("PS"))
            // users_info_ -> user id - > PSN_account
            userRef.child(FIREBASE_USER_PS_GAME_PROVIDER).setValue(userGameProviderAcc);
        else if (platform.equalsIgnoreCase("XBOX"))
            // users_info_ -> user id - > XBOX_life_account
            userRef.child(FIREBASE_USER_XBOX_GAME_PROVIDER).setValue(userGameProviderAcc);
        else if (platform.equalsIgnoreCase("PC"))
        {
            // users_info_ -> user id - > PC_game_provider_accopunt STWAM , BATTLENET .. etc
            userRef.child(FIREBASE_USER_PC_GAME_PROVIDER).child(gameProvider).setValue(userGameProviderAcc);
        }

    }



}
