package com.example.kay.hoplay.Cores.RequestCore;

import android.util.Log;

import com.example.kay.hoplay.CoresAbstract.RequestAbstracts.SearchRequests;
import com.example.kay.hoplay.Interfaces.FirebasePaths;
import com.example.kay.hoplay.Models.GameModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class SearchRequestCore extends SearchRequests implements FirebasePaths {


    @Override
    protected void searchForRequest(HashMap<String, String> data) {

        String gameName = data.get("name");
        String region = data.get("region");
        String gamePlat = data.get("platform");
        String playersNumber = data.get("playersNumber");


        GameModel model = app.getGameManager().getGameByName(gameName.toLowerCase());
        if(model == null) {
            Log.e("----->","NULL SearchRequestCore");
            return;
        }
        String gameId = model.getGameID();

        DatabaseReference gameRef  = app.getDatabaseRequests().child(gamePlat).child(gameId).child(region);
        Query query = gameRef.orderByChild(FIREBASE_REQUEST_TIME_STAMP_ATTR);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
