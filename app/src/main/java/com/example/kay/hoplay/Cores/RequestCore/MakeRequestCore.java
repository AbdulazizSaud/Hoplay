package com.example.kay.hoplay.Cores.RequestCore;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.kay.hoplay.App.App;
import com.example.kay.hoplay.Interfaces.FirebasePaths;
import com.example.kay.hoplay.Models.SavedRequestModel;
import com.example.kay.hoplay.CoresAbstract.RequestAbstracts.MakeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by Kay on 2/14/2017.
 */

public class MakeRequestCore extends MakeRequest implements FirebasePaths{





    @Override
    protected void OnClickHolders(SavedRequestModel model, View v) {
        // here .. blablabla ..

    }

    @Override
    protected void onStartActivity() {

    }

    @Override
    protected void checkUserGamesNumber() {
        String userID = app.getUserInformation().getUID();
        final DatabaseReference userGames = app.getDatabaseUsers().child(userID+"/"+FIREBASE_FAVOR_GAMES_PATH);

        userGames.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                long numberOfUserGames = dataSnapshot.getChildrenCount();
                canMakeRequest(numberOfUserGames > 0);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


}
