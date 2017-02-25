package com.example.kay.hoplay.CommunityComponents;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.kay.hoplay.Chat.ChatActivity;
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

public class UserFriendsActivity extends UserFriends {



    @Override
    protected void OnClickHolders(FriendCommonModel model) {
        Log.i("---------->", model.getKey());
        String UID =  app.getAuth().getCurrentUser().getUid();
        String room_key =  createChat(UID,model.getKey());

        Intent chatActivity = new Intent(this, ChatActivity.class);

        chatActivity.putExtra("room_key",room_key);
        chatActivity.putExtra("friend_key",model.getKey());
        chatActivity.putExtra("friend_username",model.getUsername());
        chatActivity.putExtra("friend_picture",model.getPictureUrl());

        startActivity(chatActivity);

    }






    private String createChat(String UID, String friendKey)
    {

        DatabaseReference refPrivate =  app.getDatabasChat().child("private");
        String key  = refPrivate.push().getKey();

        DatabaseReference chatRoom = refPrivate.child(key);


        DatabaseReference roomInfo =   chatRoom.child("Details");

        roomInfo.child("Users").child(UID).setValue(UID);
        roomInfo.child("Users").child(friendKey).setValue(friendKey);


        String accessKey = roomInfo.push().getKey();
        roomInfo.child("access_key").setValue(accessKey);

        DatabaseReference messages =   chatRoom.child("Messages");
        messages.child("Test_Message_").child("Username").setValue("test");
        messages.child("Test_Message_").child("Message").setValue("test");



        // Set Referance
        DatabaseReference refChat = app.getDatabaseUsers().child(UID).child("_chat_refs_");
        refChat.child("_private_").child(key).setValue(accessKey);


        // Pending
        DatabaseReference refPendingChat = app.getDatabasChat().child("PendingChat");
        DatabaseReference pendingChat = refPendingChat.child(friendKey).child("private").child(UID);

        pendingChat.child("ChatID").setValue(key);
        pendingChat.child("access_key").setValue(accessKey);

        return key;
    }


    @Override
    public void loadFriendList()
    {

        String UID =  app.getAuth().getCurrentUser().getUid();
        DatabaseReference usersData = app.getDatabaseUsers();
        DatabaseReference user = usersData.child(UID).child("_friends_list_");

        user.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                Iterator i = dataSnapshot.getChildren().iterator();
                FriendCommonModel friendCommonModel;
                Object data;


                String friend_key, friend_username, friend_picture_url = "default";
                while (i.hasNext()) {

                    friendCommonModel = new FriendCommonModel();
                    data = i.next();
                    Log.i("--------->",data.toString());

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
