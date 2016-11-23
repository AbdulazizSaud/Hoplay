package com.example.kay.hoplay.model;

/**
 * Created by khaledAlhindi on 11/22/2016 AD.
 */

public class RecentActivityList {


    public String getGameName() {
        return gameName;
    }

    public String getGamePhotoURL() {
        return gamePhotoURL;
    }

    public String getActivityDescription() {
        return activityDescription;
    }

    public String getActivityDate() {
        return activityDate;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    public void setGamePhotoURL(String gamePhotoURL) {
        this.gamePhotoURL = gamePhotoURL;
    }

    public void setActivityDescription(String activityDescription) {
        this.activityDescription = activityDescription;
    }

    public void setActivityDate(String activityDate) {
        this.activityDate = activityDate;
    }

    // here it should be a behivors something like a opponent id , my id and so long.
        String gameName,gamePhotoURL,activityDescription,activityDate;

        public RecentActivityList(String gameName, String gamePhotoURL,String activityDescription, String activityDate) {
            this.gameName = gameName;
            this.gamePhotoURL = gamePhotoURL;
            this.activityDescription = "";
            this.activityDate = "0 min ago";
        }
    public  RecentActivityList(){}


}
