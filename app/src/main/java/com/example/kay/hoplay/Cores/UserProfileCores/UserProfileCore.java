package com.example.kay.hoplay.Cores.UserProfileCores;

import com.example.kay.hoplay.Interfaces.FirebasePaths;
import com.example.kay.hoplay.CoresAbstract.ProfileAbstracts.UserProfile;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;



public class UserProfileCore extends UserProfile implements FirebasePaths{

    long gameCount = 0;
    long friendCount = 0;


    @Override
    protected void OnStartActitvty() {
        setFriendCountGame();
        setGameCount();
        loadRecentActivtiy();
    }



    private void setFriendCountGame()
    {

        DatabaseReference myFavorGames = app.getDatabaseUsers().child(app.getUserInformation().getUID()+"/"+FIREBASE_FRIENDS_LIST_ATTR);
        myFavorGames.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot friendsSnaps) {

                friendCount = friendsSnaps.getChildrenCount();
                setFriendsNumber(friendCount+"");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void setGameCount()
    {

        DatabaseReference myFavorGames = app.getDatabaseUsers().child(app.getUserInformation().getUID()+"/"+FIREBASE_FAVOR_GAMES_PATH);
        myFavorGames.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot gamesShots) {

                gameCount = gamesShots.getChildrenCount();
                setGamesNumber(gameCount+"");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void loadRecentActivtiy()
    {
        DatabaseReference recentGameRef = app.getDatabaseUsers().child(app.getUserInformation().getUID()+"/"+FIREBASE_RECENT_GAMES_PATH);
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

                           addRecentGame(gameKey,gameName,gamePic,"here description","time ago");
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
