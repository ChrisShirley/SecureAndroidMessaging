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



    private  SQLiteDatabase mDatabase;
    private  String databaseName;
    private String databaseSetupString;




    public DatabaseHelper(Context context, String dbName, int databaseVersion) {
        super(context, dbName, null, databaseVersion);
        databaseName = dbName;
    }

    public void setupDatabase(String setup)
    {
        databaseSetupString = setup;
    }

    @Override
    public void onCreate(SQLiteDatabase database) {

        database.execSQL(databaseSetupString);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        if(databaseName!=null)
            db.execSQL("DROP TABLE IF EXISTS " + databaseName);
        onCreate(db);
    }

}