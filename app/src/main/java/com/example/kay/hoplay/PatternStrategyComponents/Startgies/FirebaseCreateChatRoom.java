package com.example.kay.hoplay.PatternStrategyComponents.Startgies;

import com.example.kay.hoplay.App.App;
import com.example.kay.hoplay.Interfaces.FirebasePaths;
import com.google.firebase.database.DatabaseReference;

/**
 * Created by Kay on 3/19/2017.
 */

public abstract class FirebaseCreateChatRoom  implements FirebasePaths{


    private App app;


    public FirebaseCreateChatRoom()
    {
        app  = App.getInstance();

    }


    public FirebaseCreateChatRoom(String uid,String friendUid)
    {
        app  = App.getInstance();
        createPrivateChat(uid,friendUid);
    }



    private String createPrivateChat(String UID,String friendUID)
    {

        //app.getDatabasChat().child("_private_");

        // path --> /Chat/_private_
        DatabaseReference refPrivate = app.getFirebaseDatabase().getReferenceFromUrl(FB_PRIVATE_CHAT_PATH);
        String key  = refPrivate.push().getKey();

        // path --> /Chat/_private/[KEY]
        DatabaseReference chatRoom = refPrivate.child(key);

        // path --> /Chat/_private/[KEY]/_details_
        DatabaseReference roomInfo =   chatRoom.child(FIREBASE_DETAILS_ATTR);

        // path --> /Chat/_private/[KEY]/_details_/_users_
        DatabaseReference roomUsers = roomInfo.child(FIREBASE_USERS_LIST_ATTR);

        roomUsers.child(UID).setValue(UID);
        roomUsers.child(friendUID).setValue(friendUID);

        String accessKey = roomInfo.push().getKey();
        roomInfo.child("access_key").setValue(accessKey);

        // path --> /Chat/_private/[KEY]/_messages_
        DatabaseReference messages =   chatRoom.child(FIREBASE_CHAT_MESSAGES);
        messages.child("_last_message_").setValue("none");



        // Set Referance

        // path --> /_users_info_/[UID]/_chat_refs_/_private_

        String privateChatPath = FB_USERS_PATH+UID+"/"+FIREBASE_USER_PRIVATE_CHAT;
        DatabaseReference refUsePrivaterChats = app.getFirebaseDatabase().getReferenceFromUrl(privateChatPath);
        refUsePrivaterChats.child(key).setValue(accessKey);


        // Pending

        // path --> /Chat/_pending_chat_/[FRIEND_KEY]/_private_/[UID]
        DatabaseReference refPendingChat = app.getDatabasChat().child(FIREBASE_PENDING_CHAT_ATTR).child(friendUID).child(FIREBASE_PRIVATE_ATTR).child(UID);
        refPendingChat.child("chat_id").setValue(key);
        refPendingChat.child("access_key").setValue(accessKey);

        afterCreateChat(key);
        return key;
    }


    public abstract void afterCreateChat(String key);

}
