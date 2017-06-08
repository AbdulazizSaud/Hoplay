package com.example.kay.hoplay.Cores.RequestCore;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kay.hoplay.Cores.UserProfileCores.AddGameCore;
import com.example.kay.hoplay.Fragments.NewRequestFragment;
import com.example.kay.hoplay.Interfaces.FirebasePaths;
import com.example.kay.hoplay.Models.RequestModel;
import com.example.kay.hoplay.R;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.util.ArrayList;


public class NewRequestFragmentCore extends NewRequestFragment implements FirebasePaths {


    private DatabaseReference saveRequestRef;

    private ChildEventListener saveRequestEventListener = new ChildEventListener() {
        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
            if (dataSnapshot.getValue() == null)
                return;

            addToSaveRequest(dataSnapshot.getValue(RequestModel.class), dataSnapshot.getKey());
        }

        @Override
        public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            // IF EXSIST = DO CHANGES ELSE DO ADD

            int index = Integer.parseInt(dataSnapshot.getKey());
            RequestModel requestModel = dataSnapshot.getValue(RequestModel.class);
            updateSavedRequest(index,requestModel);
           // addToSaveRequest(dataSnapshot.getValue(RequestModel.class), dataSnapshot.getKey());

        }

        @Override
        public void onChildRemoved(DataSnapshot dataSnapshot) {


        }

        @Override
        public void onChildMoved(DataSnapshot dataSnapshot, String s) {

        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };


    @Override
    protected void OnClickHolders(RequestModel model, View v,int position) {

        Toast.makeText(getContext(),model.getGameId()+"",Toast.LENGTH_LONG).show();
        showSavedRequestDialog(model, v,position);

    }


    @Override
    protected void deleteSavedRequest(RequestModel model) {

        removeFromSaveRequest(model);

        saveRequestRef.getParent().child("count").setValue(arrayList.size());

        saveRequestRef.setValue(arrayList, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                if(databaseError !=null)
                {
                }
            }
        });
    }

    @Override
    protected void OnStartActivity() {

        saveRequestRef = app.getDatabaseUsersInfo().child(app.getUserInformation().getUID()).child(FIREBASE_SAVED_REQS_PATH);
        saveRequestRef.addChildEventListener(saveRequestEventListener);
    }
}
