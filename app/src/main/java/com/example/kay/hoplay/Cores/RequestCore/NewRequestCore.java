package com.example.kay.hoplay.Cores.RequestCore;

import android.util.Log;
import android.util.LongSparseArray;

import com.example.kay.hoplay.CoresAbstract.RequestAbstracts.NewRequest;
import com.example.kay.hoplay.Interfaces.FirebasePaths;
import com.example.kay.hoplay.Models.GameModel;
import com.example.kay.hoplay.Models.RequestModel;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class NewRequestCore extends NewRequest implements FirebasePaths{


    protected void OnStartActivity() {

    }

    @Override
    protected void loadstandards()
    {

        // Load  user games
        ArrayList<GameModel> games = app.getGameManager().getAllGames();
        for (GameModel gameModel : games)
        {
            gamesList.add(gameModel.getGameName());
        }


        // Load regions
        DatabaseReference regionsRef = app.getDatabaseRegions();
        regionsRef.addValueEventListener(new ValueEventListener() {
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




    } // End of load method




    @Override
    protected void requestInput(String platform, String game, String matchType, String region, String numberOfPlayers, String rank , String description) {


        String gameId = "";

        ArrayList<GameModel> userGames = app.getGameManager().getAllGames();
        for (GameModel gameModel : userGames)
        {
            if (gameModel.getGameName().equalsIgnoreCase(game))
            {
                gameId=gameModel.getGameID();
            }
        }

        // users_info_ -> user id  -> _requests_refs_
        DatabaseReference userRequestRef = app.getDatabaseUsersInfo().child(app.getUserInformation().getUID()).child(FIREBASE_USER_REQUESTS_REF);
        // _requests_ ->  platform -> gameId -> region
        DatabaseReference requestsRef = app.getDatabaseRequests().child(platform.toUpperCase()).child(gameId).child(region);

        String requestKey = requestsRef.push().getKey();
        String requestAdmin = app.getUserInformation().getUID();

        // _requests_ ->  platform -> gameId -> region - > request ID
        DatabaseReference request = requestsRef.child(requestKey);


        Log.i("here" , request.toString());

        // set req ref in the user tree
        userRequestRef.child(requestKey).setValue(requestKey);

        // This request model should be added to the database as a hashmap
        RequestModel requestModel = new RequestModel(requestKey,game,requestAdmin,description,region);

        // set the request info under the requests tree


        request.child("admin").setValue(app.getUserInformation().getUID());
        request.child("request_title").setValue(game);
        request.child("description").setValue(description);
        request.child("region").setValue(region);
        request.child("time_stamp").setValue(ServerValue.TIMESTAMP);



    }


}
