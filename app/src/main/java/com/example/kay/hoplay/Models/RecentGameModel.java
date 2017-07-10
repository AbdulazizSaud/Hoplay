package com.example.kay.hoplay.Models;

import android.util.Log;

/**
 * Created by khaledAlhindi on 11/22/2016 AD.
 */

public class RecentGameModel extends GameModel {


    // here it should be a behivors something like a opponent id , my id and so long.
    private String activityDescription;
    private long timeStamp;


    public RecentGameModel(String gameID, String gameName, String gamePhotoUrl, String supportedPlatforms , String activityDescription, long timeStamp) {

        super(gameID, gameName, gamePhotoUrl, supportedPlatforms);
        this.timeStamp = timeStamp;
        this.activityDescription = activityDescription;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }


    public String getActivityDescription() {
        return activityDescription;
    }

    public void setActivityDescription(String activityDescription) {
        this.activityDescription = activityDescription;
    }
}
