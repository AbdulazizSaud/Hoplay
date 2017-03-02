package com.example.kay.hoplay.model;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

/**
 * Created by Kay on 3/1/2017.
 */

public abstract class ChildEventListenerModel implements ChildEventListener {


    @Override
    public abstract void onChildAdded(DataSnapshot dataSnapshot, String s);

    @Override
    public abstract void onChildChanged(DataSnapshot dataSnapshot, String s);

    @Override
    public void onChildRemoved(DataSnapshot dataSnapshot) {

    }

    @Override
    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }
}
