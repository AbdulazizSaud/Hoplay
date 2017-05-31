package com.example.kay.hoplay.Cores.RequestCore;

import android.content.Intent;
import android.util.Log;

import com.example.kay.hoplay.Cores.ChatCore.ChatCore;
import com.example.kay.hoplay.Cores.ChatCore.CreateChat;
import com.example.kay.hoplay.CoresAbstract.RequestAbstracts.RequestLobby;
import com.example.kay.hoplay.Interfaces.FirebasePaths;
import com.example.kay.hoplay.Models.FriendCommonModel;
import com.example.kay.hoplay.Models.PlayerModel;
import com.example.kay.hoplay.Models.RequestModel;
import com.example.kay.hoplay.Services.CallbackHandlerCondition;
import com.example.kay.hoplay.Services.HandlerCondition;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;


public class RequestLobbyCore extends RequestLobby implements FirebasePaths {

    private String adminPicture,adminUser;
    private boolean isDone=false;
    private  DatabaseReference requestRef;

    private ChildEventListener onAddPlayerEvent = new ChildEventListener() {
        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
            try {

                addPlayer(
                        dataSnapshot.child("uid").getValue(String.class),
                        dataSnapshot.child("username").getValue(String.class)
                );

            }catch (NullPointerException e)
            {
                return;
            }


        }

        @Override
        public void onChildChanged(DataSnapshot dataSnapshot, String s) {

        }

        @Override
        public void onChildRemoved(DataSnapshot dataSnapshot) {
            removePlayer(dataSnapshot.child("uid").getValue(String.class));
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

        String path = requestModel.getPlatform()+"/"+requestModel.getGameId()+"/"+requestModel.getRegion()+"/"
                +requestModel.getRequestId();

        requestRef = app.getDatabaseRequests().child(path);



        if (requestModel == null)
            return;


        app.getDatabaseUsersInfo().child(requestModel.getAdmin()).child(FIREBASE_DETAILS_ATTR)
                .addListenerForSingleValueEvent(getAdminInfo);

        CallbackHandlerCondition callback = new CallbackHandlerCondition() {
            @Override
            public boolean callBack() {
                if(isDone) {
                    setLobbyInfo(
                            requestModel.getRequestPicture(),
                            requestModel.getMatchType(),
                            adminUser,
                            adminPicture,
                            requestModel.getPlayers(),
                            requestModel.getRank(),
                            requestModel.getRegion()
                    );



                    new HandlerCondition(new CallbackHandlerCondition() {
                        @Override
                        public boolean callBack() {
                            requestRef.child("players").addChildEventListener(onAddPlayerEvent);
                            return true;
                        }
                    },1000);

                }
                return isDone;
            }
        };

        new HandlerCondition(callback, 0);




    }

    @Override
    protected void joinToRequest() {

        if(isExsist(app.getUserInformation().getUID()))
            return;

        String uid =  app.getUserInformation().getUID();
        String reqId = requestModel.getRequestId();

        requestModel.getPlayers().add(new PlayerModel(
                uid,
                app.getUserInformation().getUsername()
        ));


        requestRef.child("players").setValue(requestModel.getPlayers());

       app.getDatabaseUsersInfo()
               .child(app.getUserInformation().getUID())
               .child(FIREBASE_USER_REQUESTS_REF)
               .child(reqId).setValue(reqId);

        CreateChat createChat = new CreateChat();
        createChat.setValueUserRef(uid,requestModel.getRequestId());
        createChat.setValueUsersChat(FIREBASE_PUBLIC_ATTR,reqId,uid);

        jumpToLobbyChat(requestModel);
    }

    @Override
    protected void jumpToLobbyChat(RequestModel model) {
        Intent chatActivity = new Intent(getApplicationContext(), ChatCore.class);

        chatActivity.putExtra("room_key", model.getRequestId());
        chatActivity.putExtra("room_type", FIREBASE_PUBLIC_ATTR);
        chatActivity.putExtra("room_name", model.getRequestTitle());
        chatActivity.putExtra("room_picture", model.getRequestPicture());

        finish();
        startActivity(chatActivity);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        requestRef.child("players").removeEventListener(onAddPlayerEvent);
    }

    @Override
    protected void onStop() {
        super.onStop();
        requestRef.child("players").removeEventListener(onAddPlayerEvent);
    }
}
