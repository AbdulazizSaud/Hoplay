package com.example.kay.hoplay.util;

import com.example.kay.hoplay.App.App;
import com.example.kay.hoplay.Cores.ChatCore.CreateChat;
import com.example.kay.hoplay.Cores.RequestCore.LobbyFragmentCore;
import com.example.kay.hoplay.Interfaces.FirebasePaths;
import com.example.kay.hoplay.Models.GameModel;
import com.example.kay.hoplay.Models.PlayerModel;
import com.example.kay.hoplay.Models.RequestModel;
import com.example.kay.hoplay.Models.RequestModelRefrance;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ServerValue;

import java.util.ArrayList;
import java.util.HashMap;



public class Request implements FirebasePaths {


    RequestModelRefrance requestModelRefrance;

    public Request(String platform, String gameName, String matchType, String region, String numberOfPlayers, String rank, String description){

        App app = App.getInstance();

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
        hashMap.put("timeStamp", ServerValue.TIMESTAMP);


        requestModelRefrance = new RequestModelRefrance(requestKey,gameModel.getGameID(),platform,region);
        // set req ref in the user_info
        userRequestRef.setValue(requestModelRefrance);


        requestRef.setValue(requestModel);
        requestRef.updateChildren(hashMap);

        new CreateChat().createPublicFirebaseChat(requestModel);
    }

    public RequestModelRefrance getRequestModelRefrance() {
        return requestModelRefrance;
    }


}
