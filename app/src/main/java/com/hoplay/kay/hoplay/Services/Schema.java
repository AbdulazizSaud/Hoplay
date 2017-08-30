package com.hoplay.kay.hoplay.Services;

import android.provider.BaseColumns;

public final class Schema  {


    public Schema(){}

    /* Inner class that defines the table contents */
    public static class SchemaInfo implements BaseColumns {
        public static final String TABLE_NAME = "HoplayData";
        public static final String COLUMN_NAME_USERS_CHATS = "users_chats";
        public static final String COLUMN_NAME_SIGNUP_STAMP = "signup_stamp";
        public static final String COLUMN_NAME_USERNAME= "username";
        public static final String COLUMN_NAME_USERNAME_INDEX = "username_index";
        public static final String COLUMN_NAME_TIME_STAMP = "time_stamp";
    }



}
//