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

    // socket
    protected Socket socket;

    /********** Events **********/

    // this events on execute when user receive the message
    protected Emitter.Listener onMessage = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    receiveMessage();

                }
            });
        }
    };

    // this events on execute when  user typing
    protected Emitter.Listener onTyping = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    String username = String.valueOf(args[0]);

                }
            });
        }
    };

    // this events on execute when user join the room
    protected Emitter.Listener onAddUser = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    String username = String.valueOf(args[0]);

                }
            });
        }
    };

    /**************************/

    // set up chat app using socket io
    @Override
    public void setupChat() {
        // get socket io instance and init events
        socket = App.getInstance().getSocket();
        socket.on(App.MESSAGE_EVENT, onMessage);
        socket.on(App.TYPEING_EVENT,onTyping);
        socket.on(App.ADD_USER_EVENT,onAddUser);
    }

    @Override
    // this method for send message , execute only when a user click on send button
    protected void sendMessage(String message) {
        super.sendMessage(message);

        // here pass a data as json and emit to socket event
        JSONObject data = new JSONObject();
        try {
            data.put("username", myUsername);
            data.put("message", message);
            socket.emit(App.MESSAGE_EVENT, data);

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
    @Override
    // this method for receive message , execute only when a user receive a message
    protected void receiveMessage(Objects... args) {
        super.receiveMessage(args);
        //........
    }




}
