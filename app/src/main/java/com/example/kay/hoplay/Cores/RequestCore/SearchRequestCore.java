package com.example.kay.hoplay.Cores.RequestCore;

import android.util.Log;

import com.example.kay.hoplay.CoresAbstract.RequestAbstracts.SearchRequests;
import com.example.kay.hoplay.Interfaces.FirebasePaths;
import com.example.kay.hoplay.Models.GameModel;
import com.example.kay.hoplay.Models.RequestModel;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

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
    protected void searchForRequest(RequestModel requestModel) {

        String gameName =requestModel.getRequestTitle();
        String region = requestModel.getRegion();
        String gamePlat = requestModel.getPlatform();
        final String matchType = requestModel.getMatchType();
        final int playersNumber = requestModel.getPlayerNumber();
        final String rank = requestModel.getRank();
        final Map requrstTime=requestModel.getTimeStamp();
        final Long longTIMEstamp=(Long)requrstTime.get(".sv");



        GameModel model = app.getGameManager().getGameByName(gameName.toLowerCase());
        if(model == null) {
            Log.e("----->","NULL SearchRequestCore");
            return;
        }
        String gameId = model.getGameID();

        DatabaseReference gameRef  = app.getDatabaseRequests().child(gamePlat).child(gameId).child(region);
        final Query query = gameRef.orderByChild(FIREBASE_REQUEST_TIME_STAMP_ATTR);
        final ArrayList<RequestModel> requestModelArrayList = new ArrayList<>();

        final ChildEventListener childEventListenerSearchResults =new ChildEventListener() {
            Boolean flag=true;
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                RequestModel requestModel = dataSnapshot.getValue(RequestModel.class);
              if(ValidtimeRequest(longTIMEstamp))
                   return;

                if(requestModel.getPlayerNumber() != playersNumber)
                    flag=false;
                else if(!matchType.equals(requestModel.getMatchType()))
                    flag=false;
                else if(!rank.equals(requestModel.getRank()))
                    flag=false;

                if(flag) {
                    requestModelArrayList.add(requestModel);
                    Log.e("FOUND", requestModel.getAdmin());
                }







                }



            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

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
        };
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                query.addChildEventListener(childEventListenerSearchResults);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


//        query.addChildEventListener(new ChildEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                if(dataSnapshot.getValue() !=null)
//                {
//                    Iterable<DataSnapshot> requestSnaps = dataSnapshot.getChildren();
//                    RequestModel requestModel;
//
//                    int i=0;
//                    for(DataSnapshot request : requestSnaps)
//                    {
//                        requestModel = new RequestModel(
//                                request.getKey(),
//                                request.child("request_title").getValue(String.class),
//                                request.child("players_number").getValue(Integer.class),
//                                request.child("admin").getValue(String.class),
//                                request.child("description").getValue(String.class),
//                                request.child("region").getValue(String.class)
//                        );
//                        requestModelArrayList.add(requestModel);
//                    }
//                }
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });
    }

        protected boolean ValidtimeRequest(Long giventime){
            long curenttime= Long.parseLong(ServerValue.TIMESTAMP.get(".sv"));
            if(curenttime-giventime>172800)
                return false;

            return true;
        }
}
