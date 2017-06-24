package com.example.kay.hoplay.Cores.UserProfileCores;

import android.util.Log;

import com.example.kay.hoplay.Interfaces.FirebasePaths;
import com.example.kay.hoplay.CoresAbstract.ProfileAbstracts.UserProfile;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;


public class UserProfileCore extends UserProfile implements FirebasePaths {

    long gameCount = 0;
    long friendCount = 0;


    private ChildEventListener userInformationEventListener = new ChildEventListener() {
        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String s) {

        }

        @Override
        public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            if (dataSnapshot.getKey().equals(FIREBASE_BIO_ATTR) && dataSnapshot.getValue() != null) {
                String value = dataSnapshot.getValue(String.class);
                app.getUserInformation().setBio(value);
                setNicknameTextView(value);
            }

            if (dataSnapshot.getKey().equals(FIREBASE_PICTURE_URL_ATTR) && dataSnapshot.getValue() != null) {
                String value = dataSnapshot.getValue(String.class);
                app.getUserInformation().setPictureURL(value);
                loadPicture();

            }

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
    };


    @Override
    protected void OnStartActitvty() {
        setFriendCountGame();
        setGameCount();
        loadRecentActivtiy();

        app.getDatabaseUsersInfo().child(app.getUserInformation().getUID() + "/" + FIREBASE_DETAILS_ATTR).addChildEventListener(userInformationEventListener);
    }


    private void setFriendCountGame() {

        DatabaseReference myFavorGames = app.getDatabaseUsersInfo().child(app.getUserInformation().getUID() + "/" + FIREBASE_FRIENDS_LIST_ATTR);
        myFavorGames.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot friendsSnaps) {

                friendCount = friendsSnaps.getChildrenCount();
                setFriendsNumber(friendCount + "");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void setGameCount() {

        DatabaseReference myFavorGames = app.getDatabaseUsersInfo().child(app.getUserInformation().getUID() + "/" + FIREBASE_FAVOR_GAMES_PATH);
        myFavorGames.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot gamesShots) {

                gameCount = gamesShots.getChildrenCount();
                setGamesNumber(gameCount + "");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void loadRecentActivtiy() {
        DatabaseReference recentGameRef = app.getDatabaseUsersInfo().child(app.getUserInformation().getUID() + "/" + FIREBASE_RECENT_GAMES_PATH);

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

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //userInformationRef.removeEventListener(userInformationEventListener);
    }
}
