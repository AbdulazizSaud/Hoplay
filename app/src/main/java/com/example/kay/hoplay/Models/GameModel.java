package com.example.kay.hoplay.Models;

import java.util.ArrayList;

/**
 * Created by khaledAlhindi on 3/19/2017 AD.
 */

public class GameModel {
    private String gameName ;
    private String gameID;
    private String gamePhotoUrl;
    private String gameType;
    private String gamePlatforms;


    private int maxPlayers;
    private Ranks gameRanks;

    public GameModel(String gameID, String gameName, String gamePhotoUrl, String gamePlatforms, String gameType , int maxPlayers,ArrayList<Rank> ranks) {
        this.gameName = gameName;
        this.gameID = gameID;
        this.gamePlatforms = gamePlatforms;
        this.gamePhotoUrl = gamePhotoUrl;
        this.maxPlayers = maxPlayers;

        gameRanks = new Ranks();


        for(Rank rank : ranks)
        {
            gameRanks.addRank(rank.getRankName(),rank.getRankUrl());
        }

        this.gameType = gameType;

    }

    public GameModel(String gameID, String gameName, String gamePhotoUrl, String gamePlatforms) {
        this.gameName = gameName;
        this.gameID = gameID;
        this.gamePlatforms = gamePlatforms;
        this.gamePhotoUrl = gamePhotoUrl;
    }
    public GameModel(String gameID, String gameName, String gamePhotoUrl, String gamePlatforms, String gameType , int maxPlayers) {
        this.gameName = gameName;
        this.gameID = gameID;
        this.gamePlatforms = gamePlatforms;
        this.gamePhotoUrl = gamePhotoUrl;
        this.maxPlayers = maxPlayers;
        this.gameType = gameType;

    }


    public String getGameName() {
        return gameName;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    public String getGameID() {
        return gameID;
    }

    public void setGameID(String gameID) {
        this.gameID = gameID;
    }

    public String getGamePhotoUrl() {
        return gamePhotoUrl;
    }

    public void setGamePhotoUrl(String gamePhotoUrl) {
        this.gamePhotoUrl = gamePhotoUrl;
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

    public int getMaxPlayers() {
        return maxPlayers;
    }

    public void setMaxPlayers(int maxPlayers) {
        this.maxPlayers = maxPlayers;
    }

    public Ranks getGameRanks() {
        return gameRanks;
    }

    public void setGameRanks(Ranks gameRanks) {
        this.gameRanks = gameRanks;
    }
}
