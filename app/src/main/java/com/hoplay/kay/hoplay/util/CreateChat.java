package com.hoplay.kay.hoplay.util;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;

import com.hoplay.kay.hoplay.App.App;
import com.hoplay.kay.hoplay.Cores.ChatCore.ChatCore;
import com.hoplay.kay.hoplay.Interfaces.FirebasePaths;
import com.hoplay.kay.hoplay.Models.ChatMessage;
import com.hoplay.kay.hoplay.Models.FriendCommonModel;
import com.hoplay.kay.hoplay.Models.RequestModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;



public class CreateChat implements FirebasePaths {


    private App app;

    public CreateChat()
    {
        app= App.getInstance();
    }


    private String createPrivateFirebaseChat(String UID, String friendUID) {

        // path --> /Chat/_private_
        DatabaseReference refPrivate = app.getFirebaseDatabase().getReferenceFromUrl(FB_PRIVATE_CHAT_PATH);
        String key = refPrivate.push().getKey();

        // path --> /Chat/_private/[KEY]
        DatabaseReference chatRoom = refPrivate.child(key);

        // path --> /Chat/_private/[KEY]/_info_
        DatabaseReference roomInfo = chatRoom.child(FIREBASE_DETAILS_ATTR);

        // path --> /Chat/_private/[KEY]/_details_/_users_
        DatabaseReference roomUsers = roomInfo.child(FIREBASE_USERS_LIST_ATTR);

        roomUsers.child(UID).setValue(UID);
        roomUsers.child(friendUID).setValue(friendUID);

        String accessKey = roomInfo.push().getKey();
        roomInfo.child("access_key").setValue(accessKey);

        // path --> /Chat/_private/[KEY]/_messages_
        DatabaseReference messagesRef = chatRoom.child(FIREBASE_CHAT_MESSAGES);

        ChatMessage chatMessage = new ChatMessage();
        chatMessage.setId(key);
        chatMessage.setMessage("hello");
        chatMessage.setUsername("Null");
        chatMessage.setTimestamp(ServerValue.TIMESTAMP);
        chatMessage.setCounter(0);

        messagesRef.child("_last_message_").setValue(chatMessage);



        // Set Referance for users

        // path --> /_users_info_/[UID]/_chat_refs_/_private_

        String privateChatPath = UID + "/" + FIREBASE_USER_PRIVATE_CHAT;
        DatabaseReference refUsePrivaterChats = app.getDatabaseUsersInfo().child(privateChatPath);
        refUsePrivaterChats.child(key).child(FIREBASE_COUNTER_PATH).setValue(0);
        refUsePrivaterChats.child(key).child(FIREBASE_OPPONENT_ID_ATTR).setValue(friendUID);


        // Pending


        // path --> /Chat/_pending_chat_/[FRIEND_KEY]/_private_/[UID]
        DatabaseReference refPendingChat = app.getDatabasChat().child(FIREBASE_PENDING_CHAT_ATTR).child(friendUID).child(FIREBASE_PRIVATE_ATTR).child(UID);
        refPendingChat.child("chat_id").setValue(key);
        refPendingChat.child("access_key").setValue(accessKey);
        return key;
    }

    public String createPublicFirebaseChat(RequestModel requestModel) {

        String key = requestModel.getRequestId();
        String UID = requestModel.getAdmin();

        // path --> /Chat/_public_
        DatabaseReference refPublic = app.getFirebaseDatabase().getReferenceFromUrl(FB_PUBLIC_CHAT_PATH);

        // path --> /Chat/_public_/[KEY]
        DatabaseReference chatRoom = refPublic.child(key);

        // path --> /Chat/_public_/[KEY]/_info_
        DatabaseReference roomInfo = chatRoom.child(FIREBASE_DETAILS_ATTR);

        // path --> /Chat/_public_/[KEY]/_info_/_users_
        DatabaseReference roomUsers = roomInfo.child(FIREBASE_USERS_LIST_ATTR);
        roomUsers.child(UID).setValue(UID);

        String accessKey = roomInfo.push().getKey();
        roomInfo.child("access_key").setValue(accessKey);
        roomInfo.child("room_info/gameId").setValue(requestModel.getGameId());

        // path --> /Chat/_public_/[KEY]/_messages_
        DatabaseReference messagesRef = chatRoom.child(FIREBASE_CHAT_MESSAGES);


        ChatMessage chatMessage = new ChatMessage();
        chatMessage.setId(key);
        chatMessage.setMessage("hello");
        chatMessage.setUsername("Null");
        chatMessage.setTimestamp(ServerValue.TIMESTAMP);
        chatMessage.setCounter(0);

        messagesRef.child("_last_message_").setValue(chatMessage);



        // Set Referance for users

        // path --> /_users_info_/[UID]/_chat_refs_/_public_
        setValueUserRef(UID,key);
        return key;
    }


    public void setValueUserRef(String uid ,String roomKey)
    {
        String publicChatPath = uid + "/" + FIREBASE_USER_PUBLIC_CHAT;
        DatabaseReference refUserPublicChats = app.getDatabaseUsersInfo().child(publicChatPath);
        refUserPublicChats.child(roomKey).child(FIREBASE_COUNTER_PATH).setValue(0);
        refUserPublicChats.child(roomKey).child(FIREBASE_OPPONENT_ID_ATTR).setValue(roomKey);
    }

    public void setValueUsersChat(String type,String roomId,String userId)
    {
        DatabaseReference refChatRoom = app.getDatabasChat().child(type).child(roomId);
        DatabaseReference roomUsers = refChatRoom.child(FIREBASE_CHAT_USERS_LIST_PATH);
        roomUsers.child(userId).setValue(userId);
    }



    private void jumpToPrivateChat(Context c , FriendCommonModel model, String roomKey) {
        Intent chatActivity = new Intent(c, ChatCore.class);
        chatActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        chatActivity.putExtra("room_key", roomKey);
        chatActivity.putExtra("room_type", FIREBASE_PRIVATE_ATTR);
        chatActivity.putExtra("room_name", model.getFriendUsername());
        chatActivity.putExtra("room_picture", model.getUserPictureURL());

        chatActivity.putExtra("friend_key", model.getFriendKey());

        c.startActivity(chatActivity);
    }



    public void createPrivateChat(final Context activity,final FriendCommonModel model) {
        final String opponentKey = model.getFriendKey();
        final String  currentUserId = app.getUserInformation().getUID();
        String privateChatPath = currentUserId + "/" + FIREBASE_USER_PRIVATE_CHAT;


        DatabaseReference chatRef = app.getDatabaseUsersInfo().child(privateChatPath);
        final Query query = chatRef.orderByChild(FIREBASE_OPPONENT_ID_ATTR).startAt(opponentKey).endAt(opponentKey + "\uf8ff").limitToFirst(1);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String roomKey;
                if (dataSnapshot.getValue() == null) {
                    roomKey = createPrivateFirebaseChat(currentUserId, model.getFriendKey());


                    jumpToPrivateChat(activity,model, roomKey);
                } else {
                    Iterable<DataSnapshot> chatRoom = dataSnapshot.getChildren();
                    for (DataSnapshot data : chatRoom)
                        jumpToPrivateChat(activity,model, data.getKey());

                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
}
