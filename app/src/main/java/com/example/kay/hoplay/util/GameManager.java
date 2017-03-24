package com.example.kay.hoplay.util;

import com.example.kay.hoplay.Models.GameDetails;

import java.util.ArrayList;

/**
 * Created by Kay on 3/23/2017.
 */

public class GameManager {

    private ArrayList<GameDetails> gamesCompList;
    private ArrayList<GameDetails> gamesCOOPList;

    public void addGame(String gameId , String gamename , int maxPlayers, String gamePicture, String[] ranks,boolean isCompetitive)
    {

        GameDetails gameDetails = new GameDetails(gameId,gamename,maxPlayers,gamePicture);

        for(String rank : ranks)
        {
            gameDetails.addRank(rank);
        }

        if(isCompetitive)
        gamesCompList.add(gameDetails);
        else
            gamesCOOPList.add(gameDetails);
    }


    private void Clear()
    {
        gamesCOOPList.clear();
        gamesCompList.clear();
    }


}
