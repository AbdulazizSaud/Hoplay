package com.example.kay.hoplay.App;

import android.app.Activity;
import android.app.Application;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.example.kay.hoplay.Interfaces.Constants;
import com.example.kay.hoplay.PatternStrategyComponents.PattrenContext;
import com.example.kay.hoplay.PatternStrategyComponents.PattrenStrategyInterface;
import com.example.kay.hoplay.Services.ErrorHandler;
import com.example.kay.hoplay.Services.GetAPI;
import com.example.kay.hoplay.Services.LruBitmapCache;
import com.example.kay.hoplay.Interfaces.SocketIOEvents;
import com.google.firebase.auth.FirebaseAuth;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

/*
    this class basiclly do linking between all difreent activity that trying to get a same specfic values
    it's impelmented as singleton stratgey
 */

public class App extends Application implements SocketIOEvents,Constants{

    private static App instance;


    private FirebaseAuth mAuth;  // firebase auth
    private PattrenContext pattrenContext; // pattren stratgey
    private ImageLoader imageLoader; // Image loader from url

    private  Socket socketIO; // socket io
    {
        //this method call init socket
        initSocket();
    }



    @Override
    public void onCreate() {

        super.onCreate();
        instance = this;
        pattrenContext = new PattrenContext();
        mAuth = FirebaseAuth.getInstance();
        socketIO.connect();

    }


    // this method init socket io
    private void initSocket() {
        try {
            socketIO = IO.socket(CHAT_PATH);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    // this old method, basiclly it get/pass a data android from api as json
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


    // this method return a instance of this app class
    public static synchronized App getInstance(){
        return instance;
    }
    // this method return a socket io
    public Socket getSocket(){
        return socketIO;
    }
    // this method return firebase auth
    public FirebaseAuth getAuth(){
        return mAuth;
    }
    // thi method return a imageloader
    public ImageLoader getImageLoader(){
        // create a new request queue for picture
        RequestQueue requestQueue  = Volley.newRequestQueue(getApplicationContext());
        if(imageLoader == null)
            imageLoader = new ImageLoader(requestQueue,new LruBitmapCache());
        return imageLoader;
    }
    //this method execute stratgey which it pass as parameter and return a results as hash
    public HashMap<String ,String> executeStratgy(PattrenStrategyInterface strategyInterface){
        return  pattrenContext.executeStratgy(strategyInterface);
    }



}
