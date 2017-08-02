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
                 Iterable<DataSnapshot> chats =  dataSnapshot.getChildren();
                 for(DataSnapshot chat:chats)
                 {
                     chat.getRef().removeValue();

                 }
             }

             @Override
             public void onCancelled(DatabaseError databaseError) {

             }
         });

    }



    @Override
    protected boolean OnLongClickHolders(FriendCommonModel model) {
        return true;
    }


}