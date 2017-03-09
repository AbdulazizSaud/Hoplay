package com.example.kay.hoplay.CommunityComponents;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;

import com.example.kay.hoplay.Chat.ChatActivity;
import com.example.kay.hoplay.Interfaces.FirebasePaths;
import com.example.kay.hoplay.model.ChildEventListenerModel;
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
        refAuthUserChats = app.getDatabaseUsers().child(app.getUserInformation().getUID()).child(FIREBASE_USER_CHAT_REFERENCES);
        loadChats();

    }

    private void loadChats()
    {
        loadPrivatePendingChats();
         loadPrivateChat();

    }


    private void loadPrivatePendingChats()
    {



        String uid = app.getUserInformation().getUID();

        final DatabaseReference refPendingChat = app.getDatabasChat().child(FIREBASE_PENDING_CHAT_ATTR).child(uid).child(FIREBASE_PRIVATE_ATTR);

        refPendingChat.addChildEventListener(new ChildEventListenerModel() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                String chatId = dataSnapshot.child("chat_id").getValue().toString();
                refAuthUserChats.child(FIREBASE_PRIVATE_ATTR).child(chatId).setValue(chatId);
                refPendingChat.child(dataSnapshot.getKey()).removeValue();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }
        });
    }

    private void loadPrivateChat()
    {


        String uid = app.getUserInformation().getUID();

        // path -->
        refAuthUserChats.child(FIREBASE_PRIVATE_ATTR).addChildEventListener(new ChildEventListenerModel() {
            @Override
            public void onChildAdded(final DataSnapshot privateChat, String s) {

                DatabaseReference refChat = app.getFirebaseDatabase().getReferenceFromUrl(FB_PRIVATE_CHAT_PATH+privateChat.getKey());
                refChat.addListenerForSingleValueEvent(readUsersInformationOnChat(privateChat));


                refChat.child("_messages_").child("_last_message_").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        Log.i("private------->",privateChat.toString());
                        for(CommunityUserList chats : communityUserLists)
                        {
                              if(chats.getChatKey().equals(privateChat.getKey()))
                                {
                                    chats.setLastMsg(dataSnapshot.getValue().toString());
                                    mAdapter.notifyDataSetChanged();
                                break;
                                }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });


            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                Log.i("------->",dataSnapshot.toString());
            }
        });


    }

    @NonNull
    private ValueEventListener readUsersInformationOnChat(final DataSnapshot dataSnapshot) {
        return new ValueEventListener() {
            @Override
            public void onDataChange(final DataSnapshot chatInfo) {

                Iterator usersInChat = chatInfo.child(FIREBASE_DETAILS_ATTR).child("_users_").getChildren().iterator();

                while (usersInChat.hasNext())
                {
                    DataSnapshot data = (DataSnapshot) usersInChat.next();
                    if(!data.getKey().equals(app.getUserInformation().getUID()))
                    {
                        String userKey = data.getKey();

                        // hna fe b8
                        app.getDatabaseUsers().child(userKey).child(FIREBASE_DETAILS_ATTR).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot userInfo) {

                                String  lastMessage= "none";

                                if(chatInfo.child("_messages_").hasChild("_last_message_"))
                                    lastMessage = chatInfo.child("_messages_").child("_last_message_").getValue().toString();

                                addUserChat(dataSnapshot.getKey(),userInfo.child("_username_").getValue().toString(),userInfo.child("_picUrl_").getValue().toString(),lastMessage);
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    }
                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
    }



    private void addUserChat(String userKey, String username,String pictureURL,String lastMessage) {

        CommunityUserList communityUserList = new CommunityUserList();
        communityUserList.setChatKey(userKey);
        communityUserList.setFullName(username);
        communityUserList.setUserPictureURL(pictureURL);
        communityUserList.setLastMsg(lastMessage);
        addToList(communityUserList);
    }




}
