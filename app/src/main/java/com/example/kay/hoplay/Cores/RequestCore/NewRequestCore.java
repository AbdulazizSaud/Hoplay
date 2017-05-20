package com.example.kay.hoplay.Cores.RequestCore;

import android.util.Log;

import com.example.kay.hoplay.App.App;
import com.example.kay.hoplay.util.TimeStamp;
import com.example.kay.hoplay.CoresAbstract.RequestAbstracts.NewRequest;
import com.example.kay.hoplay.Interfaces.FirebasePaths;
import com.example.kay.hoplay.Models.GameModel;
import com.example.kay.hoplay.Models.RequestModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class NewRequestCore extends NewRequest implements FirebasePaths{



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
    protected void saveGameProviderAccount(String gameProvider ,String userGameProviderAcc , String platform) {

        // users_info_ -> user id
        DatabaseReference userRef = app.getDatabaseUsersInfo().child(app.getUserInformation().getUID());
        if (platform.equalsIgnoreCase("PS"))
            // users_info_ -> user id - > PSN_account
            userRef.child(FIREBASE_PS_GAME_PROVIDER).setValue(userGameProviderAcc);
        else if (platform.equalsIgnoreCase("XBOX"))
            // users_info_ -> user id - > XBOX_life_account
            userRef.child(FIREBASE_XBOX_GAME_PROVIDER).setValue(userGameProviderAcc);
        else if (platform.equalsIgnoreCase("PC"))
        {
            // users_info_ -> user id - > PC_game_provider_accopunt STWAM , BATTLENET .. etc
            userRef.child(FIREBASE_PC_GAME_PROVIDER).child(gameProvider).setValue(userGameProviderAcc);
        }

    }

    protected void loadStandards()
    {



    } // End of load method

    @Override
    protected void request(String platform, String gameName, String matchType, String region, String numberOfPlayers, String rank, String description) {


        GameModel gameModel = app.getGameManager().getGameByName(gameName);

        // users_info_ -> user id  -> _requests_refs_
        DatabaseReference userRequestRef = app.getDatabaseUsersInfo().child(app.getUserInformation().getUID()).child(FIREBASE_USER_REQUESTS_REF);
        // _requests_ ->  platform -> GameID -> Region
        DatabaseReference requestsRef = app.getDatabaseRequests().child(platform.toUpperCase()).child(gameModel.getGameID()).child(region);


        String requestKey = requestsRef.push().getKey();
        String requestAdmin = app.getUserInformation().getUID();
        // _requests_ -> platform -> GameID -> Region -> request id
        DatabaseReference requestRef = requestsRef.child(requestKey);



        // set req ref in the user tree
        userRequestRef.child(requestKey).setValue(requestKey);


        // set the request info under the requests tree

        HashMap<String,Object> data = new HashMap<>();
       // TimeStamp timeStamp=new TimeStamp();

        RequestModel requestModel=new RequestModel(
                platform,
                gameName,
                app.getUserInformation().getUID(),
                description,
                region,
                Integer.parseInt(numberOfPlayers),
                matchType,
                rank);

        requestModel.setAdminName(app.getUserInformation().getUsername());
        requestModel.setGameId(gameModel.getGameID());
        HashMap hashMap =new HashMap();
        hashMap.put("timeStamp",ServerValue.TIMESTAMP);

        requestRef.setValue(requestModel);
        requestRef.updateChildren(hashMap);

    }


}
