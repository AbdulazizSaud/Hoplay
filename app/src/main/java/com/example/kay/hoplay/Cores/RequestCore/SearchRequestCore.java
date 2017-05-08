package com.example.kay.hoplay.Cores.RequestCore;

import android.util.Log;

import com.example.kay.hoplay.CoresAbstract.RequestAbstracts.SearchRequests;
import com.example.kay.hoplay.Interfaces.FirebasePaths;
import com.example.kay.hoplay.Models.GameModel;
import com.example.kay.hoplay.Models.RequestModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class SearchRequestCore extends SearchRequests implements FirebasePaths {



    @Override
    protected void OnStartActivity() {
        // Load regions
        DatabaseReference regionsRef = app.getDatabaseRegions();
        regionsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                if (dataSnapshot != null) {
                    for (DataSnapshot region : dataSnapshot.getChildren()) {
                        regionList.add(region.getValue(String.class).trim());
                    }
                }

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }


    @Override
    protected void searchForRequest(final HashMap<String, String> data) {

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

        final ArrayList<RequestModel> requestModelArrayList = new ArrayList<>();
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue() !=null)
                {
                    Iterable<DataSnapshot> requestSnaps = dataSnapshot.getChildren();
                    RequestModel requestModel;

                    int i=0;
                    for(DataSnapshot request : requestSnaps)
                    {
                        requestModel = new RequestModel(
                                request.getKey(),
                                request.child("request_title").getValue(String.class),
                                request.child("players_number").getValue(Integer.class),
                                request.child("admin").getValue(String.class),
                                request.child("description").getValue(String.class),
                                request.child("region").getValue(String.class)
                        );
                        requestModelArrayList.add(requestModel);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

}
