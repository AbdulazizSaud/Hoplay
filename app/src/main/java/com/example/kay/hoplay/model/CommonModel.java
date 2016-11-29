package com.example.kay.hoplay.model;

/**
 * Created by Kay on 11/29/2016.
 */

public class CommonModel {

    private String urlOfPicture, title,subTitle , time , numberOfPlayer;

    public CommonModel(String urlOfPicture, String title, String subTitle, String time, String numberOfPlayer) {
        this.urlOfPicture = urlOfPicture;
        this.title = title;
        this.subTitle = subTitle;
        this.time = time;
        this.numberOfPlayer = numberOfPlayer;
    }

    public CommonModel(String urlOfPicture, String title, String subTitle, String time) {
        this.urlOfPicture = urlOfPicture;
        this.title = title;
        this.subTitle = subTitle;
        this.time = time;
    }

    public CommonModel(String urlOfPicture, String title, String subTitle) {
        this.urlOfPicture = urlOfPicture;
        this.title = title;
        this.subTitle = subTitle;

    }


    public String getNumberOfPlayer() {
        return numberOfPlayer;
    }

    public void setNumberOfPlayer(String numberOfPlayer) {
        this.numberOfPlayer = numberOfPlayer;
    }

    public String getUrlOfPicture() {
        return urlOfPicture;
    }

    public void setUrlOfPicture(String urlOfPicture) {
        this.urlOfPicture = urlOfPicture;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubTitle() {
        return subTitle;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
