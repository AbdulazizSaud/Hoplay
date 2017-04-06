package com.example.kay.hoplay.Cores.UserProfileCores;

import android.content.Intent;
import android.widget.EditText;

import com.example.kay.hoplay.Cores.ParentCore.UserListCore;
import com.example.kay.hoplay.Models.FriendCommonModel;



public class FriendsListCore extends UserListCore {


    @Override
    protected void OnClickHolders(FriendCommonModel model)
    {
        Intent i = new Intent(this,ViewFriendProfileCore.class);
        startActivity(i);
    }

    @Override
    protected void onStartActivity() {


        searchEditText.setVisibility(EditText.GONE);
        searchEditText = null;

        loadFriendList();
    }


}
