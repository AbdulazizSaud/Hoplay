package com.example.kay.hoplay.Cores;

import android.content.Intent;
import android.view.View;

import com.example.kay.hoplay.CoresAbstract.Community;
import com.example.kay.hoplay.Cores.ChatCore.ChatCore;
import com.example.kay.hoplay.Interfaces.FirebasePaths;
import com.example.kay.hoplay.Models.ChildEventListenerModel;
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


    private DatabaseReference refAuthUserChats;
    private DatabaseReference refPrivateChat;
    private DatabaseReference refPrivateChatUsersInfo;
    private DatabaseReference refLastMessage;
    private DatabaseReference refUserInfo;


    private ChildEventListener privateChatListener = new ChildEventListener() {
        @Override
        public void onChildAdded(final DataSnapshot chatRef, String s) {

            // on first time
            refPrivateChat = app.getDatabasChat().child(FIREBASE_PRIVATE_ATTR).child(chatRef.getKey());
            refPrivateChatUsersInfo = refPrivateChat.child(FIREBASE_DETAILS_ATTR).child(FIREBASE_USERS_LIST_ATTR);


            refPrivateChatUsersInfo.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot usersSnap) {
                    Iterable<DataSnapshot> users = usersSnap.getChildren();

                    for (DataSnapshot user : users) {
                        refUserInfo = app.getDatabaseUsers().child(user.getKey()).child(FIREBASE_DETAILS_ATTR);
                        final String userKey = user.getKey();

                        if (isMe(userKey))
                            continue;

                        refUserInfo.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(final DataSnapshot userInfo) {

                                if (!isMe(userKey)) {

                                    refPrivateChat.child("_messages_/_last_message_").addListenerForSingleValueEvent(new ValueEventListener() {
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
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

            refLastMessage = refPrivateChat.child("_messages_/_last_message_");
            refLastMessage.addValueEventListener(lastMessageListener);
            refLastMessage.child("_counter_").addValueEventListener(LastMessageCounterListener);


        }

        @Override
        public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            // each time it's added a new thing
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


    private ValueEventListener lastMessageListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {

            if(dataSnapshot.child("_message_").getValue() == null )
                return;

            String chatKey = dataSnapshot.getRef().getParent().getParent().getKey();
            String msg = dataSnapshot.child("_message_").getValue().toString().trim();
            String timeStamp = dataSnapshot.child("_time_stamp_").getValue().toString().trim();
            updateLastMessage(chatKey, msg, timeStamp);

        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };
    private ValueEventListener LastMessageCounterListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot chatCounter) {


            if (chatCounter.getValue() == null)
                return;

            final String chatKey = chatCounter.getRef().getParent().getParent().getParent().getKey().trim();
            final String currentChatCounterAsString = chatCounter.getValue().toString().trim();


            refAuthUserChats.child(FIREBASE_PRIVATE_ATTR + "/" + chatKey).addListenerForSingleValueEvent(new ValueEventListener() {
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

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };


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

        String uid = app.getUserInformation().getUID();


        // here we will get a pending chats ref
        final DatabaseReference refPendingChat = app
                .getDatabasChat()
                .child(FIREBASE_PENDING_CHAT_ATTR + "/" + uid + "/" + FIREBASE_PRIVATE_ATTR);

        refPendingChat.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                // get a chat id  from pending chat and set a ref on chat refreance in user_info
                String chatId = dataSnapshot.child("chat_id").getValue().toString();
                refAuthUserChats.child(FIREBASE_PRIVATE_ATTR).child(chatId).setValue(0);

                // remove a ref from pending chat
                refPendingChat.child(dataSnapshot.getKey()).removeValue();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                // get a chat id  from pending chat and set a ref on chat refreance in user_info
                String chatId = dataSnapshot.child("chat_id").getValue().toString();
                refAuthUserChats.child(FIREBASE_PRIVATE_ATTR).child(chatId).setValue(0);

                // remove a ref from pending chat
                refPendingChat.child(dataSnapshot.getKey()).removeValue();
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

    private void loadPrivateChat() {
        refAuthUserChats.child(FIREBASE_PRIVATE_ATTR).addChildEventListener(privateChatListener);

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


    private void updateCounter(String chatKey, String counter) {


        for (CommunityChatModel communityChatModel : communityUserLists) {
            if (communityChatModel.getChatKey().equals(chatKey)) {
                communityChatModel.setChatCounter(Long.parseLong(counter));
                mAdapter.notifyDataSetChanged();
                break;
            }
        }
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


}
