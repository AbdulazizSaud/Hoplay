package com.hoplay.kay.hoplay.Cores.UserProfileCores.ParentCore;

import android.util.Log;

import com.hoplay.kay.hoplay.CoresAbstract.ProfileAbstracts.UserList;
import com.hoplay.kay.hoplay.Interfaces.FirebasePaths;
import com.hoplay.kay.hoplay.Models.FriendCommonModel;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public abstract class UserListCore extends UserList implements FirebasePaths{




    protected abstract void OnClickHolders(final FriendCommonModel model);
    protected abstract void onStartActivity();


    @Override
    public void loadFriendList()
    {
        final String UID =  app.getAuth().getCurrentUser().getUid();


        final DatabaseReference usersData = app.getDatabaseUsersInfo();
        DatabaseReference friendList = usersData.child(UID).child(FIREBASE_FRIENDS_LIST_ATTR);

        friendList.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(final DataSnapshot rootSnapshots, String s) {
                usersData.child(rootSnapshots.getKey()+"/"+FIREBASE_DETAILS_ATTR).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        if(dataSnapshot.getValue() !=null){
                            Log.i("--->", rootSnapshots.toString()+"..."  + dataSnapshot.getValue().toString());

                            addUser(rootSnapshots.getKey(),dataSnapshot);

                        }
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
    protected void searchForUser(String value) {

        DatabaseReference userRef = app.getDatabaseUsersInfo();


        Query query = userRef.orderByChild(FIREBASE_USERNAME_PATH).startAt(value).endAt(value+"\uf8ff").limitToFirst(10);

        getData(query);
    }



    private void getData(Query query) {

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if(dataSnapshot.getValue() != null)
                {
                    Iterable<DataSnapshot> iterable = dataSnapshot.getChildren();

                    for (DataSnapshot shot : iterable) {
                        addUser(shot.getKey(), shot);
                    }
                    hideLoadingAnimation();
                    hideNotFoundSearch();
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });
    }

    private void addUser(String key , DataSnapshot dataSnapshot) {

        String username = dataSnapshot.child(FIREBASE_USERNAME_ATTR).getValue(String.class);
        String picUrl = dataSnapshot.child(FIREBASE_PICTURE_URL_ATTR).getValue(String.class);

        if(!username.equals(app.getUserInformation().getUsername()) && !checkIsInList(username))
        {
            addToUserList(key,username,picUrl,true);
        }
    }




}
