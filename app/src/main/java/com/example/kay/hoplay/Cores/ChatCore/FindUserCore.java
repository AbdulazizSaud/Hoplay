package com.example.kay.hoplay.Cores.ChatCore;

import android.content.Intent;
import android.view.View;

import com.example.kay.hoplay.Cores.UserProfileCores.ParentCore.UserListCore;
import com.example.kay.hoplay.Models.FriendCommonModel;
import com.example.kay.hoplay.util.CreateChat;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;


public class FindUserCore extends UserListCore {


    private CreateChat createChat;

    @Override
    protected void OnClickHolders(final FriendCommonModel model) {
        String currentUserId = app.getAuth().getCurrentUser().getUid();
        createChat.createPrivateChat(getApplicationContext(),model);

        // users_info -> user key -> _firends_list_
        final DatabaseReference userFriendsListRef = app.getDatabaseUsersInfo().child(app.getUserInformation().getUID()).child(FIREBASE_FRIENDS_LIST_ATTR);

       // Add friend to the friend list
        userFriendsListRef.child(model.getFriendKey()).setValue(model.getFriendKey());

    }





    @Override
    protected void onStartActivity() {
        createChat = new CreateChat();
        loadFriendList();

        // Make the no friends section invisible
        noFriendsMessage.setVisibility(View.GONE);
        addFriendsButton.setVisibility(View.GONE);
        noFriendsImageview.setVisibility(View.GONE);
    }

    @Override
    protected void createChat(FriendCommonModel friendCommonModel) {
        return;
    }

    @Override
    protected void removeFriend(String friendKey, int friendsNumber) {

    }

    @Override
    protected boolean OnLongClickHolders(FriendCommonModel model) {
        return false;
    }




}
