package com.example.kay.hoplay.Cores.RequestCore;

import android.util.Log;
import android.view.View;

import com.example.kay.hoplay.Fragments.NewRequestFragment;
import com.example.kay.hoplay.Interfaces.FirebasePaths;
import com.example.kay.hoplay.Models.RequestModel;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class NewRequestFragmentCore extends NewRequestFragment implements FirebasePaths {



    @Override
    protected void OnClickHolders(RequestModel model, View v) {
        removeRequestFromFirebase(model);
    }

    protected void removeRequestFromFirebase(RequestModel model)
    {
        removeFromSaveRequest(model);
        app.getDatabaseUsersInfo().child(app.getUserInformation().getUID()).child(FIREBASE_SAVED_REQS_PATH).setValue(arrayList);
    }

    @Override
    protected void OnStartActivity() {
        app.getDatabaseUsersInfo().child(app.getUserInformation().getUID()).child(FIREBASE_SAVED_REQS_PATH).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if (dataSnapshot.getValue() == null)
                    return;

                addToSaveRequest(dataSnapshot.getValue(RequestModel.class));
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                addToSaveRequest(dataSnapshot.getValue(RequestModel.class));

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

                removeFromSaveRequest(dataSnapshot.getValue(RequestModel.class));
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
