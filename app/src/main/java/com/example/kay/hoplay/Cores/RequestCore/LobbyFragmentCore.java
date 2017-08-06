package com.example.kay.hoplay.Cores.RequestCore;

import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.kay.hoplay.Fragments.LobbyFragment;
import com.example.kay.hoplay.Interfaces.Constants;
import com.example.kay.hoplay.Interfaces.FirebasePaths;
import com.example.kay.hoplay.Models.GameModel;
import com.example.kay.hoplay.Models.PlayerModel;
import com.example.kay.hoplay.Models.RequestModel;
import com.example.kay.hoplay.Models.RequestModelReference;
import com.example.kay.hoplay.R;
import com.example.kay.hoplay.Services.CallbackHandlerCondition;
import com.example.kay.hoplay.Services.HandlerCondition;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;


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
                        String picture = dataSnapshot.child(FIREBASE_PICTURE_URL_ATTR).getValue(String.class);

                        PlayerModel player = new PlayerModel(uid, username);

                        player.setProfilePicture(picture);


                        if (player.getUID().equals(app.getUserInformation().getUID()))
                            lobby.getJoinButton().setVisibility(View.INVISIBLE);

                        // set the lobby border width
                        lobby.setGameBorderWidth(8);



                        // TODO : ERROR
                        if (requestModelRefrance.getPlatform().equalsIgnoreCase("PS")) {
                            player.setGamePovider("PSN Account");
                            player.setGameProviderAcc(dataSnapshot.child(FIREBASE_USER_PS_GAME_PROVIDER_ATTR).getValue(String.class));
                            lobby.setGameBorderColor(ContextCompat.getColor(getContext(), R.color.ps_color));


                        } else if (requestModelRefrance.getPlatform().equalsIgnoreCase("XBOX")) {
                            player.setGamePovider("XBOX Account");
                            player.setGameProviderAcc(dataSnapshot.child(FIREBASE_USER_XBOX_GAME_PROVIDER_ATTR).getValue(String.class));
                            lobby.setGameBorderColor(ContextCompat.getColor(getContext(), R.color.xbox_color));

                        } else {
                            String pcGameProvider = app.getGameManager().getPcGamesWithProviders().get(requestModelRefrance.getGameId().trim());

                            player.setGamePovider(pcGameProvider);
                            if (dataSnapshot.child(FIREBASE_USER_PC_GAME_PROVIDER_ATTR + "/" + pcGameProvider).getValue() != null)
                                player.setGameProviderAcc(dataSnapshot.child(FIREBASE_USER_PC_GAME_PROVIDER_ATTR + "/" + pcGameProvider).getValue(String.class));
                            lobby.setGameBorderColor(ContextCompat.getColor(getContext(), R.color.pc_color));

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

            if(player.getUID().equals(app.getUserInformation().getUID())) {

                // here you can put message
                Toast.makeText(app.getMainAppMenuCore(),"You have been kicked by an admin",Toast.LENGTH_LONG).show();
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



            if(dataSnapshot.getValue(RequestModel.class) == null || dataSnapshot.getValue() == null )
                app.getMainAppMenuCore().cancelRequest();


            requestModel = dataSnapshot.getValue(RequestModel.class);
            gameModel = app.getGameManager().getGameById(requestModel.getGameId());
            requestModel.setRequestPicture(gameModel.getGamePhotoUrl());


            app.getDatabaseUsersInfo().child(requestModel.getAdmin() + "/" + FIREBASE_PICTURE_URL_PATH).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    String adminPicture = dataSnapshot.getValue(String.class);
                    lobby.setLobbyInfo(requestModel,adminPicture);

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
        app.getMainAppMenuCore().cancelRequest();
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
        if(lobby.getAdminUid().equals(app.getUserInformation().getUID()))
        {
            app.getDatabaseRequests().child(lobby.getRequestPath()).child("players").child(model.getUID()).removeValue();

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
}
