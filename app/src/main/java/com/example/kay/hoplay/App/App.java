package com.example.kay.hoplay.App;

import android.app.Application;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.example.kay.hoplay.Activities.MainAppMenu;
import com.example.kay.hoplay.Interfaces.Constants;
import com.example.kay.hoplay.Services.ErrorHandler;
import com.example.kay.hoplay.Services.GetAPI;
import com.example.kay.hoplay.Services.LruBitmapCache;
import com.example.kay.hoplay.Interfaces.SocketIOEvents;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

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

    public FirebaseAuth getmAuth() {
        return mAuth;
    }

    public void setmAuthListener(FirebaseAuth.AuthStateListener authStateListener){
        mAuth.addAuthStateListener(authStateListener);
    }
    public void removemAuthListener(FirebaseAuth.AuthStateListener authStateListener){
        mAuth.removeAuthStateListener(authStateListener);
    }

    private SQLiteDatabase sqLiteDatabase;
    private   FirebaseAuth mAuth;


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
        mAuth = FirebaseAuth.getInstance();



        SQLiteManagement();

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



    public void SQLiteManagement(){


        sqLiteDatabase = this.openOrCreateDatabase("Hoplay_Lite",MODE_PRIVATE,null);

//        // delete table
//        String deleteTables = "DROP TABLE ChatAdapter";
//        sqLiteDatabase.execSQL(deleteTables);
//        deleteTables="DROP TABLE ChatMessage";
//        sqLiteDatabase.execSQL(deleteTables);
//        //

        String sql1 = "CREATE TABLE IF NOT EXISTS ChatAdapter(" +
                "CHAT_ID INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, " +
                "receiver_ID varchar(255) NOT NULL UNIQUE," +
                "last_message varchar(255)," +
                "receiver_Picture TEXT(255) )";

        String sql2 = "CREATE TABLE IF NOT EXISTS ChatMessage(" +
                "MESSAGE_ID INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, " +
                "sender_ID varchar(255) NOT NULL," +
                "receiver_ID varchar(255) NOT NULL," +
                "message TEXT(255)" +
                ")";

        sqLiteDatabase.execSQL(sql1);
        sqLiteDatabase.execSQL(sql2);



    }


    public void insertIntoCASQL(String receiverID, String lastMessage, String receiverPicture){

        String sql = "INSERT INTO ChatAdapter(receiver_ID,last_message,receiver_Picture) Values("
                 +"'"+receiverID+"',"+"'"+lastMessage+"',"+"'"+receiverPicture+"'"
                +")";
        sqLiteDatabase.execSQL(sql);


    }

    public void insertIntoCMSQL(String id,String toId,String message){

        String sql = "INSERT INTO ChatMessage(sender_ID,receiver_ID,message) Values("
                +"'"+id+"',"+"'"+toId+"',"+"'"+message+"')";
        sqLiteDatabase.execSQL(sql);

    }

    public Cursor getCASQL(){
        Cursor c = sqLiteDatabase.rawQuery("SELECT * FROM ChatAdapter",null);
        return c;
    }

    public Cursor getCMSQL(String myID ,String receiverID){
        String query ="SELECT * FROM ChatMessage WHERE (sender_ID = '"+ myID +"' AND receiver_ID = '"+receiverID+"') or (sender_ID = '"+receiverID+"' AND receiver_ID = '"+myID+"')";
        Cursor c = sqLiteDatabase.rawQuery(query,null);
        return c;
    }



}
