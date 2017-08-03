package com.example.kay.hoplay.Cores.ChatCore;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import com.example.kay.hoplay.Cores.ForgetPasswordCore;
import com.example.kay.hoplay.Cores.UserProfileCores.ViewFriendProfileCore;
import com.example.kay.hoplay.CoresAbstract.ChatAbstracts.Chat;
import com.example.kay.hoplay.Interfaces.FirebasePaths;
import com.example.kay.hoplay.Models.PlayerModel;
import com.example.kay.hoplay.R;
import com.example.kay.hoplay.Services.CallbackHandlerCondition;
import com.example.kay.hoplay.Services.HandlerCondition;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Random;


/**
 * Created by Kay on 10/30/2016.
 * this class implemetns a chat activity
 */

public class ChatCore extends Chat implements FirebasePaths {

    private String chatRoomKey = null;
    private String roomName = null, roomPictureUrl = null, chatRoomType = null;
    private long lastMessageCounter;
    private boolean usersLoaded = false;
    private DatabaseReference refRoom, refMessages;
    private String opponentId;
    private boolean cutLoob = false;


    // Joiner username
    private String joinerUsername;

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
    public void setupChat() {

        //load message
        Intent i = getIntent();
        chatRoomKey = i.getStringExtra("room_key");
        chatRoomType = i.getStringExtra("room_type");
        roomName = i.getStringExtra("room_name");
        roomPictureUrl = i.getStringExtra("room_picture");

        final boolean isPrivate = chatRoomType.equals(FIREBASE_PRIVATE_ATTR);
        final String currentUID = app.getUserInformation().getUID();
        String pathChatRoomType;

        if (isPrivate) {
            pathChatRoomType = FB_PRIVATE_CHAT_PATH;
            opponentId = i.getStringExtra("opponent_key");
        } else {
            pathChatRoomType = FB_PUBLIC_CHAT_PATH;
            opponentId = null;
        }

        refRoom = app.getFirebaseDatabase().getReferenceFromUrl(pathChatRoomType + chatRoomKey);
        refMessages = refRoom.child("_messages_");






        // here will be to procedure  in two condition : private, public
        // in case private : it will check the type than it will escape the current user till it find a bio of oppsite user and added to bio of the chat
        // in case public : it will check the type than it will add all user in chat in bio of the chat
        refRoom.child(FIREBASE_CHAT_USERS_LIST_PATH).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                for (final DataSnapshot users : dataSnapshot.getChildren()) {

                    if (isPrivate && currentUID.equals(users.getKey()))
                        continue;
                    if (isPrivate && cutLoob)
                        break;

                    app.getDatabaseUsersInfo().child(users.getKey() + "/" + FIREBASE_DETAILS_ATTR)
                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {

                                    String username = dataSnapshot.child(FIREBASE_USERNAME_ATTR).getValue(String.class);

                                    playerOnChat.put(users.getKey(), new PlayerModel(users.getKey(), username));

                                    if (!isPrivate) {
                                        addUserToSubtitle(username);
                                    } else {
                                        String bio = dataSnapshot.child(FIREBASE_BIO_ATTR).getValue(String.class);
                                        addUserToSubtitle(bio);
                                        cutLoob = true;
                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });


                }
                usersLoaded = true;
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        // make delay till the users is loaded
        CallbackHandlerCondition callbackHandlerCondition = new CallbackHandlerCondition() {
            @Override
            public boolean callBack() {
                if (usersLoaded) {
                    setRoomDetails(roomName, roomPictureUrl);
                    loadMessages();
                }
                return usersLoaded;
            }
        };
        new HandlerCondition(callbackHandlerCondition, 0);

    }


    public void loadMessages() {

        refMessages.child("_last_message_/_counter_").addValueEventListener(counterListener);
        refMessages.child("_packets_").addChildEventListener(messagesPacketsListener);
    }


    private void addMessage(DataSnapshot dataSnapshot) {

        if (isEmpty(dataSnapshot))
            return;


        String chatKey = dataSnapshot.child("_message_key_").getValue(String.class);
        String senderId = dataSnapshot.child("_username_").getValue(String.class);
        String message = dataSnapshot.child("_message_").getValue(String.class);
        long timestamp = dataSnapshot.child("_time_stamp_").getValue(long.class);
        String senderUsername = "Someone";
        boolean isYou = senderId.equals(app.getUserInformation().getUID());

        PlayerModel playerModel = playerOnChat.get(senderId);

        if (playerModel != null)
            senderUsername = playerModel.getUsername();

        addMessage(chatKey, senderId, senderUsername, message,timestamp, isYou);

    }

    @Override
    protected void sendMessageToFirebase(String messageText) {

        if (sendMessage(messageText)) {

            String messageKey = refMessages.push().getKey();

            HashMap<String, Object> map = new HashMap<>();
            map.put("_message_key_", messageKey);
            map.put("_message_", messageText);
            map.put("_username_", app.getUserInformation().getUID());
            map.put("_time_stamp_", ServerValue.TIMESTAMP);
            map.put("_counter_", ++lastMessageCounter);

            DatabaseReference messageRef = refMessages.child("_packets_").child(messageKey);
            messageRef.setValue(map);
            DatabaseReference lastMessageRef = refMessages.child("_last_message_");
            lastMessageRef.setValue(map);
        }
    }

    @Override
    protected void viewPorfileProccess() {
        if (opponentId == null)
            return;

        Intent i = new Intent(this, ViewFriendProfileCore.class);
        i.putExtra("user_key", opponentId);
        startActivity(i);

    }

    @Override
    protected void viewLobbyProccess() {
        Intent intent = new Intent();
        intent.putExtra("result", "lobby");
        setResult(RESULT_OK, intent);
        finish();
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




    private String getJoinerUsername(String userKey){

        app.getDatabaseUsersInfo().child(userKey).child(FIREBASE_DETAILS_ATTR).child(FIREBASE_USERNAME_ATTR).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                    joinerUsername = dataSnapshot.getValue().toString();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return joinerUsername;
    }

}
