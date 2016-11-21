package com.example.kay.hoplay.model;


public class CommunityUserList {

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getUserPictureURL() {
        return userPictureURL;
    }

    public void setUserPictureURL(String userPictureURL) {
        this.userPictureURL = userPictureURL;
    }

    public String getLastMsg() {
        return lastMsg;
    }

    public void setLastMsg(String lastMsg) {
        this.lastMsg = lastMsg;
    }

    public String getLastMsgDate() {
        return lastMsgDate;
    }

    public void setLastMsgDate(String lastMsgDate) {
        this.lastMsgDate = lastMsgDate;
    }

    // here it should be a behivors something like a opponent id , my id and so long.
    String fullName,userPictureURL,lastMsg,lastMsgDate;

    public CommunityUserList(String fullName, String userPictureURL) {
        this.fullName = fullName;
        this.userPictureURL = userPictureURL;
        this.lastMsg = "";
        this.lastMsgDate = "0 min ago";
    }
}
