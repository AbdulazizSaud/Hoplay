package com.example.kay.hoplay.Cores.UserProfileCores;

import android.content.Intent;
import android.util.Log;
import android.widget.ImageView;

import com.example.kay.hoplay.App.App;
import com.example.kay.hoplay.CoresAbstract.ProfileAbstracts.ViewFriendProfile;
import com.example.kay.hoplay.Interfaces.FirebasePaths;
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

        setNicknameTextView(friendUsername);
        setUsernameProfile(friendUsername);

        loadFriendData(friendKey);
        loadRecentActivtiy(friendKey);


    }

    private void loadFriendData(String key)
    {
       DatabaseReference userRef =  app.getDatabaseUsers().child(key);
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

                String username = dataSnapshot.child("_username_").getValue().toString().trim();
                String nickname = dataSnapshot.child("_nickname_").getValue().toString().trim();
                String pictureUrl =  dataSnapshot.child("_picUrl_").getValue().toString().trim();


                app.loadingImage(userPictureCircleImageView,pictureUrl);
                setUsernameProfile("@"+username);
                setNicknameTextView(nickname);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }


    private void loadRecentActivtiy(String friendKey)
    {
        DatabaseReference recentGameRef = app.getDatabaseUsers().child(friendKey+"/"+FIREBASE_RECENT_GAMES_PATH);
        recentGameRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> myFavorSnaps = dataSnapshot.getChildren();

                for (DataSnapshot gameSnap : myFavorSnaps)
                {
                    final String gameType = gameSnap.getValue().toString().trim();
                    final String gameKey =  gameSnap.getKey();
                    app.getDatabaseGames().child(gameType+"/"+gameKey).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot gameShot) {
                            String gameName = gameShot.child("name").getValue().toString().trim();
                            String gamePic = gameShot.child("photo").getValue().toString().trim();
                            // Fetch recent games from database and bind it to the recyclerview
                            addRecentGame(gameKey,gameName,gamePic,"","");

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

}
