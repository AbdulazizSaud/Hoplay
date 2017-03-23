package com.example.kay.hoplay.model;

/**
 * Created by khaledAlhindi on 3/19/2017 AD.
 */

public class Games {
    private String gameName ;
    private String gameID;
    private String gamePhotoUrl;

    public Games(String gameName, String gameID, String gamePhotoUrl) {
        this.gameName = gameName;
        this.gameID = gameID;
        this.gamePhotoUrl = gamePhotoUrl;
    }

    public String getGameName() {
        return gameName;
    }

    public String getGameID() {
        return gameID;
    }

    public String getGamePhotoUrl() {
        return gamePhotoUrl;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    public void setGameID(String gameID) {
        this.gameID = gameID;
    }

    public void setGamePhotoUrl(String gamePhotoUrl) {
        this.gamePhotoUrl = gamePhotoUrl;
    }
}
