package com.example.kay.hoplay.App;

import android.app.Application;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.example.kay.hoplay.Adapters.ViewHolders;
import com.example.kay.hoplay.Interfaces.Constants;
import com.example.kay.hoplay.PatternStrategyComponents.PattrenContext;
import com.example.kay.hoplay.PatternStrategyComponents.PattrenStrategyInterface;
import com.example.kay.hoplay.R;
import com.example.kay.hoplay.Services.ErrorHandler;
import com.example.kay.hoplay.Services.GetAPI;
import com.example.kay.hoplay.Services.LruBitmapCache;
import com.example.kay.hoplay.Interfaces.FirebasePaths;
import com.example.kay.hoplay.model.CommonModel;
import com.example.kay.hoplay.model.UserInformation;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;

import de.hdodenhof.circleimageview.CircleImageView;
import io.socket.client.IO;
import io.socket.client.Socket;

/*
    this class basiclly do linking between all difreent activity that trying to get a same specfic values
    it's impelmented as singleton stratgey
 */

public class App extends Application implements FirebasePaths,Constants{

    private static App instance;


    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseUsers;
    private DatabaseReference databaseChat;
    private DatabaseReference databaseAuthUserDataRef;

    private FirebaseAuth mAuth;  // firebase auth
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    private PattrenContext pattrenContext; // pattren stratgey
    private ImageLoader imageLoader; // Image loader from url

    private UserInformation userInformation;



    @Override
    public void onCreate() {

        super.onCreate();
        instance = this;
        pattrenContext = new PattrenContext();
        mAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        getFirebaseDatabase().setPersistenceEnabled(true);
        databaseUsers = firebaseDatabase.getReferenceFromUrl(FB_ROOT).child(FIREBASE_USERS_INFO_ATTR);
        databaseChat = firebaseDatabase.getReferenceFromUrl(FB_ROOT).child(FIREBASE_CHAT_ATTR);
        userInformation = new UserInformation();
    }



    // this method return a instance of this app class
    public static synchronized App getInstance(){
        return instance;
    }
    // this method return a socket io
    // this method return firebase auth
    public FirebaseAuth getAuth(){
        return mAuth;
    }

    // thi method return a imageloader
    private ImageLoader getImageLoader(){
        // create a new request queue for picture
        RequestQueue requestQueue  = Volley.newRequestQueue(getApplicationContext());
        if(imageLoader == null)
            imageLoader = new ImageLoader(requestQueue,new LruBitmapCache());
        return imageLoader;
    }


    public void loadingImage(ViewHolders holder, String pictureURL) {

        CircleImageView picture = holder.getPicture();
        picture.setImageResource(R.drawable.profile_default_photo);
        holder.setPicture(picture);
        loadPicture(pictureURL, holder.getPicture());
    }



    public CircleImageView loadingImage(CircleImageView pictureView ,String pictureURL) {

        pictureView.setImageResource(R.drawable.profile_default_photo);
        loadPicture(pictureURL,pictureView);
        return pictureView;
    }


    private void loadPicture(String pictureURL,CircleImageView circleImageView) {
        if(pictureURL != null) {
            if (pictureURL.length() > 0 && !pictureURL.startsWith("default")) {
                getImageLoader().get(pictureURL,
                        ImageLoader.getImageListener(
                                circleImageView
                                , R.drawable.profile_default_photo
                                , R.drawable.profile_default_photo));

            }
        }
    }


    public void signOut()
    {
        mAuth.signOut();
    }

    public FirebaseDatabase getFirebaseDatabase() {
        return firebaseDatabase;
    }
    public DatabaseReference getDatabaseUsers() {
        return databaseUsers;
    }
    public DatabaseReference getDatabasChat() {
        return databaseChat;
    }

    public DatabaseReference getDatabaseAuthUserDataRef() {
        return databaseAuthUserDataRef;
    }

    public void setDatabaseAuthUserDataRef(DatabaseReference databaseAuthUserDataRef) {
        this.databaseAuthUserDataRef = databaseAuthUserDataRef;
    }

    public UserInformation getUserInformation() {
        return userInformation;
    }

    public FirebaseAuth.AuthStateListener getmAuthStateListener() {
        return mAuthStateListener;
    }

    public void setmAuthStateListener(FirebaseAuth.AuthStateListener mAuthStateListener) {
        this.mAuthStateListener = mAuthStateListener;
    }
}


