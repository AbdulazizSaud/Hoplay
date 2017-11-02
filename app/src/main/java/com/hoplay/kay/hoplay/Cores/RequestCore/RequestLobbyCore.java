package com.hoplay.kay.hoplay.Cores.RequestCore;

import android.content.Intent;
import android.widget.Toast;

import com.hoplay.kay.hoplay.CoresAbstract.RequestAbstracts.RequestLobby;
import com.hoplay.kay.hoplay.Interfaces.FirebasePaths;
import com.hoplay.kay.hoplay.Models.GameModel;
import com.hoplay.kay.hoplay.Models.PlayerModel;
import com.hoplay.kay.hoplay.Models.RequestModel;
import com.hoplay.kay.hoplay.Services.CallbackHandlerCondition;
import com.hoplay.kay.hoplay.Services.HandlerCondition;
import com.hoplay.kay.hoplay.FirebaseControllers.Request;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.hoplay.kay.hoplay.FirebaseControllers.MessagePack;


public class RequestLobbyCore extends RequestLobby implements FirebasePaths {


    private GameModel gameModel;
    private RequestModel requestModel;
    private String adminPicture;
    private boolean isDone=false;
    private  DatabaseReference requestRef;

    private ChildEventListener onAddPlayerEvent = new ChildEventListener() {
        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String s) {

            if(dataSnapshot.getValue() == null)
                return;

            final String uid = dataSnapshot.child("uid").getValue(String.class);
            final String username = dataSnapshot.child("username").getValue(String.class);
            app.getDatabaseUsersInfo().child(uid+"/"+FIREBASE_DETAILS_ATTR).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    String picture = dataSnapshot.child(FIREBASE_PICTURE_URL_ATTR).getValue(String.class);

                    PlayerModel player = new PlayerModel(uid,username);

                    player.setProfilePicture(picture);
                    lobby.setupPlayerInformation(dataSnapshot,requestModel, player);


                    lobby.addPlayer(player);
                    requestModel.addPlayer(player);

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }

        @Override
        public void onChildChanged(DataSnapshot dataSnapshot, String s) {

        }

        @Override
        public void onChildRemoved(DataSnapshot dataSnapshot) {

            PlayerModel player = new PlayerModel(dataSnapshot.child("uid").getValue(String.class), dataSnapshot.child("username").getValue(String.class));


            requestModel.removePlayer(player);
            lobby.removePlayer(player);

            if(lobby.getCurrentPlayerNumber() < 1)
                finish();


            if(player.getUID().equals(app.getUserInformation().getUID())) {

                // here you can put message
                Toast.makeText(app.getMainAppMenuCore(),"You have been kicked by an admin",Toast.LENGTH_LONG).show();

                requestRef.child("player").removeEventListener(onAddPlayerEvent);
                app.getMainAppMenuCore().cancelRequest();
            }
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
             isDone = true;
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };

    @Override
    protected void OnStartActivity() {
        Intent i = getIntent();
        final String requstId = (String) i.getStringExtra("requestId");


        // here we will retreive the data;
        requestModel = app.getRequestModelResult(requstId);
        gameModel =  app.getGameManager().getGameById(requestModel.getGameId());

        String path = requestModel.getPlatform().toUpperCase()+"/"+requestModel.getGameId()+"/"+requestModel.getRegion()+"/"
                +requestModel.getRequestId();

        requestRef = app.getDatabaseRequests().child(path);
        requestRef.child("players").addChildEventListener(onAddPlayerEvent);

        if (requestModel == null)
            return;


        app.getDatabaseUsersInfo().child(requestModel.getAdmin()).child(FIREBASE_DETAILS_ATTR)
                .addListenerForSingleValueEvent(getAdminInfo);

        CallbackHandlerCondition callback = new CallbackHandlerCondition() {
            @Override
            public boolean callBack() {
                if(isDone)
                    lobby.setLobbyInfo(requestModel,adminPicture);
                return isDone;
            }
        };

        new HandlerCondition(callback, 0);


        lobby.setMatchTypeImage(requestModel);

    }

    @Override
    protected void joinToRequest() {

        if(lobby.isExsist(app.getUserInformation().getUID()))
            return;

        if(lobby.getCurrentPlayerNumber() == requestModel.getPlayerNumber())
        {
            Toast.makeText(this,"Lobby is full",Toast.LENGTH_SHORT).show();
            return;
        }

        if(app.getMainAppMenuCore().getRequestModelRef() !=null) {
            app.getMainAppMenuCore().cancelRequest();
        }

        CallbackHandlerCondition callbackHandlerCondition = new CallbackHandlerCondition() {
            @Override
            public boolean callBack() {


                String uid =  app.getUserInformation().getUID();
                String reqId = requestModel.getRequestId();

                GameModel gameModel = app.getGameManager().getGameById(requestModel.getGameId());

                PlayerModel player = new PlayerModel(uid, app.getUserInformation().getUsername());
                requestModel.addPlayer(player);

                requestRef.child("players").setValue(requestModel.getPlayers());

                Request request = new Request();
                request.setUserReference(
                        app.getDatabaseUsersInfo().child(uid),
                        reqId,
                        requestModel.getMatchType(),
                        gameModel.getGameID(),
                        gameModel.getGameType(),
                        requestModel.getPlatform(),
                        requestModel.getRegion());




                DatabaseReference refMessages = app.getFirebaseDatabase().getReferenceFromUrl(FB_PUBLIC_CHAT_PATH).child(reqId).child("_messages_");
                new MessagePack(refMessages,app.getUserInformation().getUsername()+ " Joined the request","_join_");


                app.switchMainAppMenuFragment(new LobbyFragmentCore(request.getRequestModelReference()),2);
                jumpToLobbyChat(requestModel,FIREBASE_PUBLIC_ATTR);

                return true;
            }
        };

        HandlerCondition handlerCondition = new HandlerCondition(callbackHandlerCondition,1000);


    }




}
