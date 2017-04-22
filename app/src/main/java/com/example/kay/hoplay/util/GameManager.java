package com.example.kay.hoplay.util;

import com.example.kay.hoplay.Interfaces.FirebasePaths;
import com.example.kay.hoplay.Models.GameModel;

import java.util.ArrayList;
import java.util.HashMap;


public class GameManager implements FirebasePaths{


    private HashMap<String,GameModel>  allGames =  new HashMap<>();
    private HashMap<String,GameModel> gamesCompList  = new HashMap<>();
    private HashMap<String,GameModel> gamesCOOPList =  new HashMap<>();


    public void addGame(GameModel gameModel, HashMap<String,String> ranks)
    {

        allGames.put(gameModel.getGameID(),gameModel);

        if(gameModel.getGameType().equals(FIREBASE_GAME_COMPETITVE_ATTR))
        gamesCompList.put(gameModel.getGameID(),gameModel);
        else
            gamesCOOPList.put(gameModel.getGameID(),gameModel);
    }


    public GameModel getGame(String gameId){

        if(gamesCompList.containsKey(gameId))
            return gamesCompList.get(gameId);
        else if(gamesCompList.containsKey(gameId))
        return gamesCOOPList.get(gameId);


        return null;
    }

    private void clear()
    {
        gamesCOOPList.clear();
        gamesCompList.clear();
    }

    public ArrayList<GameModel> getCompetitiveGames(){

        return new ArrayList<GameModel>(gamesCompList.values());
    }

    public ArrayList<GameModel> getCoopGames(){

        return new ArrayList<GameModel>(gamesCOOPList.values());
    }

    public ArrayList<GameModel> getAllGames(){
        return new ArrayList<GameModel>(allGames.values());
    }


}
