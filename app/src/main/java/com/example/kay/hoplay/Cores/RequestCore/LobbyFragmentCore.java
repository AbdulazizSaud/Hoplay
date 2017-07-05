package com.example.kay.hoplay.Cores.RequestCore;

import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.kay.hoplay.App.App;
import com.example.kay.hoplay.Fragments.LobbyFragment;
import com.example.kay.hoplay.Interfaces.Constants;
import com.example.kay.hoplay.Interfaces.FirebasePaths;
import com.example.kay.hoplay.Models.GameModel;
import com.example.kay.hoplay.Models.PlayerModel;
import com.example.kay.hoplay.Models.RequestModel;
import com.example.kay.hoplay.Models.RequestModelRefrance;
import com.example.kay.hoplay.Services.CallbackHandlerCondition;
import com.example.kay.hoplay.Services.HandlerCondition;
import com.example.kay.hoplay.util.GameManager;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class LobbyFragmentCore extends LobbyFragment implements FirebasePaths,Constants {


    private RequestModel requestModel;
    private GameModel gameModel;
    private String adminPicture, adminUser;
    private boolean isDone = false;
    private DatabaseReference requestRef;
    private RequestModelRefrance requestModelRefrance;


    private ChildEventListener onAddPlayerEvent = new ChildEventListener() {
        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
            try {

                PlayerModel player = new PlayerModel(dataSnapshot.child("uid").getValue(String.class), dataSnapshot.child("username").getValue(String.class));

                final String uid = dataSnapshot.child("uid").getValue(String.class);
                final String username = dataSnapshot.child("username").getValue(String.class);


                app.getDatabaseUsersInfo().child(uid+"/"+FIREBASE_DETAILS_ATTR).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String picture = dataSnapshot.child(FIREBASE_PICTURE_URL_ATTR).getValue(String.class);

                        PlayerModel player = new PlayerModel(uid,username);

                        player.setProfilePicture(picture);


                        if (player.getUID().equals(app.getUserInformation().getUID()))
                            lobby.getJoinButton().setVisibility(View.INVISIBLE);


                        if (requestModelRefrance.getPlatform().equalsIgnoreCase("PS"))
                        {

                            player.setGamePovider("PSN Account");
                            player.setGameProviderAcc(dataSnapshot.child(FIREBASE_USER_PS_GAME_PROVIDER_ATTR).getValue(String.class));
                        }
                        else if (requestModelRefrance.getPlatform().equalsIgnoreCase("XBOX"))
                        {
                            player.setGamePovider("XBOX Account");
                            player.setGameProviderAcc(dataSnapshot.child(FIREBASE_USER_XBOX_GAME_PROVIDER_ATTR).getValue(String.class));
                        }
                        else{
                            String pcGameProvider = app.getGameManager().getPcGamesWithProviders().get(requestModelRefrance.getGameId().trim());

                            player.setGamePovider(pcGameProvider);
                            if(dataSnapshot.child(FIREBASE_USER_PC_GAME_PROVIDER_ATTR+"/"+pcGameProvider).getValue() !=null)
                            player.setGameProviderAcc(dataSnapshot.child(FIREBASE_USER_PC_GAME_PROVIDER_ATTR+"/"+pcGameProvider).getValue(String.class));
                        }

                        lobby.addPlayer(player);
                        requestModel.getPlayers().add(player);

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


    private ValueEventListener onLoadLobbyInformation = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {


            requestModel = dataSnapshot.getValue(RequestModel.class);

            gameModel =  app.getGameManager().getGameById(requestModel.getGameId());


            boolean b = (app.getUserInformation().getUsername()).equals(requestModel.getAdminName());


            adminUser = app.getUserInformation().getUsername();
            adminPicture = app.getUserInformation().getPictureURL();

            lobby.setLobbyInfo(
                    gameModel.getGamePhotoUrl(),
                    requestModel.getMatchType(),
                    adminUser,
                    adminPicture,
                    requestModel.getRank(),
                    requestModel.getRegion()
            );

        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };

    public LobbyFragmentCore(RequestModelRefrance requestModelRefrance) {
        super();

        if (requestModelRefrance == null)
            return;
        this.requestModelRefrance = requestModelRefrance;


    }

    @Override
    protected void OnStartActivity() {


        if (requestModelRefrance == null)
            return;

        String path = requestModelRefrance.getPlatform().toUpperCase() + "/"
                + requestModelRefrance.getGameId() + "/"
                + requestModelRefrance.getRegion() + "/"
                + requestModelRefrance.getRequestId();

        requestRef = app.getDatabaseRequests().child(path);
        requestRef.addListenerForSingleValueEvent(onLoadLobbyInformation);
        requestRef.child("players").addChildEventListener(onAddPlayerEvent);


        app.getTimeStamp().setTimestampLong();
        CallbackHandlerCondition callbackHandlerCondition = new CallbackHandlerCondition() {
            @Override
            public boolean callBack() {

                long currentTimestamp= app.getTimeStamp().getTimestampLong();
                long last48 = requestModel.getTimeStamp()+ DUE_REQUEST_TIME_IN_VALUE_HOURS;

                if(currentTimestamp !=-1)
                {
                    if(currentTimestamp >=last48) {
                        cancelRequest();
                        //Toast.makeText(getContext(),"Your request has been expired",Toast.LENGTH_LONG).show();
                        return true;
                    }
                }
                return false;
            }
        };

        HandlerCondition condition = new HandlerCondition(callbackHandlerCondition,1000);
    }





    @Override
    protected void cancelRequest(){

        app.cancelRequest();
        removeListener();

    }

    private void removeListener()
    {
        requestRef.removeEventListener(onLoadLobbyInformation);
        requestRef.child("player").removeEventListener(onAddPlayerEvent);
    }
}
