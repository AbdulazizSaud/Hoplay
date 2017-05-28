package com.example.kay.hoplay.Cores.ChatCore;

import android.content.Intent;

import com.example.kay.hoplay.Cores.ParentCore.UserListCore;
import com.example.kay.hoplay.Models.FriendCommonModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;


public class FindUserCore extends UserListCore {


    private CreateChat createChat;

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


    private void checkChatExist(final FriendCommonModel model, final String currentUserId) {
        String opponentKey = model.getFriendKey();
        String privateChatPath = currentUserId + "/" + FIREBASE_USER_PRIVATE_CHAT;

        DatabaseReference chatRef = app.getDatabaseUsersInfo().child(privateChatPath);
        final Query query = chatRef.orderByChild(FIREBASE_OPPONENT_ID_PATH).startAt(opponentKey).endAt(opponentKey + "\uf8ff").limitToFirst(1);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String roomKey;
                if (dataSnapshot.getValue() == null) {
                    roomKey = createChat.createPrivateFirebaseChat(currentUserId, model.getFriendKey());
                    jumpToPrivateChat(model, roomKey);
                } else {
                    Iterable<DataSnapshot> chatRoom = dataSnapshot.getChildren();
                    for (DataSnapshot data : chatRoom)
                        jumpToPrivateChat(model, data.getKey());
                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    @Override
    protected void onStartActivity() {
        createChat = new CreateChat();
        loadFriendList();
    }
}
