package com.example.kay.hoplay.Models;

import java.util.ArrayList;

/**
 * Created by khaledAlhindi on 3/19/2017 AD.
 */

public class GameModel {
    protected String gameName ;
    protected String gameID;
    protected String gamePhotoUrl;
    protected String gameType;
    protected String gamePlatforms;



    protected int maxPlayers;
    protected ArrayList<String> gameRanks;

    public GameModel(String gameID, String gameName, String gamePhotoUrl, String gamePlatforms, String gameType , int maxPlayers) {
        this.gameName = gameName;
        this.gameID = gameID;
        this.gamePlatforms = gamePlatforms;
        this.gamePhotoUrl = gamePhotoUrl;
        this.maxPlayers = maxPlayers;
        gameRanks = new ArrayList<>();
        this.gameType = gameType;

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

    public String getGameType() {
        return gameType;
    }

    public void setGameType(String gameType) {
        this.gameType = gameType;
    }

    public String getGamePlatforms() {
        return gamePlatforms;
    }

    public void setGamePlatforms(String gamePlatforms) {
        this.gamePlatforms = gamePlatforms;
    }
}
