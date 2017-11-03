package com.hoplay.kay.hoplay.Cores.ChatCore;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hoplay.kay.hoplay.Cores.UserProfileCores.ViewFriendProfileCore;
import com.hoplay.kay.hoplay.CoresAbstract.ChatAbstracts.Chat;
import com.hoplay.kay.hoplay.Interfaces.FirebasePaths;
import com.hoplay.kay.hoplay.Models.ChatMessage;
import com.hoplay.kay.hoplay.Models.PlayerModel;
import com.hoplay.kay.hoplay.Services.CallbackHandlerCondition;
import com.hoplay.kay.hoplay.Services.HandlerCondition;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.hoplay.kay.hoplay.FirebaseControllers.MessagePack;

import java.util.HashMap;


/**
 * Created by Kay on 10/30/2016.
 * this class implemetns a chat activity
 */

public class ChatCore extends Chat implements FirebasePaths {

    private String chatRoomKey = null;
    private String roomName = null, roomPictureUrl = null, chatRoomType = null;
    private long lastMessageCounter;
    private DatabaseReference refRoom, refMessages;
    private boolean isPrivate;
    private String currentUID , opponentId;


    private SharedPreferences mPrefs;


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

    private ChildEventListener playerOnRoom = new ChildEventListener() {
        @Override
        public void onChildAdded(final DataSnapshot user, String s) {


            if (isPrivate && currentUID.equals(user.getKey()))
                return;

            setPlayerInfo(user);

        }

        @Override
        public void onChildChanged(DataSnapshot dataSnapshot, String s) {

        }

        @Override
        public void onChildRemoved(DataSnapshot dataSnapshot) {

            if (dataSnapshot.getKey().equals(currentUID))
                return;

            PlayerModel playerModel = playerOnChat.get(dataSnapshot.getKey());
            String message = roomName + ":" + playerModel.getUsername() + " Left the lobby";

            playerOnChat.remove(dataSnapshot.getKey());
            savePlayers();

            leaveMessage(playerModel.getUsername(), message);
            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();

            savePlayers();
        }

        @Override
        public void onChildMoved(DataSnapshot dataSnapshot, String s) {

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

        // Set the oponent name to use it in add friend method
        opponentName = roomName ;

        isPrivate = chatRoomType.equals(FIREBASE_PRIVATE_ATTR);
        currentUID = app.getUserInformation().getUID();
        String pathChatRoomType;

        if (isPrivate) {
            pathChatRoomType = FB_PRIVATE_CHAT_PATH;
            opponentId = i.getStringExtra("opponent_key");


            // Set proper menu after checking the opponent : is friend or not
            if (opponentId!=null)
                  checkIsFriend(opponentId);
            else
                checkIsFriend(opponentKey);


        } else {
            pathChatRoomType = FB_PUBLIC_CHAT_PATH;
            opponentId = null;
        }

         mPrefs = getSharedPreferences("PREFERENCE", Context.MODE_PRIVATE);

        playerOnChat = retreivePlayers();


        refRoom = app.getFirebaseDatabase().getReferenceFromUrl(pathChatRoomType + chatRoomKey);

        refMessages = refRoom.child("_messages_");

        // here will be to procedure  in two condition : private, public
        // in case private : it will check the type than it will escape the current user till it find a bio of oppsite user and added to bio of the chat
        // in case public : it will check the type than it will add all user in chat in bio of the chat

        refRoom.child(FIREBASE_CHAT_USERS_LIST_PATH).addChildEventListener(playerOnRoom);


        int cooldownTime = 0;

        if(playerOnChat == null) {
            playerOnChat = new HashMap<String, PlayerModel>();
            cooldownTime = 1000;
        }


        new HandlerCondition(new CallbackHandlerCondition() {
            @Override
            public boolean callBack() {
                loadMessages();
                return true;
            }
        },cooldownTime);


        setRoomDetails(roomName, roomPictureUrl);

    }


    public void loadMessages() {

        refMessages.child("_last_message_/counter").addValueEventListener(counterListener);
        refMessages.child("_packets_").addChildEventListener(messagesPacketsListener);
    }


    private void addMessage(DataSnapshot dataSnapshot) {


        if (isEmpty(dataSnapshot))
            return;


        ChatMessage message = dataSnapshot.getValue(ChatMessage.class);

        String senderId = message.getUsername();

        String senderUsername = "Someone";
        boolean isYou = senderId.equals(app.getUserInformation().getUID());

        message.setMe(isYou);

        PlayerModel playerModel = playerOnChat.get(senderId);


        if (playerModel != null)
            senderUsername = playerModel.getUsername();
        else if (message.isHotKeys())
            senderUsername = senderId;

        message.setUsername(senderUsername);
        addMessage(message);

    }

    @Override
    protected void sendMessageToFirebase(String messageText) {

        if (sendMessage(messageText)) {
            new MessagePack(refMessages, messageText, app.getUserInformation().getUID(), ++lastMessageCounter);
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

    @Override
    protected void checkIsFriend(final String opponentKey) {
        final DatabaseReference userRef = app.getDatabaseUsersInfo().child(app.getUserInformation().getUID());


        // now we check if the current user friend with this guy or not
        userRef.child(FIREBASE_FRIENDS_LIST_ATTR).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild(opponentKey))
                    isFriend = true;
                else
                    isFriend = false;


                isDone = true ;
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    protected void addThisUserAsFriend() {
        DatabaseReference userRef = app.getDatabaseUsersInfo().child(app.getUserInformation().getUID());

        // Add this user as a friend
        userRef.child(FIREBASE_FRIENDS_LIST_ATTR).child(opponentId).setValue(opponentId);

    }


    private boolean isEmpty(DataSnapshot dataSnapshot) {
        return dataSnapshot.child("username").getValue() == null || dataSnapshot.child("message").getValue() == null;
    }
    private void setPlayerInfo(final DataSnapshot user) {
        app.getDatabaseUsersInfo().child(user.getKey() + "/" + FIREBASE_DETAILS_ATTR)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        String username = dataSnapshot.child(FIREBASE_USERNAME_ATTR).getValue(String.class);


                        playerOnChat.put(user.getKey(), new PlayerModel(user.getKey(), username));
                        savePlayers();

                        if (!isPrivate) {
                            addUserToSubtitle(username);
                        } else {
                            String bio = dataSnapshot.child(FIREBASE_BIO_ATTR).getValue(String.class);
                            addUserToSubtitle(bio);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }
    private void savePlayers() {
        SharedPreferences.Editor prefsEditor = mPrefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(playerOnChat);
        prefsEditor.putString(chatRoomKey, json);
        prefsEditor.apply();
    }

    private HashMap<String, PlayerModel> retreivePlayers() {
        Gson gson = new Gson();
        String json = mPrefs.getString(chatRoomKey, "");

        HashMap<String, PlayerModel> playerModelHashMap = gson.fromJson(json, new TypeToken<HashMap<String, PlayerModel>>(){}.getType() ) ;


        return playerModelHashMap;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        refMessages.child("_packets_").removeEventListener(messagesPacketsListener);
        refMessages.child("_last_message_/_counter_").removeEventListener(counterListener);

    }





}
