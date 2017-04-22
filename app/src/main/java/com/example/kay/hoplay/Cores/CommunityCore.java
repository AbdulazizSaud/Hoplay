package com.example.kay.hoplay.Cores;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;

import com.example.kay.hoplay.CoresAbstract.Community;
import com.example.kay.hoplay.Cores.ChatCore.ChatCore;
import com.example.kay.hoplay.Interfaces.FirebasePaths;
import com.example.kay.hoplay.Models.CommunityChatModel;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by Kay on 2/12/2017.
 */

public class CommunityCore extends Community implements FirebasePaths {


    private DatabaseReference refAuthCurrentUserChats;
    private DatabaseReference refLastMessage;
    private DatabaseReference refUserInfo;
    private DatabaseReference refPendingChat;
    private static long numberOfUnseenMessages ;


    private ChildEventListener privateSingleChatListener = new ChildEventListener() {
        @Override
        public void onChildAdded(final DataSnapshot chatRef, String s) {

            String opponentKey = (String)chatRef.child(FIREBASE_OPPONENT_ID_PATH).getValue();
            if(opponentKey == null)
                return;

            // on first time
            final DatabaseReference refCurrentChat = app.getDatabasChat().child(FIREBASE_PRIVATE_ATTR).child(chatRef.getKey());

            refUserInfo = app.getDatabaseUsersInfo().child(opponentKey).child(FIREBASE_DETAILS_ATTR);

            refUserInfo.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(final DataSnapshot userInfo) {

                    refCurrentChat.child("_messages_/_last_message_").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot lasMsgSnap) {

                            if(lasMsgSnap.child("_message_").getValue() == null )
                                return;

                            addUserChatToList(
                                    chatRef.getKey(),
                                    "_private_",
                                    userInfo.child("_username_").getValue().toString().trim(),
                                    userInfo.child("_picUrl_").getValue().toString().trim(),
                                    lasMsgSnap.child("_message_").getValue().toString().trim(),
                                    lasMsgSnap.child("_time_stamp_").getValue().toString().trim(),
                                    Long.parseLong(lasMsgSnap.child("_counter_").getValue().toString().trim())
                            );
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });


                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

            refLastMessage = refCurrentChat.child("_messages_/_last_message_");
            refLastMessage.addValueEventListener(lastMessageListener);
        }

