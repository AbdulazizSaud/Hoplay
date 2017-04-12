package com.example.kay.hoplay.Cores.ChatCore;

import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.example.kay.hoplay.Cores.ParentCore.UserListCore;
import com.example.kay.hoplay.Models.FriendCommonModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.StringTokenizer;

import static android.R.attr.key;
import static android.R.attr.mode;
import static android.R.attr.value;

/**
 * Created by Kay on 3/19/2017.
 */

public class CreateChatCore extends UserListCore {


    @Override
    protected void OnClickHolders(final FriendCommonModel model) {
        String currentUserId = app.getAuth().getCurrentUser().getUid();


        checkChatExist(model, currentUserId);


    }

    private void jumpToPrivateChat(FriendCommonModel model, String roomKey) {
        Intent chatActivity = new Intent(getApplicationContext(), ChatCore.class);
        chatActivity.putExtra("room_key", roomKey);
        chatActivity.putExtra("room_type", "_private_");
        chatActivity.putExtra("friend_key", model.getFriendKey());
        chatActivity.putExtra("friend_username", model.getFriendUsername());
        chatActivity.putExtra("friend_picture", model.getUserPictureURL());
        finish();
        startActivity(chatActivity);
    }


    private String createFirebasePrivateChat(String UID, String friendUID) {
        //app.getDatabasChat().child("_private_");

        // path --> /Chat/_private_
        DatabaseReference refPrivate = app.getFirebaseDatabase().getReferenceFromUrl(FB_PRIVATE_CHAT_PATH);
        String key = refPrivate.push().getKey();

        // path --> /Chat/_private/[KEY]
        DatabaseReference chatRoom = refPrivate.child(key);

        // path --> /Chat/_private/[KEY]/_details_
        DatabaseReference roomInfo = chatRoom.child(FIREBASE_DETAILS_ATTR);

        // path --> /Chat/_private/[KEY]/_details_/_users_
        DatabaseReference roomUsers = roomInfo.child(FIREBASE_USERS_LIST_ATTR);

        roomUsers.child(UID).setValue(UID);
        roomUsers.child(friendUID).setValue(friendUID);

        String accessKey = roomInfo.push().getKey();
        roomInfo.child("access_key").setValue(accessKey);

        // path --> /Chat/_private/[KEY]/_messages_
        DatabaseReference messagesRef = chatRoom.child(FIREBASE_CHAT_MESSAGES);
        HashMap<String, Object> messageMap = new HashMap<>();

        messageMap.put("_message_", "hello");
        messageMap.put("_username_", "test");
        messageMap.put("_time_stamp_", ServerValue.TIMESTAMP);
        messageMap.put("_counter_", 0);
        messagesRef.child("_last_message_").setValue(messageMap);


        // Set Referance for users

        // path --> /_users_info_/[UID]/_chat_refs_/_private_

        String privateChatPath = UID + "/" + FIREBASE_USER_PRIVATE_CHAT;
        DatabaseReference refUsePrivaterChats = app.getDatabaseUsers().child(privateChatPath);
        refUsePrivaterChats.child(key).child(FIREBASE_COUNTER_PATH).setValue(0);
        refUsePrivaterChats.child(key).child(FIREBASE_OPPONENT_ID_PATH).setValue(friendUID);


        // Pending

        // path --> /Chat/_pending_chat_/[FRIEND_KEY]/_private_/[UID]
        DatabaseReference refPendingChat = app.getDatabasChat().child(FIREBASE_PENDING_CHAT_ATTR).child(friendUID).child(FIREBASE_PRIVATE_ATTR).child(UID);
        refPendingChat.child("chat_id").setValue(key);
        refPendingChat.child("access_key").setValue(accessKey);

        return key;
    }


    private void checkChatExist(final FriendCommonModel model, final String currentUserId) {
        String opponentKey = model.getFriendKey();
        String privateChatPath = currentUserId + "/" + FIREBASE_USER_PRIVATE_CHAT;

        DatabaseReference chatRef = app.getDatabaseUsers().child(privateChatPath);
        final Query query = chatRef.orderByChild(FIREBASE_OPPONENT_ID_PATH).startAt(opponentKey).endAt(opponentKey + "\uf8ff").limitToFirst(1);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String roomKey ;
                if (dataSnapshot.getValue() == null) {
                     roomKey = createFirebasePrivateChat(currentUserId, model.getFriendKey());
                    jumpToPrivateChat(model,roomKey);
                }
                else
                {
                    Iterable<DataSnapshot> chatRoom = dataSnapshot.getChildren();
                    for(DataSnapshot data : chatRoom)
                    jumpToPrivateChat(model,data.getKey());
                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    @Override
    protected void onStartActivity() {

        loadFriendList();
    }
}
