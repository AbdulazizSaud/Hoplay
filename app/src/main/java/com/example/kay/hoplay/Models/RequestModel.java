package com.example.kay.hoplay.Models;



import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



public  class RequestModel implements Parcelable{

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
    private float gameVersion;

    private String adminName;
    private String requestPicture;
    private String gameId;
    private HashMap<String,PlayerModel> players;


    private String savedReqUniqueID;




    protected RequestModel(Parcel in) {
        platform = in.readString();
        requestId = in.readString();
        requestTitle = in.readString();
        admin = in.readString();
        description = in.readString();
        region = in.readString();
        playerNumber = in.readInt();
        matchType = in.readString();
        rank = in.readString();
        timeStamp = in.readLong();
        gameVersion = in.readFloat();
        adminName = in.readString();
        requestPicture = in.readString();
        gameId = in.readString();
        savedReqUniqueID = in.readString();
        players = new HashMap<>();
    }

    public static final Creator<RequestModel> CREATOR = new Creator<RequestModel>() {
        @Override
        public RequestModel createFromParcel(Parcel in) {
            return new RequestModel(in);
        }

        @Override
        public RequestModel[] newArray(int size) {
            return new RequestModel[size];
        }
    };

    public String getSavedReqUniqueID() {
        return savedReqUniqueID;
    }

    public void setSavedReqUniqueID(String savedReqUniqueID) {
        this.savedReqUniqueID = savedReqUniqueID;
    }

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
        players = new HashMap<>();

    }

    public RequestModel(String platform, String requestTitle, String region, int playerNumber, String matchType, String rank, long timeStamp) {
        this.platform = platform;
        this.requestTitle = requestTitle;
        this.region = region;
        this.playerNumber = playerNumber;
        this.matchType = matchType;
        this.rank = rank;
        this.timeStamp = timeStamp;
        players = new HashMap<>();

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
        players = new HashMap<>();

    }


    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
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
//

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

    public HashMap<String,PlayerModel> getPlayers() {
        return players;
    }

    public void setPlayers( HashMap<String,PlayerModel> players) {
        this.players = players;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(platform);
        dest.writeString(requestId);
        dest.writeString(requestTitle);
        dest.writeString(admin);
        dest.writeString(description);
        dest.writeString(region);
        dest.writeInt(playerNumber);
        dest.writeString(matchType);
        dest.writeString(rank);
        dest.writeLong(timeStamp);
        dest.writeFloat(gameVersion);
        dest.writeString(adminName);
        dest.writeString(requestPicture);
        dest.writeString(gameId);
        dest.writeString(savedReqUniqueID);
    }

    public void addPlayer(PlayerModel playerModel)
    {
        if(!players.containsKey(playerModel.getUID()))
            players.put(playerModel.getUID(),playerModel);
    }

    public void removePlayer(PlayerModel playerModel)
    {
        if(players.containsKey(playerModel.getUID()))
            players.remove(playerModel.getUID());
    }
}

