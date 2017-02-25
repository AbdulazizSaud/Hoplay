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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

public class UserFriendsActivity extends UserFriends {



    @Override
    protected void OnClickHolders(FriendCommonModel model) {
        Log.i("---------->", model.getKey());
         String uid = app.getAuth().getCurrentUser().getUid();
        createChat(uid,model.getKey());
    }



    private void createChat(final String userKey , final String friendKey)
    {

        final DatabaseReference refPrivate =  app.getDatabasChat().child("private");


        String roomKey = refPrivate.push().getKey();

        DatabaseReference messagesRef =   refPrivate.child(friendKey).child("Messages");

        String messageKey  = messagesRef.push().getKey();
        messagesRef.child(messageKey).child("username").setValue("test");
        messagesRef.child(messageKey).child("message").setValue("test");



        DatabaseReference user =  app.getDatabaseUsers().child(userKey);
        user.child("_chat_refs_").child("_private_").child(friendKey).setValue(friendKey);

        DatabaseReference pendingChat = app.getDatabaseRoot().child("PendingChat");
        pendingChat.child(friendKey).child("private").child(userKey).setValue("private");

    }


    @Override
    public void loadFriendList() {

        final String uid = app.getAuth().getCurrentUser().getUid();


        DatabaseReference usersData = app.getDatabaseUsers();
        DatabaseReference user = usersData.child(uid);

        user.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterator i = dataSnapshot.child("_friends_list_").getChildren().iterator();
                FriendCommonModel friendCommonModel;
                Object data;
                String friend_key, friend_username, friend_picture_url = "default";
                while (i.hasNext()) {
                    friendCommonModel = new FriendCommonModel();
                    data = i.next();

                    DataSnapshot shot = (DataSnapshot) data;

                    friend_key = shot.getKey();
                    friend_username = (String)shot.getValue();

                    friendCommonModel.setKey(friend_key);
                    friendCommonModel.setUsername(friend_username);

                    addToList(friendCommonModel);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
}
