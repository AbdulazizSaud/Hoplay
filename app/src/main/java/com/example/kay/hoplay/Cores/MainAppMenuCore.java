package com.example.kay.hoplay.Cores;

import android.support.annotation.NonNull;
import android.widget.Toast;

import com.example.kay.hoplay.Cores.RequestCore.LobbyFragmentCore;
import com.example.kay.hoplay.Cores.RequestCore.NewRequestFragmentCore;
import com.example.kay.hoplay.CoresAbstract.MainAppMenu;
import com.example.kay.hoplay.App.App;
import com.example.kay.hoplay.Interfaces.FirebasePaths;
import com.example.kay.hoplay.Models.GameModel;
import com.example.kay.hoplay.Models.Rank;
import com.example.kay.hoplay.Models.RequestModel;
import com.example.kay.hoplay.Models.RequestModelRefrance;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;



public class MainAppMenuCore extends MainAppMenu implements FirebasePaths{

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener authStateListener;



    @Override
    protected void onStop() {
        // remove authstate.listenr to firebase auth
        mAuth.removeAuthStateListener(authStateListener);
        super.onStop();

    }

    @Override
    protected void onStart(){
        // add authstate.listenr to firebase auth
        mAuth.addAuthStateListener(authStateListener);
        super.onStart();

    }

