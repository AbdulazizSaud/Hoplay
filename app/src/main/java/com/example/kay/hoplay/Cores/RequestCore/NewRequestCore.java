package com.example.kay.hoplay.Cores.RequestCore;

import android.widget.Toast;

import com.example.kay.hoplay.Cores.ChatCore.CreateChat;
import com.example.kay.hoplay.Models.PlayerModel;
import com.example.kay.hoplay.CoresAbstract.RequestAbstracts.NewRequest;
import com.example.kay.hoplay.Interfaces.FirebasePaths;
import com.example.kay.hoplay.Models.GameModel;
import com.example.kay.hoplay.Models.RequestModel;
import com.example.kay.hoplay.Models.RequestModelRefrance;
import com.example.kay.hoplay.R;
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

    @Override
    protected void addSaveRequestToFirebase() {

        DatabaseReference ref = app.getDatabaseUsersInfo().child(app.getUserInformation().getUID()).child(FIREBASE_SAVED_REQS_PATH);
        ref.getParent().child("count").setValue(app.getSavedRequests().size(), new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                if(databaseError != null)
                {
                     // <<
                }else {
                    //
                    String msg = String.format(getResources().getString(R.string.new_request_finish_save_request_message), "");
                    Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
                    finish();
                }
            }
        });

        ref.setValue(app.getSavedRequests());
    }


    @Override
    protected void addRequestToFirebase(String platform, String gameName, String matchType, String region, String numberOfPlayers, String rank, String description) {


        GameModel gameModel = app.getGameManager().getGameByName(gameName);

        // users_info_ -> user id  -> _requests_refs_
        DatabaseReference userRequestRef = app.getDatabaseUsersInfo().child(app.getUserInformation().getUID()).child(FIREBASE_USER_REQUESTS_ATTR);
        // _requests_ ->  platform -> GameID -> Region
        DatabaseReference requestsRef = app.getDatabaseRequests().child(platform.toUpperCase()).child(gameModel.getGameID()).child(region);


        String requestKey = requestsRef.push().getKey();
        String requestAdmin = app.getUserInformation().getUID();
        // _requests_ -> platform -> GameID -> Region -> addRequestToFirebase id
        DatabaseReference requestRef = requestsRef.child(requestKey);


        // set the addRequestToFirebase info under the requests tree

        HashMap<String,Object> data = new HashMap<>();
       // TimeStamp timeStamp=new TimeStamp();

        int numberPlayers = numberOfPlayers.equals("All Numbers") ? gameModel.getMaxPlayers():Integer.parseInt(numberOfPlayers);
        RequestModel requestModel=new RequestModel(
                platform,
                gameName,
                requestAdmin,
                description,
                region,
                numberPlayers,
                matchType,
                rank);

        ArrayList<PlayerModel> playerModels = new ArrayList<PlayerModel>();
        playerModels.add(new PlayerModel(
                app.getUserInformation().getUID(),
                app.getUserInformation().getUsername()
        ));

        requestModel.setPlayers(playerModels);
        requestModel.setAdminName(app.getUserInformation().getUsername());
        requestModel.setGameId(gameModel.getGameID());
        requestModel.setRequestId(requestKey);

        HashMap hashMap =new HashMap();
        hashMap.put("timeStamp",ServerValue.TIMESTAMP);


        RequestModelRefrance requestModelRefrance = new RequestModelRefrance(requestKey,gameModel.getGameID(),platform,region);
        // set req ref in the user_info
        userRequestRef.setValue(requestModelRefrance);


        requestRef.setValue(requestModel);
        requestRef.updateChildren(hashMap);

        new CreateChat().createPublicFirebaseChat(requestModel);

        app.switchMainAppMenuFragment(new LobbyFragmentCore(requestModelRefrance));

    }



}
