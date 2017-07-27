package com.example.kay.hoplay.Models;



public class LobbyInformation {

    private String adminUid ,adminName,adminPicture;
    private String  lobbyPictureURL, matchType,rank, region;

    public LobbyInformation(String adminUid, String adminName, String adminPicture, String lobbyPictureURL, String matchType, String rank, String region) {
        this.adminUid = adminUid;
        this.adminName = adminName;
        this.adminPicture = adminPicture;
        this.lobbyPictureURL = lobbyPictureURL;
        this.matchType = matchType;
        this.rank = rank;
        this.region = region;
    }

    public String getAdminUid() {
        return adminUid;
    }

    public void setAdminUid(String adminUid) {
        this.adminUid = adminUid;
    }

    public String getAdminName() {
        return adminName;
    }

    public void setAdminName(String adminName) {
        this.adminName = adminName;
    }

    public String getAdminPicture() {
        return adminPicture;
    }

    public void setAdminPicture(String getAdminPicture) {
        this.adminPicture = getAdminPicture;
    }

    public String getLobbyPictureURL() {
        return lobbyPictureURL;
    }

    public void setLobbyPictureURL(String lobbyPictureURL) {
        this.lobbyPictureURL = lobbyPictureURL;
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

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }
}
