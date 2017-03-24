package com.example.kay.hoplay.Models;

/**
 * Created by khaledAlhindi on 11/23/2016 AD.
 */

public class SavedRequestModel extends GameDetails {

    private String activityDate;
    private String activityDescription;

    // here it should be a behivors something like a opponent id , my id and so long.
    private String gameName, gamePhotoURL;

    public SavedRequestModel(String gameID, String gameName, int maxPlayers, String gamePhotoUrl, String activityDescription, String activityDate) {
        super(gameID, gameName, maxPlayers, gamePhotoUrl);
        this.activityDate = activityDate;
        this.activityDescription = activityDescription;

    }

    public void setNumberOfPlayers(int numberOfPlayers) {
        this.maxPlayers = numberOfPlayers;
    }

    public int getNumberOfPlayers()
    {
        return maxPlayers;
    }

    public String getActivityDescription() {
        return activityDescription;
    }

    public void setActivityDescription(String activityDescription) {
        this.activityDescription = activityDescription;
    }
}



