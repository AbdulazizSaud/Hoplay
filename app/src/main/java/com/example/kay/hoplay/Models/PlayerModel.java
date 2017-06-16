package com.example.kay.hoplay.Models;


public class PlayerModel{

    private String username,UID;

    public PlayerModel(){}
    public PlayerModel(String playerUid,String playerName)
    {
        this.username = playerName;
        this.UID = playerUid;
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

    public void setUID(String UID) {
        this.UID = UID;
    }
}
