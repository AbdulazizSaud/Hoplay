package com.example.kay.hoplay.Cores.UserProfileCores;

import android.app.Dialog;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;

import com.example.kay.hoplay.Cores.UserProfileCores.ParentCore.UserListCore;
import com.example.kay.hoplay.CoresAbstract.MainAppMenu;
import com.example.kay.hoplay.Models.FriendCommonModel;
import com.example.kay.hoplay.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;


public class FriendsListCore extends UserListCore {


    @Override
    protected void OnClickHolders(FriendCommonModel model)
    {
        Intent i = new Intent(this,ViewFriendProfileCore.class);
        i.putExtra("user_key",model.getFriendKey());
        startActivity(i);
    }

    @Override
    protected void onStartActivity() {


        // hide the search bar
        searchEditText.setVisibility(EditText.GONE);
        searchEditText = null;
        hideLoadingAnimation();

        loadFriendList();
    }


    @Override
    protected void removeFriend(String friendKey,  int friendsNumber) {
        // users_info_ -> user id
        DatabaseReference userInfoRef =  app.getDatabaseUsersInfo().child(app.getUserInformation().getUID());

        // users_info_ -> user id  -> _friend_list_
         userInfoRef .child(FIREBASE_FRIENDS_LIST_ATTR+"/"+friendKey).removeValue();
        // users_info_ -> user id  -> _chat_
         userInfoRef.child(FIREBASE_USER_PRIVATE_CHAT).orderByChild(FIREBASE_OPPONENT_ID_ATTR).equalTo(friendKey).addListenerForSingleValueEvent(new ValueEventListener() {
             @Override
             public void onDataChange(DataSnapshot dataSnapshot) {
                 Log.i("-->",dataSnapshot.toString());
                 Iterable<DataSnapshot> chats =  dataSnapshot.getChildren();
                 for(DataSnapshot chat:chats)
                 {
                     chat.getRef().removeValue();

                     // HERE CALL THE CALL BACK METHOD
                     if (countFriends(app.getUserInformation().getUID())<1)
                        showNoFrindsElements();
                     else
                         hideNoFriendsElements();

                 }
             }

             @Override
             public void onCancelled(DatabaseError databaseError) {

             }
         });

    }




    private long countFriends(String userID)
    {

        final long[] count = {0};

        DatabaseReference userInfoRef =  app.getDatabaseUsersInfo().child(userID);
            userInfoRef.child(FIREBASE_FRIENDS_LIST_ATTR).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                  count[0] =  dataSnapshot.getChildrenCount();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        return count[0];
    }




    @Override
    protected boolean OnLongClickHolders(FriendCommonModel model) {
        return true;
    }

    @Override
    protected void showNoFriendsSection() {


        Animation slideDown = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_down_view);

        noFriendsMessage.setVisibility(View.INVISIBLE);
        noFriendsMessage.startAnimation(slideDown);
        noFriendsMessage.setVisibility(View.VISIBLE);

        noFriendsImageview.setVisibility(View.INVISIBLE);
        noFriendsImageview.startAnimation(slideDown);
        noFriendsImageview.setVisibility(View.VISIBLE);


        addFriendsButton.setVisibility(View.INVISIBLE);
        addFriendsButton.startAnimation(slideDown);
        addFriendsButton.setVisibility(View.VISIBLE);

    }

    @Override
    protected void hideNoFriendsSection() {
        noFriendsMessage.setVisibility(View.GONE);
        addFriendsButton.setVisibility(View.GONE);
        noFriendsImageview.setVisibility(View.GONE);
    }


    private void showNoFrindsElements(){

        Animation slideDown = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_down_view);

        noFriendsMessage.setVisibility(View.INVISIBLE);
        noFriendsMessage.startAnimation(slideDown);
        noFriendsMessage.setVisibility(View.VISIBLE);

        noFriendsImageview.setVisibility(View.INVISIBLE);
        noFriendsImageview.startAnimation(slideDown);
        noFriendsImageview.setVisibility(View.VISIBLE);


        addFriendsButton.setVisibility(View.INVISIBLE);
        addFriendsButton.startAnimation(slideDown);
        addFriendsButton.setVisibility(View.VISIBLE);
    }

    private void hideNoFriendsElements()
    {
        noFriendsMessage.setVisibility(View.GONE);
        addFriendsButton.setVisibility(View.GONE);
        noFriendsImageview.setVisibility(View.GONE);
    }


}