package com.example.kay.hoplay.model;

import java.util.ArrayList;

/**
 * Created by khaledAlhindi on 3/19/2017 AD.
 */

public class Game {
    private String gameName ;
    private String gameID;
    private String gamePhotoUrl;



    private int madxPlayers;
    private ArrayList<String> gameRanks;

    public Game(String gameID,String gameName,int maxPlayers, String gamePhotoUrl) {
        this.gameName = gameName;
        this.gameID = gameID;
        this.gamePhotoUrl = gamePhotoUrl;
        this.madxPlayers = maxPlayers;

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


    public int getMadxPlayers() {
        return madxPlayers;
    }

    public void setMadxPlayers(int madxPlayers) {
        this.madxPlayers = madxPlayers;
    }

    public ArrayList<String> getGameRanks() {
        return gameRanks;
    }

    public void setGameRanks(ArrayList<String> gameRanks) {
        this.gameRanks = gameRanks;
    }
}
