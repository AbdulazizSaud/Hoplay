package com.example.kay.hoplay.CommunityComponents;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;

import com.example.kay.hoplay.Chat.ChatCore;
import com.example.kay.hoplay.Interfaces.FirebasePaths;
import com.example.kay.hoplay.Models.ChildEventListenerModel;
import com.example.kay.hoplay.Models.CommunityChatModel;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.Iterator;

/**
 * Created by Kay on 2/12/2017.
 */

public class CommunityCore extends Community implements FirebasePaths {


    private DatabaseReference refAuthUserChats;
    private DatabaseReference refPrivateChat;
    private DatabaseReference refPrivateChatUsersInfo;
    private DatabaseReference refUserInfo;




    private ChildEventListener privateChatListener = new ChildEventListener() {
        @Override
        public void onChildAdded(DataSnapshot chatRef, String s) {

            // on first time
            refPrivateChat = app.getDatabasChat().child(FIREBASE_PRIVATE_ATTR).child(chatRef.getKey());
            refPrivateChatUsersInfo = refPrivateChat.child(FIREBASE_DETAILS_ATTR).child(FIREBASE_USERS_LIST_ATTR);

            refPrivateChatUsersInfo.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot usersSnap) {
                    Iterable<DataSnapshot> users = usersSnap.getChildren();

                    for(DataSnapshot user : users)
                    {
                        refUserInfo = app.getDatabaseUsers().child(user.getKey()).child(FIREBASE_DETAILS_ATTR);
                        refUserInfo.addListenerForSingleValueEvent(retreiveUserInfoListener);
                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });


        }

        @Override
        public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                // each time it's added a new thing
        }

        @Override
        public void onChildRemoved(DataSnapshot dataSnapshot) {
                // when ref removed

        }

        @Override
        public void onChildMoved(DataSnapshot dataSnapshot, String s) {

        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };


    private ValueEventListener retreiveUserInfoListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
//
            String userId =  dataSnapshot.getRef().getParent().getKey();
            if(!userId.equals(app.getUserInformation().getUID()))
            {

            }
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };
    // chunks var
    String userKey;

    @Override
    protected void OnClickHolders(CommunityChatModel model, View v) {
        Intent i = new Intent(v.getContext(), ChatCore.class);
        i.putExtra("room_key", model.getChatKey());
        i.putExtra("room_type", model.getChatType());
        i.putExtra("friend_username", model.getChatName());
        i.putExtra("friend_picture", model.getUserPictureURL());

        v.getContext().startActivity(i);
    }

    @Override
    protected void OnStartActivity() {

        // bug error on creation right here
        refAuthUserChats = app.getDatabaseUsers().child(app.getUserInformation().getUID()).child(FIREBASE_USER_CHAT_REFERENCES);
        loadChats();

    }

    private void loadChats() {
        loadPrivatePendingChats();
        loadPrivateChat();
    }


    private void loadPrivatePendingChats() {


    }

    private void loadPrivateChat()
    {
        refAuthUserChats.child(FIREBASE_PRIVATE_ATTR).addChildEventListener(privateChatListener);
    }






    private void addUserChatToList(String chatKey, String chatType, String username, String pictureURL, String lastMessage, long messageNumber) {

        CommunityChatModel communityUserList = new CommunityChatModel();
        communityUserList.setChatKey(chatKey);
        communityUserList.setChatType(chatType);
        communityUserList.setChatName(username);
        communityUserList.setUserPictureURL(pictureURL);
        communityUserList.setLastMsg(lastMessage);
        communityUserList.setChatCounter(messageNumber);
        addToList(communityUserList);
    }


}
