package com.example.kay.hoplay.CommunityComponents;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.kay.hoplay.PatternStrategyComponents.PattrenStrategy;
import com.example.kay.hoplay.R;
import com.example.kay.hoplay.model.FriendCommonModel;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Iterator;

public class UserFriendsActivity extends UserFriends {


    @Override
    protected void OnClickHolders() {


    }


    @Override
    public void loadFriendList() {

        final String uid = app.getAuth().getCurrentUser().getUid();


        DatabaseReference user = app.getDatabaseUsers().child(uid);



        user.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
              Iterator i = dataSnapshot.child("_friends_list_").getChildren().iterator();

                while(i.hasNext()){
                    Log.i("------->",i.next() +" ");
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }
}
