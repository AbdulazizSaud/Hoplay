package com.example.kay.hoplay.Models;

import java.util.ArrayList;

/**
 * Created by khaledAlhindi on 3/19/2017 AD.
 */

public class GameDetails {
    protected String gameName ;
    protected String gameID;
    protected String gamePhotoUrl;



    protected int maxPlayers;
    protected ArrayList<String> gameRanks;

    public GameDetails(String gameID, String gameName, int maxPlayers, String gamePhotoUrl) {
        this.gameName = gameName;
        this.gameID = gameID;
        this.gamePhotoUrl = gamePhotoUrl;
        this.maxPlayers = maxPlayers;

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

    public void addRank(String rank)
    {
        gameRanks.add(rank);
    }


    public int getMaxPlayers() {
        return maxPlayers;
    }

    public void setMaxPlayers(int maxPlayers) {
        this.maxPlayers = maxPlayers;
    }

    public ArrayList<String> getGameRanks() {
        return gameRanks;
    }

    public void setGameRanks(ArrayList<String> gameRanks) {
        this.gameRanks = gameRanks;
    }
}
