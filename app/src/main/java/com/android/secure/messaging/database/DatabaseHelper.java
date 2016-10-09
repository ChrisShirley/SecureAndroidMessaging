package com.android.secure.messaging.database;

/**
 * Created by cjs07f on 10/2/16.
 */

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


/**
 * @author Chris J. Shirley
 *
 */
public class DatabaseHelper extends SQLiteOpenHelper {


    public static final String ID = "_id";
    public static final String TABLE_CONTACTS = "contacts";

    public static final String CONTACT_ID ="default_id";
    public static final String NAME = "name";
    public static final String EMAIL = "email";
    public static final String PKEY = "pkey";


    private static final String DATABASE_NAME = "mobs.db";
    private static final int DATABASE_VERSION = 1;



    private static final String CONTACT_TABLE_CREATE = "create table "
            + TABLE_CONTACTS + "(" +
            NAME + " text not null, "+
            EMAIL +" text not null, "+
            PKEY +" text PRIMARY KEY);";


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {

        database.execSQL(CONTACT_TABLE_CREATE);


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONTACTS);
        onCreate(db);
    }

}