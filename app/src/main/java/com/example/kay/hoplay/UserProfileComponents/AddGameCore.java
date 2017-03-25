package com.example.kay.hoplay.UserProfileComponents;

import android.util.Log;
import android.view.View;

import com.example.kay.hoplay.Interfaces.FirebasePaths;
import com.example.kay.hoplay.Models.FriendCommonModel;
import com.example.kay.hoplay.Models.GameDetails;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by Kay on 3/25/2017.
 */

public class AddGameCore extends AddGame implements FirebasePaths {


    @Override
    protected void searchForGame(String value) {
        DatabaseReference gamesRef = app.getDatabaseGames();

        Query query = gamesRef.child("_competitive_").orderByChild("name").startAt(value).endAt(value+"\uf8ff").limitToFirst(10);
        getData(query);
         query = gamesRef.child("_coop_").orderByChild("name").startAt(value).endAt(value+"\uf8ff").limitToFirst(10);
        getData(query);
    }

    private void getData(Query query)
    {
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if(dataSnapshot.getValue() != null)
                {

                    Iterable<DataSnapshot> iterable = dataSnapshot.getChildren();

                    for (DataSnapshot shot : iterable)
                    {
                        addGame(shot.getKey(),shot);
                    }
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });

    }

    @Override
    protected void OnClickHolders(GameDetails gameDetails, View v) {

    }

    private void addGame(String key , DataSnapshot dataSnapshot) {

        String gameId =  key;
        String gameName = dataSnapshot.child("name").getValue().toString().trim();
        String gamPic = dataSnapshot.child("photo").getValue().toString().trim();
        String maxPlayerAsString =  dataSnapshot.child("max_player").getValue().toString().trim();
        int maxPlayer = Integer.parseInt(maxPlayerAsString);
        addGame(gameId,gameName,maxPlayer,gamPic);

    }

}