package com.hoplay.kay.hoplay.Models;

import android.graphics.Bitmap;

import java.util.HashMap;

/**
 * Created by Kay on 3/3/2017.
 */

public class UserInformation {
    private String username,pictureURL,UID,nickName , userEmail,bio;
    private boolean isPremium=false;
    private String PSNAcc , XboxLiveAcc;

    // first string for the game provder , second one for the account
    private HashMap<String,String>  pcGamesAcc ;

    // first string for the game , second one for the provider
    private HashMap<String,String>  pcGamesWithProviders;



    public HashMap<String, String> getPcGamesWithProviders() {
        return pcGamesWithProviders;
    }

    public void setPcGamesWithProviders(HashMap<String, String> pcGamesWithProviders) {
        this.pcGamesWithProviders = pcGamesWithProviders;
    }

    private Bitmap pictureBitMap;

    public UserInformation(String username)
    {
        this.username = username;
    }

    public UserInformation(){}

    //
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getPictureURL() {
        return pictureURL;
    }
    public void setPictureURL(String pictureURL) {
        this.pictureURL = pictureURL;
    }
    public String getUID() {
        return UID;
    }
    public void setUID(String UID) {
        this.UID = UID;
    }
    public String getNickName() {
        return nickName;
    }
    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getPSNAcc() {
        return PSNAcc;
    }

    public String getXboxLiveAcc() {
        return XboxLiveAcc;
    }

    public HashMap<String, String> getPcGamesAcc() {
        return pcGamesAcc;
    }

    public void setPSNAcc(String PSNAcc) {
        this.PSNAcc = PSNAcc;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public void setXboxLiveAcc(String xboxLiveAcc) {
        XboxLiveAcc = xboxLiveAcc;
    }

    public void setPcGamesAcc(HashMap<String, String> pcGamesAcc) {
        this.pcGamesAcc = pcGamesAcc;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public boolean isPremium() {
        return isPremium;
    }

    public void setPremium(boolean premium) {
        isPremium = premium;
    }

    public Bitmap getPictureBitMap() {
        return pictureBitMap;
    }

    public void setPictureBitMap(Bitmap pictureBitMap) {
        this.pictureBitMap = pictureBitMap;
    }
}