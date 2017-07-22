package com.example.kay.hoplay.Cores.RequestCore;

import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;

import com.example.kay.hoplay.CoresAbstract.RequestAbstracts.RequestLobby;
import com.example.kay.hoplay.Interfaces.FirebasePaths;
import com.example.kay.hoplay.Models.GameModel;
import com.example.kay.hoplay.Models.PlayerModel;
import com.example.kay.hoplay.Models.RequestModel;
import com.example.kay.hoplay.R;
import com.example.kay.hoplay.Services.CallbackHandlerCondition;
import com.example.kay.hoplay.Services.HandlerCondition;
import com.example.kay.hoplay.util.Request;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;


public class RequestLobbyCore extends RequestLobby implements FirebasePaths {


    private GameModel gameModel;
    private RequestModel requestModel;
    private String adminPicture,adminUser;
    private boolean isDone=false;
    private  DatabaseReference requestRef;

    private ChildEventListener onAddPlayerEvent = new ChildEventListener() {
        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
            try {


                final String uid = dataSnapshot.child("uid").getValue(String.class);
                final String username = dataSnapshot.child("username").getValue(String.class);


                app.getDatabaseUsersInfo().child(uid+"/"+FIREBASE_DETAILS_ATTR).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String picture = dataSnapshot.child(FIREBASE_PICTURE_URL_ATTR).getValue(String.class);

                        PlayerModel player = new PlayerModel(uid,username);

                        player.setProfilePicture(picture);


                        // set the lobby border width
                        lobby.setGameBorderWidth(8);

                        if (player.getUID().equals(app.getUserInformation().getUID()))
                            lobby.getJoinButton().setVisibility(View.INVISIBLE);


                        if (requestModel.getPlatform().equalsIgnoreCase("PS"))
                        {

                            player.setGamePovider("PSN Account");
                            player.setGameProviderAcc(dataSnapshot.child(FIREBASE_USER_PS_GAME_PROVIDER_ATTR).getValue(String.class));
                            lobby.setGameBorderColor(ContextCompat.getColor(getApplicationContext(), R.color.ps_color));

                        }
                        else if (requestModel.getPlatform().equalsIgnoreCase("XBOX"))
                        {
                            player.setGamePovider("XBOX Account");
                            player.setGameProviderAcc(dataSnapshot.child(FIREBASE_USER_XBOX_GAME_PROVIDER_ATTR).getValue(String.class));
                            lobby.setGameBorderColor(ContextCompat.getColor(getApplicationContext(), R.color.xbox_color));
                        }
                        else{
                            String pcGameProvider = app.getGameManager().getPcGamesWithProviders().get(requestModel.getGameId().trim());

                            player.setGamePovider(pcGameProvider);
                            if(dataSnapshot.child(FIREBASE_USER_PC_GAME_PROVIDER_ATTR+"/"+pcGameProvider).getValue() !=null)
                                player.setGameProviderAcc(dataSnapshot.child(FIREBASE_USER_PC_GAME_PROVIDER_ATTR+"/"+pcGameProvider).getValue(String.class));

                            lobby.setGameBorderColor(ContextCompat.getColor(getApplicationContext(), R.color.pc_color));
                        }

                        lobby.addPlayer(player);
                        requestModel.addPlayer(player);


                        // Set match type image
                        if (requestModel.getMatchType().equalsIgnoreCase("Competitive"))
                            lobby.setMatchImage(R.drawable.ic_whatshot_competitive_24dp);
                        else if (requestModel.getMatchType().equalsIgnoreCase("Qhick Match"))
                            lobby.setMatchImage(R.drawable.ic_whatshot_quick_match_24dp);
                        else
                            lobby.setMatchImage(R.drawable.ic_whatshot_unfocused_24dp);

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            } catch (NullPointerException e) {
                Log.i("---->",e.getMessage());
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
             adminUser  = dataSnapshot.child(FIREBASE_USERNAME_ATTR).getValue(String.class);
             isDone = true;
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };

    @Override
    protected void OnStartActivity() {
        Intent i = getIntent();
        String requstId = (String) i.getStringExtra("requestId");


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

    @Override
    protected void joinToRequest() {

        if(lobby.isExsist(app.getUserInformation().getUID()))
            return;



        if(app.getMainAppMenuCore().getRequestModelRef() !=null)
            app.getMainAppMenuCore().cancelRequest();

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


                app.switchMainAppMenuFragment(new LobbyFragmentCore(request.getRequestModelReference()));
                jumpToLobbyChat(requestModel,FIREBASE_PUBLIC_ATTR);

                return true;
            }
        };

        HandlerCondition handlerCondition = new HandlerCondition(callbackHandlerCondition,1000);


    }




}
