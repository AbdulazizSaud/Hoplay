package com.hoplay.kay.hoplay.Models;

import java.util.ArrayList;


public class Ranks {

    private ArrayList<Rank> ranksList;

    public Ranks()
    {
        ranksList =  new ArrayList<Rank>();
    }

    public void addRank(String rankName , String rankUrl)
    {
        ranksList.add(new Rank(rankName,rankUrl));
    }

    public void removeRank(String rankName)
    {
       for(Rank rank : ranksList)
       {
           if(rank.getRankName().equals(rankName))
           {
               ranksList.remove(rank);
               break;
           }
       }
    }

    public Rank getRank(String rankName)
    {
        for(Rank rank : ranksList)
        {
            if(rank.getRankName().equals(rankName))
            {
                return rank;
            }
        }
        return null;
    }


    public ArrayList<Rank> getRanksList() {
        return ranksList;
    }
}
