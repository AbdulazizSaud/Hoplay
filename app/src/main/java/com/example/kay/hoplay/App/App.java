package com.example.kay.hoplay.App;

import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.example.kay.hoplay.Adapters.ViewHolders;
import com.example.kay.hoplay.Cores.MainAppMenuCore;
import com.example.kay.hoplay.Cores.RequestCore.NewRequestFragmentCore;
import com.example.kay.hoplay.CoresAbstract.MainAppMenu;
import com.example.kay.hoplay.Fragments.ParentRequestFragments;
import com.example.kay.hoplay.Models.RequestModel;
import com.example.kay.hoplay.R;
import com.example.kay.hoplay.Services.LruBitmapCache;
import com.example.kay.hoplay.Interfaces.FirebasePaths;
import com.example.kay.hoplay.Models.UserInformation;
import com.example.kay.hoplay.util.BitmapOptimizer;
import com.example.kay.hoplay.util.GameManager;
import com.example.kay.hoplay.util.TimeStamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;

/*
    this class basiclly do linking between all difreent activity that trying to get a same specfic values
    it's impelmented as singleton stratgey
 */

public class App extends Application implements FirebasePaths {

    private static App instance;

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseUserNames;
    private DatabaseReference databaseUsersInfo;
    private DatabaseReference databaseChat;
    private DatabaseReference databaseGames;
    private DatabaseReference databaseRequests;
    private DatabaseReference databaseRegions;
    private FirebaseAuth mAuth;  // firebase auth
    private FirebaseStorage storage;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    private ImageLoader imageLoader; // Image loader from url


    private UserInformation userInformation;
    private GameManager gameManager;
    private TimeStamp timeStamp;
    private ArrayList<RequestModel> requestResultList = new ArrayList<>();

    private ArrayList<RequestModel> savedRequests;

    private MainAppMenuCore mainAppMenuCore;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        mAuth = FirebaseAuth.getInstance();
        storage = FirebaseStorage.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        getFirebaseDatabase().setPersistenceEnabled(true);
        databaseUserNames = firebaseDatabase.getReferenceFromUrl(FB_ROOT).child(FIREBASE_USER_NAMES_ATTR);
        databaseUsersInfo = firebaseDatabase.getReferenceFromUrl(FB_ROOT).child(FIREBASE_USERS_INFO_ATTR);
        databaseRequests = firebaseDatabase.getReferenceFromUrl(FB_ROOT).child(FB_REQUESTS_REFERENCE);
        databaseChat = firebaseDatabase.getReferenceFromUrl(FB_ROOT).child(FIREBASE_CHAT_ATTR);
        databaseGames = firebaseDatabase.getReferenceFromUrl(FB_ROOT).child(FIREBASE_GAMES_REFERENCES);
        databaseRegions = firebaseDatabase.getReferenceFromUrl("https://hoplay-18a08.firebaseio.com/_regions_");
        userInformation = new UserInformation();
        gameManager = new GameManager();
        timeStamp = new TimeStamp();
        savedRequests = new ArrayList<RequestModel>();
    }


    // this method return a instance of this app class
    public static synchronized App getInstance() {
        return instance;
    }

    // this method return a socket io
    // this method return firebase auth
    public FirebaseAuth getAuth() {
        return mAuth;
    }

    // thi method return a imageloader
    private ImageLoader getImageLoader() {
        // create a new addRequestToFirebase queue for picture
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        if (imageLoader == null)
            imageLoader = new ImageLoader(requestQueue, new LruBitmapCache());
        return imageLoader;
    }


    public void loadingImage(Context c, ViewHolders holder, String pictureURL) {


        if (pictureURL == null || pictureURL.isEmpty() || pictureURL.equals("\\s++"))
            return;


        Picasso.with(c)
                .load(pictureURL)
                .error(R.drawable.profile_default_photo)
                .into(holder.getPicture());


    }


    public Bitmap loadingImage(CircleImageView pictureView, String pictureURL) {

        pictureView.setImageResource(R.drawable.profile_default_photo);
        Bitmap bitmap = loadPicture(pictureURL, pictureView);


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


    private Bitmap loadPicture(String pictureURL, CircleImageView circleImageView) {
        if (pictureURL != null) {
            if (pictureURL.length() > 5 && !pictureURL.startsWith("default") && !pictureURL.startsWith("game")) {

                return getImageLoader().get(pictureURL,
                        ImageLoader.getImageListener(
                                circleImageView
                                , R.drawable.profile_default_photo
                                , R.drawable.profile_default_photo)).getBitmap();
            }
        }
        return null;
    }

    public Bitmap getBitmapFromUrl(String pictureUrl) {
        return getImageLoader().get(pictureUrl, null).getBitmap();
    }

    public UploadTask uploadPicture(CircleImageView circleImageView, String uid) {

        circleImageView.setDrawingCacheEnabled(true);
        circleImageView.buildDrawingCache();
        Bitmap bitmap = circleImageView.getDrawingCache();

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] picData = byteArrayOutputStream.toByteArray();

        StorageReference storageReference = storage.getReference();
        StorageReference picRef = storageReference.child(uid + "/profile_picture.png");

        UploadTask uploadTask = picRef.putBytes(picData);
        return uploadTask;
    }


    public void signOut() {
        mAuth.signOut();
    }

    public FirebaseDatabase getFirebaseDatabase() {
        return firebaseDatabase;
    }

    public DatabaseReference getDatabaseUsersInfo() {
        return databaseUsersInfo;
    }

    public DatabaseReference getDatabasChat() {
        return databaseChat;
    }

    public DatabaseReference getDatabaseGames() {
        return databaseGames;
    }

    public DatabaseReference getDatabaseUserNames() {
        return databaseUserNames;
    }

    public DatabaseReference getDatabaseRegions() {
        return databaseRegions;
    }

    public DatabaseReference getDatabaseRequests() {
        return databaseRequests;
    }

    public synchronized UserInformation getUserInformation() {
        return userInformation;
    }


    public void setmAuthStateListener(FirebaseAuth.AuthStateListener mAuthStateListener) {
        this.mAuthStateListener = mAuthStateListener;
    }


    public String convertFromTimeStampToDate(String timeStamp) {
        try {
            DateFormat sdf = new SimpleDateFormat("MM/dd/yy");
            Date netDate = (new Date(Long.parseLong(timeStamp)));
            return sdf.format(netDate);
        } catch (Exception e) {
            return "null";
        }
    }


    public GameManager getGameManager() {
        return gameManager;
    }

    public TimeStamp getTimeStamp() {
        return timeStamp;
    }


    public void setSearchRequestResult(ArrayList<RequestModel> resultList) {
        this.requestResultList = resultList;
    }

    public ArrayList<RequestModel> getSavedRequests() {
        return savedRequests;
    }

    public void setSavedRequests(ArrayList<RequestModel> savedRequests) {
        this.savedRequests = savedRequests;
    }

    public ArrayList<RequestModel> getSearchRequestResult() {
        return requestResultList;
    }

    public RequestModel getRequestModelResult(String key) {
        for (RequestModel requestModel : requestResultList) {
            if (requestModel.getRequestId().equals(key)) {
                return requestModel;
            }
        }
        return null;
    }

    public void setMainAppMenuCore(MainAppMenuCore mainAppMenuCore) {
        this.mainAppMenuCore = mainAppMenuCore;
    }

    public void switchMainAppMenuFragment(ParentRequestFragments fragments) {
        mainAppMenuCore.switchFragment(fragments);
    }

    public FirebaseStorage getFirebaseStorage() {
        return storage;
    }

}


