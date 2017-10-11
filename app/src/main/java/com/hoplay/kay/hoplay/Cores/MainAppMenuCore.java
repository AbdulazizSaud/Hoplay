package com.hoplay.kay.hoplay.Cores;

import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.hoplay.kay.hoplay.Cores.RequestCore.LobbyFragmentCore;
import com.hoplay.kay.hoplay.Cores.RequestCore.NewRequestFragmentCore;
import com.hoplay.kay.hoplay.CoresAbstract.Lobby.Lobby;
import com.hoplay.kay.hoplay.CoresAbstract.MainAppMenu;
import com.hoplay.kay.hoplay.App.App;
import com.hoplay.kay.hoplay.CoresAbstract.RequestAbstracts.SearchRequests;
import com.hoplay.kay.hoplay.Interfaces.FirebasePaths;
import com.hoplay.kay.hoplay.Models.GameModel;
import com.hoplay.kay.hoplay.Models.Rank;
import com.hoplay.kay.hoplay.Models.RequestModel;
import com.hoplay.kay.hoplay.Models.RequestModelReference;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.hoplay.kay.hoplay.Services.CallbackHandlerCondition;
import com.hoplay.kay.hoplay.Services.FirebaseInstanceIdService;
import com.hoplay.kay.hoplay.Services.HandlerCondition;

import java.util.ArrayList;
import java.util.HashMap;


public class MainAppMenuCore extends MainAppMenu implements FirebasePaths{

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener authStateListener;


    private RequestModelReference requestModelRef;
    Boolean pointsIsDone = false ;


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

                    String promoCode = dataSnapshot.child(FIREBASE_DETAILS_ATTR+"/"+FIREBASE_PROMO_CODE_ATTR).getValue(String.class);



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


                    if(promoCode !=null)
                    {
                    app.getDatabasePromoCode().child("pointing/"+promoCode.toLowerCase()+"/users/"+username.toLowerCase()).setValue(true);
                    }




                    // To show the message once
                    if (!App.isWelcomed)
                    {
                        welcomeMessage(username);
                        App.isWelcomed=true;
                    }



                    // set current user information
                    app.getUserInformation().setUsername(username.toLowerCase());
                    app.getUserInformation().setBio(bio);
                    app.getUserInformation().setPremium(false);
                    app.getUserInformation().setNickName(bio);
                    app.getUserInformation().setPictureURL(picUrl);
                    app.getUserInformation().setPSNAcc(PSNAcc);
                    app.getUserInformation().setXboxLiveAcc(XboxLiveAcc);
                    app.getUserInformation().setPcGamesAcc(pcGamesAccs);



                    // Assign Points
                    final Long[] userPoints = new Long[1];
                    try {

                        app.getDatabasePromoCode().child("pointing").child(app.getUserInformation().getUsername().toLowerCase()).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {


                                for (DataSnapshot childSnapshot : dataSnapshot.getChildren())
                                {
                                    if (childSnapshot.getValue() != null)
                                    {
                                        try {
                                            userPoints[0] = childSnapshot.getValue(Long.class);
                                        }catch (Exception e){}

                                    }

                                }

                                Log.i("===================>","POINTS -->"+userPoints[0]);
                                pointsIsDone = true ;
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });

                    }catch (Exception e){}



                    // User Token
                    app.setUserToken(FirebaseInstanceIdService.recentRoken);
                    Log.i("===>",FirebaseInstanceIdService.recentRoken);
                    app.getDatabaseUsersTokens().child(app.getUserInformation().getUID()).setValue(app.getUserToken());


//                    app.getUserInformation().setPictureBitMap(app.getBitmapFromUrl(picUrl));
//
//                    Log.i("-------->",)


                    // load games
                    loadFavorGamesList();


                    // load saved reqs
                    loadSavedRequests(user.getUid());


                    // check addRequestToFirebase
                    checkRequest();



                    // Assign Points
                    CallbackHandlerCondition callback = new CallbackHandlerCondition() {
                        @Override
                        public boolean callBack() {
                            if(pointsIsDone)
                            {
                                // Default value
                                app.getUserInformation().setHopyPoints("0");

                                // Get value
                                if (userPoints[0] !=null)
                               app.getUserInformation().setHopyPoints(userPoints[0].toString());
                            }
                            return pointsIsDone;
                        }
                    };
                    new HandlerCondition(callback, 0);




//
//                    new CountDownTimer(2000, 1000) {
//
//                        public void onTick(long millisUntilFinished) {
//
//                        }
//
//                        public void onFinish() {
//                            Log.i("=================>","POINTS --> "+ userPoints[0]);
//                        }
//                    }.start();
//




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
                    ranks.add(new Rank(rank.getValue().toString(),rank.getValue(String.class)));
                }
                GameModel gameModel = new GameModel(gameKey,gameName,gamePhoto,platforms,gameType,maxPlayers,ranks,gameProvider);
                app.getGameManager().addGame(gameModel);

                app.getGameManager().getPcGamesWithProviders().put(gameKey,gameProvider);



            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



            // Load games in auto complete edititext : search request
            App.gameAdapter.clear();
            App.loadGames();


    }


    protected void checkRequest() {

        String userID = app.getUserInformation().getUID();
        final DatabaseReference userReqsRef = app.getDatabaseUsersInfo().child(userID+"/"+ FIREBASE_USER_REQUESTS_ATTR);

        userReqsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                long numberOfReqs = dataSnapshot.getChildrenCount();



                if(dataSnapshot.getValue() !=null)
                    setRequestModelRef(dataSnapshot.getValue(RequestModelReference.class));

                if(dataSnapshot.getValue() !=null) {

                    menuPagerAdapter.setParentRequestFragments(new LobbyFragmentCore(requestModelRef));
                }
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
        return dataSnapshot.hasChild(FIREBASE_USERNAME_PATH) &&  dataSnapshot.hasChild(FIREBASE_PICTURE_URL_PATH);
    }



    public void cancelRequest() {
        final String uid = app.getUserInformation().getUID();


        String path = requestModelRef.getPlatform() + "/"
                + requestModelRef.getGameId() + "/"
                + requestModelRef.getRegion() + "/"
                + requestModelRef.getRequestId();

        DatabaseReference requestRef = app.getDatabaseRequests().child(path);

        requestRef.child("players").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Iterable<DataSnapshot> shots = dataSnapshot.getChildren();

                for (DataSnapshot snapshot : shots) {
                    if (snapshot.child("uid").getValue(String.class).equals(uid)) {
                        snapshot.getRef().setValue(null);
                        setRequestModelRef(requestModelRef);
                        break;
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        app.getDatabaseUsersInfo().child(uid + "/" + FIREBASE_USER_REQUESTS_ATTR).removeValue();
        app.getDatabaseUsersInfo().child(uid + "/" + FIREBASE_USER_PUBLIC_CHAT + "/" + requestModelRef.getRequestId()).removeValue();

        app.getDatabasChat().child(FIREBASE_PUBLIC_ATTR+"/" + requestModelRef.getRequestId()).child(FIREBASE_CHAT_USERS_LIST_PATH).child(uid).removeValue();

        app.switchMainAppMenuFragment(new NewRequestFragmentCore());
    }


    public RequestModelReference getRequestModelRef() {
        return requestModelRef;
    }

    public void setRequestModelRef(RequestModelReference requestModelRef) {
        this.requestModelRef = requestModelRef;
    }
}
