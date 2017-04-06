package com.example.kay.hoplay.Cores.ParentCore;

import com.example.kay.hoplay.CoresAbstract.ProfileAbstracts.UserList;
import com.example.kay.hoplay.Interfaces.FirebasePaths;
import com.example.kay.hoplay.Models.ChildEventListenerModel;
import com.example.kay.hoplay.Models.FriendCommonModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public abstract class UserListCore extends UserList implements FirebasePaths{



    private FriendCommonModel friendCommonModel;

    protected abstract void OnClickHolders(final FriendCommonModel model);
    protected abstract void onStartActivity();


    @Override
    public void loadFriendList()
    {
        final String UID =  app.getAuth().getCurrentUser().getUid();


        final DatabaseReference usersData = app.getDatabaseUsers();
        DatabaseReference friendList = usersData.child(UID).child(FIREBASE_FRIENDS_LIST_ATTR);

        friendList.addChildEventListener(new ChildEventListenerModel() {
            @Override
            public void onChildAdded(final DataSnapshot rootSnapshots, String s) {
                usersData.child(rootSnapshots.getKey()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        addUser(rootSnapshots.getKey(),dataSnapshot);

                        addToFriendUserList(friendCommonModel);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }
        });
    }


    @Override
    protected void searchForUser(String value) {

        DatabaseReference userRef = app.getDatabaseUsers();
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

        if(!username.equals(app.getUserInformation().getUsername()))
        {
            friendCommonModel = new FriendCommonModel();
            friendCommonModel.setFriendKey(key);
            friendCommonModel.setFriendUsername(username);
            friendCommonModel.setUserPictureURL(picUrl);
            addToUserList(friendCommonModel);
        }
    }


}
