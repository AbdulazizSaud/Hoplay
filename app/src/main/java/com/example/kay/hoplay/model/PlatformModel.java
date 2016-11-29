package com.example.kay.hoplay.model;

/**
 * Created by Kay on 11/29/2016.
 */

public class PlatformModel {

    public enum Platform{
        PC,PS,XBOX
    }


    private String urlOfPicture  , gameName, description , numberOfPlayers , requester;
    private Platform platform;

    public PlatformModel(Platform platform , String urlOfPicture, String gameName, String description, String numberOfPlayers, String requester) {
        this.urlOfPicture = urlOfPicture;
        this.gameName = gameName;
        this.description = description;
        this.numberOfPlayers = numberOfPlayers;
        this.requester = requester;
        this.platform = platform;
    }

    public String getUrlOfPicture() {
        return urlOfPicture;
    }

    public void setUrlOfPicture(String urlOfPicture) {
        this.urlOfPicture = urlOfPicture;
    }

    public String getGameName() {
        return gameName;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getNumberOfPlayers() {
        return numberOfPlayers;
    }

    public void setNumberOfPlayers(String numberOfPlayers) {
        this.numberOfPlayers = numberOfPlayers;
    }

    public String getRequester() {
        return requester;
    }

    public void setRequester(String requester) {
        this.requester = requester;
    }

    public Platform getPlatform() {
        return platform;
    }

    public void setPlatform(Platform platform) {
        this.platform = platform;
    }
}