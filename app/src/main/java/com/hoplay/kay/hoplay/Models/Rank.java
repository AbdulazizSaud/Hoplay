package com.hoplay.kay.hoplay.Models;

/**
 * Created by BOXTECH on 4/25/2017.
 */

public class Rank {

    private String rankName;
    private String rankUrl;

    public Rank(String rankName, String rankUrl) {
        this.rankName = rankName;
        this.rankUrl = rankUrl;
    }

    public String getRankName() {
        return rankName;
    }

    public void setRankName(String rankName) {
        this.rankName = rankName;
    }

    public String getRankUrl() {
        return rankUrl;
    }

    public void setRankUrl(String rankUrl) {
        this.rankUrl = rankUrl;
    }
}
