package com.example.kay.hoplay.Services;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

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
                    Schema.SchemaInfo.COLUMN_NAME_USERS_CHATS + " varchar(100) primary key"+")";

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


    // Insert  Deleted users keys
    public long insertKey(String key){
        SQLiteDatabase dbW  = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Schema.SchemaInfo.COLUMN_NAME_USERS_CHATS,key);
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



}
