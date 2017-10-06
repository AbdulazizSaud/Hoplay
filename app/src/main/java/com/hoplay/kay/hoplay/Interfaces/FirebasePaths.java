package com.hoplay.kay.hoplay.Interfaces;

/**
 * Created by Azoz-LabTop on 11/21/2016.
 */

public interface FirebasePaths {




    public static final String FIREBASE_PRIVATE_ATTR = "_private_";
    public static final String FIREBASE_PUBLIC_ATTR = "_public_";


    public static final String FIREBASE_FRIENDS_LIST_ATTR = "_friends_list_";
    public static final String FIREBASE_DETAILS_ATTR = "_info_";
    public static final String FIREBASE_USER_NAMES_ATTR = "_user_names_";
    public static final String FIREBASE_USERS_INFO_ATTR = "_users_info_";
    public static final String FIREBASE_USERS_LIST_ATTR = "_users_";
    public static final String FIREBASE_GAME_COMPETITVE_ATTR = "_competitive_";
    public static final String FIREBASE_GAME_QUICK_ATTR = "_quick_game_";
    public static final String FIREBASE_GAME_COOP_ATTR = "_coop_";
    public static final String FIREBASE_REQUEST_REGION_ATTR = "region";
    public static final String FIREBASE_REQUEST_PLAYERS_NUMBER_ATTR = "players_number";
    public static final String FIREBASE_REQUEST_TIME_STAMP_ATTR = "timeStamp";


    public static final String FIREBASE_SAVED_REQUESTS_REQUESTS_ATTR="Requests";
    public static final String FIREBASE_PROMO_CODE_POINTS_ATTR ="Points";
    public static final String FIREBASE_PROMO_CODE_ATTR ="_promo_code_";

    public static final String FIREBASE_GAMES_REFERENCES = "_games_";
    public static final String FIREBASE_GAMES_NAME_ATTR_REFERENCES = "name";
    public static final String FIREBASE_GAMES_PHOTO_ATTR_REFERENCES = "photo";




    public static final String FIREBASE_GAME_PLATFORMS_ATTR = "platforms";
    public static final String FIREBASE_GAME_PC_GAME_PROVIDER = "pc_game_provider";
    public static final String FIREBASE_USER_CHAT_REFERENCES = "_chat_refs_";
    public static final String FIREBASE_GAME_MAX_PLAYER_ATTR = "max_player";
    public static final String FIREBASE_CHAT_ATTR = "Chat";
    public static final String FIREBASE_SAVED_REQS_ATTR = "_saved_requests_";

    public static final String FIREBASE_PENDING_CHAT_ATTR = "_pending_chat_";
    public static final String FIREBASE_CHAT_MESSAGES = "_messages_";
    public static final String FIREBASE_USER_PRIVATE_CHAT = FIREBASE_USER_CHAT_REFERENCES+"/"+FIREBASE_PRIVATE_ATTR;
    public static final String FIREBASE_USER_PUBLIC_CHAT = FIREBASE_USER_CHAT_REFERENCES+"/"+FIREBASE_PUBLIC_ATTR;
    public static final String FIREBASE_CHAT_USERS_LIST_PATH = FIREBASE_DETAILS_ATTR+"/"+FIREBASE_USERS_LIST_ATTR;
    public static final String FIREBASE_SAVED_REQS_PATH = FIREBASE_GAMES_REFERENCES+"/"+FIREBASE_SAVED_REQS_ATTR+"/Requests";


    public static final String FIREBASE_COUNTER_PATH = "_counter_";
    public static final String FIREBASE_OPPONENT_ID_ATTR = "_opponent_ID_";


    // ROOT PATH

    public static final String FB_ROOT = "https://hoplay-18a08.firebaseio.com/";
    public static final String FB_USERS_PATH = FB_ROOT+FIREBASE_USERS_INFO_ATTR+"/";
    public static final String FB_CHAT_PATH = FB_ROOT+FIREBASE_CHAT_ATTR+"/";
    public static final String FB_PRIVATE_CHAT_PATH = FB_CHAT_PATH+FIREBASE_PRIVATE_ATTR+"/";
    public static final String FB_PUBLIC_CHAT_PATH = FB_CHAT_PATH+FIREBASE_PUBLIC_ATTR+"/";
    public static final String FB_PENDING_CHAT_PATH = FB_CHAT_PATH+FIREBASE_PENDING_CHAT_ATTR+"/";



