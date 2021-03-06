package com.example.kay.hoplay.Cores.ChatCore;

import android.content.Intent;
import android.util.Log;
import android.view.View;

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
        // users_info -> user key -> _firends_list_
        final DatabaseReference userFriendsListRef = app.getDatabaseUsersInfo().child(app.getUserInformation().getUID()).child(FIREBASE_FRIENDS_LIST_ATTR);

        // If you want to add directly without friend request : uncomment this codes
//        userFriendsListRef.child(model.getFriendKey());
//        Log.i("shithappens",userFriendsListRef+"");
    }

    private void jumpToPrivateChat(FriendCommonModel model, String roomKey) {
        Intent chatActivity = new Intent(getApplicationContext(), ChatCore.class);
        chatActivity.putExtra("room_key", roomKey);
        chatActivity.putExtra("room_type", FIREBASE_PRIVATE_ATTR);
        chatActivity.putExtra("room_name", model.getFriendUsername());
        chatActivity.putExtra("room_picture", model.getUserPictureURL());

        chatActivity.putExtra("friend_key", model.getFriendKey());

        finish();
        startActivity(chatActivity);
    }


    private void checkChatExist(final FriendCommonModel model, final String currentUserId) {
        final String opponentKey = model.getFriendKey();
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

    @Override
    protected void showFriendpopup(FriendCommonModel friendCommonModel) {

    }

    @Override
    protected void removeFriendAnimation(View viewToAnimate1, View viewToAnimate2, int position) {

    }


}
