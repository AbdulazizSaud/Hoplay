package com.example.kay.hoplay.model;


public class CommunityUserList  extends CommonModel {




    // here it should be a behivors something like a opponent id , my id and so long.
    private String chatKey,fullName,receiverID,lastMsg,lastMsgDate;

    public CommunityUserList() {
        this.chatKey = null;
        this.receiverID = "none";
        this.fullName = "none";
        this.userPictureURL = "default";

        this.lastMsg = "";
        this.lastMsgDate = "0 min ago";
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
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

    public String getChatKey() {
        return chatKey;
    }

    public void setChatKey(String chatKey) {
        this.chatKey = chatKey;
    }
}
