package com.example.kay.hoplay;


public class CommunityUserList {

    // here it should be a behivors something like a opponent id , my id and so long.
    String fullName,userPictureURL,lastMsg,lastMsgDate;

    public CommunityUserList(String fullName, String userPictureURL) {
        this.fullName = fullName;
        this.userPictureURL = userPictureURL;
        this.lastMsg = "";
        this.lastMsgDate = "0 min ago";
    }
}
