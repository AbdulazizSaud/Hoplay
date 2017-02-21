package com.example.kay.hoplay.CommunityComponents;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.kay.hoplay.PatternStrategyComponents.PattrenStrategy;
import com.example.kay.hoplay.R;
import com.example.kay.hoplay.model.FriendCommonModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.Iterator;

public class UserFriendsActivity extends UserFriends {


    @Override
    protected void OnClickHolders() {


    }


    @Override
    public void loadFriendList() {

        final String uid = app.getAuth().getCurrentUser().getUid();


        DatabaseReference user = app.getDatabaseUsers().child(uid);

        user.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild("_friends_list_"))
                {
                    Iterator iterator = dataSnapshot.child("_friends_list_").getChildren().iterator();

                    String friend_key,friend_username,friend_picture;
                    FriendCommonModel friendCommonModel = new FriendCommonModel();
                    while(iterator.hasNext()){
                       friend_key =  ((DataSnapshot) iterator.next()).getKey();
                        friend_username =  (String)((DataSnapshot) iterator.next()).getValue();
                        Log.i("------------->",friend_key+"  "+"" + friend_username);
                        //addToList();
                    }
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
}
