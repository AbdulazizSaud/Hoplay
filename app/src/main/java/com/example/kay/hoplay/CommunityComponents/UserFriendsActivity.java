package com.example.kay.hoplay.CommunityComponents;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.kay.hoplay.Chat.ChatActivity;
import com.example.kay.hoplay.Interfaces.FirebasePaths;
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
import java.util.Random;

public class UserFriendsActivity extends UserFriends implements FirebasePaths{


    private FriendCommonModel friendCommonModel;

    @Override
    protected void OnClickHolders(FriendCommonModel model) {
        Log.i("---------->", model.getKey());
        String UID =  app.getAuth().getCurrentUser().getUid();
        String room_key =  createPrivateChat(UID,model.getKey(),model.getPictureUrl(),model.getUsername());

        Intent chatActivity = new Intent(this, ChatActivity.class);


        chatActivity.putExtra("room_key",room_key);
        chatActivity.putExtra("friend_key",model.getKey());
        chatActivity.putExtra("friend_username",model.getUsername());
        chatActivity.putExtra("friend_picture",model.getPictureUrl());

        startActivity(chatActivity);
        finish();

    }


    private String createPrivateChat(String UID, String friendKey,String picture,String username)
    {

        //app.getDatabasChat().child("_private_");

        // path --> /Chat/_private_
        DatabaseReference refPrivate = app.getFirebaseDatabase().getReferenceFromUrl(FB_PRIVATE_PATH);
        String key  = refPrivate.push().getKey();

        // path --> /Chat/_private/[KEY]
        DatabaseReference chatRoom = refPrivate.child(key);

        // path --> /Chat/_private/[KEY]/_details_
        DatabaseReference roomInfo =   chatRoom.child(FIREBASE_DETAILS_ATTR);

        // path --> /Chat/_private/[KEY]/_details_/_users_
        DatabaseReference roomUsers = roomInfo.child(FIREBASE_USERS_LIST_ATTR);

        roomUsers.child(UID).setValue(UID);
        roomUsers.child(friendKey).setValue(friendKey);

        String accessKey = roomInfo.push().getKey();
        roomInfo.child("access_key").setValue(accessKey);

        // path --> /Chat/_private/[KEY]/_messages_
        DatabaseReference messages =   chatRoom.child(FIREBASE_CHAT_MESSAGES);
        messages.child("Test Message").child("_username_").setValue("test");
        messages.child("Test Message").child("_messages_").setValue("test");



        // Set Referance

        // path --> /_users_info_/[UID]/_chat_refs_/_private_

        String privateChatPath = FB_USERS_PATH+UID+"/"+FIREBASE_USER_PRIVATE_CHAT;
        DatabaseReference refUsePrivaterChats = app.getFirebaseDatabase().getReferenceFromUrl(privateChatPath);
        refUsePrivaterChats.child(key).setValue(accessKey);


        // Pending

        // path --> /Chat/_pending_chat_/[FRIEND_KEY]/_private_/[UID]
        DatabaseReference refPendingChat = app.getDatabasChat().child(FIREBASE_PENDING_CHAT_ATTR).child(friendKey).child(FIREBASE_PRIVATE_ATTR).child(UID);
        refPendingChat.child("chat_id").setValue(key);
        refPendingChat.child("access_key").setValue(accessKey);

        return key;
    }



    @Override
    public void loadFriendList()
    {
        final String UID =  app.getAuth().getCurrentUser().getUid();


        final DatabaseReference usersData = app.getDatabaseUsers();
        DatabaseReference friendList = usersData.child(UID).child(FIREBASE_FRIENDS_LIST_ATTR);

        friendList.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(final DataSnapshot rootSnapshots, String s) {


                usersData.child(rootSnapshots.getKey()).child(FIREBASE_DETAILS_ATTR).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        friendCommonModel= new FriendCommonModel();
                        friendCommonModel.setKey(rootSnapshots.getKey());
                        friendCommonModel.setUserPictureURL(dataSnapshot.child("_picUrl_").getValue().toString());
                        friendCommonModel.setUsername(dataSnapshot.child("_username_").getValue().toString());
                        addToList(friendCommonModel);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });


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
