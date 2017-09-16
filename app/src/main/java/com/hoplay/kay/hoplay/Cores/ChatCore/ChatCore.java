package com.hoplay.kay.hoplay.Cores.ChatCore;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.hoplay.kay.hoplay.Cores.ForgetPasswordCore;
import com.hoplay.kay.hoplay.Cores.UserProfileCores.ViewFriendProfileCore;
import com.hoplay.kay.hoplay.CoresAbstract.ChatAbstracts.Chat;
import com.hoplay.kay.hoplay.Interfaces.FirebasePaths;
import com.hoplay.kay.hoplay.Models.ChatMessage;
import com.hoplay.kay.hoplay.Models.PlayerModel;
import com.hoplay.kay.hoplay.R;
import com.hoplay.kay.hoplay.Services.CallbackHandlerCondition;
import com.hoplay.kay.hoplay.Services.HandlerCondition;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.hoplay.kay.hoplay.util.setMessagePack;

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
    private DatabaseReference refRoom, refMessages;
    private String opponentId;
    private boolean isPrivate;
    private String currentUID;





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

    private ChildEventListener playerOnRoom =  new ChildEventListener() {
        @Override
        public void onChildAdded(final DataSnapshot user, String s) {


               if (isPrivate && currentUID.equals(user.getKey()))
                        return;


            app.getDatabaseUsersInfo().child(user.getKey() + "/" + FIREBASE_DETAILS_ATTR)
                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {

                                    String username = dataSnapshot.child(FIREBASE_USERNAME_ATTR).getValue(String.class);

                                    playerOnChat.put(user.getKey(), new PlayerModel(user.getKey(), username));

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
//

        }

        @Override
        public void onChildChanged(DataSnapshot dataSnapshot, String s) {

        }

        @Override
        public void onChildRemoved(DataSnapshot dataSnapshot) {

            if(dataSnapshot.getKey().equals(currentUID))
                return;

            PlayerModel playerModel = playerOnChat.get(dataSnapshot.getKey());
            String message = roomName+":"+playerModel.getUsername()+" Left the lobby";

            playerOnChat.remove(dataSnapshot.getKey());
            leaveMessage(playerModel.getUsername(),message);
            Toast.makeText(getApplicationContext(),message,Toast.LENGTH_SHORT).show();
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

          isPrivate = chatRoomType.equals(FIREBASE_PRIVATE_ATTR);
          currentUID = app.getUserInformation().getUID();
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

        refRoom.child(FIREBASE_CHAT_USERS_LIST_PATH).addChildEventListener(playerOnRoom);
        loadMessages();
        setRoomDetails(roomName, roomPictureUrl);

    }


    public void loadMessages() {

        refMessages.child("_last_message_/counter").addValueEventListener(counterListener);
        refMessages.child("_packets_").addChildEventListener(messagesPacketsListener);
    }


    private void addMessage(DataSnapshot dataSnapshot) {



        if (isEmpty(dataSnapshot))
            return;


        ChatMessage message =dataSnapshot.getValue(ChatMessage.class);

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
            new setMessagePack(refMessages,messageText,app.getUserInformation().getUID(),++lastMessageCounter);
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
        return dataSnapshot.child("username").getValue() == null || dataSnapshot.child("message").getValue() == null;
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        refMessages.child("_packets_").removeEventListener(messagesPacketsListener);
        refMessages.child("_last_message_/_counter_").removeEventListener(counterListener);

    }






}
