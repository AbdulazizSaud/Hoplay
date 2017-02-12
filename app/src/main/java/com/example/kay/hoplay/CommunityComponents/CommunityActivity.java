package com.example.kay.hoplay.CommunityComponents;

import android.content.Intent;
import android.view.View;

import com.example.kay.hoplay.Chat.ChatActivity;
import com.example.kay.hoplay.model.CommunityUserList;

/**
 * Created by Kay on 2/12/2017.
 */

public class CommunityActivity extends CommunityFragment{


    private String accountUsername="test";

    @Override
    protected void OnClickHolders(CommunityUserList model, View v) {
        String id = model.getReceiverID();
        Intent i = new Intent(v.getContext(),ChatActivity.class);
        i.putExtra("receiverUsername",id);
        i.putExtra("myUsername",accountUsername);

        v.getContext().startActivity(i);
    }
}
