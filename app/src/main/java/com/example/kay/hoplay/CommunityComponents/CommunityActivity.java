package com.example.kay.hoplay.CommunityComponents;

import android.content.Intent;
import android.databinding.tool.util.L;
import android.util.Log;
import android.view.View;

import com.example.kay.hoplay.Chat.ChatActivity;
import com.example.kay.hoplay.Interfaces.FirebasePaths;
import com.example.kay.hoplay.model.CommunityUserList;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by Kay on 2/12/2017.
 */

public class CommunityActivity extends CommunityFragment implements FirebasePaths {


    private String uid;
    private DatabaseReference refAuthUserChats;
    private ArrayList<String> roomKeys;

    @Override
    protected void OnClickHolders(CommunityUserList model, View v) {
        Intent i = new Intent(v.getContext(), ChatActivity.class);
        i.putExtra("room_key",model.getChatKey());
        i.putExtra("friend_username", model.getFullName());
        i.putExtra("friend_picture",model.getUserPictureURL());

        v.getContext().startActivity(i);
    }

    @Override
    protected void OnStartActivity() {
        uid =  app.getAuth().getCurrentUser().getUid();
        refAuthUserChats = app.getDatabaseUsers().child(uid).child(FIREBASE_USER_CHAT_REFERENCES);

        loadChats();

    }

    private void loadChats()
    {
        loadPrivatePendingChats();
        loadPrivateChat();
    }

    private void loadPrivateChat()
    {

        // path -->
//        refAuthUserChats.child(FIREBASE_PRIVATE_ATTR).addChildEventListener(new ChildEventListener() {
//            @Override
//            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
//
//                CommunityUserList communityUserList = new CommunityUserList();
//                communityUserList.setChatKey(dataSnapshot.getKey());
//                addToList(communityUserList);
//            }
//
//            @Override
//            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
//
//            }
//
//            @Override
//            public void onChildRemoved(DataSnapshot dataSnapshot) {
//
//            }
//
//            @Override
//            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
//
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });
    }



    private void loadPrivatePendingChats()
    {
        final DatabaseReference refPendingChat = app.getDatabasChat().child(FIREBASE_PENDING_CHAT_ATTR).child(uid).child(FIREBASE_PRIVATE_ATTR);

        refPendingChat.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                String chatId = dataSnapshot.child("chat_id").getValue().toString();
                refAuthUserChats.child(FIREBASE_PRIVATE_ATTR).child(chatId).setValue(chatId);
                refPendingChat.child(dataSnapshot.getKey()).removeValue();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

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
        });
    }


}
