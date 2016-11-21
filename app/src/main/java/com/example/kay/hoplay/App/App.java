package com.example.kay.hoplay.App;

import android.app.Application;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.example.kay.hoplay.Interfaces.Constants;
import com.example.kay.hoplay.Services.LruBitmapCache;
import com.example.kay.hoplay.Interfaces.SocketIOEvents;

import java.net.URISyntaxException;

import io.socket.client.IO;
import io.socket.client.Socket;


public class App extends Application implements SocketIOEvents,Constants{

    private static App instance;


    private ImageLoader imageLoader;


    public static String clientID;
    private  Socket socketIO;
    {
        try {
            socketIO = IO.socket(CHAT_PATH);
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
