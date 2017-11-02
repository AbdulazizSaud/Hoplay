package com.hoplay.kay.hoplay.FirebaseControllers;

import com.hoplay.kay.hoplay.App.App;
import com.hoplay.kay.hoplay.Interfaces.FirebasePaths;
import com.hoplay.kay.hoplay.Models.GameModel;
import com.hoplay.kay.hoplay.Models.PlayerModel;
import com.hoplay.kay.hoplay.Models.RequestModel;
import com.hoplay.kay.hoplay.Models.RequestModelReference;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ServerValue;

import java.util.HashMap;


public class Request implements FirebasePaths {

    RequestModelReference requestModelReference;
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
        setUserReference(userRef, requestKey,requestModel.getMatchType(), gameModel.getGameID(), gameModel.getGameType(), platform, region);
        //-------------------------------------------

        HashMap hashMap = new HashMap();
        hashMap.put(FIREBASE_REQUEST_TIME_STAMP_ATTR, ServerValue.TIMESTAMP);

        requestRef.setValue(requestModel);
        requestRef.updateChildren(hashMap);
        createChat(requestModel);
    }


    public void setUserReference(DatabaseReference userRef, String requestId,String requestType, String gameId, String gameType, String platform, String region) {

        String currentUid = app.getUserInformation().getUID();

        DatabaseReference requestRef = userRef.child(FIREBASE_USER_REQUESTS_ATTR);
        // users_info_ -> user id  -> _games_->_recent_played_
        DatabaseReference userRecentPlayedRef = userRef.child(FIREBASE_RECENT_GAMES_PATH);

        requestModelReference = new RequestModelReference(requestId, gameId, platform, region);

        String recentPlayedKey = userRecentPlayedRef.push().getKey();
        DatabaseReference recentGameRef = userRecentPlayedRef.child(recentPlayedKey);
        HashMap<String, Object> recentData = new HashMap<>();


        recentData.put("game_id", gameId);
        recentData.put("game_type", gameType);
        recentData.put("request_platform", platform);
        recentData.put("match_type", requestType);
        recentData.put(FIREBASE_REQUEST_TIME_STAMP_ATTR, ServerValue.TIMESTAMP);

        recentGameRef.setValue(recentData);
        requestRef.setValue(requestModelReference);

        CreateChat createChat = new CreateChat();
        createChat.setValueUserRef(currentUid,requestId);
        createChat.setValueUsersChat(FIREBASE_PUBLIC_ATTR,requestId,currentUid);


    }

    public void createChat(RequestModel requestModel) {
        new CreateChat().createPublicFirebaseChat(requestModel);

    }

    public RequestModelReference getRequestModelReference() {
        return requestModelReference;
    }


}
