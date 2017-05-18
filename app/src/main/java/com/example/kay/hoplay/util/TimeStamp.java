package com.example.kay.hoplay.util;

import android.util.Log;


import com.example.kay.hoplay.App.App;
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
    String useruid;
    private long finalCurrentTime;



public void setUseruid(String useruid){
    this.useruid=useruid;
}
    public void setTimestampLong(){

        DatabaseReference databaseReference=App.getInstance().getDatabaseUsersInfo().child(useruid+"/currentTimeStamp");

        databaseReference.setValue(ServerValue.TIMESTAMP);
        finalCurrentTime=-1;
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
               long currentTime =  dataSnapshot.getValue(long.class);
                String currentTimeStirng = String.valueOf(currentTime);
                currentTimeStirng=currentTimeStirng.substring(0,currentTimeStirng.length());
                finalCurrentTime=Long.parseLong(currentTimeStirng);


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public long getTimestampLong (){
        return finalCurrentTime;
    }
}
