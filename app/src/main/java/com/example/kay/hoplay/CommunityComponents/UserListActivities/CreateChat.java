package com.example.kay.hoplay.CommunityComponents.UserListActivities;

import android.content.Intent;

import com.example.kay.hoplay.Chat.ChatActivity;
import com.example.kay.hoplay.CommunityComponents.UserListActivity;
import com.example.kay.hoplay.PatternStrategyComponents.Startgies.FirebaseCreateChatRoom;
import com.example.kay.hoplay.Models.FriendCommonModel;

/**
 * Created by Kay on 3/19/2017.
 */

public class CreateChat extends UserListActivity {

    @Override
    protected void OnClickHolders(final FriendCommonModel model) {
        String myUID =  app.getAuth().getCurrentUser().getUid();
        String friendUID = model.getKey();
        FirebaseCreateChatRoom firebaseCreateChatRoom = new FirebaseCreateChatRoom(myUID, friendUID) {
            @Override
            public void afterCreateChat(String roomKey) {
                Intent chatActivity = new Intent(getApplicationContext(), ChatActivity.class);
                chatActivity.putExtra("room_key", roomKey);
                chatActivity.putExtra("friend_key", model.getKey());
                chatActivity.putExtra("friend_username", model.getUsername());
                chatActivity.putExtra("friend_picture", model.getPictureUrl());

                finish();

                startActivity(chatActivity);

            }
        };

    }

    @Override
    protected void onStartActivity() {

        loadFriendList();
    }
}
