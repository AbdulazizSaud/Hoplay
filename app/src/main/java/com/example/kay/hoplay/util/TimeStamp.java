package com.example.kay.hoplay.util;

import android.util.Log;


import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;



/**
 * Created by Kay on 5/8/2017.
 */

public class TimeStamp {
    private String time ;
    private long finalCurrentTime;
private boolean flag=false;
    DatabaseReference firebaseDatabase = FirebaseDatabase.getInstance().getReference();
    public TimeStamp(){

    }


    public long getCreatedTimestampLong(){

        firebaseDatabase.child("currentTimeStamp").setValue(ServerValue.TIMESTAMP);
        firebaseDatabase.child("currentTimeStamp").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
               long currentTime =  dataSnapshot.getValue(long.class);
                String currentTimeStirng = String.valueOf(currentTime);
                currentTimeStirng=currentTimeStirng.substring(0,currentTimeStirng.length()-3);
                finalCurrentTime=Long.parseLong(currentTimeStirng);


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        Log.e("ITS WORK", String.valueOf(finalCurrentTime));
       return finalCurrentTime;
    }
}
