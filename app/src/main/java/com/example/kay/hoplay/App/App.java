package com.example.kay.hoplay.App;

import android.app.Application;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.example.kay.hoplay.Interfaces.Constants;
import com.example.kay.hoplay.Services.ErrorHandler;
import com.example.kay.hoplay.Services.GetAPI;
import com.example.kay.hoplay.Services.LruBitmapCache;
import com.example.kay.hoplay.Interfaces.SocketIOEvents;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;

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

    public JSONObject getAPI(String apiURL, HashMap<String,String> data){
        String api_json= null;
        JSONObject resJSON=null;


        GetAPI api = new GetAPI(data);
        try {

            api_json = api.execute(apiURL).get();
            if(ErrorHandler.isError(api_json,getApplicationContext())) return null;

            resJSON = new JSONObject(api_json);

            if(resJSON.getString("type").equals("failed")) {
                Toast.makeText(this, resJSON.getString("msg"), Toast.LENGTH_SHORT).show();
                return null;
            }


            Toast.makeText(this, resJSON.getString("msg"), Toast.LENGTH_SHORT).show();


        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }


        return resJSON;

    }




}
