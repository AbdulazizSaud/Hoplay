package com.example.kay.hoplay.Cores.RequestCore;

import android.util.Log;

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

                request.setRequestPicture(gameModel.getGamePhotoUrl());
                addResult(request);
            }


        }
    }



    private ValueEventListener getGameInfo(final RequestModel request)
    {

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


}
