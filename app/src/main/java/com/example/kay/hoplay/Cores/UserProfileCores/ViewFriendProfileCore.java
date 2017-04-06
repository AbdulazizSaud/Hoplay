package com.example.kay.hoplay.Cores.UserProfileCores;

import android.widget.EditText;

import com.example.kay.hoplay.Cores.ParentCore.UserListCore;
import com.example.kay.hoplay.Models.FriendCommonModel;

/**
 * Created by Kay on 3/19/2017.
 */

public class ViewFriendProfileCore extends UserListCore {



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
