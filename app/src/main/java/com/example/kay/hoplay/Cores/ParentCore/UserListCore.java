package com.example.kay.hoplay.Cores.ParentCore;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.example.kay.hoplay.CoresAbstract.ProfileAbstracts.UserList;
import com.example.kay.hoplay.Interfaces.FirebasePaths;
import com.example.kay.hoplay.Models.FriendCommonModel;
import com.example.kay.hoplay.R;
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
                usersData.child(rootSnapshots.getKey()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        addUser(rootSnapshots.getKey(),dataSnapshot);
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
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });
    }

    private void addUser(String key , DataSnapshot dataSnapshot) {

        String username = dataSnapshot.child(FIREBASE_USERNAME_PATH).getValue().toString();
        String picUrl = dataSnapshot.child(FIREBASE_PICTURE_URL_PATH).getValue().toString();

        if(!username.equals(app.getUserInformation().getUsername()) && !checkIsInList(username))
        {
            addToUserList(key,username,picUrl,true);
        }
    }




}
