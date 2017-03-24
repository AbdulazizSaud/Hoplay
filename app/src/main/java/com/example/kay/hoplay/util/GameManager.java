package com.example.kay.hoplay.util;

import com.example.kay.hoplay.model.Game;

import java.util.ArrayList;

/**
 * Created by Kay on 3/23/2017.
 */

public class GameManager {

    private ArrayList<Game> gamesCompList;
    private ArrayList<Game> gamesCOOPList;

    public void addGame(String gameId , String gamename , int maxPlayers, String gamePicture, String[] ranks,boolean isCompetitive)
    {

        Game game = new Game(gameId,gamename,maxPlayers,gamePicture);

        for(String rank : ranks)
        {
            game.addRank(rank);
        }

        if(isCompetitive)
        gamesCompList.add(game);
        else
            gamesCOOPList.add(game);
    }


    private void Clear()
    {
        gamesCOOPList.clear();
        gamesCompList.clear();
    }


}
