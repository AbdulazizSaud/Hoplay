package com.example.kay.hoplay.util;

import android.util.Log;

import com.example.kay.hoplay.Interfaces.Constants;
import com.example.kay.hoplay.Interfaces.FirebasePaths;
import com.example.kay.hoplay.Models.GameModel;

import java.util.ArrayList;
import java.util.HashMap;


public class GameManager implements FirebasePaths,Constants{


    private HashMap<String,GameModel>  allGamesIds =  new HashMap<>();
    private HashMap<String,GameModel>  allGamesName =  new HashMap<>();
    private HashMap<String,GameModel> gamesCompList  = new HashMap<>();
    private HashMap<String,GameModel> gamesCOOPList =  new HashMap<>();


    // first string represnt the game id , second one represnt the pc game provider
    private HashMap<String,String>  pcGamesWithProviders = new HashMap<String,String>();

    public HashMap<String, String> getPcGamesWithProviders() {
        return pcGamesWithProviders;
    }

    public void setPcGamesWithProviders(HashMap<String, String> pcGamesWithProviders) {
        this.pcGamesWithProviders = pcGamesWithProviders;
    }

    public void addGame(GameModel gameModel)
    {


        allGamesIds.put(gameModel.getGameID(),gameModel);
        allGamesName.put(gameModel.getGameName().toLowerCase(),gameModel);

        if(gameModel.getGameType().equals(FIREBASE_GAME_COMPETITVE_ATTR))
        gamesCompList.put(gameModel.getGameID(),gameModel);
        else
            gamesCOOPList.put(gameModel.getGameID(),gameModel);
    }


    public void deleteGame(String gameId,String gameName)
    {
        if (isCompetitive(gameId))
        {
            gamesCompList.remove(gameId);
            allGamesIds.remove(gameId);
            allGamesName.remove(gameName);
        }
        else {
            gamesCOOPList.remove(gameId);
            allGamesIds.remove(gameId);
            allGamesName.remove(gameName);
        }
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

    public int getUserGamesNumber(){

        return  allGamesIds.size();
    }

    public boolean checkIfHasGameByName(String gameName)
    {
        GameModel gameModel = null ;
        gameModel =  getGameByName(gameName);

        if (gameModel != null)
            return true;

        return false;
    }


    public  String getGameType(String gameType){

        String value = GAME_TYPE_QUICK_MATCH;

        switch (gameType)
        {
            case FIREBASE_GAME_COMPETITVE_ATTR: return GAME_TYPE_COMPETITVE;
            case FIREBASE_GAME_QUICK_ATTR: return GAME_TYPE_QUICK_MATCH;
            case FIREBASE_GAME_COOP_ATTR: return GAME_TYPE_COOP;

        }
        return value;
    }


}
