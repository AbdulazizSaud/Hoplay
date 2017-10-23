package com.hoplay.kay.hoplay.Cores.RequestCore;

import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.hoplay.kay.hoplay.Fragments.LobbyFragment;
import com.hoplay.kay.hoplay.Interfaces.Constants;
import com.hoplay.kay.hoplay.Interfaces.FirebasePaths;
import com.hoplay.kay.hoplay.Models.GameModel;
import com.hoplay.kay.hoplay.Models.PlayerModel;
import com.hoplay.kay.hoplay.Models.RequestModel;
import com.hoplay.kay.hoplay.Models.RequestModelReference;
import com.hoplay.kay.hoplay.R;
import com.hoplay.kay.hoplay.Services.CallbackHandlerCondition;
import com.hoplay.kay.hoplay.Services.HandlerCondition;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.hoplay.kay.hoplay.util.setMessagePack;


public class LobbyFragmentCore extends LobbyFragment implements FirebasePaths, Constants {


    private RequestModel requestModel;
    private GameModel gameModel;
    private boolean isDone = false;
    private DatabaseReference requestRef;
    private RequestModelReference requestModelRefrance;


    private ChildEventListener onAddPlayerEvent = new ChildEventListener() {
        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
            try {

                final String uid = dataSnapshot.child("uid").getValue(String.class);
                final String username = dataSnapshot.child("username").getValue(String.class);


                app.getDatabaseUsersInfo().child(uid + "/" + FIREBASE_DETAILS_ATTR).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {


                        PlayerModel player = new PlayerModel(uid, username);
                        String picture = dataSnapshot.child(FIREBASE_PICTURE_URL_ATTR).getValue(String.class);


                        player.setProfilePicture(picture);
                        lobby.setupPlayerInformation(dataSnapshot, requestModel, player);


                        lobby.addPlayer(player);
                        requestModel.addPlayer(player);


                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            } catch (NullPointerException e) {
                Log.i("---->", e.getMessage());
                return;
            }

        }

        @Override
        public void onChildChanged(DataSnapshot dataSnapshot, String s) {

        }

        @Override
        public void onChildRemoved(DataSnapshot dataSnapshot) {

            PlayerModel player = new PlayerModel(dataSnapshot.child("uid").getValue(String.class), dataSnapshot.child("username").getValue(String.class));
            requestModel.removePlayer(player);
            lobby.removePlayer(player);

            if (player.getUID().equals(app.getUserInformation().getUID()) && !leaveing) {
                // here you can put message
                Toast.makeText(app.getMainAppMenuCore(), "You have been kicked by an admin", Toast.LENGTH_LONG).show();
                cancelRequest();

            }
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


            if (dataSnapshot.getValue(RequestModel.class) == null || dataSnapshot.getValue() == null)
                app.getMainAppMenuCore().cancelRequest();


            requestModel = dataSnapshot.getValue(RequestModel.class);
            gameModel = app.getGameManager().getGameById(requestModel.getGameId());
            requestModel.setRequestPicture(gameModel.getGamePhotoUrl());

            lobby.setMatchTypeImage(requestModel);

            app.getDatabaseUsersInfo().child(requestModel.getAdmin() + "/" + FIREBASE_PICTURE_URL_PATH).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    String adminPicture = dataSnapshot.getValue(String.class);
                    lobby.setLobbyInfo(requestModel, adminPicture);

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });


        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };
//


    public LobbyFragmentCore() {
    }


    public LobbyFragmentCore(RequestModelReference requestModelRef) {
        super();


        this.requestModelRefrance = requestModelRef;
        app.getMainAppMenuCore().setRequestModelRef(requestModelRef);

    }

    @Override
    protected void OnStartActivity() {


        if (requestModelRefrance == null) {
            app.getMainAppMenuCore().cancelRequest();
        }

        String path = requestModelRefrance.getPlatform().toUpperCase() + "/"
                + requestModelRefrance.getGameId() + "/"
                + requestModelRefrance.getRegion() + "/"
                + requestModelRefrance.getRequestId();

        requestRef = app.getDatabaseRequests().child(path);
        requestRef.addListenerForSingleValueEvent(onLoadLobbyInformation);

        new HandlerCondition(new CallbackHandlerCondition() {
            @Override
            public boolean callBack() {
                requestRef.child("players").addChildEventListener(onAddPlayerEvent);
                return true;
            }
        }, 1000);


        checkLobbyExpire();
    }

    private void checkLobbyExpire() {
        app.getTimeStamp().setTimestampLong();
        CallbackHandlerCondition callbackHandlerCondition = new CallbackHandlerCondition() {
            @Override
            public boolean callBack() {

                long currentTimestamp = app.getTimeStamp().getTimestampLong();
                long last48 = requestModel.getTimeStamp() + DUE_REQUEST_TIME_IN_VALUE_HOURS;

                if (currentTimestamp != -1) {
                    if (currentTimestamp >= last48) {
                        cancelRequest();
                        //Toast.makeText(getContext(),"Your request has been expired",Toast.LENGTH_LONG).show();
                        return true;
                    }
                }
                return false;
            }
        };

        HandlerCondition condition = new HandlerCondition(callbackHandlerCondition, 1000);
    }


    @Override
    protected void cancelRequest() {
        removeListener();

        setChatValuePack("_leave_", "left the lobby", app.getUserInformation().getUsername());

        app.getMainAppMenuCore().cancelRequest();
        leaveing = true;
    }


    @Override
    protected void addPlayerToFreind(PlayerModel model) {
        // users_info -> user key -> _firends_list_
        final DatabaseReference userFriendsListRef = app.getDatabaseUsersInfo().child(app.getUserInformation().getUID()).child(FIREBASE_FRIENDS_LIST_ATTR);

        // Add friend to the friend list
        userFriendsListRef.child(model.getUID()).setValue(model.getUID());
    }

    @Override
    protected void removePlayerFromLobby(PlayerModel model) {
        if (lobby.getAdminUid().equals(app.getUserInformation().getUID())) {
            app.getDatabaseRequests().child(lobby.getRequestPath()).child("players").child(model.getUID()).removeValue();

            setChatValuePack("_kicked_", "Kicked from the lobby", model.getUsername());
        }

    }

    @Override
    protected void updateAdminInformation(RequestModel model) {
        app.getDatabaseRequests().child(lobby.getRequestPath()).child("admin").setValue(model.getAdmin());
        app.getDatabaseRequests().child(lobby.getRequestPath()).child("adminName").setValue(model.getAdminName());
    }

    private void removeListener() {
        requestRef.removeEventListener(onLoadLobbyInformation);
        requestRef.child("player").removeEventListener(onAddPlayerEvent);
    }


    private void setChatValuePack(String status, String message, String username) {
        DatabaseReference refMessages = app.getFirebaseDatabase().getReferenceFromUrl(FB_PUBLIC_CHAT_PATH).child(requestModel.getRequestId()).child("_messages_");
        new setMessagePack(refMessages, username + " " + message, status);
    }
}
