package com.example.kay.hoplay.CommunityComponents.UserListActivities;

import android.widget.EditText;

import com.example.kay.hoplay.CommunityComponents.UserListCore;
import com.example.kay.hoplay.Models.FriendCommonModel;

/**
 * Created by Kay on 3/19/2017.
 */

public class ViewFriendProfile extends UserListCore {



    @Override
    protected void OnClickHolders(FriendCommonModel model)
    {

    }

    @Override
    protected void onStartActivity() {


        searchEditText.setVisibility(EditText.GONE);
        searchEditText = null;

        loadFriendList();
    }
}
