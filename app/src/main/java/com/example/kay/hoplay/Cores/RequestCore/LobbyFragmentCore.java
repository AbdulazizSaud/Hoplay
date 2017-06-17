package com.example.kay.hoplay.Cores.RequestCore;

import android.util.Log;
import android.view.View;

import com.example.kay.hoplay.App.App;
import com.example.kay.hoplay.Fragments.LobbyFragment;
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


public class LobbyFragmentCore extends LobbyFragment implements FirebasePaths {


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

                if (player.getUID().equals(app.getUserInformation().getUID()))
                    lobby.getJoinButton().setVisibility(View.INVISIBLE);


                if (requestModel.getPlatform().equalsIgnoreCase("PS"))
                {

                    player.setGamePovider("PSN Account");
                    player.setGameProviderAcc(app.getUserInformation().getPSNAcc());
                }
                else if (requestModel.getPlatform().equalsIgnoreCase("XBOX"))
                {
                    player.setGamePovider("XBOX Account");
                    player.setGameProviderAcc(app.getUserInformation().getXboxLiveAcc());
                }
                else{
                    String pcGameProvider = app.getGameManager().getPcGamesWithProviders().get(requestModel.getGameId().trim());

                    player.setGamePovider(pcGameProvider);
                    player.setGameProviderAcc(app.getUserInformation().getPcGamesAcc().get(pcGameProvider));
                }


                lobby.addPlayer(player);
                requestModel.getPlayers().add(player);

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

        String path = requestModelRefrance.getPlatform() + "/"
                + requestModelRefrance.getGameId() + "/"
                + requestModelRefrance.getRegion() + "/"
                + requestModelRefrance.getRequestId();

        requestRef = app.getDatabaseRequests().child(path);
        requestRef.addListenerForSingleValueEvent(onLoadLobbyInformation);
        requestRef.child("players").addChildEventListener(onAddPlayerEvent);

    }

    @Override
    protected void cancelRequest(){
        final String uid = app.getUserInformation().getUID();

        requestRef.child("players").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Iterable<DataSnapshot> shots = dataSnapshot.getChildren();

                for(DataSnapshot snapshot : shots)
                {
                    if(snapshot.child("uid").getValue(String.class).equals(uid)){
                        snapshot.getRef().setValue(null);
                        break;
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        app.getDatabaseUsersInfo().child(uid+"/"+FIREBASE_USER_REQUESTS_ATTR).removeValue();
        app.getDatabaseUsersInfo().child(uid+"/"+FIREBASE_USER_PUBLIC_CHAT+"/"+requestModel.getRequestId()).removeValue();
        app.switchMainAppMenuFragment(new NewRequestFragmentCore());
        removeListener();


    }

    private void removeListener()
    {
        requestRef.removeEventListener(onLoadLobbyInformation);
        requestRef.child("player").removeEventListener(onAddPlayerEvent);
    }
}
