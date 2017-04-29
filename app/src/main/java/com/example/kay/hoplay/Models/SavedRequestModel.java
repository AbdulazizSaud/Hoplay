package com.example.kay.hoplay.Models;

/**
 * Created by khaledAlhindi on 11/23/2016 AD.
 */

public class SavedRequestModel extends GameModel {

    private String activityDate;
    private String activityDescription;

    // here it should be a behivors something like a opponent id , my id and so long.
    private String gameName, gamePhotoURL;

    public SavedRequestModel(String gameID, String gameName, int maxPlayers, String gamePhotoUrl , String supportedPlatforms, String activityDescription, String activityDate ) {
        super(gameID, gameName, gamePhotoUrl, supportedPlatforms);
        this.setMaxPlayers(maxPlayers);
        this.activityDate = activityDate;
        this.activityDescription = activityDescription;

    }

    public String getActivityDescription() {
        return activityDescription;
    }

    public void setActivityDescription(String activityDescription) {
        this.activityDescription = activityDescription;
    }
}



