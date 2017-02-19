package com.example.kay.hoplay.CommunityComponents;

import android.content.Intent;
import android.util.Log;
import android.view.View;

import com.example.kay.hoplay.Chat.ChatActivity;
import com.example.kay.hoplay.model.CommunityUserList;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Iterator;

/**
 * Created by Kay on 2/12/2017.
 */

public class CommunityActivity extends CommunityFragment {


    @Override
    protected void OnClickHolders(CommunityUserList model, View v) {
        String id = model.getReceiverID();
        Intent i = new Intent(v.getContext(), ChatActivity.class);
        i.putExtra("receiverUsername", id);
        v.getContext().startActivity(i);
    }

    @Override
    protected void OnStartActivity() {

        final String uid = app.getAuth().getCurrentUser().getUid();

//
        DatabaseReference user = app.getDatabaseUsers().child(uid);

        user.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild("_friends_list_")) {

                    Iterator i = dataSnapshot.child("_friends_list_").getChildren().iterator();
                    while (i.hasNext()){
                        Log.i("---------->","hello = " +i.next());
                    }
                } else {

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });





    }
}
