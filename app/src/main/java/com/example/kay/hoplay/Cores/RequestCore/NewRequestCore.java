package com.example.kay.hoplay.Cores.RequestCore;

import android.util.Log;

import com.example.kay.hoplay.CoresAbstract.RequestAbstracts.NewRequest;
import com.example.kay.hoplay.Interfaces.FirebasePaths;
import com.example.kay.hoplay.Models.GameModel;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class NewRequestCore extends NewRequest implements FirebasePaths{



    protected void OnStartActivity() {

    }

    protected void loadstandards()
    {

        // Load  user games
        ArrayList<GameModel> games = app.getGameManager().getAllGames();
        for (GameModel gameModel : games)
        {
            gamesList.add(gameModel.getGameName());
        }


        // Load regions
        DatabaseReference regionsRef = app.getDatabaseRegions();
        regionsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                if (dataSnapshot != null) {
                    for (DataSnapshot region : dataSnapshot.getChildren()) {
                        Log.i("bobo",region.toString());
                        regionList.add(region.getValue(String.class).trim());
                    }
                }

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    } // End of load method




}
