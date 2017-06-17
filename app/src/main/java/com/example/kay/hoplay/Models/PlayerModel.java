package com.example.kay.hoplay.Models;


public class PlayerModel{

    private String username,UID , gamePovider , gameProviderAcc;

    public PlayerModel(){}
    public PlayerModel(String playerUid,String playerName)
    {
        this.username = playerName;
        this.UID = playerUid;
    }
    public PlayerModel(String playerUID , String playerName , String gameProv , String gameProvAcc)
    {
        this.UID = playerUID;
        this.username = playerName;
        this.gamePovider = gameProv;
        this.gameProviderAcc = gameProvAcc;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUID() {
        return UID;
    }

    public String getGamePovider() {
        return gamePovider;
    }

    public void setGamePovider(String gamePovider) {
        this.gamePovider = gamePovider;
    }

    public String getGameProviderAcc() {
        return gameProviderAcc;
    }

    public void setGameProviderAcc(String gameProviderAcc) {
        this.gameProviderAcc = gameProviderAcc;
    }

    public void setUID(String UID) {
        this.UID = UID;
    }

}
