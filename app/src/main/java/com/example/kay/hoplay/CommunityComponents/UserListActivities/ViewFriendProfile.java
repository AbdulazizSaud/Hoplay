package com.example.kay.hoplay.CommunityComponents.UserListActivities;

import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.example.kay.hoplay.CommunityComponents.UserListActivity;
import com.example.kay.hoplay.R;
import com.example.kay.hoplay.model.FriendCommonModel;

/**
 * Created by Kay on 3/19/2017.
 */

public class ViewFriendProfile extends UserListActivity {



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
