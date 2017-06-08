package com.example.kay.hoplay.Cores.Lobby;


import android.view.View;

import com.example.kay.hoplay.App.App;
import com.example.kay.hoplay.Cores.ChatCore.CreateChat;
import com.example.kay.hoplay.Interfaces.FirebasePaths;
import com.example.kay.hoplay.Models.GameModel;
import com.example.kay.hoplay.Models.PlayerModel;
import com.example.kay.hoplay.Models.RequestModel;
import com.example.kay.hoplay.Services.CallbackHandlerCondition;
import com.example.kay.hoplay.Services.HandlerCondition;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

public class LobbyCore implements FirebasePaths {


    private App app;
    private GameModel gameModel;
    private RequestModel requestModel;
    private String adminPicture, adminUser;
    private boolean isDone = false;
    private DatabaseReference requestRef;
    private Lobby lobby;

    private DatabaseReference playersRef;


    private ChildEventListener onAddPlayerEventListener = new ChildEventListener() {
        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
            try {

                PlayerModel player = new PlayerModel(dataSnapshot.child("uid").getValue(String.class), dataSnapshot.child("username").getValue(String.class));

                if (player.getUID().equals(app.getUserInformation().getUID()) || adminUser.equals(player.getUsername()))
                    lobby.getJoinButton().setVisibility(View.INVISIBLE);

                lobby.addPlayer(player);
                requestModel.getPlayers().add(player);

            } catch (NullPointerException e) {
                return;
            }


        }

        @Override
        public void onChildChanged(DataSnapshot dataSnapshot, String s) {

        }

        @Override
        public void onChildRemoved(DataSnapshot dataSnapshot) {

            PlayerModel player = new PlayerModel(dataSnapshot.child("uid").getValue(String.class), dataSnapshot.child("username").getValue(String.class));

            if (player.getUID().equals(app.getUserInformation().getUID()))
                lobby.getJoinButton().setVisibility(View.VISIBLE);
        }

        @Override
        public void onChildMoved(DataSnapshot dataSnapshot, String s) {

        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };

    private ValueEventListener getAdminInfo = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            adminPicture = dataSnapshot.child(FIREBASE_PICTURE_URL_ATTR).getValue(String.class);
            adminUser = dataSnapshot.child(FIREBASE_USERNAME_ATTR).getValue(String.class);
            isDone = true;
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };



    public LobbyCore(RequestModel model, Lobby lobby) {

        requestModel = model;
        gameModel = app.getGameManager().getGameById(requestModel.getGameId());

        String path = requestModel.getPlatform() + "/"
                + requestModel.getGameId() + "/"
                + requestModel.getRegion() + "/"
                + requestModel.getRequestId();

        app = App.getInstance();
        requestRef = app.getDatabaseRequests().child(path);
        this.lobby = lobby;

        OnStartActivity();
    }


    public void OnStartActivity() {


        playersRef =  requestRef.child("players");
        playersRef.addChildEventListener(onAddPlayerEventListener);

        if(!requestModel.getAdmin().equals(app.getUserInformation().getUID()))
        app.getDatabaseUsersInfo().child(requestModel.getAdmin()).child(FIREBASE_DETAILS_ATTR)
                .addListenerForSingleValueEvent(getAdminInfo);
        else
            isDone=true;

        CallbackHandlerCondition callback = new CallbackHandlerCondition() {
            @Override
            public boolean callBack() {
                if (isDone)
                    lobby.setLobbyInfo(
                            gameModel.getGamePhotoUrl(),
                            requestModel.getMatchType(),
                            adminUser,
                            adminPicture,
                            requestModel.getRank(),
                            requestModel.getRegion()
                    );
                return isDone;
            }
        };

        new HandlerCondition(callback, 0);

    }


    public void joinToRequest() {

        if (lobby.isExsist(app.getUserInformation().getUID()))
            return;

        String uid = app.getUserInformation().getUID();
        String reqId = requestModel.getRequestId();

        requestModel.getPlayers().add(new PlayerModel(uid, app.getUserInformation().getUsername()));

        requestRef.child("players").setValue(requestModel.getPlayers());

        app.getDatabaseUsersInfo()
                .child(app.getUserInformation().getUID())
                .child(FIREBASE_USER_REQUESTS_ATTR)
                .child(reqId).setValue(reqId);

        CreateChat createChat = new CreateChat();
        createChat.setValueUserRef(uid, requestModel.getRequestId());
        createChat.setValueUsersChat(FIREBASE_PUBLIC_ATTR, reqId, uid);

    }

    public DatabaseReference getRequestReference() {
        return requestRef;
    }


    public void removeListener()
    {
        playersRef.removeEventListener(onAddPlayerEventListener);
    }
}
