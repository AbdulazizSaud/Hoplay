package com.example.kay.hoplay.Services;

import android.provider.BaseColumns;

public final class Schema  {


    public Schema(){}

    /* Inner class that defines the table contents */
    public static class SchemaInfo implements BaseColumns {
        public static final String TABLE_NAME = "deleted_chats";
        public static final String COLUMN_NAME_USERS_CHATS = "users_chats";

    }



}
//