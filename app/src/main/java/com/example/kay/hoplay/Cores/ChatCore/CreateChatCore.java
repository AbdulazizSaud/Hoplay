package com.example.kay.hoplay.Cores.ChatCore;

import android.content.Intent;

import com.example.kay.hoplay.Cores.ParentCore.UserListCore;
import com.example.kay.hoplay.Models.FriendCommonModel;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ServerValue;

import java.util.HashMap;

/**
 * Created by Kay on 3/19/2017.
 */

public class CreateChatCore extends UserListCore {

    @Override
    protected void OnClickHolders(final FriendCommonModel model) {
        String myUID =  app.getAuth().getCurrentUser().getUid();
        String friendUID = model.getFriendKey();


        // create new chat
        //
        String roomKey = createPrivateChat(myUID,friendUID);
        Intent chatActivity = new Intent(getApplicationContext(), ChatCore.class);
        chatActivity.putExtra("room_key", roomKey);
        chatActivity.putExtra("room_type","_private_");
        chatActivity.putExtra("friend_key", model.getFriendKey());
        chatActivity.putExtra("friend_username", model.getFriendUsername());
        chatActivity.putExtra("friend_picture", model.getUserPictureURL());

        finish();

        startActivity(chatActivity);


    }






    private String createPrivateChat(String UID ,String friendUID)
    {
        //app.getDatabasChat().child("_private_");

        // path --> /Chat/_private_
        DatabaseReference refPrivate = app.getFirebaseDatabase().getReferenceFromUrl(FB_PRIVATE_CHAT_PATH);
        String key  = refPrivate.push().getKey();

        // path --> /Chat/_private/[KEY]
        DatabaseReference chatRoom = refPrivate.child(key);

        // path --> /Chat/_private/[KEY]/_details_
        DatabaseReference roomInfo =   chatRoom.child(FIREBASE_DETAILS_ATTR);

        // path --> /Chat/_private/[KEY]/_details_/_users_
        DatabaseReference roomUsers = roomInfo.child(FIREBASE_USERS_LIST_ATTR);

        roomUsers.child(UID).setValue(UID);
        roomUsers.child(friendUID).setValue(friendUID);

        String accessKey = roomInfo.push().getKey();
        roomInfo.child("access_key").setValue(accessKey);

        // path --> /Chat/_private/[KEY]/_messages_
        DatabaseReference messagesRef =   chatRoom.child(FIREBASE_CHAT_MESSAGES);
        HashMap<String,Object> messageMap = new HashMap<>();

        messageMap.put("_message_","hello");
        messageMap.put("_username_","test");
        messageMap.put("_time_stamp_",ServerValue.TIMESTAMP);
        messageMap.put("_counter_",0);
        messagesRef.child("_last_message_").setValue(messageMap);


        // Set Referance for users

        // path --> /_users_info_/[UID]/_chat_refs_/_private_

        String privateChatPath = FB_USERS_PATH+UID+"/"+FIREBASE_USER_PRIVATE_CHAT;
        DatabaseReference refUsePrivaterChats = app.getFirebaseDatabase().getReferenceFromUrl(privateChatPath);
        refUsePrivaterChats.child(key).setValue("");
        refUsePrivaterChats.child(key).child("_counter_").setValue(0);
        refUsePrivaterChats.child(key).child("_opponent_ID_").setValue(friendUID);



        // Pending

        // path --> /Chat/_pending_chat_/[FRIEND_KEY]/_private_/[UID]
        DatabaseReference refPendingChat = app.getDatabasChat().child(FIREBASE_PENDING_CHAT_ATTR).child(friendUID).child(FIREBASE_PRIVATE_ATTR).child(UID);
        refPendingChat.child("chat_id").setValue(key);
        refPendingChat.child("access_key").setValue(accessKey);

        return key;
    }

    @Override
    protected void onStartActivity() {

        loadFriendList();
    }
}
