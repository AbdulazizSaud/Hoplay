package com.example.kay.hoplay.Models;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;



public class RequestModel  {

    private String platform;
    private String requestId ;
    private String requestTitle ;
    private String admin ;
    private String description ;
    private String region ;
    private int playerNumber=0;
    private String matchType;
    private String rank;
    private long timeStamp;


    private String adminName;
    private String requestPicture;
    private List<String> users;
    private String gameId;
    private ArrayList<PlayerModel> players;

    public RequestModel() {
    }

    public RequestModel(String platform, String requestTitle, String admin, String description, String region, int playerNumber, String matchType, String rank, long timeStamp) {
        this.platform=platform;
        this.admin = admin;
        this.description = description;
        this.region = region;
        this.playerNumber = playerNumber;
        this.matchType = matchType;
        this.rank = rank;
        this.requestTitle = requestTitle;
        this.timeStamp=timeStamp;
    }

    public RequestModel(String platform, String requestTitle, String region, int playerNumber, String matchType, String rank, long timeStamp) {
        this.platform = platform;
        this.requestTitle = requestTitle;
        this.region = region;
        this.playerNumber = playerNumber;
        this.matchType = matchType;
        this.rank = rank;
        this.timeStamp = timeStamp;
    }



    public RequestModel(String platform, String gameName, String admin, String description, String region, int playerNumber , String matchType, String rank) {
        this.platform = platform;
        this.requestTitle = gameName;
        this.admin = admin;
        this.region = region;
        this.playerNumber = playerNumber;
        this.matchType = matchType;
        this.rank = rank;
        this.description = description;
    }


    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }

    public List<String> getUsers() {
        return users;
    }

    public void setUsers(List<String> users) {
        this.users = users;
    }

    public String getRequestId() {
        return requestId;
    }

    public String getRequestTitle() {
        return requestTitle;
    }

    public String getAdmin() {
        return admin;
    }

    public String getDescription() {
        return description;
    }

    public String getRegion() {
        return region;
    }

    public int getPlayerNumber() {
        return playerNumber;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public void setAdmin(String admin) {
        this.admin = admin;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public void setPlayerNumber(int playerNumber) {
        this.playerNumber = playerNumber;
    }

    public String getMatchType() {
        return matchType;
    }

    public void setMatchType(String matchType) {
        this.matchType = matchType;
    }

    public String getRank() {
        return rank;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public void setRequestTitle(String requestTitle) {
        this.requestTitle = requestTitle;
    }

    public String getAdminName() {
        return adminName;
    }

    public void setAdminName(String adminName) {
        this.adminName = adminName;
    }

    public String getRequestPicture() {
        return requestPicture;
    }

    public void setRequestPicture(String requestPicture) {
        this.requestPicture = requestPicture;
    }

    public String getGameId() {
        return gameId;
    }

    public void setGameId(String gameId) {
        this.gameId = gameId;
    }

    public ArrayList<PlayerModel> getPlayers() {
        return players;
    }

    public void setPlayers(ArrayList<PlayerModel> players) {
        this.players = players;
    }

}

