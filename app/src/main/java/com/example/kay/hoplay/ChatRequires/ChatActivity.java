package com.example.kay.hoplay.ChatRequires;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;

import com.example.kay.hoplay.Activities.MainAppMenu;
import com.example.kay.hoplay.Adapters.ChatAdapter;
import com.example.kay.hoplay.App.App;
import com.example.kay.hoplay.R;
import com.example.kay.hoplay.model.ChatMessage;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;

import emojicon.EmojiconEditText;
import emojicon.EmojiconGridView;
import emojicon.EmojiconsPopup;
import emojicon.emoji.Emojicon;
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

    // set up chat app using socket io
    @Override
    protected void setupChat() {
        // get socket io instance and init events
        socket = App.getInstance().getSocket();
        socket.on(App.MESSAGE_EVENT, onMessage);
        socket.on(App.TYPEING_EVENT,onTyping);
        socket.on(App.ADD_USER_EVENT,onAddUser);
    }

    // this method for send message , execute only when a user click on send button
    public void sendMessage(String message) {
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

    // this method for receive message , execute only when a user receive a message
    public void receiveMessage(Objects... args) {
        super.receiveMessage(args);
        //........
    }


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

}