    // Requests
    public static final String FB_REQUESTS_REFERENCE = "_requests_";

    // Regions
    public static final String FB_REGIONS_REFERENCE = "_regions_" ;



    // Support
    public static final String FIREBASE_SUPPORT_REFERENCE = "_support_";
    public static  final String FIREBASE_SUPPORT_TITLE_ATTR = "title";
    public static final String FIREBASE_SUPPORT_MESSAGE_ATTR = "message";
    public static  final String FIREBASE_SUPPORT_EMAIL_ATTR = "email";
    public static  final String FIREBASE_SUPPORT_USERNAME = "username";
    public static final  String FIREBASE_SUPPORT_UID = "UID";

    // USER INFO


    public static final String FIREBASE_USERNAME_ATTR = "_username_";
    public static final String FIREBASE_BIO_ATTR = "_bio_";
    public static final String FIREBASE_ACCOUNT_TYPE_ATTR = "acc_type";
    public static final String FIREBASE_PICTURE_URL_ATTR =  "_picUrl_";
    public static final String FIREBASE_NICKNAME_ATTR =  "_nickname_";
    public static final String FIREBASE_EMAIL_ATTR = "_email_";
    public static final String FIREBASE_FAVOR_GAME_ATTR = "_favor_games_";
    public static final String FIREBASE_RECENT_GAME_ATTR = "_recent_played_";
    public static final String FIREBASE_USER_REQUESTS_ATTR = "_requests_refs_";




    public static final String FIREBASE_USER_PC_GAME_PROVIDER_ATTR = "PC_game_providers";
    public static final String FIREBASE_USER_PS_GAME_PROVIDER_ATTR = "PSN_account";
    public static final String FIREBASE_USER_XBOX_GAME_PROVIDER_ATTR ="XBOX_live_account";

    public static final String FIREBASE_USER_PC_GAME_PROVIDER = FIREBASE_DETAILS_ATTR+"/"+FIREBASE_USER_PC_GAME_PROVIDER_ATTR;
    public static final String FIREBASE_USER_PS_GAME_PROVIDER = FIREBASE_DETAILS_ATTR+"/"+FIREBASE_USER_PS_GAME_PROVIDER_ATTR;
    public static final String FIREBASE_USER_XBOX_GAME_PROVIDER = FIREBASE_DETAILS_ATTR+"/"+FIREBASE_USER_XBOX_GAME_PROVIDER_ATTR;


    public static final String FIREBASE_USERNAME_PATH = FIREBASE_DETAILS_ATTR+"/"+FIREBASE_USERNAME_ATTR;

    public static final String FIREBASE_BIO_PATH = FIREBASE_DETAILS_ATTR+"/"+FIREBASE_BIO_ATTR;
    public static final String FIREBASE_ACCOUNT_TYPE_PATH = FIREBASE_DETAILS_ATTR+"/"+FIREBASE_ACCOUNT_TYPE_ATTR;

    public static final String FIREBASE_PICTURE_URL_PATH =  FIREBASE_DETAILS_ATTR+"/"+FIREBASE_PICTURE_URL_ATTR;
    public static final String FIREBASE_NICKNAME_PATH = FIREBASE_DETAILS_ATTR+"/"+FIREBASE_NICKNAME_ATTR;
    public static final String FIREBASE_EMAIL_PATH = FIREBASE_DETAILS_ATTR+"/"+FIREBASE_EMAIL_ATTR;
    public static final String FIREBASE_FAVOR_GAMES_PATH =FIREBASE_GAMES_REFERENCES+"/"+FIREBASE_FAVOR_GAME_ATTR;
    public static final String FIREBASE_RECENT_GAMES_PATH =FIREBASE_GAMES_REFERENCES+"/"+FIREBASE_RECENT_GAME_ATTR;


    public static final String FIREBASESTORAGE_USERINFO_ATTR ="UsersInfo";

    public static  final String FIREBASE_USERS_TOKENS_ATTR = "_users_tokens_";

}
