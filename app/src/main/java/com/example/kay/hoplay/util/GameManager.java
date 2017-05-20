package com.example.kay.hoplay.util;

import android.util.Log;

import com.example.kay.hoplay.Interfaces.FirebasePaths;
import com.example.kay.hoplay.Models.GameModel;

import java.util.ArrayList;
import java.util.HashMap;


public class GameManager implements FirebasePaths{


    private HashMap<String,GameModel>  allGamesIds =  new HashMap<>();
    private HashMap<String,GameModel>  allGamesName =  new HashMap<>();
    private HashMap<String,GameModel> gamesCompList  = new HashMap<>();
    private HashMap<String,GameModel> gamesCOOPList =  new HashMap<>();


    public void addGame(GameModel gameModel)
    {


        allGamesIds.put(gameModel.getGameID(),gameModel);
        allGamesName.put(gameModel.getGameName().toLowerCase(),gameModel);

        if(gameModel.getGameType().equals(FIREBASE_GAME_COMPETITVE_ATTR))
        gamesCompList.put(gameModel.getGameID(),gameModel);
        else
            gamesCOOPList.put(gameModel.getGameID(),gameModel);
    }


    public GameModel getGameByName(String name)
    {
        GameModel gameModel = null;
        gameModel = allGamesName.get(name.toLowerCase());
        return gameModel;
    }

    public GameModel getGameById(String gameId){

            return allGamesIds.get(gameId);
    }

    public boolean isCompetitive(String gameId)
    {
        return gamesCompList.containsKey(gameId);
    }

    public ArrayList<GameModel> getCompetitiveGamesArrayList(){

        return new ArrayList<>(gamesCompList.values());
    }

    public ArrayList<GameModel> getCoopGamesArrayList(){

        return new ArrayList<>(gamesCOOPList.values());
    }

    public ArrayList<GameModel> getAllGamesArrayList(){
        return new ArrayList<>(allGamesIds.values());
    }

    private void clear()
    {
        gamesCOOPList.clear();
        gamesCompList.clear();
    }


}
