package com.example.kay.hoplay.util;

import com.example.kay.hoplay.App.App;
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
    App app;


    public Request()
    {
        app =  App.getInstance();
    }

    public Request(String platform, String gameName, String matchType, String region, String numberOfPlayers, String rank, String description) {

        app =  App.getInstance();

        GameModel gameModel = app.getGameManager().getGameByName(gameName);

        // users_info_ -> user id
        DatabaseReference userRef = app.getDatabaseUsersInfo().child(app.getUserInformation().getUID());

        // _requests_ ->  platform -> GameID -> Region
        DatabaseReference requestsRef = app.getDatabaseRequests().child(platform.toUpperCase()).child(gameModel.getGameID()).child(region);


        String requestKey = requestsRef.push().getKey();
        String requestAdmin = app.getUserInformation().getUID();
        // _requests_ -> platform -> GameID -> Region -> addRequestToFirebase id
        DatabaseReference requestRef = requestsRef.child(requestKey);


        // set the addRequestToFirebase info under the requests tree

        HashMap<String, Object> data = new HashMap<>();
        // TimeStamp timeStamp=new TimeStamp();

        int numberPlayers = numberOfPlayers.equals("All Numbers") ? gameModel.getMaxPlayers() : Integer.parseInt(numberOfPlayers);
        RequestModel requestModel = new RequestModel(
                platform,
                gameName,
                requestAdmin,
                description,
                region,
                numberPlayers,
                matchType,
                rank);


        requestModel.addPlayer(new PlayerModel(
                app.getUserInformation().getUID(),
                app.getUserInformation().getUsername()
        ));


        requestModel.setAdminName(app.getUserInformation().getUsername());
        requestModel.setGameId(gameModel.getGameID());
        requestModel.setRequestId(requestKey);


        //-------------------------------------------
        setUserReference(userRef, requestKey, gameModel.getGameID(), gameModel.getGameType(), platform, region);
        //-------------------------------------------

        HashMap hashMap = new HashMap();
        hashMap.put(FIREBASE_REQUEST_TIME_STAMP_ATTR, ServerValue.TIMESTAMP);

        requestRef.setValue(requestModel);
        requestRef.updateChildren(hashMap);
        createChat(requestModel);
    }


    public void setUserReference(DatabaseReference userRef, String requestId, String gameId, String gameType, String platform, String region) {

        String currentUid = app.getUserInformation().getUID();

        DatabaseReference requestRef = userRef.child(FIREBASE_USER_REQUESTS_ATTR);
        // users_info_ -> user id  -> _games_->_recent_played_
        DatabaseReference userRecentPlayedRef = userRef.child(FIREBASE_RECENT_GAMES_PATH);

        requestModelRefrance = new RequestModelRefrance(requestId, gameId, platform, region);

        String recentPlayedKey = userRecentPlayedRef.push().getKey();
        DatabaseReference recentGameRef = userRecentPlayedRef.child(recentPlayedKey);
        HashMap<String, Object> recentData = new HashMap<>();

        recentData.put("game_id", gameId);
        recentData.put("game_type", gameType);
        recentData.put(FIREBASE_REQUEST_TIME_STAMP_ATTR, ServerValue.TIMESTAMP);

        recentGameRef.setValue(recentData);
        requestRef.setValue(requestModelRefrance);

        CreateChat createChat = new CreateChat();
        createChat.setValueUserRef(currentUid,requestId);
        createChat.setValueUsersChat(FIREBASE_PUBLIC_ATTR,requestId,currentUid);


    }

    public void createChat(RequestModel requestModel) {
        new CreateChat().createPublicFirebaseChat(requestModel);

    }

    public RequestModelRefrance getRequestModelRefrance() {
        return requestModelRefrance;
    }


}