        @Override
        public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            // each time it's added a new thing
            Log.i("new chat ----->",dataSnapshot.toString());

        }

        @Override
        public void onChildRemoved(DataSnapshot dataSnapshot) {
            removeFromList(dataSnapshot.getKey());
        }

        @Override
        public void onChildMoved(DataSnapshot dataSnapshot, String s) {

        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };
    private ChildEventListener privatePendingChatListener;
    private ValueEventListener lastMessageListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {


            if(dataSnapshot.child("_message_").getValue() == null &&  dataSnapshot.child("_time_stamp_").getValue() == null)
                return;

            String chatKey = dataSnapshot.getRef().getParent().getParent().getKey();
            String msg = dataSnapshot.child("_message_").getValue().toString().trim();
            String timeStamp = dataSnapshot.child("_time_stamp_").getValue().toString().trim();

            String currentChatCounterAsString = dataSnapshot.child("_counter_").getValue().toString().trim();


            updateLastMessage(chatKey, msg, timeStamp);
            setCounter(chatKey, currentChatCounterAsString);

        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };




    @Override
    protected void OnStartActivity() {


        // bug error on creation right here
        refAuthCurrentUserChats = app.getDatabaseUsersInfo().child(app.getUserInformation().getUID()).child(FIREBASE_USER_CHAT_REFERENCES);
        loadPrivatePendingChats();
        loadPrivateChat();

    }


    private void loadPrivatePendingChats() {

        String uid = app.getUserInformation().getUID();


        // here we will get a pending chats ref
                 refPendingChat = app
                .getDatabasChat()
                .child(FIREBASE_PENDING_CHAT_ATTR + "/" + uid + "/" + FIREBASE_PRIVATE_ATTR);


        refPendingChat.addChildEventListener(getPendingChatEventListener());

    }

    @NonNull
    private ChildEventListener getPendingChatEventListener() {
        return privatePendingChatListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                setPendingChatToUserRef(dataSnapshot, refPendingChat);

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                // get a chat id  from pending chat and set a ref on chat refreance in user_info
                setPendingChatToUserRef(dataSnapshot, refPendingChat);
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
        };
    }

    private void setPendingChatToUserRef(DataSnapshot dataSnapshot, DatabaseReference refPendingChat) {
        // get a chat id  from pending chat and set a ref on chat refreance in user_info
        String chatKey = dataSnapshot.child("chat_id").getValue().toString();
        String opponentId = dataSnapshot.getKey();

        // path --> /_users_info_/[UID]/_chat_refs_/_private_/[chatKey]/
        DatabaseReference currentChatRef = refAuthCurrentUserChats.child(FIREBASE_PRIVATE_ATTR).child(chatKey);
        currentChatRef.child(FIREBASE_OPPONENT_ID_PATH).setValue(opponentId);
        currentChatRef.child(FIREBASE_COUNTER_PATH).setValue(0);


        // remove a ref from pending chat
        refPendingChat.child(dataSnapshot.getKey()).removeValue();
    }

    private void loadPrivateChat() {
        refAuthCurrentUserChats.child(FIREBASE_PRIVATE_ATTR).addChildEventListener(privateSingleChatListener);
    }


    private void updateLastMessage(String chatKey, String message, String time) {

        for (CommunityChatModel communityChatModel : communityUserLists) {
            if (communityChatModel.getChatKey().equals(chatKey)) {

                communityChatModel.setLastMsg(message);
                communityChatModel.setLastMsgDate(app.convertFromTimeStampToDate(time));
                mAdapter.notifyDataSetChanged();
                break;
            }
        }
    }
    private void setCounter(final String chatKey,final String currentChatCounterAsString){
        refAuthCurrentUserChats.child(FIREBASE_PRIVATE_ATTR + "/" + chatKey+ "/"+FIREBASE_COUNTER_PATH ).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot userChatRef) {

                if (userChatRef.getValue() == null)
                    return;

                long currentCounter = Long.parseLong(currentChatCounterAsString);
                long value = Long.parseLong(userChatRef.getValue().toString().trim());
                long res = currentCounter - value;




                updateCounter(chatKey, String.valueOf(res));
            }


            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    private void updateCounter(String chatKey, String counter) {


        for (CommunityChatModel communityChatModel : communityUserLists) {
            if (communityChatModel.getChatKey().equals(chatKey)) {
                communityChatModel.setChatCounter(Long.parseLong(counter));
                mAdapter.notifyDataSetChanged();
                break;
            }
        }
    }



    @Override
    protected void OnClickHolders(CommunityChatModel model, View v) {
        Intent i = new Intent(v.getContext(), ChatCore.class);
        i.putExtra("room_key", model.getChatKey());
        i.putExtra("room_type", model.getChatType());
        i.putExtra("friend_username", model.getChatName());
        i.putExtra("friend_picture", model.getUserPictureURL());

        v.getContext().startActivity(i);
    }

    private boolean isMe(String userKey) {
        return userKey.equals(app.getUserInformation().getUID());
    }



    private void addUserChatToList(String chatKey, String chatType, String chatName, String pictureURL, String lastMessage, String lastDate, long messageNumber) {

        CommunityChatModel communityUserList = new CommunityChatModel();
        communityUserList.setChatKey(chatKey);
        communityUserList.setChatType(chatType);
        communityUserList.setChatName(chatName);
        communityUserList.setUserPictureURL(pictureURL);
        communityUserList.setLastMsg(lastMessage);
        communityUserList.setChatCounter(messageNumber);
        communityUserList.setLastMsgDate(app.convertFromTimeStampToDate(lastDate));
        addToList(communityUserList);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        refAuthCurrentUserChats.child(FIREBASE_PRIVATE_ATTR).removeEventListener(privateSingleChatListener);
        refLastMessage.removeEventListener(lastMessageListener);
     //   refPendingChat.removeEventListener(privatePendingChatListener);
    }
}
