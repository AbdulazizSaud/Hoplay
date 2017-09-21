package com.hoplay.kay.hoplay.Cores;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.view.View;

import com.hoplay.kay.hoplay.CoresAbstract.Community;
import com.hoplay.kay.hoplay.Cores.ChatCore.ChatCore;
import com.hoplay.kay.hoplay.Interfaces.FirebasePaths;
import com.hoplay.kay.hoplay.Models.ChatMessage;
import com.hoplay.kay.hoplay.Models.CommunityChatModel;
import com.hoplay.kay.hoplay.Models.GameModel;
import com.hoplay.kay.hoplay.R;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.Random;

import static android.content.Context.NOTIFICATION_SERVICE;



public class CommunityCore extends Community implements FirebasePaths {


    private DatabaseReference refAuthCurrentUserChats;
    private DatabaseReference refUserInfo;
    private DatabaseReference refPendingChat;
    private int uniqeID=0;
    private  String pastMessage = "PAST MESSAGE";
    private  String newMessage = "NEW MESSAGE";
    private boolean isFirstMessage = true;


    private ChildEventListener privateSingleChatListener = new ChildEventListener() {
        @Override
        public void onChildAdded(final DataSnapshot dataSnapshot, String s) {
            addNewPrivateChat(dataSnapshot);
        }

        @Override
        public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            // each time it's added a new thing
            addNewPrivateChat(dataSnapshot);

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


    private ChildEventListener publicChatListener = new ChildEventListener() {
        @Override
        public void onChildAdded(final DataSnapshot dataSnapshot, String s) {
            addNewPublicChat(dataSnapshot);

        }

        @Override
        public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            // each time it's added a new thing
            addNewPublicChat(dataSnapshot);
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



    private void addNewPrivateChat(final DataSnapshot chatRef) {
        final String opponentKey = (String) chatRef.child(FIREBASE_OPPONENT_ID_ATTR).getValue();
        if (opponentKey == null)
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



                        if (lasMsgSnap.getValue() == null || lasMsgSnap.child("message").getValue() == null)
                            return;

                        try {

                            ChatMessage message = lasMsgSnap.getValue(ChatMessage.class);

                            CommunityChatModel communityChatModel = addUserChatToList(chatRef.getKey(),
                                    FIREBASE_PRIVATE_ATTR,
                                    userInfo.child("_username_").getValue(String.class),
                                    userInfo.child("_picUrl_").getValue(String.class),
                                    message.getMessage(),
                                    (long)message.getTimestamp(),
                                    message.getCounter()
                            );
                            communityChatModel.setOpponentId(opponentKey);

                        } catch (NullPointerException e) {

                        }

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

        DatabaseReference refLastMessage = refCurrentChat.child("_messages_/_last_message_");
        refLastMessage.addValueEventListener(lastMessageListener);
    }


    private void addNewPublicChat(final DataSnapshot chatRef) {
        // on first time
        final DatabaseReference refCurrentChat = app.getDatabasChat().child(FIREBASE_PUBLIC_ATTR).child(chatRef.getKey());
        refCurrentChat.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot chatSnapShot) {

                if (chatSnapShot.child("_messages_/_last_message_").getValue() == null)
                    return;

                DataSnapshot lastMessageSnapshots = chatSnapShot.child("_messages_/_last_message_");

                String gameIdPath = FIREBASE_DETAILS_ATTR + "/room_info/gameId";

                String values = chatSnapShot.child(gameIdPath).getValue(String.class);

                String chatName = "Unknown";
                String chatPicture = "Unknown";


                if (values != null) {
                    GameModel gameModel = app.getGameManager().getGameById(values);
                    if (gameModel != null) {
                        chatName = gameModel.getGameName();
                        chatPicture = gameModel.getGamePhotoUrl();
                    }
                }

                ChatMessage message = lastMessageSnapshots.getValue(ChatMessage.class);


                addUserChatToList(
                        chatRef.getKey(),
                        FIREBASE_PUBLIC_ATTR,
                        chatName,
                        chatPicture,
                        message.getMessage(),
                        (long)message.getTimestamp(),
                        message.getCounter()
                );
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        DatabaseReference refLastMessage = refCurrentChat.child("_messages_/_last_message_");
        refLastMessage.addValueEventListener(lastMessageListener);
    }

    private ChildEventListener privatePendingChatListener;


    private ValueEventListener lastMessageListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot lastMsgInfo) {

            ChatMessage message = lastMsgInfo.getValue(ChatMessage.class);

            if (message == null)
                return;


            final String chatKey = lastMsgInfo.getRef().getParent().getParent().getKey();
            final String usernameKey = message.getUsername();
            final String msg = message.getMessage();
            final long timeStamp = (long)message.getTimestamp();
            final String currentChatCounterAsString = String.valueOf(message.getCounter());

           final String type = (lastMsgInfo.getRef().toString().contains(FIREBASE_PRIVATE_ATTR)) ? FIREBASE_PRIVATE_ATTR : FIREBASE_PUBLIC_ATTR;

            app.getDatabaseUsersInfo().child(usernameKey).child(FIREBASE_USERNAME_PATH).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    String joinerUsername = dataSnapshot.getValue(String.class);
                    if (app.getUserInformation().getUID().equals(usernameKey))
                        joinerUsername = "You";

                    updateLastMessage(chatKey, joinerUsername, msg, timeStamp);
                    setCounter(chatKey,type,joinerUsername, msg, currentChatCounterAsString);


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

    private void setCounter(final String chatKey, String chatType, final String joinerUsername, final String msg, final String currentChatCounterAsString) {
        // this listner for chat counter + notify shit

        refAuthCurrentUserChats.child(chatType + "/" + chatKey + "/" + FIREBASE_COUNTER_PATH).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot userChatRef) {

                if (userChatRef.getValue() == null)
                    return;

                long currentCounter = Long.parseLong(currentChatCounterAsString);
                long value = Long.parseLong(userChatRef.getValue().toString().trim());
                long res = currentCounter - value;

                // set the new message , past message will change in the notification method
                newMessage = msg;


                if (res > 0 && !newMessage.equalsIgnoreCase(pastMessage))
                    notifyUser(chatKey, joinerUsername, msg);

                updateCounter(chatKey, String.valueOf(res));
            }


            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    @Override
    protected void OnStartActivity() {


        // bug error on creation right here
        refAuthCurrentUserChats = app.getDatabaseUsersInfo().child(app.getUserInformation().getUID()).child(FIREBASE_USER_CHAT_REFERENCES);
        loadPrivatePendingChats();
        loadPrivateChat();
        loadPublicChat();


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
        currentChatRef.child(FIREBASE_OPPONENT_ID_ATTR).setValue(opponentId);
        currentChatRef.child(FIREBASE_COUNTER_PATH).setValue(0);


        // remove a ref from pending chat
        refPendingChat.child(dataSnapshot.getKey()).removeValue();
    }

    private void loadPrivateChat() {
        refAuthCurrentUserChats.child(FIREBASE_PRIVATE_ATTR).addChildEventListener(privateSingleChatListener);
    }

    private void loadPublicChat() {
        refAuthCurrentUserChats.child(FIREBASE_PUBLIC_ATTR).addChildEventListener(publicChatListener);
    }


    private void updateLastMessage(String chatKey, String username, String message, long time) {



        boolean isChatDeleted = app.getSchemaHelper().isExistKey(chatKey);

        CommunityChatModel communityChatModel = isChatDeleted ? deletedCommunityChatModelHashMap.get(chatKey) :communityChatModelHashMap.get(chatKey) ;


        if(communityChatModel == null)
            return;


        boolean b = time != app.getSchemaHelper().searchTimeStampByKey(chatKey);


        if(isChatDeleted && b ) {
            app.getSchemaHelper().deleteKey(chatKey);
            addToList(communityChatModel);
            deletedCommunityChatModelHashMap.remove(communityChatModel);
        }

        communityChatModel.setLastMsg(username + ": " + message);
        communityChatModel.setTimeStamp(time);
        mAdapter.notifyDataSetChanged();


    }


    private void updateCounter(String chatKey, String counter) {


        for (CommunityChatModel communityChatModel : communityUserLists) {
            if (communityChatModel.getChatKey().equals(chatKey)) {

                // Check if the chat is exist or not : if it exist then dont increase the chats counter
                if (Long.parseLong(counter)>0 && !app.getChatsCounterRefs().containsKey(chatKey))
                {
                    app.increaseChatsByOne();
                    app.getChatsCounterRefs().put(chatKey,counter);
                }

                communityChatModel.setChatCounter(Long.parseLong(counter));
                mAdapter.notifyDataSetChanged();
                break;
            }
        }
    }


    @Override
    protected void OnClickHolders(CommunityChatModel model, View v) {
        Intent i = new Intent(v.getContext(), ChatCore.class);
        setChatIntent(model, i);

        app.getMainAppMenuCore().startActivityForResult(i, 1);
    }

    private void setChatIntent(CommunityChatModel model, Intent i) {
        i.putExtra("room_key", model.getChatKey());
        i.putExtra("room_type", model.getChatType());
        i.putExtra("room_name", model.getChatName());
        i.putExtra("room_picture", model.getUserPictureURL());

        if (model.getChatType().equals(FIREBASE_PRIVATE_ATTR))
            i.putExtra("opponent_key", model.getOpponentId());
    }

    private boolean isMe(String userKey) {
        return userKey.equals(app.getUserInformation().getUID());
    }


    private CommunityChatModel addUserChatToList(String chatKey, String chatType, String chatName, String pictureURL, String lastMessage, long timeStamp, long messageNumber) {

        CommunityChatModel communityUserList = new CommunityChatModel();
        communityUserList.setChatKey(chatKey);
        communityUserList.setChatType(chatType);
        communityUserList.setChatName(chatName);
        communityUserList.setUserPictureURL(pictureURL);
        communityUserList.setLastMsg(lastMessage);
        communityUserList.setChatCounter(messageNumber);
        communityUserList.setTimeStamp(timeStamp);
        addToList(communityUserList);
        return communityUserList;
    }


    @Override
    protected void removeChatFromlist(CommunityChatModel model) {

        deletedCommunityChatModelHashMap.put(model.getChatKey(),model);
        app.getSchemaHelper().insertKey(model.getChatKey(),model.getTimeStamp());
        removeFromList(model.getChatKey());
    }


    private void notifyUser(String chatKey, String joinerUsername, String message) {
        NotificationCompat.Builder notification;



        CommunityChatModel communityChatModel = communityChatModelHashMap.get(chatKey) == null? deletedCommunityChatModelHashMap.get(chatKey) : communityChatModelHashMap.get(chatKey);

        if(communityChatModel == null)
            return;

        joinerUsername = communityChatModel.getChatType().equals(FIREBASE_PRIVATE_ATTR) ? "" : joinerUsername+": ";
        Intent intent = new Intent(app.getMainAppMenuCore(), ChatCore.class);
        setChatIntent(communityChatModel, intent);


        Random random = new Random();
        uniqeID = random.nextInt(999999999-1)+1;



        Uri sound = Uri.parse("android.resource://" + app.getPackageName() + "/" + R.raw.communicationchannel);


        notification = new NotificationCompat.Builder(app.getMainAppMenuCore());
        notification.setAutoCancel(true);
        notification.setSmallIcon(R.drawable.ic_stat_hoplaylogo);
        notification.setTicker("This is ticker");
        notification.setWhen(System.currentTimeMillis());
        notification.setContentTitle(communityChatModel.getChatName());
        notification.setContentText(joinerUsername+ message);
        notification.setSound(sound);

        PendingIntent pendingIntent = PendingIntent.getActivity(app.getMainAppMenuCore(), uniqeID, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        notification.setContentIntent(pendingIntent);


        NotificationManager nm = (NotificationManager) app.getMainAppMenuCore().getSystemService(NOTIFICATION_SERVICE);
        nm.notify(uniqeID, notification.build());

        // set the message as a past message to avoid repeatness
        pastMessage = message;

    }



    @Override
    public void onDestroy() {
        super.onDestroy();
        refAuthCurrentUserChats.child(FIREBASE_PRIVATE_ATTR).removeEventListener(privateSingleChatListener);
//        refLastMessage.removeEventListener(lastMessageListener);
//        refPendingChat.removeEventListener(privatePendingChatListener);
    }
}
