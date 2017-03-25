package com.example.kay.hoplay.Models;

/**
 * Created by khaledAlhindi on 11/22/2016 AD.
 */

public class RecentGameModel extends GameDetails{


    // here it should be a behivors something like a opponent id , my id and so long.
    private String activityDate;
    private String activityDescription;

    public RecentGameModel(String gameID, String gameName, String gamePhotoUrl , String activityDescription, String activityDate) {
        super(gameID, gameName, 0, gamePhotoUrl);
        this.activityDate = activityDate;
        this.activityDescription = activityDescription;
    }


    public String getActivityDate() {
        return activityDate;
    }

    public void setActivityDate(String activityDate) {
        this.activityDate = activityDate;
    }

    public String getActivityDescription() {
        return activityDescription;
    }

    public void setActivityDescription(String activityDescription) {
        this.activityDescription = activityDescription;
    }
}