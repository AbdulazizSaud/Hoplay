package com.example.kay.hoplay.App;

import android.app.Application;
import android.graphics.Bitmap;
import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.example.kay.hoplay.Adapters.ViewHolders;
import com.example.kay.hoplay.Interfaces.Constants;
import com.example.kay.hoplay.R;
import com.example.kay.hoplay.Services.LruBitmapCache;
import com.example.kay.hoplay.Interfaces.FirebasePaths;
import com.example.kay.hoplay.model.UserInformation;
import com.example.kay.hoplay.util.BitmapOptimizer;
import com.example.kay.hoplay.util.GameManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.ByteArrayOutputStream;

import de.hdodenhof.circleimageview.CircleImageView;

/*
    this class basiclly do linking between all difreent activity that trying to get a same specfic values
    it's impelmented as singleton stratgey
 */

public class App extends Application implements FirebasePaths,Constants{

    private static App instance;


    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseUsers;
    private DatabaseReference databaseChat;
    private DatabaseReference databaseGames;
    private DatabaseReference databaseAuthUserDataRef;

    private FirebaseAuth mAuth;  // firebase auth
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    private ImageLoader imageLoader; // Image loader from url

    private UserInformation userInformation;
    private GameManager gameManager;


    @Override
    public void onCreate() {

        super.onCreate();
        instance = this;
        mAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        getFirebaseDatabase().setPersistenceEnabled(true);
        databaseUsers = firebaseDatabase.getReferenceFromUrl(FB_ROOT).child(FIREBASE_USERS_INFO_ATTR);
        databaseChat = firebaseDatabase.getReferenceFromUrl(FB_ROOT).child(FIREBASE_CHAT_ATTR);
        databaseGames = firebaseDatabase.getReferenceFromUrl(FB_ROOT).child(FIREBASE_GAMES_REFERENCES);
        userInformation = new UserInformation();
        gameManager = new GameManager();
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


    public Bitmap loadingImage(ViewHolders holder, String pictureURL) {

        CircleImageView picture = holder.getPicture();
        picture.setImageResource(R.drawable.profile_default_photo);
        holder.setPicture(picture);

        Bitmap bitmap = loadPicture(pictureURL, holder.getPicture());

//        if(bitmap !=null)
//        resizeBitmap(holder.getPicture(), bitmap);

        return bitmap;
    }



    public Bitmap loadingImage(CircleImageView pictureView ,String pictureURL) {

        pictureView.setImageResource(R.drawable.profile_default_photo);
        Bitmap bitmap = loadPicture(pictureURL,pictureView);

//        if(bitmap !=null)
//        resizeBitmap(pictureView, bitmap);

        return bitmap;
    }

    private void resizeBitmap(CircleImageView pictureView, Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        BitmapOptimizer optimizer = new BitmapOptimizer();
        optimizer.decodeSampledBitmapFromByteArray(byteArray, 10, 10);

        pictureView.setImageBitmap(bitmap);
    }


    private Bitmap loadPicture(String pictureURL,CircleImageView circleImageView) {
        if(pictureURL != null) {
            if (pictureURL.length() > 0 && !pictureURL.startsWith("default")) {

               return getImageLoader().get(pictureURL,
                        ImageLoader.getImageListener(
                                circleImageView
                                , R.drawable.profile_default_photo
                                , R.drawable.profile_default_photo)).getBitmap();
            }
        }
        return null;
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
    public DatabaseReference getDatabaseGames() {
        return databaseGames;
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

    public void addGame(String gameID,String gameName,int maxPlayers, String gamePicURL,String[] ranks,boolean isCompetitive)
    {
        gameManager.addGame(gameID,gameName,maxPlayers,gamePicURL,ranks,isCompetitive);
    }
}


