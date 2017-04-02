package com.example.kay.hoplay.CommunityComponents;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;

import com.example.kay.hoplay.Chat.ChatCore;
import com.example.kay.hoplay.Interfaces.FirebasePaths;
import com.example.kay.hoplay.Models.ChildEventListenerModel;
import com.example.kay.hoplay.Models.CommunityChatModel;
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


    // chunks var
    String userKey;

    @Override
    protected void OnClickHolders(CommunityChatModel model, View v) {
        Intent i = new Intent(v.getContext(), ChatCore.class);
        i.putExtra("room_key",model.getChatKey());
        i.putExtra("friend_username", model.getFullName());
        i.putExtra("friend_picture",model.getUserPictureURL());

        v.getContext().startActivity(i);
    }

    @Override
    protected void OnStartActivity() {

        // bug error on creation right here
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


        // here we will get a pending chats ref
        final DatabaseReference refPendingChat = app
                .getDatabasChat()
                .child(FIREBASE_PENDING_CHAT_ATTR+"/"+uid+"/"+FIREBASE_PRIVATE_ATTR);

        refPendingChat.addChildEventListener(new ChildEventListenerModel() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                // get a chat id  from pending chat and set a ref on chat refreance in user_info
                String chatId = dataSnapshot.child("chat_id").getValue().toString();
                refAuthUserChats.child(FIREBASE_PRIVATE_ATTR).child(chatId).setValue(chatId);

                // remove a ref from pending chat
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

        // path --> /_users_info/[UID]/_chat_refs_/_private_

        refAuthUserChats.child(FIREBASE_PRIVATE_ATTR).addChildEventListener(new ChildEventListenerModel() {
            @Override
            public void onChildAdded(final DataSnapshot chatUpdated, String s) {

                DatabaseReference refChat = app.getFirebaseDatabase().getReferenceFromUrl(FB_PRIVATE_CHAT_PATH + chatUpdated.getKey());
                refChat.addListenerForSingleValueEvent(readUsersInformationOnChat());

                refChat.child("_messages_"+"/"+"_last_message_").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot lastMessageValue) {
                        updateLastMessage(chatUpdated.getKey(), lastMessageValue.getValue().toString().trim());
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

    private void updateLastMessage(String chatUpdatedKey,String lastMessage) {
        for(CommunityChatModel chats : communityUserLists)
        {
              if(chats.getChatKey().equals(chatUpdatedKey))
                {
                    chats.setLastMsg(lastMessage);
                    mAdapter.notifyDataSetChanged();
                break;
                }
        }
    }

    @NonNull
    private ValueEventListener readUsersInformationOnChat() {
        return new ValueEventListener() {
            @Override
            public void onDataChange(final DataSnapshot currentChat) {

                // path --> /_chat_ref/_private_/[CHAT_ID]/_info_/_users_
                Iterable<DataSnapshot> usersInChat = currentChat.child(FIREBASE_DETAILS_ATTR).child("_users_").getChildren();
                for(DataSnapshot user : usersInChat)
                {
                    userKey  = user.getKey();

                    // here we will check if i'm not in users list xD (bug)
                    if(!userKey.equals(app.getUserInformation().getUID()))
                    {
                        // path --> /_users_info/[UID]/_details_
                        app.getDatabaseUsers().child(userKey)
                                .child(FIREBASE_DETAILS_ATTR)
                                .addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {

                                        // Set up a chat

                                        boolean isLastMsgExisted = currentChat.child("_messages_").hasChild("_last_message_");
                                        String lastMessage = "none";

                                        String username =  dataSnapshot.child("_username_").getValue().toString().trim();
                                        String picUrl  =  dataSnapshot.child("_picUrl_").getValue().toString().trim();
                                        String messageCounter =  currentChat.child("_messages_"+"/"+"_counter_").getValue().toString().trim();

                                          if(isLastMsgExisted)
                                            lastMessage = currentChat.child("_messages_"+"/"+"_last_message_").getValue().toString();

                                        addUserChatToList(userKey,username,picUrl,lastMessage,messageCounter);


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



    private void addUserChatToList(String userKey, String username,String pictureURL,String lastMessage,String messageNumber) {

        CommunityChatModel communityUserList = new CommunityChatModel();
        communityUserList.setChatKey(userKey);
        communityUserList.setFullName(username);
        communityUserList.setUserPictureURL(pictureURL);
        communityUserList.setLastMsg(lastMessage);
        addToList(communityUserList);
    }




}
