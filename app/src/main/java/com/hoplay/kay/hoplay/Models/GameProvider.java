package com.hoplay.kay.hoplay.Models;

/**
 * Created by Kay on 6/10/2017.
 */

public class GameProvider {

    String gameProvider , gameProviderAcc , username;


    public GameProvider(){};
    public GameProvider(String gameProv , String gameProvAcc , String userName)
    {
        this.gameProvider = gameProv;
        this.gameProviderAcc = gameProvAcc;
        this.username=userName;
    }


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getGameProvider() {
        return gameProvider;
    }

    public void setGameProvider(String gameProvider) {
        this.gameProvider = gameProvider;
    }

    public String getGameProviderAcc() {
        return gameProviderAcc;
    }

    public void setGameProviderAcc(String gameProviderAcc) {
        this.gameProviderAcc = gameProviderAcc;
    }
}
