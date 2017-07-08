package com.example.kay.hoplay.Cores.UserProfileCores;

import android.content.Intent;
import android.util.Log;

import com.example.kay.hoplay.CoresAbstract.ProfileAbstracts.ViewFriendProfile;
import com.example.kay.hoplay.Interfaces.FirebasePaths;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;


public class ViewFriendProfileCore extends ViewFriendProfile  implements FirebasePaths {

    String friendKey,friendUsername,friendPicture;

    @Override
    protected void OnStartActitvty() {

        Intent i = getIntent();
        friendKey = i.getStringExtra("user_key");
        friendUsername = i.getStringExtra("user_name");
        friendPicture = i.getStringExtra("user_picture");


        setUsernameProfile(friendUsername);

        loadFriendData(friendKey);
        loadRecentActivtiy(friendKey);


    }

    private void loadFriendData(String key)
    {
       DatabaseReference userRef =  app.getDatabaseUsersInfo().child(key);
        userRef.child(FIREBASE_FAVOR_GAMES_PATH).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                long count = dataSnapshot.getChildrenCount();

                setGamesNumber(count+"");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        userRef.child(FIREBASE_FRIENDS_LIST_ATTR).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                long count = dataSnapshot.getChildrenCount();
                setFriendsNumber(count+"");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        userRef.child(FIREBASE_DETAILS_ATTR).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                if(dataSnapshot.getValue() == null)
                    return;

                String username="",nickname="",pictureUrl="";
                try {
                     username = dataSnapshot.child(FIREBASE_USERNAME_ATTR).getValue(String.class);
                     nickname = dataSnapshot.child(FIREBASE_BIO_ATTR).getValue(String.class);
                     pictureUrl = dataSnapshot.child(FIREBASE_PICTURE_URL_ATTR).getValue(String.class);
                }catch (NullPointerException e)
                {
                    nickname = "null";
                }
                app.loadingImage(userPictureCircleImageView,pictureUrl);
                setUsernameProfile("@"+username);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }



    private void loadRecentActivtiy(String friendKey) {
        DatabaseReference recentGameRef = app.getDatabaseUsersInfo().child(friendKey + "/" + FIREBASE_RECENT_GAMES_PATH);
        DatabaseReference bioRef =  app.getDatabaseUsersInfo().child(friendKey).child(FIREBASE_DETAILS_ATTR).child(FIREBASE_BIO_ATTR);


                recentGameRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                final String gameType = dataSnapshot.child("game_type").getValue(String.class);
                final String matchType = dataSnapshot.child("match_type").getValue(String.class);
                final String platform = dataSnapshot.child("request_platform").getValue(String.class);
                final String gameKey = dataSnapshot.child("game_id").getValue(String.class);
                final String timeStmap = String.valueOf(dataSnapshot.child(FIREBASE_REQUEST_TIME_STAMP_ATTR).getValue(Long.class));


                app.getDatabaseGames().child(gameType + "/" + gameKey).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot gameShot) {

                        String gameName = gameShot.child(FIREBASE_GAMES_NAME_ATTR_REFERENCES).getValue(String.class);
                        String gamePic = gameShot.child(FIREBASE_GAMES_PHOTO_ATTR_REFERENCES).getValue(String.class);

                        addRecentGame(gameKey, gameName, gamePic,platform, matchType, app.convertFromTimeStampToDate(timeStmap));

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

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


        bioRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                   String bio =  dataSnapshot.getValue(String.class);
                if (bio!=null)
                    setBio(bio);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }


}
