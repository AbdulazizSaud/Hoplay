package com.example.kay.hoplay.Chat;

import android.content.Intent;
import android.util.Log;

import com.android.volley.toolbox.ImageLoader;
import com.example.kay.hoplay.App.App;
import com.example.kay.hoplay.Interfaces.FirebasePaths;
import com.example.kay.hoplay.R;
import com.example.kay.hoplay.model.ChildEventListenerModel;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

import de.hdodenhof.circleimageview.CircleImageView;
import emojicon.emoji.Objects;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import okhttp3.internal.cache.DiskLruCache;

/**
 * Created by Kay on 10/30/2016.
 * this class implemetns a chat activity
 */

public class ChatActivity extends Chat implements FirebasePaths {
    private String chatRoomKey=null;


    private String friendUsername=null,friendPicture=null,friendKey=null;
    DatabaseReference refRoom,refMessages;
    // set up chat app
    @Override
    public void setupChat() {
    //load message
        Intent i = getIntent();
        chatRoomKey = i.getStringExtra("room_key");
        friendUsername = i.getStringExtra("friend_username");
        friendPicture = i.getStringExtra("friend_picture");
        refRoom = app.getFirebaseDatabase().getReferenceFromUrl(FB_PRIVATE_CHAT_PATH+chatRoomKey);
        Log.i("------->",refRoom.toString());
        refMessages = refRoom.child("_messages_");

        setupRoomDetails();
        loadMessage();


    }

    private void loadMessage() {




        refMessages.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                if(isEmpty(dataSnapshot))
                    return;
                String username = dataSnapshot.child("_username_").getValue().toString();
                String message = dataSnapshot.child("_message_").getValue().toString();

                addMessage(username,message,username.equals(app.getUID()));

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                if(isEmpty(dataSnapshot))
                    return;
                String username = dataSnapshot.child("_username_").getValue().toString();
                String message = dataSnapshot.child("_message_").getValue().toString();


                if(!username.equals(app.getUID()))
                addMessage(username,message,false);
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

    private boolean isEmpty(DataSnapshot dataSnapshot) {
        return dataSnapshot.child("_username_").getValue() == null || dataSnapshot.child("_message_").getValue() == null;
    }


    private void setupRoomDetails() {

        roomPicture.setImageResource(R.drawable.profile_default_photo);

            if (friendPicture.length() > 0 && !friendPicture.equals("default"))
            {
                app.getImageLoader().get(friendPicture,
                        ImageLoader.getImageListener(
                                roomPicture
                                , R.drawable.profile_default_photo
                                , R.drawable.profile_default_photo));
            }


        roomName.setText(friendUsername);

    }

    @Override
    // this method for send message , execute only when a user click on send button
    protected void sendMessage(String message) {
        super.sendMessage(message);
        String message_key = refMessages.push().getKey();

        refMessages.child(message_key).child("_username_").setValue(app.getUID());
        refMessages.child(message_key).child("_message_").setValue(message);

        refMessages.child("_last_message_").setValue(message);

    }
    @Override
    // this method for receive message , execute only when a user receive a message
    protected void receiveMessage(Objects... args) {
        super.receiveMessage(args);
        //........
    }




}
