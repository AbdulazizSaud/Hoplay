package com.example.kay.hoplay.Chat;

import android.content.Intent;

import com.example.kay.hoplay.App.App;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

import emojicon.emoji.Objects;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

/**
 * Created by Kay on 10/30/2016.
 * this class implemetns a chat activity
 */

public class ChatActivity extends Chat {
    private String chatRoomKey=null;

    private String uid,username;

    private String friendUsername=null,friendPicture=null,friendKey=null;
    DatabaseReference refRoom,refMessages;

    // set up chat app
    @Override
    public void setupChat() {
    //load message
        Intent i = getIntent();

        String uid =  app.getAuth().getCurrentUser().getUid();
        app.getDatabaseUsers().child(uid).child("_info_").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                username = (String)dataSnapshot.child("_username_").getValue();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        chatRoomKey = i.getStringExtra("room_key");
        friendUsername = i.getStringExtra("friend_username");
        friendPicture = i.getStringExtra("friend_picture");
        friendKey = i.getStringExtra("friend_key");

        refRoom = app.getDatabasChat().child(chatRoomKey);
        refMessages = refRoom.child("Messages");

    }

    @Override
    // this method for send message , execute only when a user click on send button
    protected void sendMessage(String message) {
        super.sendMessage(message);
        String message_key = refMessages.push().getKey();

        refMessages.child(message_key).child("username").setValue(uid);
        refMessages.child(message_key).child("message").setValue(message);

    }
    @Override
    // this method for receive message , execute only when a user receive a message
    protected void receiveMessage(Objects... args) {
        super.receiveMessage(args);
        //........
    }




}
