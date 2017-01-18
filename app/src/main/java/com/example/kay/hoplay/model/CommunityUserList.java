package com.example.kay.hoplay.model;


public class CommunityUserList {




    // here it should be a behivors something like a opponent id , my id and so long.
    private String receiverID,fullName,userPictureURL,lastMsg,lastMsgDate;

    public CommunityUserList(String receiverID,String fullName, String userPictureURL) {
        this.receiverID = receiverID;
        this.fullName = fullName;
        this.userPictureURL = userPictureURL;
        this.lastMsg = "";
        this.lastMsgDate = "0 min ago";
    }

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

    public String getReceiverID() {
        return receiverID;
    }

    public void setReceiverID(String receiverID) {
        this.receiverID = receiverID;
    }
}
