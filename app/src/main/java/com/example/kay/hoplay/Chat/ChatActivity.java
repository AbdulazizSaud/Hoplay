package com.example.kay.hoplay.Chat;

import android.content.Intent;

import com.example.kay.hoplay.Interfaces.FirebasePaths;
import com.example.kay.hoplay.R;
import com.example.kay.hoplay.Models.ChildEventListenerModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;

import emojicon.emoji.Objects;


/**
 * Created by Kay on 10/30/2016.
 * this class implemetns a chat activity
 */

public class ChatActivity extends Chat implements FirebasePaths {

    private String chatRoomKey=null;
    private String friendUsername=null,friendPictureURL=null,friendKey=null;

    DatabaseReference refRoom,refMessages;
    // set up chat app
    @Override
    public void setupChat() {
    //load message
        Intent i = getIntent();
        chatRoomKey = i.getStringExtra("room_key");
        friendUsername = i.getStringExtra("friend_username");
        friendPictureURL = i.getStringExtra("friend_picture");
        refRoom = app.getFirebaseDatabase().getReferenceFromUrl(FB_PRIVATE_CHAT_PATH+chatRoomKey);
        refMessages = refRoom.child("_messages_");

        setupRoomDetails();
        loadMessage();


    }



    private void addMessageToChat(DataSnapshot dataSnapshot,boolean isAddChild)
    {
        if(isEmpty(dataSnapshot))
            return;
        String username = dataSnapshot.child("_username_").getValue().toString();
        String message = dataSnapshot.child("_message_").getValue().toString();

        boolean isYou = username.equals(app.getUserInformation().getUID());



            addMessage(username,message,isYou);
            //addMessage(username,message,true);

    }
    private void loadMessage() {


        refMessages.addChildEventListener(new ChildEventListenerModel() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                addMessageToChat(dataSnapshot,true);

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                addMessageToChat(dataSnapshot,false);

            }
        });

    }

    private boolean isEmpty(DataSnapshot dataSnapshot) {
        return dataSnapshot.child("_username_").getValue() == null || dataSnapshot.child("_message_").getValue() == null;
    }


    private void setupRoomDetails() {

        roomPicture.setImageResource(R.drawable.profile_default_photo);
        app.loadingImage(roomPicture,friendPictureURL);
        roomName.setText(friendUsername);

    }

    @Override
    // this method for send message , execute only when a user click on send button
    protected void sendMessage(String message) {
        super.sendMessage(message);
        String message_key = refMessages.push().getKey();

        refMessages.child(message_key).child("_username_").setValue(app.getUserInformation().getUID());
        refMessages.child(message_key).child("_message_").setValue(message);
        refMessages.child("_last_message_").setValue(message);

    }
    @Override
    // this method for receive message , execute only when a user receive a message
    protected void receiveMessage(Objects... args) {
        super.receiveMessage(args);
        //........
    }




}
