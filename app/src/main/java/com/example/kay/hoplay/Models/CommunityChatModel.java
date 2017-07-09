package com.example.kay.hoplay.Models;


public class CommunityChatModel {





    // here it should be a behivors something like a opponent id , my id and so long.
    private String chatKey,chatType,chatName,lastMsg,lastMsgDate,userPictureURL;
    private long chatCounter;
    private String timeStamp;

    public CommunityChatModel() {
        this.chatKey = null;
        this.chatName = "none";
        this.userPictureURL = "default";

        this.lastMsg = "";
        this.lastMsgDate = "0 min ago";
    }

    public String getUserPictureURL() {
        return userPictureURL;
    }
    public void setUserPictureURL(String userPictureURL) {
        this.userPictureURL = userPictureURL;
    }

    public String getChatKey() {
        return chatKey;
    }

    public void setChatKey(String chatKey) {
        this.chatKey = chatKey;
    }

    public String getChatName() {
        return chatName;
    }

    public void setChatName(String chatName) {
        this.chatName = chatName;
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

    public long getChatCounter() {
        return chatCounter;
    }

    public void setChatCounter(long chatCounter) {
        this.chatCounter = chatCounter;
    }

    public String getChatType() {
        return chatType;
    }

    public void setChatType(String chatType) {
        this.chatType = chatType;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }
}
