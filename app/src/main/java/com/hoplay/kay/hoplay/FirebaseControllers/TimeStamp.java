package com.hoplay.kay.hoplay.FirebaseControllers;


import com.hoplay.kay.hoplay.App.App;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

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

    public static String getTimeAgoFromTimestamp(long time) {
        if (time < 1000000000000L) {
            // if timestamp given in seconds, convert to millis
            time *= 1000;
        }

        long now = System.currentTimeMillis();
        ;
        if (time > now || time <= 0) {
            return null;
        }


        int SECOND_MILLIS = 1000;
        int MINUTE_MILLIS = 60 * SECOND_MILLIS;
        int HOUR_MILLIS = 60 * MINUTE_MILLIS;
        int DAY_MILLIS = 24 * HOUR_MILLIS;

        // TODO: localize
        final long diff = now - time;
        if (diff < 1000 * 60) {
            return "just now";
        } else if (diff < 2 * MINUTE_MILLIS) {
            return "a minute ago";
        } else if (diff < 50 * MINUTE_MILLIS) {
            return diff / MINUTE_MILLIS + " minutes ago";
        } else if (diff < 90 * MINUTE_MILLIS) {
            return "an hour ago";
        } else if (diff < 24 * HOUR_MILLIS) {
            return diff / HOUR_MILLIS + " hours ago";
        } else if (diff < 48 * HOUR_MILLIS) {
            return "yesterday";
        } else {
            return diff / DAY_MILLIS + " days ago";
        }
    }

}
