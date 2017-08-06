package com.example.kay.hoplay.Models;


import java.lang.ref.ReferenceQueue;

public class LobbyInformation {



    private RequestModel requestModel;
    private String adminPicture;

    public LobbyInformation(RequestModel model) {

        requestModel = model;
    }

    public String getAdminUid() {
        return requestModel.getAdmin();
    }

    public void setAdminUid(String adminUid) {
        requestModel.setAdmin(adminUid);
    }

    public String getAdminName() {
        return requestModel.getAdminName();
    }

    public void setAdminName(String adminName) {
        requestModel.setAdminName(adminName);
    }

    public String getAdminPicture() {
        return adminPicture;
    }

    public void setAdminPicture(String getAdminPicture) {
        this.adminPicture = getAdminPicture;
    }

    public String getLobbyPictureURL() {
        return requestModel.getRequestPicture();
    }

    public void setLobbyPictureURL(String lobbyPictureURL) {
       requestModel.setRequestPicture(lobbyPictureURL);
    }

    public String getMatchType() {
        return requestModel.getMatchType();
    }

    public void setMatchType(String matchType) {
        requestModel.setMatchType(matchType);
    }

    public String getRank() {
        return requestModel.getRank();
    }

    public void setRank(String rank) {
        requestModel.setRank(rank);
    }

    public String getRegion() {
        return requestModel.getRegion();
    }

    public void setRegion(String region) {
        requestModel.setRegion(region);
    }

    public String getGameID()
    {
        return requestModel.getGameId();
    }

    public String getPlatform()
    {
        return requestModel.getPlatform();
    }

    public String getRequestID()
    {
        return requestModel.getRequestId();
    }


    public RequestModel getRequestModel()
    {
        return requestModel;
    }



}
