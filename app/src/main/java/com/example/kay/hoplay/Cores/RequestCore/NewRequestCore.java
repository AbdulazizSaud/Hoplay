package com.example.kay.hoplay.Cores.RequestCore;

import android.util.Log;

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

    protected void loadStandards()
    {



    } // End of load method

    @Override
    protected void requestInput(String platform, String gameName, String matchType, String region, String numberOfPlayers, String rank, String description) {


        GameModel gameModel = app.getGameManager().getGameByName(gameName);


        // users_info_ -> user id  -> _requests_refs_
        DatabaseReference userRequestRef = app.getDatabaseUsersInfo().child(app.getUserInformation().getUID()).child(FIREBASE_USER_REQUESTS_REF);
        // _requests_ -> game name -> platform
        DatabaseReference requestsRef = app.getDatabaseRequests().child(gameModel.getGameID()).child(platform.toUpperCase());

        String requestKey = requestsRef.push().getKey();
        String requestAdmin = app.getUserInformation().getUID();

        // _requests_ -> game name  -> platform -> request id
        DatabaseReference requestRef = requestsRef.child(requestKey);



        // set req ref in the user tree
        userRequestRef.child(requestKey).setValue(requestKey);


        // set the request info under the requests tree

        HashMap<String,Object> data = new HashMap<>();

        data.put("admin",app.getUserInformation().getUID());
        data.put("request_title",gameName);
        data.put("description",description);
        data.put("region",region);
        data.put("time_stamp",ServerValue.TIMESTAMP);
        data.put("rank",rank);
        data.put("players_number",numberOfPlayers);

        requestRef.setValue(data);

    }


}
