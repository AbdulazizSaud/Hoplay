package com.example.kay.hoplay;

import android.app.Application;

import java.net.URISyntaxException;

import io.socket.client.IO;
import io.socket.client.Socket;


public class SocketHandler extends Application{

    public static final String MESSAGE_EVENT = "message";
    public static final String RECEIVE_CLIENT_ID = "retID";
    public static final String ADD_USER = "adduser";

    public static String clientID;
    public static Socket socketIO;
    {
        try {
            socketIO = IO.socket(Constants.CHAT_PATH);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    public SocketHandler(){
        socketIO.connect();
    }

}
