package com.hoplay.kay.hoplay.Models;

/**
 * Created by Kay on 2/21/2017.
 */

public class FriendCommonModel {

    private String friendKey,chatType,friendUsername,userPictureURL;

    public String getFriendKey() {
        return friendKey;
    }

    public void setFriendKey(String friendKey) {
        this.friendKey = friendKey;
    }

    public String getFriendUsername() {
        return friendUsername;
    }

    public void setFriendUsername(String friendUsername) {
        this.friendUsername = friendUsername;
    }

    public String getUserPictureURL() {
        return userPictureURL;
    }

    public void setUserPictureURL(String userPictureURL) {
        this.userPictureURL = userPictureURL;
    }
}
