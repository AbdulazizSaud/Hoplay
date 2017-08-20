package com.example.kay.hoplay.Services;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.google.firebase.database.ServerValue;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by BOXTECH on 8/9/2017.
 */

public class SchemaHelper extends SQLiteOpenHelper {


    public static final String DATABASE_NAME = "HoplayDB";
    public static final int DATABASE_VERSION = 1;

    private static String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + Schema.SchemaInfo.TABLE_NAME + " (" +
                    Schema.SchemaInfo.COLUMN_NAME_USERS_CHATS + " varchar(100) primary key ,"+
                    Schema.SchemaInfo.COLUMN_NAME_SIGNUP_STAMP+" varchar(20) ,"+
                    Schema.SchemaInfo.COLUMN_NAME_TIME_STAMP+" varchar(100) ,"+
                    Schema.SchemaInfo.COLUMN_NAME_USERNAME+" varchar(200) ,"+
                    Schema.SchemaInfo.COLUMN_NAME_USERNAME_INDEX+" varchar(10)"+")";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + Schema.SchemaInfo.TABLE_NAME;


    public SchemaHelper(Context context)
    {
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }

    public SchemaHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public SchemaHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version, DatabaseErrorHandler errorHandler) {
        super(context, name, factory, version, errorHandler);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
            db.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }



    // insert username
    public long insertUsername(String username,int index)
    {
        SQLiteDatabase dbW  = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Schema.SchemaInfo.COLUMN_NAME_USERNAME,username);
        values.put(Schema.SchemaInfo.COLUMN_NAME_USERNAME_INDEX,String.valueOf(index));
        Long row = dbW.insert(Schema.SchemaInfo.TABLE_NAME,null,values);


        return row;
    }


    // get username by index
    public String getUsernameByIndex(int index)
    {
        String username = "";

        SQLiteDatabase db = getReadableDatabase();

        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = {
                Schema.SchemaInfo.COLUMN_NAME_USERNAME

        };

// Filter results WHERE "index" = 'index'
        String selection = Schema.SchemaInfo.COLUMN_NAME_USERNAME_INDEX + " = ?";
        String[] selectionArgs = { String.valueOf(index) };

// How you want the results sorted in the resulting Cursor
        String sortOrder =
                Schema.SchemaInfo.COLUMN_NAME_USERNAME + " DESC";

        Cursor cursor = db.query(
                Schema.SchemaInfo.TABLE_NAME,                     // The table to query
                projection,                               // The columns to return
                selection,                                // The columns for the WHERE clause
                selectionArgs,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                sortOrder                                 // The sort order
        );

        List itemIds = new ArrayList<>();
        while(cursor.moveToNext()) {
            String itemId = cursor.getString(
                    cursor.getColumnIndexOrThrow(Schema.SchemaInfo.COLUMN_NAME_USERNAME));
            itemIds.add(itemId);
            username = itemId;
        }
        cursor.close();


        return username;
    }


    // Insert  Deleted users keys
    public long insertKey(String key , Long timeStamp){
        SQLiteDatabase dbW  = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Schema.SchemaInfo.COLUMN_NAME_USERS_CHATS,key);
        values.put(Schema.SchemaInfo.COLUMN_NAME_TIME_STAMP, String.valueOf(timeStamp));
        Long row = dbW.insert(Schema.SchemaInfo.TABLE_NAME,null,values);


        return row;

    }

    // Delete users key ..
    public void deleteKey(String key)
    {

        SQLiteDatabase dbW  = getWritableDatabase();

        // Define 'where' part of query.
            String selection = Schema.SchemaInfo.COLUMN_NAME_USERS_CHATS + " LIKE ?";
        // Specify arguments in placeholder order.
        String[] selectionArgs = { key };
        // Issue SQL statement.
        dbW.delete(Schema.SchemaInfo.TABLE_NAME, selection, selectionArgs);
    }


    // Check is the key Exist : true if exist , false otherwise
    public boolean isExistKey(String key)
    {
        boolean found  = false;

        SQLiteDatabase dbR = getReadableDatabase();
        String[] projection = { Schema.SchemaInfo.COLUMN_NAME_USERS_CHATS };

        String selection = Schema.SchemaInfo.COLUMN_NAME_USERS_CHATS + " = ?";
        String[] selectionArgs = {key};

       // How you want the results sorted in the resulting Cursor
        String sortOrder =
                Schema.SchemaInfo.COLUMN_NAME_USERS_CHATS + " DESC";


        Cursor cursor = dbR.query(
                Schema.SchemaInfo.TABLE_NAME ,                     // The table to query
                projection,                               // The columns to return
                selection,                                // The columns for the WHERE clause
                selectionArgs,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                sortOrder                                 // The sort order
        );


        List deleted_chats_keys  = new ArrayList<>();
        while(cursor.moveToNext()) {

            String itemId = "NOT-FOUND";
            itemId = cursor.getString(
                    cursor.getColumnIndexOrThrow(Schema.SchemaInfo.COLUMN_NAME_USERS_CHATS));
            deleted_chats_keys.add(itemId);

            if (!itemId.equalsIgnoreCase("NOT-FOUND"));
                    found = true ;


        }


        return found;
    }


    public void signUpStamp()
    {
        SQLiteDatabase dbW  = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Schema.SchemaInfo.COLUMN_NAME_SIGNUP_STAMP,"YES");
        Long row = dbW.insert(Schema.SchemaInfo.TABLE_NAME,null,values);

    }

    // Search key by  timestamp
    public String searchKeyByTimeStamp(Long timeStamp){

        String key = "";

        SQLiteDatabase db = getReadableDatabase();

        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = {
                Schema.SchemaInfo.COLUMN_NAME_USERS_CHATS,
                Schema.SchemaInfo.COLUMN_NAME_TIME_STAMP
        };

// Filter results WHERE "timestamp" = 'timestam'
        String selection = Schema.SchemaInfo.COLUMN_NAME_TIME_STAMP + " = ?";
        String[] selectionArgs = { String.valueOf(timeStamp) };

// How you want the results sorted in the resulting Cursor
        String sortOrder =
                Schema.SchemaInfo.COLUMN_NAME_USERS_CHATS + " DESC";

        Cursor cursor = db.query(
                Schema.SchemaInfo.TABLE_NAME,                     // The table to query
                projection,                               // The columns to return
                selection,                                // The columns for the WHERE clause
                selectionArgs,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                sortOrder                                 // The sort order
        );

        List itemIds = new ArrayList<>();
        while(cursor.moveToNext()) {
            String itemId = cursor.getString(
                    cursor.getColumnIndexOrThrow(Schema.SchemaInfo.COLUMN_NAME_USERS_CHATS));
            itemIds.add(itemId);
        }
        cursor.close();


        return key;

    }



    // Search timestamp by key
    public long searchTimeStampByKey(String key)
    {

        String timeStamp = "";

        SQLiteDatabase db = getReadableDatabase();

        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = {
                Schema.SchemaInfo.COLUMN_NAME_TIME_STAMP,
                Schema.SchemaInfo.COLUMN_NAME_USERS_CHATS
        };

// Filter results WHERE "key" = 'key'
        String selection = Schema.SchemaInfo.COLUMN_NAME_USERS_CHATS + " = ?";
        String[] selectionArgs = { key };

// How you want the results sorted in the resulting Cursor
        String sortOrder =
                Schema.SchemaInfo.COLUMN_NAME_TIME_STAMP + " DESC";

        Cursor cursor = db.query(
                Schema.SchemaInfo.TABLE_NAME,                     // The table to query
                projection,                               // The columns to return
                selection,                                // The columns for the WHERE clause
                selectionArgs,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                sortOrder                                 // The sort order
        );

        List itemIds = new ArrayList<>();
        while(cursor.moveToNext()) {
            String itemId = cursor.getString(
                    cursor.getColumnIndexOrThrow(Schema.SchemaInfo.COLUMN_NAME_TIME_STAMP));
            itemIds.add(itemId);
            timeStamp = itemId;
        }
        cursor.close();


        if(timeStamp.isEmpty() ||timeStamp == null || timeStamp.equals("") || timeStamp.equals("\\s+"))
            timeStamp = "-1";

        return Long.parseLong(timeStamp);


    }


    // Check is the key Exist : true if exist , false otherwise
    public boolean isStamped()
    {
        boolean found  = false;

        SQLiteDatabase dbR = getReadableDatabase();
        String[] projection = { Schema.SchemaInfo.COLUMN_NAME_SIGNUP_STAMP };

        String selection = Schema.SchemaInfo.COLUMN_NAME_SIGNUP_STAMP + " = ?";
        String[] selectionArgs = {"YES"};

        // How you want the results sorted in the resulting Cursor
        String sortOrder =
                Schema.SchemaInfo.COLUMN_NAME_SIGNUP_STAMP + " DESC";


        Cursor cursor = dbR.query(
                Schema.SchemaInfo.TABLE_NAME ,                     // The table to query
                projection,                               // The columns to return
                selection,                                // The columns for the WHERE clause
                selectionArgs,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                sortOrder                                 // The sort order
        );


        List signUpStampList  = new ArrayList<>();
        while(cursor.moveToNext()) {

            String itemId = "NOT-FOUND";
            itemId = cursor.getString(
                    cursor.getColumnIndexOrThrow(Schema.SchemaInfo.COLUMN_NAME_SIGNUP_STAMP));
            signUpStampList.add(itemId);


            Log.i("=====>","ITS -_- :"+itemId);


            if (!itemId.equalsIgnoreCase("NOT-FOUND"));
            found = true ;


        }

         cursor.close();


        return found;
    }


    // Remove signup stamp
    public void removeStamp()
    {

        SQLiteDatabase dbW  = getWritableDatabase();

        // Define 'where' part of query.
        String selection = Schema.SchemaInfo.COLUMN_NAME_SIGNUP_STAMP + " LIKE ?";
        // Specify arguments in placeholder order.
        String[] selectionArgs = { "YES" };
        // Issue SQL statement.
        dbW.delete(Schema.SchemaInfo.TABLE_NAME, selection, selectionArgs);
    }






}
