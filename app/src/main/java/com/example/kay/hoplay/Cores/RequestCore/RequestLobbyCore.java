package com.example.kay.hoplay.Cores.RequestCore;

import android.content.Intent;
import android.util.Log;

import com.example.kay.hoplay.CoresAbstract.RequestAbstracts.RequestLobby;
import com.example.kay.hoplay.Interfaces.FirebasePaths;
import com.example.kay.hoplay.Models.RequestModel;
import com.example.kay.hoplay.Services.CallbackHandlerCondition;
import com.example.kay.hoplay.Services.HandlerCondition;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;


public class RequestLobbyCore extends RequestLobby implements FirebasePaths {

    private RequestModel requestModel;
    String adminPicture,adminUser;
    boolean isDone=false;

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

        if (requestModel == null)
            return;


        app.getDatabaseUsersInfo().child(requestModel.getAdmin()).child(FIREBASE_DETAILS_ATTR)
                .addListenerForSingleValueEvent(getAdminInfo);

        CallbackHandlerCondition callback = new CallbackHandlerCondition() {
            @Override
            public boolean callBack() {
                if(isDone)
                    setLobbyInfo(
                            requestModel.getRequestPicture(),
                            requestModel.getMatchType(),
                            adminUser,
                            adminPicture,
                            requestModel.getPlayers(),
                            requestModel.getRank(),
                            requestModel.getRegion()
                    );
                return isDone;
            }
        };

        new HandlerCondition(callback, 0);




        Log.i("-->", requestModel.getAdmin());
    }
}
