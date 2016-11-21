package com.example.kay.hoplay;

import android.app.Application;
import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

import java.net.URISyntaxException;

import io.socket.client.IO;
import io.socket.client.Socket;


public class App extends Application implements SocketIOEvents{

    private static App instance;


    private ImageLoader imageLoader;


    public static String clientID;
    private  Socket socketIO;
    {
        try {
            socketIO = IO.socket(Constants.CHAT_PATH);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onCreate() {

        super.onCreate();
        instance = this;

    }

    public static synchronized App getInstance(){
        return instance;
    }

    public Socket getSocket(){
        return socketIO;
    }

    public ImageLoader getImageLoader(){
        RequestQueue requestQueue  = Volley.newRequestQueue(getApplicationContext());

            if(imageLoader == null)
                imageLoader = new ImageLoader(requestQueue,new LruBitmapCache());
        return imageLoader;
    }


}