    // init method work only on start
    @Override
    public void OnStartActivity() {
        // init a firebase auth states to check if the user is already signued
        // if no , move the user to loginActivity to sign in again.


        app.setMainAppMenuCore(this);

        mAuth = App.getInstance().getAuth();
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user == null) {
                    // User is sign out
                    toLogin();
                } else if(!user.isEmailVerified()){
                    Toast.makeText(getApplicationContext(),"You need to verify your account",Toast.LENGTH_SHORT).show();
                    mAuth.signOut();
                    toLogin();
                } else {
                    app.setmAuthStateListener(authStateListener);
                    setupUserInformation(user);
                }
            }
        };

    }

    private void setupUserInformation(final FirebaseUser user) {
        app.getUserInformation().setUID(user.getUid());
        app.getUserInformation().setUserEmail(user.getEmail());

        app.getTimeStamp().setUseruid(user.getUid());
        app.getDatabaseUsersInfo().child(app.getUserInformation().getUID()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                if(isInfoValid(dataSnapshot))
                {

                    String PSNAcc = "";
                    String XboxLiveAcc = "";
                    HashMap<String,String> pcGamesAccs = new HashMap<String, String>() ;

                    String username = dataSnapshot.child(FIREBASE_USERNAME_PATH).getValue(String.class);
                    String bio = dataSnapshot.child(FIREBASE_BIO_PATH).getValue(String.class);
                    String accountType = dataSnapshot.child(FIREBASE_ACCOUNT_TYPE_PATH).getValue(String.class);
                    String picUrl = dataSnapshot.child(FIREBASE_PICTURE_URL_PATH).getValue(String.class);

                    if (dataSnapshot.hasChild(FIREBASE_USER_PS_GAME_PROVIDER))
                     PSNAcc = dataSnapshot.child(FIREBASE_USER_PS_GAME_PROVIDER).getValue(String.class);
                    if (dataSnapshot.hasChild(FIREBASE_USER_XBOX_GAME_PROVIDER))
                        XboxLiveAcc = dataSnapshot.child(FIREBASE_USER_XBOX_GAME_PROVIDER).getValue(String.class);



                    if (dataSnapshot.hasChild(FIREBASE_USER_PC_GAME_PROVIDER))
                    {
                        Iterable<DataSnapshot> snapshots = dataSnapshot.child(FIREBASE_USER_PC_GAME_PROVIDER).getChildren();
                        for (DataSnapshot pcAcc : snapshots)
                        {
                            pcGamesAccs.put(pcAcc.getKey(),pcAcc.getValue().toString());
                        }

                    }

                    welcomeMessage(username);

                    // set current user information
                    app.getUserInformation().setUsername(username);
                    app.getUserInformation().setBio(bio);
                    app.getUserInformation().setPremium(false);
                    app.getUserInformation().setNickName(bio);
                    app.getUserInformation().setPictureURL(picUrl);
                    app.getUserInformation().setPSNAcc(PSNAcc);
                    app.getUserInformation().setXboxLiveAcc(XboxLiveAcc);
                    app.getUserInformation().setPcGamesAcc(pcGamesAccs);


//                    app.getUserInformation().setPictureBitMap(app.getBitmapFromUrl(picUrl));
//
//                    Log.i("-------->",)


                    // load games
                    loadFavorGamesList();


                    // load saved reqs
                    loadSavedRequests(user.getUid());


                    // check addRequestToFirebase
                    checkRequest();

                }
                else
                {
                    app.signOut();
                    toLogin();
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }



    private void loadFavorGamesList() {
        DatabaseReference currentUserFavorGame = app.getDatabaseUsersInfo().child(app.getUserInformation().getUID() + "/" + FIREBASE_FAVOR_GAMES_PATH);

        currentUserFavorGame.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                setGamesLoaderEvent(dataSnapshot);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                setGamesLoaderEvent(dataSnapshot);
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void loadSavedRequests(String currentUID)
    {

        app.getDatabaseUsersInfo().child(currentUID).child(FIREBASE_SAVED_REQS_PATH).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() == null)
                    return;


                ArrayList<RequestModel> arrayList = new ArrayList<RequestModel>();
                Iterable<DataSnapshot> shots = dataSnapshot.getChildren();

                for (DataSnapshot shot : shots) {

                    RequestModel requestModel = shot.getValue(RequestModel.class);
                    arrayList.add(requestModel);
                }
                app.setSavedRequests(arrayList);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    private void setGamesLoaderEvent(DataSnapshot dataSnapshot) {
        final String gameKey = dataSnapshot.getKey();
        final String gameType = dataSnapshot.getValue().toString().trim();

        app.getDatabaseGames().child(gameType + "/" + gameKey).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot gameShot) {

                String gameName=  gameShot.child("name").getValue(String.class);
                String gamePhoto= gameShot.child("photo").getValue(String.class);
                String platforms= gameShot.child("platforms").getValue(String.class);
                String gameProvider = gameShot.child("pc_game_provider").getValue(String.class);
                int maxPlayers=  gameShot.child("max_player").getValue(Integer.class);

                ArrayList<Rank> ranks = new ArrayList<>();

                for(DataSnapshot rank  : gameShot.child("ranks").getChildren())
                {
                    ranks.add(new Rank(rank.getKey(),rank.getValue(String.class)));
                }
                GameModel gameModel = new GameModel(gameKey,gameName,gamePhoto,platforms,gameType,maxPlayers,ranks,gameProvider);
                app.getGameManager().addGame(gameModel);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    protected void checkRequest() {

        String userID = app.getUserInformation().getUID();
        final DatabaseReference userReqsRef = app.getDatabaseUsersInfo().child(userID+"/"+ FIREBASE_USER_REQUESTS_ATTR);

        userReqsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                long numberOfReqs = dataSnapshot.getChildrenCount();


                RequestModelRefrance requestModelRefrance = dataSnapshot.getValue(RequestModelRefrance.class);

                if(dataSnapshot.getValue() !=null)
                menuPagerAdapter.setParentRequestFragments(new LobbyFragmentCore(requestModelRefrance));
                else
                    menuPagerAdapter.setParentRequestFragments(new NewRequestFragmentCore());

                isDone=true;

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }



    private boolean isInfoValid(DataSnapshot dataSnapshot) {
        return dataSnapshot.hasChild(FIREBASE_USERNAME_PATH) && dataSnapshot.hasChild(FIREBASE_ACCOUNT_TYPE_PATH) && dataSnapshot.hasChild(FIREBASE_PICTURE_URL_PATH);
    }




}
