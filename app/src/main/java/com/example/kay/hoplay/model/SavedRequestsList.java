package com.example.kay.hoplay.model;

/**
 * Created by khaledAlhindi on 11/23/2016 AD.
 */

public class SavedRequestsList {

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    public void setGamePhotoURL(String gamePhotoURL) {
        this.gamePhotoURL = gamePhotoURL;
    }

    public void setRequestDescription(String requestDescription) {
        this.requestDescription = requestDescription;
    }

    public void setNumberOfPlayers(String numberOfPlayers) {
        this.numberOfPlayers = numberOfPlayers;
    }

    public String getGameName() {
        return gameName;
    }

    public String getGamePhotoURL() {
        return gamePhotoURL;
    }

    public String getRequestDescription() {
        return requestDescription;
    }

    public String getNumberOfPlayers() {
        return numberOfPlayers;
    }

    // here it should be a behivors something like a opponent id , my id and so long.
    String gameName,gamePhotoURL,requestDescription,numberOfPlayers;

    public SavedRequestsList(String gameName, String gamePhotoURL,String requestDescription, String numberOfPlayers) {
        this.gameName = gameName;
        this.gamePhotoURL = gamePhotoURL;
        this.requestDescription = "";
        this.numberOfPlayers = "0 \n players";
    }
    public  SavedRequestsList(){}
}
