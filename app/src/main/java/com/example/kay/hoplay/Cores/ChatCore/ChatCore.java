package com.example.kay.hoplay.Cores.ChatCore;

import android.content.Intent;

import com.example.kay.hoplay.CoresAbstract.ChatAbstracts.Chat;
import com.example.kay.hoplay.Interfaces.FirebasePaths;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;


/**
 * Created by Kay on 10/30/2016.
 * this class implemetns a chat activity
 */

public class ChatCore extends Chat implements FirebasePaths {

    private String chatRoomKey = null;
    private String friendUsername = null, friendPictureURL = null, chatRoomType = null;
    private long lastMessageCounter;
    private DatabaseReference refRoom, refMessages;


    private ChildEventListener messagesPacketsListener = new ChildEventListener() {
        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
            addMessage(dataSnapshot);

        }

        @Override
        public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            addMessage(dataSnapshot);

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
    };
    private ValueEventListener counterListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            if (dataSnapshot.getValue() == null)
                return;

            final String UID = app.getUserInformation().getUID();
            lastMessageCounter = Long.valueOf(dataSnapshot.getValue().toString().trim());
            String userTypeChat = (chatRoomType.equals(FIREBASE_PRIVATE_ATTR))
                    ? FIREBASE_USER_PRIVATE_CHAT : FIREBASE_USER_PUBLIC_CHAT;

            app.getDatabaseUsersInfo().child(UID).child(userTypeChat).child(chatRoomKey).child(FIREBASE_COUNTER_PATH).setValue(lastMessageCounter);

        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };


    // set up chat app
    @Override
    public void setupChat()
    {
        //load message
        Intent i = getIntent();
        chatRoomKey = i.getStringExtra("room_key");
        chatRoomType = i.getStringExtra("room_type");
        friendUsername = i.getStringExtra("friend_username");
        friendPictureURL = i.getStringExtra("friend_picture");

        String pathChatRoomType = (chatRoomType.equals(FIREBASE_PRIVATE_ATTR)) ? FB_PRIVATE_CHAT_PATH:FB_PUBLIC_CHAT_PATH;

        refRoom = app.getFirebaseDatabase().getReferenceFromUrl(pathChatRoomType + chatRoomKey);
        refMessages = refRoom.child("_messages_");
        setRoomDetails(friendUsername, friendPictureURL);
        loadMessages();
    }


    public void loadMessages() {

        refMessages.child("_last_message_/_counter_").addValueEventListener(counterListener);
        refMessages.child("_packets_").addChildEventListener(messagesPacketsListener);
    }


    private void addMessage(DataSnapshot dataSnapshot)
    {

        if (isEmpty(dataSnapshot))
            return;


        String chatKey = dataSnapshot.child("_message_key_").getValue().toString().trim();
        String username = dataSnapshot.child("_username_").getValue().toString().trim();
        String message = dataSnapshot.child("_message_").getValue().toString().trim();
        boolean isYou = username.equals(app.getUserInformation().getUID());

        addMessage(chatKey,username, message, isYou);

    }

    @Override
    protected void sendMessageToFirebase(String messageText) {

        if (sendMessage(messageText)) {

            String messageKey = refMessages.push().getKey();

            HashMap<String,Object> map = new HashMap<>();
            map.put("_message_key_",messageKey);
            map.put("_message_",messageText);
            map.put("_username_",app.getUserInformation().getUID());
            map.put("_time_stamp_",ServerValue.TIMESTAMP);
            map.put("_counter_",++lastMessageCounter);

            DatabaseReference messageRef = refMessages.child("_packets_").child(messageKey);
            messageRef.setValue(map);
            DatabaseReference lastMessageRef = refMessages.child("_last_message_");
            lastMessageRef.setValue(map);
        }
    }


    private boolean isEmpty(DataSnapshot dataSnapshot) {
        return dataSnapshot.child("_username_").getValue() == null || dataSnapshot.child("_message_").getValue() == null;
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        refMessages.child("_packets_").removeEventListener(messagesPacketsListener);
        refMessages.child("_last_message_/_counter_").removeEventListener(counterListener);

    }
}
