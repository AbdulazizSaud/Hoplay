package com.example.kay.hoplay.model;

/**
 * Created by Kay on 2/12/2017.
 */

public abstract class DefaultListModel {

    // here it should be a behivors something like a opponent id , my id and so long.
    protected String gameName,gamePhotoURL,activityDescription;

    public DefaultListModel(String gameName, String gamePhotoURL,String activityDescription) {
        this.gameName = gameName;
        this.gamePhotoURL = gamePhotoURL;
        this.activityDescription = "";
    }


    public String getGameName() {
        return gameName;
    }

    public String getGamePhotoURL() {
        return gamePhotoURL;
    }

    public String getActivityDescription() {
        return activityDescription;
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

}
