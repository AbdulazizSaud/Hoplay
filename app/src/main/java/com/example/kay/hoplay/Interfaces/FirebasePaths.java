package com.example.kay.hoplay.Interfaces;

/**
 * Created by Azoz-LabTop on 11/21/2016.
 */

public interface FirebasePaths {


    public static final String FIREBASE_PRIVATE_ATTR = "_private_";
    public static final String FIREBASE_USERS_INFO_ATTR = "_users_info_";
    public static final String FIREBASE_FRIENDS_LIST_ATTR = "_friends_list_";
    public static final String FIREBASE_DETAILS_ATTR = "_info_";
    public static final String FIREBASE_USERS_LIST_ATTR = "_users_";


    public static final String FIREBASE_USER_CHAT_REFERENCES = "_chat_refs_";
    public static final String FIREBASE_CHAT_ATTR = "Chat";
    public static final String FIREBASE_PENDING_CHAT_ATTR = "_pending_chat_";
    public static final String FIREBASE_CHAT_MESSAGES = "_messages_";
    public static final String FIREBASE_USER_PRIVATE_CHAT = FIREBASE_USER_CHAT_REFERENCES+"/"+FIREBASE_PRIVATE_ATTR;

    public static final String FB_ROOT = "https://hoplay-18a08.firebaseio.com/";
    public static final String FB_USERS_PATH = FB_ROOT+FIREBASE_USERS_INFO_ATTR+"/";
    public static final String FB_CHAT_PATH = FB_ROOT+FIREBASE_CHAT_ATTR+"/";
    public static final String FB_PRIVATE_PATH = FB_CHAT_PATH+FIREBASE_PRIVATE_ATTR+"/";
    public static final String FB_PENDING_CHAT_PATH = FB_CHAT_PATH+FIREBASE_PENDING_CHAT_ATTR+"/";

}
