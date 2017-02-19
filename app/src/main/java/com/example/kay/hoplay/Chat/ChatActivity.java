package com.example.kay.hoplay.Chat;

import com.example.kay.hoplay.App.App;

import org.json.JSONException;
import org.json.JSONObject;

import emojicon.emoji.Objects;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

/**
 * Created by Kay on 10/30/2016.
 * this class implemetns a chat activity
 */

public class ChatActivity extends Chat {


    // set up chat app using socket io
    @Override
    public void setupChat() {
    //load message
    }

    @Override
    // this method for send message , execute only when a user click on send button
    protected void sendMessage(String message) {
        super.sendMessage(message);

    }
    @Override
    // this method for receive message , execute only when a user receive a message
    protected void receiveMessage(Objects... args) {
        super.receiveMessage(args);
        //........
    }




}
